package com.cisco.serials.service;

import com.cisco.serials.dto.Serial;

import java.util.List;
import java.util.Set;

/**
 * Created by Alf on 27.07.2014.
 */
public interface SerialsService {

	public void saveOrUpdate(List<Serial> serials);

	public List<Serial> getAllSerials();

	public Set<String> getAllSerialsStrings();
}
