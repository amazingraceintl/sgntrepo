package com.temenos.sgntech;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * TODO: Document me!
 *
 * @author Peter Ajayi
 *
 */
public class TransUconnector {
    String Pkey;
    String reqs;
    String urlobj;
    String res;
    String token;
    String action;

    /**
     * @param pkey
     * @param reqs
     * @param url
     * @return
     * @throws IOException
     */
    public String TransUconnector(String pkey, String reqs, String url, String token, String action)
            throws IOException {
        // super();
        String readLine = null;
        Pkey = pkey;
        this.reqs = reqs;
        urlobj = url;
        this.token = token;
        this.action = action;

        System.out.println(this.action);
        URL obj = new URL(urlobj);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(this.action);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("X-PublicKey", Pkey);
        con.setRequestProperty("Host", "app.trustev.com");
        con.setRequestProperty("User-Agent", "Apache-HttpClient/4.5.5 (Java/16.0.1)");
        con.setRequestProperty("X-Authorization", this.token);
        con.setRequestProperty("charset", "utf-8");
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Accept-Encoding", "gzip,deflate");

        if (this.reqs == "NIL") {
            int responseCode = con.getResponseCode();

            System.out.println(responseCode);
            System.out.println(con.getContent());
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuffer response = new StringBuffer();
                while ((readLine = in.readLine()) != null) {
                    response.append(readLine);
                }
                in.close();
                System.out.println("JSON String Result " + response.toString());
                res = response.toString();
            } else {
                System.out.println("GET NOT WORKED");
            }
        } else 
        {
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(this.reqs);
            wr.flush();
            wr.close();
            String responseStatus = con.getResponseMessage();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            res = response.toString();
        }

        JsonParser jsonParser = new JsonParser();
        JsonElement jsonTree = jsonParser.parse(res);

        return res;
    }

}
