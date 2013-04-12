package com.chat.helper;

import java.util.ArrayList;
import java.util.List;

public class LoggedInUserDetail {
	private static final LoggedInUserDetail detail = new LoggedInUserDetail();
	private static final String delimitter = "|";
	private List<String> userNames = new ArrayList<String>();
	
	private LoggedInUserDetail() {
	}
	
	public static LoggedInUserDetail getInstance(){
		return detail;
	}
	
	public String getUserDetails(){
		return delimitTheInput(userNames);
	}

	public List<String> getLoggedInUserDetails(){
		return userNames;
	}
	private String delimitTheInput(List<String> userNames){
		String result = "";
		for(String userName : userNames){
			result += userName + delimitter ;
		}
		return result;
	}
	
}
