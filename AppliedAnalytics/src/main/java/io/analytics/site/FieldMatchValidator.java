package io.analytics.site;

import io.analytics.aspect.FieldMatch;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;


public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

	private String field1;
	private String field2;
	
	@Override
	public void initialize(final FieldMatch constraints) {
		this.field1 = constraints.first();
		this.field2 = constraints.second();
		
	}

	@Override
	public boolean isValid(final Object value, final ConstraintValidatorContext context) {
		
		try {
			final Object firstObj = BeanUtils.getProperty(value, this.field1);
			final Object secondObj = BeanUtils.getProperty(value, this.field2);
			
			return firstObj == null && secondObj == null || firstObj != null && firstObj.equals(secondObj);
		}
		catch(Exception e)
		{
			// ignore
		}
		
		return true;
	}

}
