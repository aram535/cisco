package com.cisco.prepos.services;

import com.cisco.darts.dto.Dart;
import com.cisco.exception.CiscoException;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import com.cisco.prepos.services.dart.SuitableDartsProvider;
import com.cisco.prepos.services.discount.DiscountProvider;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.promos.dto.Promo;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.cisco.prepos.model.PreposModel.EMPTY_DART;

/**
 * User: Rost
 * Date: 08.05.2014
 * Time: 0:14
 */
@Component
public class DefaultPreposModelConstructor implements PreposModelConstructor {

	@Autowired
	private DiscountProvider discountProvider;

	@Autowired
	private SuitableDartsProvider suitableDartsProvider;

	@Value("${good.threshold}")
	private double threshold;

	@Override
	public void setTreshhold(double treshhold) {
		this.threshold = treshhold;
	}

	@Override
	public double getTreshhold() {
		return threshold;
	}

    @Override
    public List<PreposModel> construct(List<Prepos> preposes, Map<String, Pricelist> pricelistsMap, Map<String, Promo> promosMap, Table<String, String, Dart> dartsTable) {

	    List<PreposModel> preposModels = Lists.newArrayList();

	    for (Prepos prepos : preposes) {

		    Map<String, Dart> suitableDarts = suitableDartsProvider.getDarts(
				    prepos.getPartNumber(), prepos.getPartnerName(), prepos.getQuantity(), prepos.getShippedDate(), dartsTable);

		    String secondPromo = prepos.getSecondPromo();
		    Dart selectedSuitableDart = getSelectedSuitableDart(suitableDarts, secondPromo, prepos.getQuantity());
		    prepos.setSecondPromo(selectedSuitableDart.getAuthorizationNumber());

			String firstPromo = getFirstPromo(promosMap, prepos.getPartNumber());
		    double gpl = getGpl(prepos.getPartNumber(), pricelistsMap);
		    double saleDiscount = getSaleDiscount(prepos.getSalePrice(), gpl);

		    Triplet<String, String, String> discountInfo = new Triplet<>(prepos.getPartNumber(), firstPromo, prepos.getSecondPromo());
		    double buyDiscount = discountProvider.getDiscount(discountInfo, dartsTable, promosMap, pricelistsMap);
		    double buyPrice = getBuyPrice(buyDiscount, gpl);
		    boolean okStatus = getOkStatus(prepos.getSalePrice(), buyPrice, threshold);

		    prepos.setFirstPromo(firstPromo);
		    prepos.setEndUser(selectedSuitableDart.getEndUserName());
		    prepos.setSaleDiscount(saleDiscount);
		    prepos.setBuyDiscount(buyDiscount);
		    prepos.setBuyPrice(buyPrice);
		    double posSum = (double) Math.round(prepos.getBuyPrice() * prepos.getQuantity() * 100) / 100;
		    prepos.setPosSum(posSum);
		    prepos.setOk(okStatus);

		    PreposModel preposModel = new PreposModel(prepos, suitableDarts, selectedSuitableDart);
		    preposModels.add(preposModel);
	    }

        return preposModels;
    }

	@Override
	public List<Prepos> getPreposesFromPreposModels(List<PreposModel> preposModels) {

		List<Prepos> preposes = Lists.newArrayList();

		for (PreposModel preposModel : preposModels) {
			preposes.add(preposModel.getPrepos());
		}

		return preposes;
	}

	@Override
	public void recountPreposPrices(PreposModel preposModel, Map<String, Pricelist> pricelistsMap, Map<String, Promo> promosMap, Table<String, String, Dart> dartsTable) {

		String firstPromo = preposModel.getPrepos().getFirstPromo();
		String partNumber = preposModel.getPrepos().getPartNumber();
		String secondPromo = preposModel.getPrepos().getSecondPromo();

		double gpl = getGpl(partNumber, pricelistsMap);
		double salePrice = preposModel.getPrepos().getSalePrice();

		Triplet<String, String, String> discountInfo = new Triplet<>(partNumber, firstPromo, secondPromo);
		double buyDiscount = discountProvider.getDiscount(discountInfo, dartsTable, promosMap, pricelistsMap);
		double buyPrice = getBuyPrice(buyDiscount, gpl);
		boolean okStatus = getOkStatus(salePrice, buyPrice, threshold);

		preposModel.getPrepos().setBuyDiscount(buyDiscount);
		preposModel.getPrepos().setBuyPrice(buyPrice);
		preposModel.getPrepos().setOk(okStatus);

	}

	private String getFirstPromo(Map<String, Promo> promosMap, String partNumber) {
		Promo firstPromo = promosMap.get(partNumber);
		if (firstPromo != null && discountProvider.isRelevant(firstPromo)) {
			return firstPromo.getCode();
		}
		return null;
	}

	private boolean getOkStatus(double salePrice, double buyPrice, double threshold) {

		if ((salePrice / buyPrice) > threshold) {
			return true;
		}
		return false;
	}

	private double getBuyPrice(double buyDiscount, double gpl) {

		double buyPrice = (double) Math.round(gpl * (1 - buyDiscount) * 100) / 100;

		return buyPrice;
	}

	private Dart getSelectedSuitableDart(Map<String, Dart> suitableDarts, String secondPromo, int saleQuantity) {

		if("".equals(secondPromo)) {
			return EMPTY_DART;
		}

		if(secondPromo != null || suitableDarts.get(secondPromo) != null) {
			return suitableDarts.get(secondPromo);
		}

		Collection<Dart> darts = suitableDarts.values();
		Iterator<Dart> dartIterator = darts.iterator();
		if (dartIterator.hasNext()) {
			Dart dart = dartIterator.next();
			dart.setQuantity(dart.getQuantity() - saleQuantity);
			return dart;
		}

		return EMPTY_DART;
	}

	private double getSaleDiscount(double price, double gpl) {

		double saleDiscount = (double) Math.round((1 - (price / gpl)) * 100) / 100;

		return saleDiscount;
	}

	private double getGpl(String partNumber, Map<String, Pricelist> pricelistsMap) {
		Pricelist pricelist = pricelistsMap.get(partNumber);

		if (pricelist == null) {
			throw new CiscoException(String.format("price for sale part number %s not found", partNumber));
		}

		return pricelist.getGpl();
	}
}
