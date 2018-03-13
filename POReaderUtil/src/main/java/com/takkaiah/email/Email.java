package com.takkaiah.email;

import java.util.List;

public class Email {
    private String from;
    private String to;
    private String subject;
    private String cc;
    private String bcc;
    
    List<EmailAttachment> attachments;
    
    
    
    public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	String msg_body;
    
    
    public Email(){
   	 
   	 
    }
    public Email(String from, String to,String subject, String msg_body) {
		super();
		this.from = from;
		this.to = to;
		this.subject=subject;
		this.msg_body = msg_body;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getMsg_body() {
		return msg_body;
	}
	public void setMsg_body(String msg_body) {
		this.msg_body = msg_body;
	}
	public List<EmailAttachment> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<EmailAttachment> attachments) {
		this.attachments = attachments;
	}
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	public String getBcc() {
		return bcc;
	}
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}
	
}
