package com.cisco.serials.dao;

import com.cisco.serials.dto.Serial;

import java.util.List;

/**
 * Created by Alf on 27.07.2014.
 */
public interface SerialsDao {

	public void saveOrUpdate(List<Serial> serials);

	List<Serial> getAllSerials();
}
