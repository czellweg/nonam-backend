package ch.nonam.worldcat.xml;

import org.junit.Assert;
import org.junit.Test;

import ch.nonam.worldcat.domain.WorldcatResult;

import com.thoughtworks.xstream.XStream;

public class FileMakerUtilityTest {

    @Test
    public void testNoResult() {
        FmpXmlResult result = FileMakerUtility.getBaseFmpXmlResultEntity();

        XStream xstream = FileMakerUtility.getXStreamInstance();
        Assert.assertTrue(xstream.toXML(result).contains(
                "<FMPXMLRESULT xmlns=\"http://www.filemaker.com/fmpxmlresult\">"));
        Assert.assertTrue(xstream.toXML(result).contains("<ERRORCODE>0</ERRORCODE>"));
        Assert.assertTrue(xstream.toXML(result).contains(
                "<PRODUCT build=\"03-21-2013\" name=\"FileMaker\" version=\"Pro 12.0v4\"/>"));
        Assert.assertTrue(xstream
                .toXML(result)
                .contains(
                        "<DATABASE dateFormat=\"D.m.yyyy\" layout=\"\" name=\"BiThek.fmp12\" records=\"-1\" timeFormat=\"k:mm:ss \"/>"));
        Assert.assertTrue(xstream.toXML(result).contains(
                "<FIELD emptyOk=\"YES\" maxRepeat=\"1\" name=\"KultN\" type=\"TEXT\"/>"));
        Assert.assertTrue(xstream.toXML(result).contains(
                "<FIELD emptyOk=\"YES\" maxRepeat=\"1\" name=\"BereichKurzzeichen\" type=\"TEXT\"/>"));
    }

    @Test
    public void testEmptyResult() {
        FmpXmlResult result = FileMakerUtility.getBaseFmpXmlResultEntity();

        result.setResultSet(new ResultSet());

        XStream xstream = FileMakerUtility.getXStreamInstance();
        Assert.assertTrue(xstream.toXML(result).contains("<RESULTSET/>"));
    }

    @Test
    public void testSingleRowSingleColResult() {
        FmpXmlResult result = FileMakerUtility.getBaseFmpXmlResultEntity();

        ResultSet resultSet = new ResultSet();
        Row row = new Row();
        row.addCol(new Col("WP Wenger-Peek Circumpolar Collection"), FileMakerUtility.getColumnIndex("nordAmerikaN"));
        resultSet.addRow(row);
        result.setResultSet(resultSet);

        XStream xstream = FileMakerUtility.getXStreamInstance();
        Assert.assertTrue(xstream.toXML(result).contains("<DATA>WP Wenger-Peek Circumpolar Collection</DATA>"));
    }

    @Test
    public void testSingleRowMultiColResult() {
        FmpXmlResult result = FileMakerUtility.getBaseFmpXmlResultEntity();

        ResultSet resultSet = new ResultSet();
        Row row = new Row();
        row.addCol(new Col("WP Wenger-Peek Circumpolar Collection"), FileMakerUtility.getColumnIndex("nordAmerikaN"));
        row.addCol(new Col("01 Arktis"), FileMakerUtility.getColumnIndex("sonstigeLaenderS"));
        resultSet.addRow(row);
        result.setResultSet(resultSet);

        XStream xstream = FileMakerUtility.getXStreamInstance();

        String xml = xstream.toXML(result);
        // System.out.println(xml);
        Assert.assertTrue(xml.contains("<DATA>WP Wenger-Peek Circumpolar Collection</DATA>"));
        Assert.assertTrue(xml.contains("<DATA>01 Arktis</DATA>"));
    }

    @Test
    public void testNonExistentUiName() {
        int index = FileMakerUtility.getColumnIndex("non-existent");
        Assert.assertEquals(-1, index);
    }

    @Test
    public void testExistingUiName() {
        int index = FileMakerUtility.getColumnIndex("nordAmerikaN");
        Assert.assertEquals(0, index);

        index = FileMakerUtility.getColumnIndex("museologieMU");
        Assert.assertEquals(2, index);

        index = FileMakerUtility.getColumnIndex("bereich");
        Assert.assertEquals(3, index);
    }

    @Test
    public void testRow() {
        FmpXmlResult result = FileMakerUtility.getBaseFmpXmlResultEntity();

        WorldcatResult wr = new WorldcatResult("Title", "Author", "12345");
        wr.setN("WP Wenger-Peek Circumpolar Collection");
        wr.setS("01 Arktis");
        wr.setBereich("Legenden, Mythen, Märchen / LM");

        result.getResultSet().addRow(new Row(wr));

        XStream xstream = FileMakerUtility.getXStreamInstance();
        String xml = xstream.toXML(result);
        System.out.println(xml);
    }

    @Test
    public void testMultipleRows() {
        FmpXmlResult result = FileMakerUtility.getBaseFmpXmlResultEntity();

        WorldcatResult wr = new WorldcatResult("Title", "Author", "12345");
        wr.setN("WP Wenger-Peek Circumpolar Collection");
        wr.setS("01 Arktis");
        wr.setBereich("Legenden, Mythen, Märchen / LM");

        Row row1 = new Row(wr);
        Row row2 = new Row(wr);
        result.getResultSet().addRow(row1);
        result.getResultSet().addRow(row2);

        XStream xstream = FileMakerUtility.getXStreamInstance();
        String xml = xstream.toXML(result);
        System.out.println(xml);
    }

}
