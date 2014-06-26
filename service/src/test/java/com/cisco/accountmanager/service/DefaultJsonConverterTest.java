package com.cisco.accountmanager.service;

import org.junit.Test;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * User: Rost
 * Date: 26.06.2014
 * Time: 20:37
 */
public class DefaultJsonConverterTest {

    private final String firstItem = "first item";
    private final String secondItem = "second item";
    private final String expectedJsonRepresentation = "[\"" + firstItem + "\",\"" + secondItem + "\"]";

    private JsonConverter jsonConverter = new DefaultJsonConverter();

    @Test
    public void thatMakesCorrectJsonRepresentationFromListOfStrings() {
        String jsonItemsRepresentation = jsonConverter.toJson(createItemsList());
        assertThat(jsonItemsRepresentation).isEqualTo(expectedJsonRepresentation);
    }

    @Test
    public void thatParseListOfStringsFromJson() {
        List<String> parsedItems = jsonConverter.fromJson(expectedJsonRepresentation);
        assertThat(parsedItems)
                .isNotNull()
                .isEqualTo(createItemsList());
    }

    private List<String> createItemsList() {
        return newArrayList(firstItem, secondItem);
    }
}
