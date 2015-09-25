package ch.nonam.worldcat.controller;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.XML;
import org.marc4j.MarcReader;
import org.marc4j.MarcWriter;
import org.marc4j.MarcXmlReader;
import org.marc4j.MarcXmlWriter;
import org.marc4j.marc.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import ch.nonam.worldcat.domain.WorldcatResult;
import ch.nonam.worldcat.xml.FileMakerUtility;
import ch.nonam.worldcat.xml.FmpXmlResult;
import ch.nonam.worldcat.xml.Row;

import com.google.common.collect.Lists;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.thoughtworks.xstream.XStream;

@RestController
public class WorldcatController {

    private static final String FILEMAKERXML = "filemakerxml-";

    private final static Logger logger = Logger.getLogger(WorldcatController.class);

    private static final String SEARCH_URL = "http://www.worldcat.org/webservices/catalog/search/worldcat/opensearch?wskey=DabeuFmWmhHAARuvWSpdjHwCNaQhGwKzxEWO780XQIteIz15C3lnBGfGCgw6zYjl9XUMNrADjnBwoySk&q={TERM}";
    private static String CONTENT_URL = "http://www.worldcat.org/webservices/catalog/content/{ID}?servicelevel=full&wskey=DabeuFmWmhHAARuvWSpdjHwCNaQhGwKzxEWO780XQIteIz15C3lnBGfGCgw6zYjl9XUMNrADjnBwoySk";

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/transform", method = RequestMethod.POST)
    public long transform(@RequestBody List<WorldcatResult> inputs) {

        if (inputs != null && inputs.size() > 0) {
            FmpXmlResult result = FileMakerUtility.getBaseFmpXmlResultEntity();
            inputs.forEach(wr -> {
                result.getResultSet().addRow(new Row(wr));
            });
            XStream xstream = FileMakerUtility.getXStreamInstance();
            String xml = xstream.toXML(result);
            
            
            long xmlId = System.currentTimeMillis();
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(FILEMAKERXML + xmlId + ".xml"), "utf-8"))) {
                
                writer.write(xml);
                return xmlId;
            } catch (Exception e) {
                logger.error(e);
            } 
        } else {
            logger.warn("No entities received in the body of the request.");
        }
        return -1l;
    }

    @RequestMapping(value = "/download/{xmlId}", method = RequestMethod.GET)
    public void downloadXml(@PathVariable(value = "xmlId") String xmlId, HttpServletResponse res) throws Exception {

        byte[] fileContent = getFileContent(Paths.get(FILEMAKERXML + xmlId + ".xml"));

        if (fileContent != null) {

            String disposition = "attachment; fileName=import.xml";
            res.setContentType("application/force-download");
            res.setHeader("Content-Transfer-Encoding", "binary");
            res.setHeader("Content-Disposition", disposition);
            res.setContentLength(fileContent.length);

            ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
            outputBuffer.write(fileContent);
            res.getOutputStream().write(outputBuffer.toByteArray());
            res.getOutputStream().flush();
            outputBuffer.close();
        } else {
            logger.warn("Downloading the file with ID " + xmlId + " was not successful.");
        }
    }
    
    /**
     * Extracts the contents of a file and returns the byte array.
     * 
     * @param filePath
     *            {@link Path} object pointing to the file for which the
     *            contents should be returned
     * @return the byte[] of the file contents, or null if no such file exists
     */
    private byte[] getFileContent(Path filePath) {
        File file = filePath.toFile();
        if (file.exists()) {
            try {
                return Files.readAllBytes(filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            logger.warn("Unable to get file contents of file "+file.toString()+": file does not exist.");
        }
        return null;
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public WorldcatResult querySingleRecord(@RequestBody WorldcatResult requestInput) throws Exception {
        String url = CONTENT_URL.replaceFirst("\\{ID\\}", requestInput.getContentId());

        TransformerFactory tFactory = TransformerFactory.newInstance();
        if (tFactory.getFeature(SAXSource.FEATURE) && tFactory.getFeature(SAXResult.FEATURE)) {

            // cast the transformer handler to a sax transformer handler
            SAXTransformerFactory saxTFactory = ((SAXTransformerFactory) tFactory);

            // create a TransformerHandler for each stylesheet.
            TransformerHandler marcToDCHandler = saxTFactory.newTransformerHandler(new StreamSource(
                    "http://www.loc.gov/standards/marcxml/xslt/MARC21slim2RDFDC.xsl"));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            marcToDCHandler.setResult(new StreamResult(out));

            // create a SAXResult with the first handler
            Result saxResult = new SAXResult(marcToDCHandler);
            String marcXmlString = restTemplate.getForObject(url, String.class);

            InputStream input = new ByteArrayInputStream(marcXmlString.getBytes(StandardCharsets.UTF_8));

            // parse the input
            MarcReader reader = new MarcXmlReader(input);
            MarcWriter xsltResultWriter = new MarcXmlWriter(saxResult);
            while (reader.hasNext()) {
                Record record = reader.next();
                xsltResultWriter.write(record);
            }
            xsltResultWriter.close();

            String finalXml = new String(out.toByteArray(), StandardCharsets.UTF_8);
            JSONObject jsonObject = XML.toJSONObject(finalXml);
            WorldcatResult result = new WorldcatResult(requestInput, jsonObject);
            return result;
        }
        return null;

    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public List<WorldcatResult> search(@RequestParam(value = "q") String term) throws Exception {
        logger.info("Searching Worldcat API with terms: " + term);
        List<WorldcatResult> results = queryAtomFeed(term);
        return results;
    }

    private List<WorldcatResult> queryAtomFeed(String term) throws MalformedURLException, IOException, FeedException {
        List<WorldcatResult> results = Lists.newArrayList();

        String url = SEARCH_URL.replaceFirst("\\{TERM\\}", term);

        XmlReader reader = null;
        try {
            URL atomUrl = new URL(url);
            reader = new XmlReader(atomUrl);
            SyndFeed feed = new SyndFeedInput().build(reader);
            for (SyndEntry entry : feed.getEntries()) {
                String link = entry.getLink();
                String id = link.substring(link.lastIndexOf("/") + 1);
                results.add(new WorldcatResult(entry.getTitle(), entry.getAuthor(), id));
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return results;
    }
}
