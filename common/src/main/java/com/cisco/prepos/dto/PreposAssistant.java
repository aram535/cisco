package com.cisco.prepos.dto;

import com.google.common.base.Function;
import com.google.common.collect.*;

/**
 * Created by Alf on 05.07.2014.
 */
public class PreposAssistant {

	public static Table<String, String, Prepos> asTable(Iterable<Prepos> preposes) {

		Table<String, String, Prepos> preposesTable = HashBasedTable.create();

		for (Prepos prepos : preposes) {
			preposesTable.put(prepos.getPartNumber(), prepos.getShippedBillNumber(), prepos);
		}

		return preposesTable;
	}

	public static ListMultimap<String, Prepos> asListMultimap(Iterable<Prepos> preposes) {

		ListMultimap<String, Prepos> preposesMultimap = Multimaps.index(preposes, new Function<Prepos, String>() {
			@Override
			public String apply(Prepos input) {
				return input.getPosreadyId();
			}
		});

		return ImmutableListMultimap.copyOf(preposesMultimap);
	}
}
