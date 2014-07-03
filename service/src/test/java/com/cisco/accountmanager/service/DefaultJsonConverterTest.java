package com.cisco.accountmanager.service;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static junitparams.JUnitParamsRunner.$;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * User: Rost
 * Date: 26.06.2014
 * Time: 20:37
 */
@RunWith(JUnitParamsRunner.class)
public class DefaultJsonConverterTest {

    private static final String EMPTY_STRING = "";
    private final String firstItem = "first item";
    private final String secondItem = "second item";
    private final String expectedJsonRepresentation = "[\"" + firstItem + "\",\"" + secondItem + "\"]";

    private JsonConverter jsonConverter = new DefaultJsonConverter();

    @Test
    @Parameters(method = "toJsonParameters")
    public void thatMakesCorrectJsonRepresentationFromListOfStrings(Set<String> items, String jsonRepresentation) {
        String jsonItemsRepresentation = jsonConverter.toJson(items);
        assertThat(jsonItemsRepresentation).isEqualTo(jsonItemsRepresentation);
    }

    @Test
    @Parameters(method = "fromJsonParameters")
    public void thatParseListOfStringsFromJson(String json, Set<String> expectedResult) {
        Set<String> parsedItems = jsonConverter.fromJson(json);
        assertThat(parsedItems)
                .isNotNull()
                .isEqualTo(expectedResult);
    }

    private Object[] fromJsonParameters() {
        return $($(expectedJsonRepresentation, createItemsList()), $(null, newHashSet()), $(EMPTY_STRING, newHashSet()), $("broken json", newHashSet()));
    }

    private Object[] toJsonParameters() {
        return $($(createItemsList(), expectedJsonRepresentation), $(newHashSet(), EMPTY_STRING), $(null, EMPTY_STRING));
    }

    private Set<String> createItemsList() {
        return newHashSet(firstItem, secondItem);
    }
}
