package com.project.doctorinsta.utils;

import android.util.Log;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EmailClient {

    public void send(String to, String userType, String password){
        MailjetClient client;
        MailjetRequest request = null;
        MailjetResponse response;
        client = new MailjetClient("20ec3f6eb1bcc2e4fc05569ce89c4704",
                "2e40ba9ef977aa4deb7d8faf997514ac", new ClientOptions("v3.1"));
        try {
            request = new MailjetRequest(Emailv31.resource)
                    .property(Emailv31.MESSAGES, new JSONArray()
                            .put(new JSONObject()
                                    .put(Emailv31.Message.FROM, new JSONObject()
                                            .put("Email", "info@zaratech.co.zw")   //tarun.sarpanjeri@gmail.com
                                            .put("Name", "DoctorInsta"))
                                    .put(Emailv31.Message.TO, new JSONArray()
                                            .put(new JSONObject()
                                                    .put("Email", to)
                                                    .put("Name", userType)))
                                    .put(Emailv31.Message.SUBJECT, "DoctorInsta password recovery.")
                                    .put(Emailv31.Message.HTMLPART, "<h3>Dear "+userType+",</h3>You may use the password provided below to login. As soon as you login please change this password for security reasons.<br/><h3>Your password: "+password+"</h3><br/>Kind regards.")
                                    .put(Emailv31.Message.CUSTOMID, "AppGettingStartedTest")));
            response = client.post(request);
            System.out.println(response.getStatus());
            System.out.println(response.getData());

        } catch (JSONException | MailjetException | MailjetSocketTimeoutException e) {
            Log.e("email error", e.getMessage());
        }


    }
}
