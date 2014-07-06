package com.cisco.claims;

import java.io.InputStream;

/**
 * Created by Alf on 05.07.2014.
 */
public interface ClaimsImporter {

	void importClaims(InputStream inputStream);
}
