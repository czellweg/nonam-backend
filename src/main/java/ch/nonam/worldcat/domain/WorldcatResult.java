package ch.nonam.worldcat.domain;

import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.util.StringUtils;

import ch.nonam.worldcat.xml.Col;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * 
 */
public class WorldcatResult {

    
    private Set<Character> trailingChars = Sets.newHashSet(',', '.');
    // fields populated after a search was done
    private String title;
    private String author;
    private String contentId;
    private String link;

    // fields populated after content-loading
    private String sprache;
    private String jahr;
    private String verlag;
    private List<String> creators;
    private List<String> type;

    // fields populated by the user after selection of which results to include
    // in the final XML
    private String n;
    private String s;
    private String mu;
    private String bereich;
    private String bereichKurzzeichen;
    private String standort;
    private String laufnummer;
    private String medienart;
    private String reihe;
    private String band;
    private String ort;
    private String isbn;
    private String seitenzahl;
    private String schlagwoerter;

    public WorldcatResult() {
        // needed by Spring
    }

    public WorldcatResult(String title, String author, String contentId) {
        this.title = title;
        this.author = author;
        this.contentId = contentId;
    }

    public WorldcatResult(WorldcatResult other, JSONObject json) {
        this(json);
        this.title = other.title;
        this.author = other.author;
        this.contentId = other.contentId;
        this.link = other.link;
        removeTrailingChars();
    }

    public WorldcatResult(JSONObject json) {

        // dc:description, dc:type, dc:language, dc:subject, dc:title,
        // dc:date, dc:relation, dc:creator, xmlns:rdf, dc:identifier,
        // dc:publisher
        JSONObject parent = ((JSONObject) json.get("rdf:Description"));

        this.sprache = parent.optString("dc:language");
        this.title = parent.optString("dc:title");
        this.jahr = parent.optString("dc:date");
        
        handlePublisherField(parent.optString("dc:publisher"));

        this.creators = Lists.newArrayList();
        JSONArray creatorList = parent.optJSONArray("dc:creator");
        if (creatorList != null && creatorList.length() > 0) {
            for (int i = 0; i < creatorList.length(); i++) {
                this.creators.add(creatorList.getString(i));
            }
        } else {
            String creatorString = parent.optString("dc:creator");
            this.creators.add(creatorString);
        }

        this.type = Lists.newArrayList();
        JSONArray typesList = parent.optJSONArray("dc:type");
        if (typesList != null && typesList.length() > 0) {
            for (int i = 0; i < typesList.length(); i++) {
                this.type.add(typesList.getString(i));
            }
        } else {
            String typeString = parent.optString("dc:type");
            this.type.add(typeString);
        }
        removeTrailingChars();
    }

    private void removeTrailingChars() {
        
        trailingChars.stream().forEach(tc -> {
            this.title = StringUtils.trimTrailingCharacter(this.title, tc);
            this.ort = StringUtils.trimTrailingCharacter(this.ort, tc);
            this.verlag = StringUtils.trimTrailingCharacter(this.verlag, tc);
            this.jahr = StringUtils.trimTrailingCharacter(this.jahr, tc);
            this.author = StringUtils.trimTrailingCharacter(this.author, tc);
        });
    }

    private void handlePublisherField(String publisherString) {
        
        String[] split = publisherString.split(":");
        if (split.length == 2) {
            this.ort = split[0].trim();
            this.verlag = split[1].trim();
        } else {
            this.verlag = publisherString;
        }
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getLink() {
        return link;
    }

    public List<String> getCreators() {
        return creators;
    }

    public List<String> getType() {
        return type;
    }

    public String getN() {

        return n != null ? n : Col.N_DEFAULT;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getS() {
        return s != null ? s : Col.S_DEFAULT;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getMu() {
        return mu;
    }

    public void setMu(String mu) {
        this.mu = mu;
    }

    public String getStandort() {
        return standort != null ? standort : Col.STANDORT_DEFAULT;
    }

    public void setStandort(String standort) {
        this.standort = standort;
    }

    public String getLaufnummer() {
        return laufnummer;
    }

    public void setLaufnummer(String laufnummer) {
        this.laufnummer = laufnummer;
    }

    public String getMedienart() {
        return medienart != null ? medienart : Col.MEDIENART_DEFAULT;
    }

    public void setMedienart(String medienart) {
        this.medienart = medienart;
    }

    public String getReihe() {
        return reihe;
    }

    public void setReihe(String reihe) {
        this.reihe = reihe;
    }

    public String getBereich() {
        return bereich;
    }

    public void setBereich(String bereich) {
        this.bereich = bereich;
    }

    public String getBereichKurzzeichen() {
        return bereichKurzzeichen;
    }

    public void setBereichKurzzeichen(String bereichKurzzeichen) {
        this.bereichKurzzeichen = bereichKurzzeichen;
    }

    public String getBand() {
        return band;
    }

    public void setBand(String band) {
        this.band = band;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getVerlag() {
        return verlag;
    }

    public void setVerlag(String verlag) {
        this.verlag = verlag;
    }

    public String getJahr() {
        return jahr;
    }

    public void setJahr(String jahr) {
        this.jahr = jahr;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getSprache() {
        return sprache;
    }

    public void setSprache(String sprache) {
        this.sprache = sprache;
    }

    public String getSeitenzahl() {
        return seitenzahl;
    }

    public void setSeitenzahl(String seitenzahl) {
        this.seitenzahl = seitenzahl;
    }

    public String getSchlagwoerter() {
        return schlagwoerter;
    }

    public void setSchlagwoerter(String schlagwoerter) {
        this.schlagwoerter = schlagwoerter;
    }
}
