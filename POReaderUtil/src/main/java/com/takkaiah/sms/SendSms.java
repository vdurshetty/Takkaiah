package com.takkaiah.sms;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SendSms {
	public static void main(String[] args) {
		try {
		String recipient = "+917674097711";
		String message = " Greetings from Mr. D VenuGopal! Have a nice day!";
		String username = "admin";
		String password = "VenuGopal";
		String originator = "+917674097711";
		String requestUrl  = "http://127.0.0.1:9501/api?action=sendmessage&" +
		 "username=" + URLEncoder.encode(username, "UTF-8") +
		 "&password=" + URLEncoder.encode(password, "UTF-8") +
		 "&recipient=" + URLEncoder.encode(recipient, "UTF-8") +
		 "&messagetype=SMS:TEXT" +
		 "&messagedata=" + URLEncoder.encode(message, "UTF-8") +
		 "&originator=" + URLEncoder.encode(originator, "UTF-8") +
		 "&serviceprovider=GSMModem1" +
		 "&responseformat=html";
		URL url = new URL(requestUrl);
		HttpURLConnection uc = (HttpURLConnection)url.openConnection();
		System.out.println(uc.getResponseMessage());
		uc.disconnect();
		} catch(Exception ex) {
		System.out.println(ex.getMessage());
		}
		}
}
