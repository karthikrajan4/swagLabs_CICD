package basePackage.utilities;

import java.io.File;

import java.text.DateFormat;

import java.text.SimpleDateFormat;

import java.util.Calendar;

import java.util.Date;

import java.util.Properties;

import javax.activation.DataHandler;

import javax.activation.DataSource;

import javax.activation.FileDataSource;

import javax.mail.Address;

import javax.mail.Message;

import javax.mail.MessagingException;

import javax.mail.Multipart;

import javax.mail.PasswordAuthentication;

import javax.mail.Session;

import javax.mail.Transport;

import javax.mail.internet.InternetAddress;

import javax.mail.internet.MimeBodyPart;

import javax.mail.internet.MimeMessage;

import javax.mail.internet.MimeMultipart;

import jxl.Sheet;

import jxl.Workbook;

public class SendingMail {
	public static void sendMail(String html) throws Exception {
		try {
			String[] sendMailTo = null;
			String[] sendMailCc = null;
			String subject = null;
			InternetAddress[] addressTo;
			InternetAddress[] addressCc;
			// Credentials and host
			String host = "smtp.gmail.com";
			final String user = ITestListenerImpl.defaultConfigProperty.get().getProperty("MailFromUser");
			final String team = ITestListenerImpl.defaultConfigProperty.get().getProperty("MailFromTeam");
			final String password = "nlxw nlzs lmwe ykof";
			// Email message content
			String bodymsg = html;
			// System.out.println("the bdymsg is " + bodymsg);
			// Get the session object
			Properties props = new Properties();
			props.put("mail.smtp.auth", true);
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.ssl.protocols", "TLSv1.2");
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.port", 587);
			props.put("mail.smtp.ssl.trust", host);
			// Session session = Session.getDefaultInstance(props);

			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(user, password);
				}
			});

			// Compose the message
			MimeMessage message = new MimeMessage(session);
			// Mailcc
			String mailTo = ITestListenerImpl.defaultConfigProperty.get().getProperty("MailTo");
			String to1 = mailTo;
			if (to1.contains(",")) {
				sendMailTo = to1.split(",");
				addressTo = new InternetAddress[sendMailTo.length];
				for (int i = 0; i < sendMailTo.length; i++) {
					addressTo[i] = new InternetAddress(sendMailTo[i]);
				}
				message.setRecipients(Message.RecipientType.TO, addressTo);
			} else if (!(to1.length() < 0)) {
				InternetAddress[] toAdressArray = InternetAddress.parse(to1);
				message.addRecipients(Message.RecipientType.TO, toAdressArray);
			} else {
				System.out.println("Invalid MailTo");
			}
			// Mailcc
			String mailCc = ITestListenerImpl.defaultConfigProperty.get().getProperty("MailCc");
			String cc = mailCc;
			if (cc.contains(",")) {
				sendMailCc = cc.split(",");
				addressCc = new InternetAddress[sendMailCc.length];
				for (int i = 0; i < sendMailCc.length; i++) {
					addressCc[i] = new InternetAddress(sendMailCc[i]);
				}
				message.setRecipients(Message.RecipientType.CC, addressCc);
			} else if (!(cc.length() < 0)) {
				InternetAddress[] ccAdressArray = InternetAddress.parse(cc);
				message.addRecipients(Message.RecipientType.CC, ccAdressArray);
			} else {
				System.out.println("Invalid MailCc");
			}
			// Print all recepients
			Address[] mssg = message.getAllRecipients();
			for (int i = 0; i < mssg.length; i++) {
				// System.out.println("The recepients are " + mssg[i]);
			}
			// Mailfrom
			message.setFrom(new InternetAddress(user, team));
			// Set Subject
			Calendar cal = Calendar.getInstance();
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			String calendarDate = dateFormat.format(cal.getTime());
			subject = ITestListenerImpl.defaultConfigProperty.get().getProperty("AppName") + " Test Automation Report "
					+ calendarDate;
			message.setSubject(subject);
			message.setContent(bodymsg, "text/html");
			// Add Attachment
			// if
			// (ITestListenerImpl.defaultConfigProperty.get().getProperty("AttachMail").equalsIgnoreCase("Y"))
			// {
//                                            if (!ITestListenerImpl.defaultConfigProperty.get().getProperty("SendMail").equalsIgnoreCase("trial")) {
//                                              String reportName =ITestListenerImpl.defaultConfigProperty.get().getProperty("AppName") +"_Detailed Report";
//                                              String directoryLocation = "c:\\bdd\\" + reportName.trim() + File.separator;
//                                              //String reportLocation =              directoryLocation + reportName.trim() + ".html";
//                                              String attachementPath =  directoryLocation + reportName.trim() + ".html";
//                                              //String attachementPath = "C:\\bdd\\MP_Suite\\MP_Suite.html";
//                                              String fileName =  ITestListenerImpl.defaultConfigProperty.get().getProperty("AppName") +"_Detailed Report"+".html"; Multipart multipart = new MimeMultipart();
//                                              MimeBodyPart textBodyPart = new MimeBodyPart();
//                                              textBodyPart.setContent(bodymsg, "text/html"); MimeBodyPart
//                                              attachmentBodyPart = new MimeBodyPart(); DataSource source = new
//                                              FileDataSource(attachementPath); // ex : "C:\\test.pdf"
//                                              attachmentBodyPart.setDataHandler(new DataHandler(source));
//                                              attachmentBodyPart.setFileName(fileName); // ex : "test.pdf"
//                                              multipart.addBodyPart(textBodyPart); // add the text part
//                                              multipart.addBodyPart(attachmentBodyPart); // add the attachement part
//                                              message.setContent(multipart);
//                                            }
			// }
			// send the message
			final long startTime = System.currentTimeMillis();
			Transport.send(message, message.getAllRecipients());
			long endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			System.out.println("==========>Mail Sent Successfully in " + (totalTime / 1000) % 60 + " secs <==========");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception in SendMail " + e.getMessage());
			// e.printStackTrace();
		}
	}
}
