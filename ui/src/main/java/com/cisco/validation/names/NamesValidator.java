package com.cisco.validation.names;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

/**
 * Created by Alf on 08.04.14.
 */

public class NamesValidator extends AbstractValidator {

	public void validate(ValidationContext ctx) {

		String name = (String) ctx.getProperties("name")[0].getValue();
		String city = (String) ctx.getProperties("city")[0].getValue();

		if (name == null || "".equals(name)) {
			this.addInvalidMessage(ctx, "name", "You must enter a name");
		}

		if (city == null || "".equals(city)) {
			this.addInvalidMessage(ctx, "city", "You must enter a city");
		}

	}
}