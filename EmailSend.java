import java.util.Properties;  
import javax.mail.*;
import javax.mail.internet.*;  
  
public class EmailSend {  

    private String user;
    private Session session;

    EmailSend(){
        String host = "smtp.gmail.com";

        // Get the session object
        Properties props = new Properties();

        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        session = Session.getDefaultInstance(props);
        
    }
    

    public boolean authenticateUser(String user, String password) {
        Session temp = session;
        try {
            // Connect to the email server and authenticate the user
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", user, password);
            transport.close();
            this.user = user; 
            return true; // User credentials are valid
        } catch (AuthenticationFailedException e) {
            session = temp; 
            return false; // User authentication failed
        } catch (MessagingException e) {
            session = temp;
            e.printStackTrace();
            return false; // Error occurred while connecting/authenticating
        }
    }


    public boolean send(String msg, String to, String subject) {  
        //Compose the message  
        try {  
            MimeMessage message = new MimeMessage(session);  
            message.setFrom(new InternetAddress(user));  
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
            message.setSubject(subject);  
            message.setText(msg);  
       
            //send the message  
            Transport.send(message);  
  
            return true; 
   
        } catch (MessagingException e) {
            e.printStackTrace();
            return false; 
        }  

    }  
}  