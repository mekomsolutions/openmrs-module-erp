/*
 * Add Copyright
 */
package org.openmrs.module.erp.impl.odoo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.module.erp.BaseOdooModel;
import org.openmrs.module.erp.OdooModel;

public class BaseOdooModelTest {
	
	class TestModel extends BaseOdooModel {}
	
	class OtherTestModel extends BaseOdooModel {}
	
	@Test
	public void equals_shouldReturnFalseIfOtherObjectIsNull() {
		assertFalse(new TestModel().equals(null));
	}
	
	@Test
	public void equals_shouldReturnFalseIfThisObjectHasNoIdAndTheOtherDoes() {
		OdooModel obj = new TestModel();
		assertNull(obj.getId());
		OdooModel other = new TestModel();
		other.setId(2);
		assertFalse(obj.equals(other));
	}
	
	@Test
	public void equals_shouldReturnFalseIfThisObjectHasAnIdAndTheOtherDoesNot() {
		OdooModel obj = new TestModel();
		obj.setId(1);
		OdooModel other = new TestModel();
		assertNull(other.getId());
		assertFalse(obj.equals(other));
	}
	
	@Test
	public void equals_shouldReturnFalseIfOtherObjectHasTheSameIdButOfDifferentType() {
		final Integer id = 1;
		OdooModel obj = new TestModel();
		obj.setId(id);
		OdooModel other = new OtherTestModel();
		other.setId(id);
		assertFalse(obj.equals(other));
	}
	
	@Test
	public void equals_shouldReturnFalseForObjectsOfTheSameTypeButDifferentIds() {
		OdooModel obj = new TestModel();
		obj.setId(1);
		OdooModel other = new TestModel();
		other.setId(2);
		assertFalse(obj.equals(other));
	}
	
	@Test
	public void equals_shouldReturnFalseForDifferentObjectsAndBothHaveNullIds() {
		OdooModel obj = new TestModel();
		assertNull(obj.getId());
		OdooModel other = new TestModel();
		assertNull(other.getId());
		assertFalse(obj.equals(other));
	}
	
	@Test
	public void equals_shouldReturnTrueForSameObjectsAndBothHaveNullIds() {
		OdooModel obj = new TestModel();
		assertNull(obj.getId());
		assertTrue(obj.equals(obj));
	}
	
	@Test
	public void hashCode_shouldReturnTheId() {
		final int id = 3;
		OdooModel obj = new TestModel();
		obj.setId(id);
		Assert.assertEquals(id, obj.hashCode());
	}
	
	@Test
	public void hashCode_shouldDelegateToSuperclassIfTheObjectHasNoId() {
		OdooModel obj = new TestModel();
		Assert.assertNotNull(obj.hashCode());
	}
	
}
