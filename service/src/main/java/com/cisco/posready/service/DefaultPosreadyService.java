package com.cisco.posready.service;

import com.cisco.clients.dto.Client;
import com.cisco.darts.dto.Dart;
import com.cisco.exception.CiscoException;
import com.cisco.posready.excel.PosreadyBuilder;
import com.cisco.prepos.dto.Prepos;
import com.cisco.pricelists.dto.Pricelist;
import com.cisco.promos.dto.Promo;
import com.google.common.collect.Table;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by Alf on 03.07.2014.
 */
@Component("posreadyService")
public class DefaultPosreadyService implements PosreadyService {

	@Value("${posready.folder}")
	private String posreadyFolder;

	@Autowired
	private PosreadyBuilder posreadyBuilder;

	@Override
	public String exportPosready(List<Prepos> preposes, Map<String, Client> clientMap,
	                             Map<String, Pricelist> pricelistsMap,
	                             Table<String, String, Dart> dartsTable, Map<String, Promo> promosMap) {

		byte[] bytes = posreadyBuilder.buildPosready(preposes, clientMap, pricelistsMap, dartsTable, promosMap);

		String uniqueID = DateIdUtil.getUniqueID();
		String fileName = String.format("%s%s.xls", posreadyFilePrefix, uniqueID);
		String filePath = String.format("%s%s", posreadyFolder, fileName);

		File posreadyFile = new File(filePath);

		try {
			FileUtils.writeByteArrayToFile(posreadyFile, bytes);
		} catch (IOException e) {
			throw new CiscoException("Failed to save posready file on disc", e);
		}

		return filePath;
	}

	void setPosreadyFolder(String posreadyFolder) {
		this.posreadyFolder = posreadyFolder;
	}

	private static class DateIdUtil
	{
		public static String getUniqueID() {
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			Calendar cal = Calendar.getInstance();

			return dateFormat.format(cal.getTime());
		}
	}
}
