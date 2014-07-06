/* 
	Description:
		ZK Essentials
	History:
		Created by dennis

Copyright (C) 2012 Potix Corporation. All Rights Reserved.
*/
package com.cisco.main.services.authentication;


public interface UserInfoService {

	/** find user by account **/
	public User findUser(String account);
	
	/** updateFromModels user **/
	public User updateUser(User user);
}
