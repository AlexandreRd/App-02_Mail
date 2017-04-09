package com.example.alejandro.app_02_mail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    Session session = null;
    ProgressDialog pDialog = null;
    Context context = null;

    EditText rec, subj, msg;
    String recipient, subject, textMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        Button login = (Button) findViewById(R.id.btn_submit);
        rec = (EditText) findViewById(R.id.et_to);
        subj = (EditText) findViewById(R.id.et_subj);
        msg = (EditText) findViewById(R.id.et_msg);

        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        recipient = rec.getText().toString();
        subject = subj.getText().toString();
        textMessage = msg.getText().toString();

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(props, new Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("kander0rd@gmail.com", "tamborin");
            }
        });

        pDialog = ProgressDialog.show(context, "", "Sending Mail...", true);

        RetreiveFeedTask task = new RetreiveFeedTask();
        task.execute();
    }

    private class RetreiveFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
           try {
               Message message = new MimeMessage(session);
               message.setFrom(new InternetAddress("testfrom354@gmail.com"));
               message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
               message.setSubject(subject);
               message.setContent(textMessage, "text/html; charset-utf-8");

               Transport.send(message);

           } catch (MessagingException e) {
               e.printStackTrace();
           } catch (Exception ex) {
               ex.printStackTrace();
           }
           return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pDialog.dismiss();
            rec.setText("");
            msg.setText("");
            subj.setText("");
            Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
        }


    }
}
