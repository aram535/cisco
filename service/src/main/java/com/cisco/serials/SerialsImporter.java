package com.cisco.serials;

import com.cisco.serials.dto.Serial;

import java.util.List;

/**
 * Created by Alf on 27.07.2014.
 */
public interface SerialsImporter {

	public List<Serial> importSerials(String serialsString);
}
