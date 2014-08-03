package com.cisco.prepos.services.filter;

import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import com.cisco.prepos.model.PreposRestrictions;
import com.google.common.collect.Lists;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.Timestamp;
import java.util.List;

import static com.cisco.prepos.services.filter.DefaultPreposFilter.GOOD;
import static com.cisco.testtools.TestObjects.*;
import static com.cisco.testtools.TestObjects.PreposFactory.newPrepos;
import static junitparams.JUnitParamsRunner.$;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * User: Rost
 * Date: 18.05.2014
 * Time: 15:02
 */

@RunWith(JUnitParamsRunner.class)
public class DefaultPreposFilterTest {


	private PreposFilter preposFilter = new DefaultPreposFilter();

    private Timestamp fromDate = getFromDate();

    private Timestamp toDate = getToDate();

    @Test
    public void returnsEmptyListIfInputIsEmpty() {
        PreposRestrictions preposRestrictions = new PreposRestrictions(PARTNER_NAME, SHIPPED_BILL_NUMBER, toDate, fromDate, PART_NUMBER, GOOD, ACCOUNT_MANAGER_NAME);
        List<PreposModel> filteredPreposes = preposFilter.filter(Lists.<PreposModel>newArrayList(), preposRestrictions);
        assertThat(filteredPreposes).isNotNull().isEmpty();
    }

    @Test
    @Parameters(method = "restrictions")
    public void returnsListsAccordingToRestrictions(PreposRestrictions restrictions) {
        Prepos prepos = newPrepos();
        List<PreposModel> preposModels = Lists.newArrayList(new PreposModel(prepos, null, null));
        List<PreposModel> filteredPreposes = preposFilter.filter(preposModels, restrictions);
        assertThat(filteredPreposes).isNotNull().hasSize(1);
        assertThat(filteredPreposes).isEqualTo(preposModels);
    }


    private Object[] restrictions() {
        return $(new PreposRestrictions(PARTNER_NAME, SHIPPED_BILL_NUMBER, toDate, fromDate, PART_NUMBER, GOOD, ACCOUNT_MANAGER_NAME),
                new PreposRestrictions(PARTNER_NAME, SHIPPED_BILL_NUMBER, toDate, null, PART_NUMBER, GOOD, ACCOUNT_MANAGER_NAME),
                new PreposRestrictions(null, SHIPPED_BILL_NUMBER, toDate, null, PART_NUMBER, GOOD, ACCOUNT_MANAGER_NAME),
                new PreposRestrictions(null, null, toDate, null, PART_NUMBER, GOOD, ACCOUNT_MANAGER_NAME),
                new PreposRestrictions(null, null, null, null, PART_NUMBER, GOOD, ACCOUNT_MANAGER_NAME),
                new PreposRestrictions(null, null, null, null, null, GOOD, ACCOUNT_MANAGER_NAME),
                new PreposRestrictions(null, null, null, null, null, null, ACCOUNT_MANAGER_NAME),
                new PreposRestrictions(null, SHIPPED_BILL_NUMBER.substring(1), null, null, null, null, null));
    }

    private Timestamp getFromDate() {
        DateTime dateTime = new DateTime(SHIPPED_DATE.getTime());
        DateTime dateTimeFrom = dateTime.minusDays(1);
        return new Timestamp(dateTimeFrom.getMillis());
    }

    private Timestamp getToDate() {
        DateTime dateTime = new DateTime(SHIPPED_DATE.getTime());
        DateTime dateTimeTo = dateTime.plusDays(1);
        return new Timestamp(dateTimeTo.getMillis());
    }
}
