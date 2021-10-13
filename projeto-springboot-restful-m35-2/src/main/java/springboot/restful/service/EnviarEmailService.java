package springboot.restful.service;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class EnviarEmailService {
	
	String email = "seuemail@.com";
	String senha = "suasenha";

	public void enviarEmail(String emailDestino, String assunto, String mensagem) throws Exception {
		
		Properties properties = new Properties();
		properties.put("mail.smtp.ssl.trust", "*");
		properties.put("mail.smtp.auth", "true"); /* Autorização */
		properties.put("mail.smtp.starttls", "true"); /* Autenticação */
		properties.put("mail.smtp.host", "smtp.gmail.com"); /* Servidor Google */
		properties.put("mail.smtp.port", "465"); /* Porta do Servidor */
		properties.put("mail.smtp.socketFactory.port", "465"); /* Especifica porta socket */
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); /* Classe de conexão socket*/
		
		Session session = Session.getInstance(properties, new Authenticator() {
				
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				
				return new PasswordAuthentication(email,senha);
			}	
		
		});		
		
		Address[] destinatario = InternetAddress.parse(emailDestino);
		
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(email)); // Quem está enviando
		message.setRecipients(Message.RecipientType.TO, destinatario); // Email de destino da mensagem que vai ser enviada
		message.setSubject(assunto); // assunto do email
		message.setText(mensagem);
		
		Transport.send(message); // envio da mensagem
	}
	
}


