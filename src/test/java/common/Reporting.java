package common;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import javax.mail.PasswordAuthentication;
//import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


/**
 * Created by SourabhM on 06-01-2016.
 */
public class Reporting {

    Reporting(File f) {

        try {
            System.out.println("Reporting Constructor --------------------------------------");
            FileWriter cwriter = new FileWriter(f.getName(), true);
            cwriter.write("<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "<style>\n" +
                    "table {width:100%;}\n" +
                    "table, th, td {border: 1px solid black;border-collapse: collapse;\n}" +
                    "th, td {padding: 15px;\n}" +
                    "table tr:nth-child(even) {background-color: #eee;}\n" +
                    "table tr:nth-child(odd) {background-color:#fff;}\n" +
                    "table th{background-color: black;color: white;}\n" +
                    "span.wrong {background-color: red;}\n" +
                    "span.expected {background-color: yellow;}\n" +
                    "</style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<table>" +
                    "<caption>" + "Report for API tests Execution" + "</caption>" +
                    "<tr>" + "<th>" + "Serial No." + "</th>" + "<th>" + "Name Of Scenario" + "</th>" + "<th>" + "Status" + "</th>" + "<th>" + "Time<br>(sec)" + "</th>" + "<th>" + "Reason" + "</th>" + "</tr>"


            );
            cwriter.close();


        } catch (IOException e) {
            System.out.println("Reporting Constructor catch");
            e.printStackTrace();
        }


    }

    public void generateReport(String passOrFail, String nameOfTc, String reason, int SerialNo, File f, String tcShortName, String time) {

        SerialNo = SerialNo - 1;


        try {

            FileWriter writer = new FileWriter(f.getName(), true);

            writer.write("<tr>" + "<td>" + SerialNo + " : " + tcShortName + "</td>" + "<td>" + nameOfTc + "</td>" + "<td>" + passOrFail + "</td>" + "<td>" + time + "</td>" + "<td>" + reason + "</td>" + "</tr>");
            writer.close();

        } catch (Exception e) {
            //catch any exceptions here
            System.out.println("inside reporter");
        }
    }


    public void sendMail(File f, String mailIdsCommaSeparated)
    {
        //List<String> mailToList = Arrays.asList(mailIdsCommaSeparated.split(","));
        final String username = "talentica2@gmail.com";
        final String password = "talentica@1";
       // String to = "talentica2@gmail.com,sourabh.munje@talentica.com";
        String from = "talentica2@gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
                       System.out.println("sending mail----------------------------");
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(mailIdsCommaSeparated));
           // message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));

            // Set Subject: header field
            message.setSubject("Api Test Results");

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message
            messageBodyPart.setText("The results for the api test is attached as an html file. Please open it with any browser to view Results\n"+"\nThanks and Regards"+"\nSourabh Munje");

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            String filename = f.toString();
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart);

            // Send message
            Transport.send(message);

            System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
