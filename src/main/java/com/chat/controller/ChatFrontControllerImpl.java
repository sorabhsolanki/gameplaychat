package com.chat.controller;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

import com.chat.helper.DoChat;
import com.chat.helper.LoggedInUserDetail;


@Resource(name="ChatFrontControllerImpl")
@Path("/chat")
public class ChatFrontControllerImpl implements ChatFrontController{

	private DoChat chat = DoChat.getInstance();
	private LoggedInUserDetail detail = LoggedInUserDetail.getInstance();
	private static final Logger log = Logger.getLogger(ChatFrontControllerImpl.class.getName());
	
	@GET
	@Path("/details")
	@Produces(MediaType.TEXT_PLAIN)
	public String getLoggedInUserDetails() {
		return detail.getUserDetails();
	}

	@GET
	@Path("/login/{username}")
	@Produces(MediaType.TEXT_PLAIN)
	public boolean loggedIn(@PathParam ("username") String userName) {
		boolean result = false;
		log.info("Logging the user with user name : " + userName);
		try {
			result = chat.loginTheUser(userName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@GET
	@Path("/publish/{data}/{username}")
	@Produces(MediaType.TEXT_PLAIN)
	public boolean publish(@PathParam ("data") String data, @PathParam ("username") String userName) {
		boolean result = false;
		try{
			chat.publishData(data, userName);
		}catch(Exception e){
			
		}
		return result; 
	}
	
	@GET
	@Path("/logout/{username}")
	@Produces(MediaType.TEXT_PLAIN)
	public boolean logout(@PathParam ("username") String userName) {
		chat.logout(userName);
		return true;
	}
	
	@GET
	@Path("/receive/{username}")
	@Produces(MediaType.TEXT_PLAIN)
	public String receiveData(@PathParam ("username") String userName) {
		return chat.getData(userName);
	}

	@GET
	@Path("/history/{username}")
	@Produces(MediaType.TEXT_PLAIN)
	public String chatHistory(@PathParam ("username") String userName){
		return chat.getChatHistory(userName);
	}
	
	// just for testing
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayHello(){
		return "Hello!!!";
	}

	
}
