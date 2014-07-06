package com.cisco.claims.excel;

import com.cisco.claims.dto.Claim;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Alf on 05.07.2014.
 */
public interface ClaimsExtractor {

	List<Claim> extract(InputStream inputStream);
}
