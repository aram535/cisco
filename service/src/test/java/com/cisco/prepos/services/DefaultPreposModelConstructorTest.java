package com.cisco.prepos.services;

import com.cisco.darts.dto.Dart;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import com.cisco.prepos.services.recount.PreposRecounter;
import com.google.common.collect.Lists;
import org.javatuples.Quartet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

import static com.cisco.darts.dto.DartAssistant.EMPTY_DART;
import static com.cisco.testtools.TestObjects.AUTHORIZATION_NUMBER;
import static com.cisco.testtools.TestObjects.DartsFactory.newDart;
import static com.cisco.testtools.TestObjects.PreposFactory.*;
import static com.google.common.collect.ImmutableMap.of;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * User: Rost
 * Date: 05.05.2014
 * Time: 0:31
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultPreposModelConstructorTest {

    private static final Prepos NEW_SIMPLE_PREPOS = newSimplePrepos();

    @InjectMocks
    private PreposModelConstructor preposModelConstructor = new DefaultPreposModelConstructor();

    @Mock
    private PreposRecounter preposRecounter;


    private List<Prepos> preposes = Lists.newArrayList(NEW_SIMPLE_PREPOS);
    private Map<String, Dart> suitableDarts;
    private final Dart dart = newDart();

    @Before
    public void init() {
        suitableDarts = of(AUTHORIZATION_NUMBER, dart);
        Quartet<Prepos, Map<String, Dart>, Dart, Boolean> recounted = new Quartet(NEW_SIMPLE_PREPOS, suitableDarts, dart, Boolean.FALSE);
        List<Quartet<Prepos, Map<String, Dart>, Dart, Boolean>> recountedList = Lists.<Quartet<Prepos, Map<String, Dart>, Dart, Boolean>>newArrayList(recounted);

        when(preposRecounter.recount(preposes)).thenReturn(recountedList);
    }

    @Test
    public void thatConstructsModelAccordingToRecounter() {
        List<PreposModel> preposModels = preposModelConstructor.construct(preposes);

        PreposModel preposModel = new PreposModel(NEW_SIMPLE_PREPOS, suitableDarts, dart);
        preposModel.setFirstPromoValid(false);
        assertThat(preposModels).isEqualTo(Lists.newArrayList(preposModel));
    }


    @Test
    public void thatConstructsEmptyModelsListIfRecounterReturnsEmptyList() {
        when(preposRecounter.recount(preposes)).thenReturn(Lists.<Quartet<Prepos, Map<String, Dart>, Dart, Boolean>>newArrayList());
        List<PreposModel> preposModels = preposModelConstructor.construct(preposes);
        assertThat(preposModels).isNotNull().isEmpty();
    }

	@Test
	public void thatPreposModelBeingCorrectlyConvertedToPreposes() {

		List<PreposModel> preposModels = getNewPreposModels();

		List<Prepos> preposes = preposModelConstructor.getPreposes(preposModels);

		assertThat(preposes).isEqualTo(newPreposList());
	}

    private List<PreposModel> getNewPreposModels() {
        Prepos prepos = newPrepos();
        Map<String, Dart> suitableDarts = of("", EMPTY_DART);
        PreposModel preposModel = new PreposModel(prepos, suitableDarts, EMPTY_DART);

        return Lists.newArrayList(preposModel);
    }
}
