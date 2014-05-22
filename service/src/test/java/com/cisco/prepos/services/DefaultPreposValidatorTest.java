package com.cisco.prepos.services;

import com.cisco.darts.dto.Dart;
import com.cisco.exception.PreposValidationException;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.cisco.testtools.TestObjects.DartsFactory.newDart;
import static com.cisco.testtools.TestObjects.PreposFactory.newPrepos;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.fail;

/**
 * User: Rost
 * Date: 22.05.2014
 * Time: 23:41
 */
public class DefaultPreposValidatorTest {

    private final Dart dart = newDart();
    private final Map<String, Dart> suitableDarts = ImmutableMap.of(dart.getAuthorizationNumber(), dart);
    private DefaultPreposValidator preposValidator = new DefaultPreposValidator();

    @Test
    public void thatThrowsExceptionWithNotValidPreposesDueToDartQuantityIfSuchExist() {
        List<PreposModel> preposModels = Lists.newArrayList();
        int dartQuantity = dart.getQuantity();

        Prepos firstPrepos = newPrepos();
        firstPrepos.setQuantity(dartQuantity);
        Prepos secondPrepos = newPrepos();
        secondPrepos.setQuantity(1);

        preposModels.add(new PreposModel(firstPrepos, suitableDarts, dart));
        preposModels.add(new PreposModel(secondPrepos, suitableDarts, dart));

        try {
            preposValidator.validate(preposModels);
        } catch (PreposValidationException ex) {
            Map<Dart, Collection<Prepos>> failedPreposes = ex.getFailedPreposes();
            assertThat(failedPreposes).hasSize(1);
            Collection<Prepos> failedByFirstDartPreposes = failedPreposes.get(dart);
            assertThat(failedByFirstDartPreposes).hasSize(2).contains(firstPrepos, secondPrepos);
            return;
        }

        fail("PreposValidationException was not thrown");
    }

    @Test
    public void thatNotThrowsExceptionIfAllPreposesAreOk() {
        List<PreposModel> preposModels = Lists.newArrayList();
        int dartQuantity = dart.getQuantity();

        Prepos firstPrepos = newPrepos();
        firstPrepos.setQuantity(dartQuantity - 1);
        Prepos secondPrepos = newPrepos();
        secondPrepos.setQuantity(1);

        preposModels.add(new PreposModel(firstPrepos, suitableDarts, dart));
        preposModels.add(new PreposModel(secondPrepos, suitableDarts, dart));
        preposValidator.validate(preposModels);
    }
}
