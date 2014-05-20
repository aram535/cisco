package com.cisco.darts.excel;

import com.cisco.darts.dao.DartsDao;
import com.cisco.darts.dto.Dart;
import com.cisco.exception.CiscoException;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

/**
 * Created by Alf on 20.05.2014.
 */
@Component("dartsImporter")
public class DefaultDartsImporter implements DartsImporter {

	@Autowired
	private DartsExtractor dartsExtractor;

	@Autowired
	private DartsDao dartsDao;

	@Transactional
	@Override
	public void importDarts(InputStream inputStream) {

		List<Dart> darts = dartsExtractor.extract(inputStream);

		if (CollectionUtils.isEmpty(darts)) {
			throw new CiscoException("Exported from excel dart are null or empty. Please, check file.");
		}

		Set<Dart> uniqueDarts = Sets.newLinkedHashSet(darts);

		dartsDao.deleteAll();
		dartsDao.saveAll(Lists.newArrayList(uniqueDarts));
	}
}
