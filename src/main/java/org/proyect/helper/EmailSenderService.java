package org.proyect.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    @Autowired
    private static JavaMailSender javaMailSender;

    public static void sendEmail(String emailTo, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailTo);
        message.setSubject(subject);
        message.setText(content);
        message.setFrom("danielhdezgarcia9daw@gmail.com");

        javaMailSender.send(message);
        System.out.println("Correo enviado exitosamente");
    }
}
