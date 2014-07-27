/* 
	Description:
		ZK Essentials
	History:
		Created by dennis

Copyright (C) 2012 Potix Corporation. All Rights Reserved.
*/
package com.cisco.main;

import com.cisco.main.services.SidebarPage;
import com.cisco.main.services.SidebarPageConfig;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SidebarPageConfigAjaxBasedImpl implements SidebarPageConfig {

    Map<String, SidebarPage> pageMap = new LinkedHashMap<String, SidebarPage>();

    public SidebarPageConfigAjaxBasedImpl() {

	    pageMap.put("fn1", new SidebarPage("fn1", "Prepos", "/imgs/table.png", "/zul/layout/pre_pos.zul"));
	    pageMap.put("fn2", new SidebarPage("fn2", "Clients", "/imgs/table.png", "/zul/layout/clients.zul"));
	    pageMap.put("fn3", new SidebarPage("fn3", "DARTs", "/imgs/table.png", "/zul/layout/darts.zul"));
	    pageMap.put("fn4", new SidebarPage("fn4", "Promos", "/imgs/table.png", "/zul/layout/promos.zul"));
	    pageMap.put("fn5", new SidebarPage("fn5", "Sales", "/imgs/table.png", "/zul/layout/sales.zul"));
	    pageMap.put("fn6", new SidebarPage("fn6", "Pricelists", "/imgs/table.png", "/zul/layout/pricelists.zul"));
	    pageMap.put("fn7", new SidebarPage("fn7", "Manager", "/imgs/table.png", "/zul/layout/account_managers.zul"));
	    pageMap.put("fn8", new SidebarPage("fn8", "Serials", "/imgs/table.png", "/zul/layout/serials.zul"));

    }

    public List<SidebarPage> getPages() {
        return new ArrayList(pageMap.values());
    }

    public SidebarPage getPage(String name) {
        return pageMap.get(name);
    }

}