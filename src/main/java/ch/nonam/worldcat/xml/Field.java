package ch.nonam.worldcat.xml;

public class Field {

//	EMPTYOK="YES" MAXREPEAT="1" NAME="KultN" TYPE="TEXT"
	private String emptyOk;
	private String maxRepeat;
	private String name;
	private String type;

	public Field(String name) {
		this("YES", "1", name, "TEXT");
	}
	
	public Field(String name, String type) {
	    this("YES", "1", name, type);
	}
	
	public Field(String emptyOk, String maxRepeat, String name, String type) {
		this.emptyOk = emptyOk;
		this.maxRepeat = maxRepeat;
		this.name = name;
		this.type = type;
	}

	public String getEmptyOk() {
		return emptyOk;
	}

	public String getMaxRepeat() {
		return maxRepeat;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}
	
	
}
