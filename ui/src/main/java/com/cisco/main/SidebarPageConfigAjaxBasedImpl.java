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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class SidebarPageConfigAjaxBasedImpl implements SidebarPageConfig {
	
	HashMap<String,SidebarPage> pageMap = new LinkedHashMap<String,SidebarPage>();
	public SidebarPageConfigAjaxBasedImpl(){		

		pageMap.put("fn1",new SidebarPage("fn1","Clients","/imgs/table.png","/zul/layout/clients.zul"));
		pageMap.put("fn2",new SidebarPage("fn2","DARTs","/imgs/table.png","/zul/layout/darts.zul"));
		pageMap.put("fn3",new SidebarPage("fn3","Prepos","/imgs/table.png","/zul/layout/pre_pos.zul"));

	}
	
	public List<SidebarPage> getPages(){
		return new ArrayList<SidebarPage>(pageMap.values());
	}
	
	public SidebarPage getPage(String name){
		return pageMap.get(name);
	}
	
}