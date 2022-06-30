package org.openmrs.module.erp;

import java.lang.reflect.Field;

import org.openmrs.api.APIException;

public class Utils {
	
	/**
	 * Sets a property value via reflection
	 * 
	 * @param target the object with the property to set
	 * @param propertyName the property name
	 * @param value the value to set
	 */
	public static void setPropertyValue(Object target, String propertyName, Object value) {
		Boolean isAccessible = null;
		Field field = null;
		try {
			field = target.getClass().getDeclaredField(propertyName);
			field.setAccessible(true);
			field.set(target, value);
		}
		catch (Exception e) {
			throw new APIException("Failed to set property value", e);
		}
		finally {
			if (field != null && isAccessible != null) {
				field.setAccessible(isAccessible);
			}
		}
	}
	
}
