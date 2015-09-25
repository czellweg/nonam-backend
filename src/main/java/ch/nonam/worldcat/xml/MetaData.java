package ch.nonam.worldcat.xml;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * Defines the fields in the FileMaker database. Note that the order of these is
 * crucial as they must correspond to the col->data entries.
 *
 */
public class MetaData {

    private List<Field> fields;
    
    public MetaData() {
		this.fields = Lists.newArrayList();
		
		// defines the ordering of the fields as returned by FileMakerUtility#getDbColumnNames
		FileMakerUtility.getDbColumnNames().forEach(colName -> {
			if (FileMakerUtility.getNumberTypeColumns().contains(colName)) {
			    this.fields.add(new Field(colName, "NUMBER"));
			} else {
			    this.fields.add(new Field(colName));
			}
		});
	}

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

}
