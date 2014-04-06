package com.cisco.validation;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

public class PrePosValidator extends AbstractValidator {

	public void validate(ValidationContext ctx) {
		String type = (String)ctx.getProperties("TYPE")[0].getValue();
		Integer sta = (Integer)ctx.getProperties("STA")[0].getValue();
		
		if(type == null || "".equals(type))
			this.addInvalidMessage(ctx, "type", "You must enter a name");
		
		if(ctx.getProperties("INV_DATE")[0].getValue() == null)
			this.addInvalidMessage(ctx, "date", "You must specify a date");
		
		if(sta == null || sta < 1 || sta > 10)
			this.addInvalidMessage(ctx, "sta", "You must give a priority > 0 && < 10");
	}
}
