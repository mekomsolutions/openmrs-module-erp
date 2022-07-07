/*
 * Add Copyright
 */
package org.openmrs.module.erp.impl.odoo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class BaseOdooModelTest {
	
	class TestModel extends BaseOdooModel {
		
		TestModel(Integer id) {
			this.id = id;
		}
	}
	
	class OtherTestModel extends BaseOdooModel {
		
		OtherTestModel(Integer id) {
			this.id = id;
		}
	}
	
	@Test
	public void equals_shouldReturnFalseIfOtherObjectIsNull() {
		assertFalse(new TestModel(null).equals(null));
	}
	
	@Test
	public void equals_shouldReturnFalseIfThisObjectHasNoIdAndTheOtherDoes() {
		OdooModel obj = new TestModel(null);
		assertNull(obj.getId());
		OdooModel other = new TestModel(2);
		assertFalse(obj.equals(other));
	}
	
	@Test
	public void equals_shouldReturnFalseIfThisObjectHasAnIdAndTheOtherDoesNot() {
		OdooModel obj = new TestModel(1);
		OdooModel other = new TestModel(null);
		assertNull(other.getId());
		assertFalse(obj.equals(other));
	}
	
	@Test
	public void equals_shouldReturnFalseIfOtherObjectHasTheSameIdButOfDifferentType() {
		final Integer id = 1;
		OdooModel obj = new TestModel(id);
		OdooModel other = new OtherTestModel(id);
		assertFalse(obj.equals(other));
	}
	
	@Test
	public void equals_shouldReturnFalseForObjectsOfTheSameTypeButDifferentIds() {
		OdooModel obj = new TestModel(1);
		OdooModel other = new TestModel(2);
		assertFalse(obj.equals(other));
	}
	
	@Test
	public void equals_shouldReturnFalseForDifferentObjectsAndBothHaveNullIds() {
		OdooModel obj = new TestModel(null);
		assertNull(obj.getId());
		OdooModel other = new TestModel(null);
		assertNull(other.getId());
		assertFalse(obj.equals(other));
	}
	
	@Test
	public void equals_shouldReturnTrueForSameObjectsAndBothHaveNullIds() {
		OdooModel obj = new TestModel(null);
		assertNull(obj.getId());
		assertTrue(obj.equals(obj));
	}
	
	@Test
	public void hashCode_shouldReturnTheId() {
		final int id = 3;
		OdooModel obj = new TestModel(id);
		Assert.assertEquals(id, obj.hashCode());
	}
	
	@Test
	public void hashCode_shouldDelegateToSuperclassIfTheObjectHasNoId() {
		OdooModel obj = new TestModel(null);
		Assert.assertNotNull(obj.hashCode());
	}
	
	@Test
	public void getValue_shouldReturnTheFieldValue() {
		Map data = new HashMap();
		data.put("firstName", "John");
		data.put("lastName", "Doe");
		OdooModel obj = new TestModel(null);
		obj.setData(data);
		Assert.assertEquals("John", obj.getValue("firstName"));
		Assert.assertEquals("Doe", obj.getValue("lastName"));
	}
	
}
