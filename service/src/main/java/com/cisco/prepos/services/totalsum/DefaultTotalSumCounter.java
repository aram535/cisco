package com.cisco.prepos.services.totalsum;

import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static com.cisco.prepos.services.discount.utils.DiscountPartCounter.getRoundedDouble;

@Component("totalSumCounter")
public class DefaultTotalSumCounter implements TotalSumCounter {

    @Override
    public double countTotalPosSum(Collection<PreposModel> preposModels) {

        double totalPosSum = 0;

        for (PreposModel preposModel : preposModels) {
            Prepos prepos = preposModel.getPrepos();
            totalPosSum += prepos.getPosSum();
        }

        totalPosSum = getRoundedDouble(totalPosSum);
        return totalPosSum;
    }
}