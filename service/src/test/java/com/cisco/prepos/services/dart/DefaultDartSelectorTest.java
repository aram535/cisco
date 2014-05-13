package com.cisco.prepos.services.dart;

import com.cisco.darts.dto.Dart;
import com.cisco.exception.CiscoException;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Map;

import static com.cisco.darts.dto.DartConstants.BLANK_AUTHORIZATION_NUMBER;
import static com.cisco.darts.dto.DartConstants.EMPTY_DART;
import static com.cisco.testtools.TestObjects.AUTHORIZATION_NUMBER;
import static com.cisco.testtools.TestObjects.DartsFactory.newDart;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * User: Rost
 * Date: 14.05.2014
 * Time: 0:09
 */
public class DefaultDartSelectorTest {

    private static final Dart DART = newDart();
    private static final String OTHER_AUTHORIZATION_NUMBER = "other authorizationNumber";
    private DefaultDartSelector defaultDartSelector = new DefaultDartSelector();

    @Test
    public void thatReturnsEmptyDartIfSecondPromoIsBlankAuthorizationNumber() {

        Dart dart = defaultDartSelector.selectDart(createSuitableDarts(), BLANK_AUTHORIZATION_NUMBER);
        assertThat(dart).isEqualTo(EMPTY_DART);
    }

    @Test
    public void thatReturnsDartBySecondPromoIfItIsNotBlankAuthorizationNumber() {
        Dart dart = defaultDartSelector.selectDart(createSuitableDarts(), AUTHORIZATION_NUMBER);
        assertThat(dart).isEqualTo(DART);
    }

    @Test
    public void thatReturnsEmptyDartIfSecondPromoIsNotBlankAuthorizationNumberButNotFoundAmongSuitableDarts() {
        Dart dart = defaultDartSelector.selectDart(createSuitableDarts(), OTHER_AUTHORIZATION_NUMBER);
        assertThat(dart).isEqualTo(EMPTY_DART);
    }

    @Test
    public void thatReturnsFirstSuitableDartIfSecondPromoIsNull() {
        Dart dart = defaultDartSelector.selectDart(createSuitableDarts(), null);
        assertThat(dart).isEqualTo(DART);
    }

    @Test(expected = CiscoException.class)
    public void thatThrowsCiscoExceptionIfSuitableDartsAreEmpty() {
        defaultDartSelector.selectDart(ImmutableMap.<String, Dart>of(), AUTHORIZATION_NUMBER);
    }

    private Map<String, Dart> createSuitableDarts() {
        return ImmutableMap.of(AUTHORIZATION_NUMBER, DART);
    }


}
