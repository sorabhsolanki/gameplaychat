package com.chat.controller;

public interface ChatFrontController {

	/*
	 * This method will return all the list of currently logged in user
	 * It will return a string separated by | 
	 * It will communicate with the gameplay fun soap webservice to get the 
	 * necessary details. 
	 */
	public String getLoggedInUserDetails();
	
	/*
	 * User call this method at the time of joining the chat server
	 * This method will register the user with the cluster
	 */
	public boolean loggedIn(String userName);
	
	/*
	 * This method will publish the data to all other nodes in the cluster
	 */
	public boolean publish(String data, String userName);
	
	/*
	 * This method will publish the data to all other nodes in the cluster
	 */
	public String receiveData(String userName);
	
	/*
	 * User call this method at the time of exiting out from the chat server
	 */
	public boolean logout(String userName);

}
