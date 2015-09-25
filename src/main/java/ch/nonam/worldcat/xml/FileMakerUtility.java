package ch.nonam.worldcat.xml;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.thoughtworks.xstream.XStream;

public final class FileMakerUtility {

    /* used to set the values in the list at the i-th place */
    private static final Map<String, Integer> columnNameToIndexMapping = Maps.newHashMap();
    private static final Map<String, String> uiNameToColumnNameMapping = Maps.newHashMap();

    private static List<String> dbColumnNames = Lists.newArrayList(
         "KultN", 
         "KultS",
         "KultMU",
         "Bereich",
//         "BereichKurzzeichen",
         "Laufnummer",
         "Medienart",
         "Standort",
         "Titel",
         "Verfasser I",
         "Nebeneintrag 1",
//         "Urheber",
         "Ort",
         "Verlag",
         "Jahr",
         "Reihe",
         "Band",
         "ISBN",
         "Sprache",
         "Unterbereich", // seitenzahl
         "Schlagwort");

    static {
        uiNameToColumnNameMapping.put("nordAmerikaN", "KultN");
        uiNameToColumnNameMapping.put("sonstigeLaenderS", "KultS");
        uiNameToColumnNameMapping.put("museologieMU", "KultMU");
        uiNameToColumnNameMapping.put("bereich", "Bereich");
//        uiNameToColumnNameMapping.put("bereichKurzzeichen", "BereichKurzzeichen");
        uiNameToColumnNameMapping.put("standort", "Standort");
        uiNameToColumnNameMapping.put("signatur", "Bandnummer");
        uiNameToColumnNameMapping.put("laufnummer", "Laufnummer");
        uiNameToColumnNameMapping.put("medienart", "Medienart");
        uiNameToColumnNameMapping.put("titel", "Titel");
        uiNameToColumnNameMapping.put("reihe", "Reihe");
        uiNameToColumnNameMapping.put("band", "Band");
        uiNameToColumnNameMapping.put("verfasser1", "Verfasser I");
        uiNameToColumnNameMapping.put("nebeneintrag1", "Nebeneintrag 1");
//        uiNameToColumnNameMapping.put("urheber", "Urheber");
        uiNameToColumnNameMapping.put("ort", "Ort");
        uiNameToColumnNameMapping.put("verlag", "Verlag");
        uiNameToColumnNameMapping.put("jahr", "Jahr");
        uiNameToColumnNameMapping.put("isbn", "ISBN");
        uiNameToColumnNameMapping.put("sprache", "Sprache");
        uiNameToColumnNameMapping.put("seitenzahl", "Unterbereich");
        uiNameToColumnNameMapping.put("schlagwoerter", "Schlagwort");

        // this determines the order of the DB-columns, left to right so to
        // speak, of how the data will be importec
        for (int i = 0; i < dbColumnNames.size(); i++) {
            columnNameToIndexMapping.put(dbColumnNames.get(i), i);
        }

    }

    private static class LazyHolder {
        private static final XStream INSTANCE = configureXStream();
    }

    public static List<String> getDbColumnNames() {
        return dbColumnNames;
    }

    private static final XStream configureXStream() {
        XStream xstream = new XStream();

        // naming of elements
        xstream.alias("FMPXMLRESULT", FmpXmlResult.class);
        xstream.aliasField("ERRORCODE", FmpXmlResult.class, "errorCode");
        xstream.aliasField("PRODUCT", FmpXmlResult.class, "product");
        xstream.aliasField("DATABASE", FmpXmlResult.class, "dataBase");
        xstream.aliasField("METADATA", FmpXmlResult.class, "metaData");
        xstream.aliasField("RESULTSET", FmpXmlResult.class, "resultSet");
        xstream.aliasField("DATA", Col.class, "data");
        xstream.alias("FIELD", Field.class);
        xstream.alias("ROW", Row.class);
        xstream.alias("COL", Col.class);
        xstream.alias("DATA", Data.class);

        xstream.useAttributeFor(FmpXmlResult.class, "xmlns");

        xstream.useAttributeFor(Product.class, "build");
        xstream.aliasAttribute(Product.class, "build", "BUILD");
        xstream.useAttributeFor(Product.class, "name");
        xstream.aliasAttribute(Product.class, "name", "NAME");
        xstream.useAttributeFor(Product.class, "version");
        xstream.aliasAttribute(Product.class, "version", "VERSION");

        xstream.useAttributeFor(DataBase.class, "dateFormat");
        xstream.aliasAttribute(DataBase.class, "dateFormat", "DATEFORMAT");
        xstream.useAttributeFor(DataBase.class, "layout");
        xstream.aliasAttribute(DataBase.class, "layout", "LAYOUT");
        xstream.useAttributeFor(DataBase.class, "name");
        xstream.aliasAttribute(DataBase.class, "name", "NAME");
        xstream.useAttributeFor(DataBase.class, "records");
        xstream.aliasAttribute(DataBase.class, "records", "RECORDS");
        xstream.useAttributeFor(DataBase.class, "timeFormat");
        xstream.aliasAttribute(DataBase.class, "timeFormat", "TIMEFORMAT");

        xstream.useAttributeFor(Field.class, "emptyOk");
        xstream.aliasAttribute(Field.class, "emptyOk", "EMPTYOK");
        xstream.useAttributeFor(Field.class, "maxRepeat");
        xstream.aliasAttribute(Field.class, "maxRepeat", "MAXREPEAT");
        xstream.useAttributeFor(Field.class, "name");
        xstream.aliasAttribute(Field.class, "name", "NAME");
        xstream.useAttributeFor(Field.class, "type");
        xstream.aliasAttribute(Field.class, "type", "TYPE");

        xstream.addImplicitCollection(ResultSet.class, "rows");
        xstream.addImplicitCollection(Row.class, "cols");
        xstream.addImplicitCollection(Col.class, "dataList");
        xstream.addImplicitCollection(MetaData.class, "fields");

        return xstream;
    }

    public static FmpXmlResult getBaseFmpXmlResultEntity() {
        FmpXmlResult result = new FmpXmlResult();
        result.setErrorCode("0");
        result.setProduct(new Product());
        result.setDataBase(new DataBase());
        result.setMetaData(new MetaData());
        result.setResultSet(new ResultSet());
        return result;
    }

    public static XStream getXStreamInstance() {
        return LazyHolder.INSTANCE;
    }

    /**
     * 
     * @param uiName
     * @return
     */
    public static int getColumnIndex(String uiName) {
        String dbColumnName = uiNameToColumnNameMapping.get(uiName);
        Integer index = columnNameToIndexMapping.get(dbColumnName);
        return index != null ? index : -1;
    }

    public static int getColumnNumber() {
        return dbColumnNames.size();
    }
    
    public static Set<String> getNumberTypeColumns() {
        return Sets.newHashSet("Band", "Laufnummer");
    }

}
