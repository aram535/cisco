package com.cisco.serials.service;

import com.cisco.serials.dto.Serial;

import java.util.List;

/**
 * Created by Alf on 10.08.2014.
 */
public interface SerialFilter {

	public List<Serial> filter(List<Serial> allSerials, String serial);
}
