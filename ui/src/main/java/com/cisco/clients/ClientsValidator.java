package com.cisco.clients;

import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by Alf on 08.04.14.
 */

public class ClientsValidator extends AbstractValidator {

    public void validate(ValidationContext ctx) {

        String name = (String) ctx.getProperties("name")[0].getValue();
        String city = (String) ctx.getProperties("city")[0].getValue();

        if (isBlank(name)) {
            this.addInvalidMessage(ctx, "name", "You must enter a name");
        }

        if (isBlank(city)) {
            this.addInvalidMessage(ctx, "city", "You must enter a city");
        }

    }
}