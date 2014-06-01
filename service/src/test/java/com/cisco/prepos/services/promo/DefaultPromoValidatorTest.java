package com.cisco.prepos.services.promo;

import com.cisco.promos.dto.Promo;
import org.junit.Test;

import java.sql.Timestamp;

import static com.cisco.testtools.TestObjects.PromosFactory.newPromo;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * User: Rost
 * Date: 01.06.2014
 * Time: 17:15
 */
public class DefaultPromoValidatorTest {

    private PromoValidator promoValidator = new DefaultPromoValidator();

    @Test
    public void thatPromoIsRelevantIfItIsNotNullAndShippedDateSuits() {
        Promo relevantPromo = newPromo();
        relevantPromo.setEndDate(new Timestamp(100L));
        boolean relevant = promoValidator.isValid(relevantPromo, 99L);
        assertThat(relevant).isEqualTo(true);
    }

    @Test
    public void thatPromoIsNotRelevantIfItIsNull() {
        boolean relevant = promoValidator.isValid(null, 99L);
        assertThat(relevant).isEqualTo(false);
    }

    @Test
    public void thatPromoIsNotRelevantIfItIsNotNullButIsExpiredForPreposShippedDate() {
        Promo relevantPromo = newPromo();
        relevantPromo.setEndDate(new Timestamp(100L));
        boolean relevant = promoValidator.isValid(relevantPromo, 101L);
        assertThat(relevant).isEqualTo(false);
    }
}
