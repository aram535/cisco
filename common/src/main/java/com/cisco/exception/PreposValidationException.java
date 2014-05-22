package com.cisco.exception;

import com.cisco.darts.dto.Dart;
import com.cisco.prepos.dto.Prepos;

import java.util.Collection;
import java.util.Map;

/**
 * User: Rost
 * Date: 23.05.2014
 * Time: 0:02
 */
public class PreposValidationException extends CiscoException {

    private Map<Dart, Collection<Prepos>> failedPreposes;

    public PreposValidationException(String message, Map<Dart, Collection<Prepos>> failedPreposes) {
        super(message);
        this.failedPreposes = failedPreposes;
    }

    public Map<Dart, Collection<Prepos>> getFailedPreposes() {
        return failedPreposes;
    }
}
