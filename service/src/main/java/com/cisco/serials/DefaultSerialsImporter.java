package com.cisco.serials;

import com.cisco.serials.dto.Serial;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Alf on 27.07.2014.
 */
@Component("serialsImporter")
public class DefaultSerialsImporter implements SerialsImporter {

	@Override
	public List<Serial> importSerials(String serialsString) {

		List<String> serialsStrings = serialsStrings = Lists.newArrayList(StringUtils.split(serialsString, "\n\r"));

		List<Serial> serials = Lists.newArrayList();
		for (String serial : serialsStrings) {
			serials.add(new Serial(serial));
		}

		return serials;
	}
}
