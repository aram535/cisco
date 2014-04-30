package com.cisco.prepos.services;

import com.cisco.clients.dto.Client;
import com.cisco.clients.service.ClientsService;
import com.cisco.darts.dto.Dart;
import com.cisco.darts.service.DartsService;
import com.cisco.exception.CiscoException;
import com.cisco.prepos.dao.PreposesDao;
import com.cisco.prepos.dto.Prepos;
import com.cisco.prepos.model.PreposModel;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.pricelists.service.PricelistsService;
import com.cisco.promos.dto.Promo;
import com.cisco.promos.service.PromosService;
import com.cisco.sales.dto.Sale;
import com.cisco.sales.service.SalesService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

import static com.cisco.sales.dto.Sale.Status.NOT_PROCESSED;

;

/**
 * User: Rost
 * Date: 21.04.2014
 * Time: 22:39
 */

@Component
@PropertySource("classpath:prepos.properties")
public class DefaultPreposConstructor implements PreposConstructor {

	@Autowired
	private PreposesDao preposesDao;

    @Autowired
    private SalesService salesService;

    @Autowired
    private ClientsService clientsService;

    @Autowired
    private PricelistsService pricelistsService;

	@Autowired
	private PromosService promoService;

	@Autowired
	private DartsService dartsService;


	@Value("${good.threshold}")
	private double threshold;

	private Map<String, Client> clientsMap;
	private Map<String, Pricelist> pricelistsMap;
	private Map<String, Promo> promosMap;
	private Table<String, String, Dart> dartsTable;

	private List<Prepos> allPreposes;

	//@PostConstruct
	public void initRelatedTablesData() {
		clientsMap = clientsService.getClientsMap();

		pricelistsMap = pricelistsService.getPricelistsMap();

		promosMap = promoService.getPromosMap();

		dartsTable = dartsService.getDartsTable();
	}

	@Override
    public List<PreposModel> getNewPreposModels() {

		initRelatedTablesData();

        List<Sale> sales = salesService.getSales(NOT_PROCESSED);

        if (CollectionUtils.isEmpty(sales)) {
            return Lists.newArrayList();
        }

        List<PreposModel> preposes = Lists.newArrayList();

        for (Sale sale : sales) {

	        PreposModel preposModel= new PreposModel();
            Prepos prepos = new Prepos();

	        prepos.setClientNumber(sale.getClientNumber());
	        prepos.setShippedDate(sale.getShippedDate());
	        prepos.setShippedBillNumber(sale.getShippedBillNumber());
	        prepos.setComment(sale.getComment());
	        prepos.setSerials(sale.getSerials());
	        prepos.setZip(sale.getClientZip());
	        prepos.setType(sale.getCiscoType());
	        prepos.setQuantity(sale.getQuantity());
	        prepos.setPartNumber(sale.getPartNumber());
	        prepos.setSalePrice(sale.getPrice());

            assignPartnerName(sale, prepos);

	        Promo firstPromo = promosMap.get(prepos.getPartNumber());
	        if (firstPromo != null) {
		        prepos.setFirstPromo(firstPromo.getCode());
	        }

	        assignSecondPromo(prepos, preposModel);

	        double gpl = getGpl(prepos);

            assignSaleDiscount(prepos, gpl);

	        assignBuyDiscount(prepos);

	        assignBuyPrice(prepos, gpl);

	        assignOk(prepos);

	        preposModel.setPrepos(prepos);
            preposes.add(preposModel);

	        sale.setStatus(Sale.Status.PROCESSED);
        }

		//update sales
        return preposes;
    }

	@Override
	public List<PreposModel> getAllPreposModels() {

		allPreposes = preposesDao.getPreposes();

		List<PreposModel> preposModels = Lists.newArrayList();
		for (Prepos prepos : allPreposes) {

			PreposModel preposModel= new PreposModel();
			preposModel.setPrepos(prepos);
			setSuitableDarts(preposModel);

			preposModels.add(preposModel);
		}

		return preposModels;
	}

	@Override
	public void save(List<PreposModel> preposModels) {

		List<Prepos> preposes = Lists.newArrayList();
		for (PreposModel model : preposModels) {
			preposes.add(model.getPrepos());
		}

		preposesDao.save(preposes);
	}

	private void assignOk(Prepos prepos) {

		if((prepos.getSalePrice() / prepos.getBuyPrice()) > threshold) {
			prepos.setOk(true);
		} else {
			prepos.setOk(false);
		}
	}

	private void assignBuyPrice(Prepos prepos, double gpl) {

		double buyPrice = gpl * (1 -  prepos.getBuyDiscount());

		prepos.setBuyPrice(buyPrice);
	}

	private void assignBuyDiscount(Prepos prepos) {

		double buyDiscount;

		if(prepos.getSecondPromo() != null) {
			buyDiscount = dartsTable.get(prepos.getPartNumber(), prepos.getSecondPromo()).getDistiDiscount();
		} else if(prepos.getFirstPromo() != null) {
			buyDiscount = promosMap.get(prepos.getPartNumber()).getDiscount();
		} else {
			buyDiscount = pricelistsMap.get(prepos.getPartNumber()).getDiscount();
		}

		prepos.setBuyDiscount(buyDiscount);
	}

	private void assignSecondPromo(Prepos prepos, PreposModel preposModel) {

		Map<String, Dart> suitableDarts = Maps.newHashMap();

		Map<String, Dart> suitableAuthNumsDarts = dartsTable.row(prepos.getPartNumber());
		for (Dart dart : suitableAuthNumsDarts.values()) {
			if(prepos.getPartnerName().equals(dart.getResellerName()) && dart.getQuantity() >= prepos.getQuantity()) {
				suitableDarts.put(dart.getAuthorizationNumber(), dart);
			}

			if(prepos.getSecondPromo() == null) {
				prepos.setSecondPromo(dart.getAuthorizationNumber());
				dart.setQuantity(dart.getQuantity() - prepos.getQuantity());
				preposModel.setSelectedPromo(dart);
				prepos.setEndUser(dart.getEndUserName());
			}

		}

		preposModel.setSuitableDarts(suitableDarts);

	}

	private void assignSaleDiscount(Prepos prepos, double gpl) {

		int saleDiscount = 100 - (int) Math.round((prepos.getSalePrice() / gpl) * 100);

		prepos.setSaleDiscount(saleDiscount);
	}

	private void assignPartnerName(Sale sale, Prepos prepos) {
		Client client = clientsMap.get(sale.getClientNumber());
		if (client != null) {
			prepos.setPartnerName(client.getName());
		} else {
			prepos.setPartnerName(sale.getClientName());
		}
	}

	private double getGpl(Prepos prepos) {
		Pricelist pricelist = pricelistsMap.get(prepos.getPartNumber());

		if (pricelist == null) {
			throw new CiscoException(String.format("price for sale part number %s not found", prepos.getPartNumber()));
		}

		return pricelist.getGpl();
	}

	private void setSuitableDarts(PreposModel preposModel) {

		if(preposModel.getPrepos().getSecondPromo() == null) {
			assignSecondPromo(preposModel.getPrepos(), preposModel);
		} else {
			Map<String, Dart> suitableDarts = Maps.newHashMap();
			Map<String, Dart> suitableAuthNumsDarts = dartsTable.row(preposModel.getPrepos().getPartNumber());

			preposModel.setSelectedPromo(suitableAuthNumsDarts.get(preposModel.getPrepos().getSecondPromo()));

			for (Dart dart : suitableAuthNumsDarts.values()) {
				if (preposModel.getPrepos().getPartnerName().equals(dart.getResellerName())
						&& dart.getQuantity() >= preposModel.getPrepos().getQuantity()) {
					suitableDarts.put(dart.getAuthorizationNumber(), dart);
				}
			}

			preposModel.setSuitableDarts(suitableDarts);
		}
	}

	public void recountPreposForNewPromo(PreposModel preposModel) {

		double distiDiscount;

		if(preposModel.getPrepos().getSecondPromo() != null) {
			distiDiscount = dartsTable.get(preposModel.getPrepos().getPartNumber(),
					preposModel.getPrepos().getSecondPromo()).getDistiDiscount();
		} else if(preposModel.getPrepos().getFirstPromo() != null) {
			distiDiscount = promosMap.get(preposModel.getPrepos().getFirstPromo()).getDiscount();
		} else {
			distiDiscount = pricelistsMap.get(preposModel.getPrepos().getPartNumber()).getDiscount();
		}

		preposModel.getPrepos().setBuyDiscount(distiDiscount);

		Pricelist pricelist = pricelistsMap.get(preposModel.getPrepos().getPartNumber());

		if (pricelist == null) {
			throw new CiscoException(String.format("price for sale part number %s not found", preposModel.getPrepos().getPartNumber()));
		}

		double gpl = pricelist.getGpl();

		double buyPrice = gpl * (1 -  preposModel.getPrepos().getBuyDiscount());

		preposModel.getPrepos().setBuyPrice(buyPrice);

		if((preposModel.getPrepos().getSalePrice() / preposModel.getPrepos().getBuyPrice()) > threshold) {
			preposModel.getPrepos().setOk(true);
		} else {
			preposModel.getPrepos().setOk(false);
		}

	}
}
