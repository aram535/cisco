/* 
	Description:
		ZK Essentials
	History:
		Created by dennis

Copyright (C) 2012 Potix Corporation. All Rights Reserved.
*/
package com.cisco.authentication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class DefaultUserInfoService implements UserInfoService,Serializable{
	private static final long serialVersionUID = 1L;

	private static String user = "cisco";

	private static String password = "cisco";

	static protected List<User> userList = new ArrayList<User>();  
	static{
		userList.add(new User(user,password,"Admin","admin@your.com"));
	}
	
	/** synchronized is just because we use static userList in this demo to prevent concurrent access **/
	public synchronized User findUser(String account){
		int s = userList.size();
		for(int i=0;i<s;i++){
			User u = userList.get(i);
			if(account.equals(u.getAccount())){
				return User.clone(u);
			}
		}
		return null;
	}
	
	/** synchronized is just because we use static userList in this demo to prevent concurrent access **/
	public synchronized User updateUser(User user){
		int s = userList.size();
		for(int i=0;i<s;i++){
			User u = userList.get(i);
			if(user.getAccount().equals(u.getAccount())){
				userList.set(i,u = User.clone(user));
				return u;
			}
		}
		throw new RuntimeException("user not found "+user.getAccount());
	}
}
