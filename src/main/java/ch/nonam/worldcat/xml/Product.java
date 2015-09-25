package ch.nonam.worldcat.xml;

public class Product {

//	BUILD="03-21-2013" NAME="FileMaker" VERSION="Pro 12.0v4"
	
	private String build;
	private String name;
	private String version;
	
	public Product() {
		this("03-21-2013", "FileMaker", "Pro 12.0v4");
	}
	
	public Product(String build, String name, String version) {
		this.build = build;
		this.name = name;
		this.version = version;
	}

	public String getBuild() {
		return build;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}
	
	
	
}
