package com.temenos.sgntech;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.temenos.sgntech.ParameterStringBuilder;

/**
 * TODO: Document me!
 *
 * @author Peter
 *
 */
public class TestingGet {

    /**
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        TestingGet testget = new TestingGet();
        testget.MyGETRequest();
  
    }  
    
    public static void MyGETRequest() throws IOException {
        final int connectTimeout = 1000;
        URL urlForGetRequest = new URL("https://app.trustev.com/api/v2.1/detaileddecision/?caseid=2bfc85c7-7924-4636-8e09-a49fc4c011bc|0d42b7a9-f8c3-488e-9e1a-358a332e1b24");
        String readLine = null;
        HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
        conection.setRequestMethod("GET");
        conection.setRequestProperty("Content-Type", "application/json");
        conection.setRequestProperty("X-Authorization", "Cooperativa_Ahorro_y_Credito_TestSite 073279ec-7239-43b9-91d9-50f9b561650b");
        conection.setRequestProperty("Host", "app.trustev.com");
        conection.setRequestProperty("User-Agent", "Apache-HttpClient/4.5.5 (Java/16.0.1)");
        conection.setRequestProperty("charset","utf-8");
        conection.setRequestProperty("Connection","Keep-Alive");
        conection.setRequestProperty("Accept-Encoding", "gzip,deflate");
       
        int responseCode = conection.getResponseCode();

        System.out.println(responseCode);
        System.out.println(conection.getContent());
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                new InputStreamReader(conection.getInputStream()));
            StringBuffer response = new StringBuffer();
            while ((readLine = in .readLine()) != null) {
                response.append(readLine);
            } in .close();
            System.out.println("JSON String Result " + response.toString());
        } else {
            System.out.println("GET NOT WORKED");
        }


}
    }
