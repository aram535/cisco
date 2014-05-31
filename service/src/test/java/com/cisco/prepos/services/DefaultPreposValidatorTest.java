package com.cisco.prepos.services;

import com.cisco.darts.dto.Dart;
import com.cisco.exception.CiscoException;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Map;

import static com.cisco.testtools.TestObjects.DartsFactory.newDart;
import static com.cisco.testtools.TestObjects.PreposFactory.newPrepos;

/**
 * User: Rost
 * Date: 22.05.2014
 * Time: 23:41
 */
public class DefaultPreposValidatorTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();

    private final Dart dart = newDart();
    private final Map<String, Dart> suitableDarts = ImmutableMap.of(dart.getAuthorizationNumber(), dart);
    private DefaultPreposValidator preposValidator = new DefaultPreposValidator();

	@Test
	public void thatThrowsExceptionWhenDartQuantityIsNotEnoughForCurrentPrepos() {
		Prepos firstPrepos = newPrepos();
		firstPrepos.setQuantity(dart.getQuantity());
		Prepos secondPrepos = newPrepos();
		secondPrepos.setQuantity(1);

		PreposModel firstPreposModel = new PreposModel(firstPrepos, suitableDarts, dart);
		PreposModel secondPreposModel = new PreposModel(secondPrepos, suitableDarts, dart);

		exception.expect(CiscoException.class);
		exception.expectMessage("Not enough quantity left in Dart. Review other preposes where current Dart is selected");

		ArrayList<PreposModel> preposModels = Lists.newArrayList(firstPreposModel, secondPreposModel);
		preposValidator.validateDartQuantity(preposModels, firstPreposModel);
	}

	@Test
	public void thatNotThrowsExceptionWhenDartQuantityIsEnoughForCurrentPrepos() {
		Prepos firstPrepos = newPrepos();
		firstPrepos.setQuantity(dart.getQuantity() - 1);
		Prepos secondPrepos = newPrepos();
		secondPrepos.setQuantity(1);

		PreposModel firstPreposModel = new PreposModel(firstPrepos, suitableDarts, dart);
		PreposModel secondPreposModel = new PreposModel(secondPrepos, suitableDarts, dart);

		ArrayList<PreposModel> preposModels = Lists.newArrayList(firstPreposModel, secondPreposModel);
		preposValidator.validateDartQuantity(preposModels, firstPreposModel);
	}

}
