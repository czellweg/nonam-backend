package ch.nonam.worldcat.xml;

public class DataBase {

	// DATEFORMAT="D.m.yyyy" LAYOUT="" NAME="BiThek.fmp12" RECORDS="7629"
	// TIMEFORMAT="k:mm:ss "
	private String dateFormat;
	private String layout;
	private String name;
	private String records;
	private String timeFormat;

	public DataBase() {
		this("D.m.yyyy", "", "BiThek.fmp12", "-1", "k:mm:ss ");
	}

	public DataBase(String dateFormat, String layout, String name,
			String records, String timeFormat) {
		this.dateFormat = dateFormat;
		this.layout = layout;
		this.name = name;
		this.records = records;
		this.timeFormat = timeFormat;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRecords() {
		return records;
	}

	public void setRecords(String records) {
		this.records = records;
	}

	public String getTimeFormat() {
		return timeFormat;
	}

	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}

}
