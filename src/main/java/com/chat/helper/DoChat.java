package com.chat.helper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.util.Util;

public class DoChat extends ReceiverAdapter {

	public static final DoChat chat = new DoChat();
	private static final String cluster = "ChatCluster";
	private boolean isNewMessageArrived = false;
	private LoggedInUserDetail detail = LoggedInUserDetail.getInstance();
	private static final String delimitter = "|";
	private static final String NO_LOGIN_YET = "Please login";
	private static final String PROCEED = "verified";

	private String data;

	private DoChat() {
		System.setProperty("jgroups.bind_addr", "172.16.2.4");
	}

	public static DoChat getInstance() {
		return chat;
	}

	public boolean loginTheUser(String userName) throws Exception {
		JChannel channel = new JChannel();
		DoChatHelper.channelMap.put(userName, channel);
		channel.setReceiver(this);
		channel.connect(cluster);
		channel.getState(null, 10000);
		detail.getLoggedInUserDetails().add(userName);
		return true;
	}

	public String publishData(String data, String userName) throws Exception {
		String result = basicLoginCheck(userName);
		if(!result.equals(PROCEED))
			return result;
		JChannel channel = DoChatHelper.channelMap.get(userName);
		data = userName + ":" + data;
		Message msg = new Message(null, null, data);
		channel.send(msg);
		isNewMessageArrived = true;
		return "";
	}

	public boolean logout(String userName) {
		JChannel channel = DoChatHelper.channelMap.get(userName);
		channel.close();
		detail.getLoggedInUserDetails().remove(userName);
		DoChatHelper.channelMap.remove(userName);
		return true;
	}

	public String getData(String userName) {
		String result = basicLoginCheck(userName);
		if(!result.equals(PROCEED))
			return result;
		if(isNewMessageArrived)
			return this.data;
		else 
			return null;
	}

	public void viewAccepted(View new_view) {
		// use log4j instead of sys out
		System.out.println("** view: " + new_view);
	}

	public void receive(Message msg) {
		String line = (String) msg.getObject();
		this.data = line;
		synchronized (DoChatHelper.state) {
			DoChatHelper.state.add(line);
		}
	}

	public void getState(OutputStream output) throws Exception {
		synchronized (DoChatHelper.state) {
			Util.objectToStream(DoChatHelper.state,
					new DataOutputStream(output));
		}
	}

	public void setState(InputStream input) throws Exception {
		List<String> list = (List<String>) Util
				.objectFromStream(new DataInputStream(input));
		synchronized (DoChatHelper.state) {
			DoChatHelper.state.clear();
			DoChatHelper.state.addAll(list);
		}
		System.out.println("received state (" + list.size()
				+ " messages in chat history):");
		for (String str : list) {
			System.out.println(str);
		}
	}
	
	public String getChatHistory(String userName){
		String result = basicLoginCheck(userName);
		if(!result.equals(PROCEED))
			return result;
		return delimitTheInput(DoChatHelper.state);
	}
	
	private String basicLoginCheck(String userName){
		if(null == userName || userName.isEmpty()){
			return NO_LOGIN_YET;
		}else if(DoChatHelper.channelMap.get(userName) == null){
			return NO_LOGIN_YET;
		}
		return PROCEED;
	}
	
	private String delimitTheInput(List<String> userNames){
		String result = "";
		for(String userName : userNames){
			result += userName + delimitter ;
		}
		return result;
	}
	

	static class DoChatHelper {
		private static Map<String, JChannel> channelMap = new HashMap<String, JChannel>();
		private static final List<String> state = new LinkedList<String>();
	}
}
