/**
 * 
 */
package mce.util;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * @author Mehmet Emin BAKIR
 *
 */
public class Email {
	private static String USER_NAME = "diyaremin21"; // GMail user name (just the part before "@gmail.com")
	private static String PASSWORD = "abc@123*"; // GMail password
	private static String RECIPIENT = "meminbakir@gmail.com";


	/**
	 * Send email via GMAIL, without attachments
	 * 
	 * @param subject
	 * @param body
	 */
	public static void send(String subject, String body) {
		send(subject, body, null);
	}
	/**
	 * Send email via GMAIL, with attachments
	 * 
	 * @param subject
	 * @param body
	 * @param attachments
	 */
	public static void send(String subject, String body, String[] attachments) {
		String from = USER_NAME;
		String pass = PASSWORD;
		String[] to = { RECIPIENT }; // list of recipient email addresses
		if (subject.isEmpty())
			subject = "Mail example";
		if (body.isEmpty())
			body = "Welcome to JavaMail!";

		sendFromGMail(from, pass, to, subject, body, attachments);
	}

	private static void sendFromGMail(String from, String pass, String[] to, String subject, String body,
			String[] attachments) {
		try {
			Properties props = System.getProperties();
			String host = "smtp.gmail.com";
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.user", from);
			props.put("mail.smtp.password", pass);
			props.put("mail.smtp.port", "587");
			props.put("mail.smtp.auth", "true");

			Session session = Session.getDefaultInstance(props, null);
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			InternetAddress[] toAddress = new InternetAddress[to.length];
			// To get the array of addresses
			for (int i = 0; i < to.length; i++) { // changed from a while loop
				toAddress[i] = new InternetAddress(to[i]);
			}
			for (int i = 0; i < toAddress.length; i++) {
				// changed from a while loop
				message.addRecipient(Message.RecipientType.TO, toAddress[i]);
			}
			message.setSubject(subject);
			// set the text message
			BodyPart messageBodyPart1 = new MimeBodyPart();
			messageBodyPart1.setText(body);
			// file path that you want to attach
			// you are able to send multiple file simultaneously
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart1);
			for (int i = 0; i < attachments.length; i++) {
				MimeBodyPart messageBodyPart2 = new MimeBodyPart();
				DataSource source = new FileDataSource(attachments[i]);
				messageBodyPart2.setDataHandler(new DataHandler(source));
				messageBodyPart2.setFileName(attachments[i]);
				multipart.addBodyPart(messageBodyPart2);
			}
			// both part add into multi-parts
			// set message content
			message.setContent(multipart);
			// Transport the message
			Transport transport = session.getTransport("smtp");
			transport.connect(host, from, pass);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			System.out.println("Email message " + subject + " successfully!");
		} catch (AddressException ae) {
			ae.printStackTrace();
		} catch (MessagingException me) {
			me.printStackTrace();
		}
	}
}