package ch.nonam.worldcat.xml;

import org.junit.Assert;
import org.junit.Test;

public class MetaDataTest {

	@Test
	public void testOrderingOfFields() {
		MetaData md = new MetaData();

		for (int i = 0; i < md.getFields().size(); i++) {
			String fieldName = md.getFields().get(i).getName();
			String columnName = FileMakerUtility.getDbColumnNames().get(i);
			
			Assert.assertEquals(fieldName, columnName);
		}
	}
}
