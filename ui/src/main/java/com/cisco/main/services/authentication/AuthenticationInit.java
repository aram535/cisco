/* 
	Description:
		ZK Essentials
	History:
		Created by dennis

Copyright (C) 2012 Potix Corporation. All Rights Reserved.
*/
package com.cisco.main.services.authentication;

import com.cisco.authentication.UserCredential;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.util.Initiator;

import java.util.Map;

public class AuthenticationInit implements Initiator {

	//services
	AuthenticationService authenticationService = new DefaultAuthenticationService();
	
	public void doInit(Page page, Map<String, Object> args) throws Exception {
		
		UserCredential cre = authenticationService.getUserCredential();
		if(cre==null || cre.isAnonymous()){
			Executions.sendRedirect("/zul/login/login.zul");
			return;
		}
	}
}