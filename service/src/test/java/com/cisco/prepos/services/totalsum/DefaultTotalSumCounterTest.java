package com.cisco.prepos.services.totalsum;

import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static com.cisco.testtools.TestObjects.PreposFactory.newPrepos;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * User: Rost
 * Date: 25.05.2014
 * Time: 15:36
 */

public class DefaultTotalSumCounterTest {

    private TotalSumCounter totalSumCounter = new DefaultTotalSumCounter();

    @Test
    public void thatCountsTotalSumOfInput() {
        Prepos firstPrepos = newPrepos();
        firstPrepos.setPosSum(10.237);
        Prepos secondPrepos = newPrepos();
        secondPrepos.setPosSum(30.5);

        PreposModel firstModel = new PreposModel(firstPrepos, null, null);
        PreposModel secondModel = new PreposModel(secondPrepos, null, null);
        List<PreposModel> models = Lists.newArrayList(firstModel, secondModel);
        double posSum = totalSumCounter.countTotalPosSum(models);
        assertThat(posSum).isEqualTo(40.74);
    }

    @Test
    public void thatTotalSumOfEmptyInputIsZero() {
        List<PreposModel> models = Lists.newArrayList();
        double posSum = totalSumCounter.countTotalPosSum(models);
        assertThat(posSum).isEqualTo(0.0);
    }
}
