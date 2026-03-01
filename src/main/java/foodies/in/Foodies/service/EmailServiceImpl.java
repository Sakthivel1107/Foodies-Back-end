package foodies.in.Foodies.service;

import foodies.in.Foodies.models.ContactRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl  implements EmailService{
    @Autowired
    private JavaMailSender mailSender;
    @Override
    public void sendEmail(ContactRequest request) throws Exception{
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("sakthivel62628@gmail.com");
            message.setSubject("Query from foodies customer");
            message.setText("Name: "+request.getName()+"\n"+"Email: "+request.getEmail()+"\n\n"+"Message:\n"+request.getMessage());
            mailSender.send(message);
        }
        catch(Exception e){
            throw new Exception("Email sending failed: "+e.getMessage());
        }
    }
}
