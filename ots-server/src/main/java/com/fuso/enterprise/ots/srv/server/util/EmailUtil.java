package com.fuso.enterprise.ots.srv.server.util;

import java.util.Properties;    
import javax.mail.*;    
import javax.mail.internet.*;
import javax.activation.*;

public class EmailUtil {
	
	public static void sendEmailBill(String to,String cc,String sub,String msg,String billFileName,String filePath){   
		
		final String username = "anushakarnam06@gmail.com";
	    final String password = "Karnam06@";

	    Properties props = new Properties();
	    props.put("mail.smtp.auth", true);
	    props.put("mail.smtp.starttls.enable", true);
	    props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.smtp.port", "587");

	    Session session = Session.getInstance(props,
	            new javax.mail.Authenticator() {
	                protected PasswordAuthentication getPasswordAuthentication() {
	                    return new PasswordAuthentication(username, password);
	                }
	            });
	    try {

	        Message message = new MimeMessage(session);
	        message.setFrom(new InternetAddress("anushakarnam06@gmail.com"));
	        message.setRecipients(Message.RecipientType.TO,
	                InternetAddress.parse(to));
	        message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
	        message.setSubject(sub);
	        message.setText(msg);

	        MimeBodyPart messageBodyPart = new MimeBodyPart();

	        Multipart multipart = new MimeMultipart();

	        messageBodyPart = new MimeBodyPart();
	        String file = filePath;
	        String fileName = billFileName;
	        DataSource source = new FileDataSource(file);
	        messageBodyPart.setDataHandler(new DataHandler(source));
	        messageBodyPart.setFileName(fileName);
	        multipart.addBodyPart(messageBodyPart);
	        message.setContent(multipart);
	        Transport.send(message);
	    } catch (MessagingException e) {
	        e.printStackTrace();
	    }
	  
	 }    
	
	
	
public static void sendOTP(String to,String cc,String sub,String msg){   
		
		final String username = "anushakarnam06@gmail.com";
	    final String password = "Karnam06@";

	    Properties props = new Properties();
	    props.put("mail.smtp.auth", true);
	    props.put("mail.smtp.starttls.enable", true);
	    props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.smtp.port", "587");

	    Session session = Session.getInstance(props,
	            new javax.mail.Authenticator() {
	                protected PasswordAuthentication getPasswordAuthentication() {
	                    return new PasswordAuthentication(username, password);
	                }
	            });
	    try {

	        Message message = new MimeMessage(session);
	        message.setFrom(new InternetAddress("anushakarnam06@gmail.com"));
	        message.setRecipients(Message.RecipientType.TO,
	                InternetAddress.parse(to));
	        message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
	        message.setSubject(sub);
	        message.setText(msg);
	        Transport.send(message);
	    } catch (MessagingException e) {
	        e.printStackTrace();
	    }
	  
	 } 
	
	

}
