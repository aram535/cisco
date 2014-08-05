package com.cisco.prepos.services.dart;

import com.cisco.darts.dto.Dart;
import com.cisco.exception.CiscoException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import static com.cisco.darts.dto.DartAssistant.EMPTY_DART;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * User: Rost
 * Date: 14.05.2014
 * Time: 0:09
 */
@Component
public class DefaultDartSelector implements DartSelector {

    @Override
    public Dart selectDart(Map<String, Dart> suitableDarts, String secondPromo) {

        if (isEmpty(suitableDarts)) {
            throw new CiscoException("Cannot select dart among empty suitable darts. Should always have EMPTY_DART");
        }

        if (secondPromo == null) {
            Collection<Dart> values = suitableDarts.values();
            Iterator<Dart> iterator = values.iterator();
            Dart firstSuitableDart = iterator.next();
            return firstSuitableDart;
        }

        Dart dart = suitableDarts.get(secondPromo);
        if (dart != null) {
            return dart;
        }

        return EMPTY_DART;

    }
}
