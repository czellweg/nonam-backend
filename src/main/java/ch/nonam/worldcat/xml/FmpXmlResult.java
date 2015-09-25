package ch.nonam.worldcat.xml;

public class FmpXmlResult {
	
	private String xmlns;
	
	private String errorCode;
	private Product product;
	private DataBase dataBase;
	private MetaData metaData;
	private ResultSet resultSet;

	public FmpXmlResult() {
		this.xmlns = "http://www.filemaker.com/fmpxmlresult";
	}

	public String getXmlns() {
		return xmlns;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public DataBase getDataBase() {
		return dataBase;
	}

	public void setDataBase(DataBase dataBase) {
		this.dataBase = dataBase;
	}

	public MetaData getMetaData() {
		return metaData;
	}

	public void setMetaData(MetaData metaData) {
		this.metaData = metaData;
	}

	public ResultSet getResultSet() {
		return resultSet;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}
	
}
