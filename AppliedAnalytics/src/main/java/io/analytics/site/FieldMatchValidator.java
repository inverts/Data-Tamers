package io.analytics.site;

import io.analytics.aspect.FieldMatch;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;


public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

	private String field1;
	private String field2;
	private String message;
	
	@Override
	public void initialize(final FieldMatch constraints) {
		this.field1 = constraints.first();
		this.field2 = constraints.second();
		this.message = constraints.message();
	}

	@Override
	public boolean isValid(final Object value, final ConstraintValidatorContext context) {
		
		try {
			final Object firstObj = BeanUtils.getProperty(value, this.field1);
			final Object secondObj = BeanUtils.getProperty(value, this.field2);

			if (firstObj == null && secondObj == null || firstObj != null && firstObj.equals(secondObj))
				return true;
			else {
				context.buildConstraintViolationWithTemplate(this.message).addPropertyNode(this.field1).addConstraintViolation();
				return false;
			}
		}
		catch(Exception e)
		{
			// ignore
		}
		
		return true;
	}

}
