package com.temenos.sgntech;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.temenos.sgntech.HashGenerator;
import com.temenos.sgntech.TransUconnector;

/**
 * TODO: Document me!
 *
 * @author Ajayi olubunmi Peter
 *
 */
public class EntryPointlevel {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        EntryPointlevel Innercalls = new EntryPointlevel();
        TransUconnector transucaller = new TransUconnector();
        String mytimestamp;
        final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println("Start Time ==== "+timestamp);
        String[] tempm = sdf2.format(timestamp).toString().toString().split("\\+");
        mytimestamp = (tempm[0] + "Z");

        String usern = "Cooperativa_Ahorro_y_Credito_TestSite";
        String psswd = "830058098b40451d8ccb366a3c88a372";
        String secret = "ae9361c550484a838ca5b6ef0c578488";
        String actionP = "POST";
        String actionG = "GET";

        String url = "https://app.trustev.com/api/v2.0/session";
        String url_case = "https://app.trustev.com/api/v2.0/case";
        String urltk = "https://app.trustev.com/api/v2.0/token";
        String Ddcurl = "https://app.trustev.com/api/v2.1/detaileddecision/";

        String Pkey = "4448edc64c334237b402bc2656953dad";
        String BlackBox = "XYZ";
        String res;
        String GeneratedCaseID = null;
        String Id_response = null;
        String result_response = null;
        String Username = "Cooperativa_Ahorro_y_Credito_TestSite";
        String Psswd = "830058098b40451d8ccb366a3c88a372";
        String sessionFlag = "false";
        String ttkey = null;
        String PIDcall = null;
        String GenTokenID = null;

        String sessionID = null;
        String caseNo = null;
        String Add1 = "1600 PARK PLACE";
        String Add2 = null;
        String Add3 = null;
        String city = "ATLANTA";
        String Countrycode = "US";
        String Fname = "PATRICK";
        String Isdft = "true";
        String Lname = "TESTP";
        String postcode = "30326";
        String State = "GA";
        String type = "0";
        String ssn = "666010016";
        String dob = "4/30/1972";
        String ctoken = null;

        JSONObject jsonObjectReq = new JSONObject();
        Map<String, String> mapp = new HashMap<>();
        mapp.put("ClientIp", "185.107.57.7");
        JSONArray Details = new JSONArray();
        Details.add(mapp);
        jsonObjectReq.put("Details", Details);
        jsonObjectReq.put("BlackBox", BlackBox);
        String xml = jsonObjectReq.toString();

        String transuconnector = transucaller.TransUconnector(Pkey, xml, url, "", actionP);
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonTree = jsonParser.parse(transuconnector);
        if (jsonTree.isJsonObject()) {
            JsonObject jsonObject = jsonTree.getAsJsonObject();
            JsonElement SessnId = jsonObject.get("SessionId");
            if (SessnId != null) {
                sessionFlag = "true";
                sessionID = SessnId.toString();
                sessionID = sessionID.substring(1, sessionID.length() - 1);

                System.out.println("SessionId ==....." + sessionID);
            }
        }
        if (sessionFlag.equals("true")) {
            HashGenerator hashgen = new HashGenerator();
            String HashResult = hashgen.HashGenerator(mytimestamp, usern, psswd, secret);
            String[] Tquery = HashResult.toString().split("\\|");
            String Ttsamp = Tquery[0];
            String Husrname = Tquery[1];
            String Hpass = Tquery[2];
            ttkey = Innercalls.PostToken(Username, Ttsamp, Husrname, Hpass); // tokenreqformation
            String Tokencaller = transucaller.TransUconnector("", ttkey, urltk, "", actionP); // TokenApicall
            JsonElement TokenjsonTree = jsonParser.parse(Tokencaller);
            if (TokenjsonTree.isJsonObject()) {
                JsonObject TokenjsonObject = TokenjsonTree.getAsJsonObject();
                JsonElement Tokenid = TokenjsonObject.get("APIToken");
                GenTokenID = Tokenid.toString();
                System.out.println("Generated token = " + GenTokenID);
            }
            if (!GenTokenID.isEmpty()) {
                final String uuid = UUID.randomUUID().toString().replace("-", "");
                System.out.println("uuid = " + uuid);
                caseNo = uuid;
                PIDcall = Innercalls.PostIDverification(caseNo, sessionID, Add1, Add2, Add3, city, Countrycode, Fname,
                        Isdft, Lname, postcode, State, type, ssn, dob);
                ctoken = GenTokenID.substring(1, GenTokenID.length() - 1);
                ctoken = Username + " " + ctoken;
                String IDcaller = transucaller.TransUconnector("", PIDcall, url_case, ctoken, actionP);
                System.out.println("ID-Verify-response is " + IDcaller);
                GeneratedCaseID = IDcaller;

                JsonElement IdjsonTree = jsonParser.parse(IDcaller);
                if (IdjsonTree.isJsonObject()) {
                    JsonObject IdjsonObject = IdjsonTree.getAsJsonObject();
                    JsonElement Idc = IdjsonObject.get("Id");
                    Id_response = Idc.toString();
                    Id_response = Id_response.substring(1, Id_response.length() - 1);
                    System.out.println("Generated token Full response  = " + GenTokenID);
                    System.out.println("Generated token = " + Id_response);
                }
            }
            if (!GeneratedCaseID.isEmpty()) {
                // ctoken
                String ddcurl = Ddcurl + "?caseid=" + Id_response;
                System.out.println(ddcurl);
                String usrFinaldcs = transucaller.TransUconnector("", "NIL", ddcurl, ctoken, actionG);
                System.out.println("Final Result is... " + usrFinaldcs);

                JsonElement ResIdjsonTree = jsonParser.parse(usrFinaldcs);
                if (ResIdjsonTree.isJsonObject()) {
                    JsonObject ResIdjsonObject = ResIdjsonTree.getAsJsonObject();
                    JsonElement Id_result = ResIdjsonObject.get("Result");
                    result_response = Id_result.toString();
                    System.out.println("Final Result is ... " + result_response);
                }
            }

        }
        System.out.println("End Time ==== "+timestamp);
    }

    public String PostToken(String Usernam, String Ttamp, String HUsername, String HPsswd) {
        // creating simple request, creating token request
        Map<Object, Object> tokenreqMap = new HashMap<Object, Object>();
        JSONObject tokenreq_jsonObj;
        tokenreqMap.put("UserName", Usernam);
        tokenreqMap.put("Timestamp", Ttamp);
        tokenreqMap.put("PasswordHash", HPsswd);
        tokenreqMap.put("UserNameHash", HUsername);
        tokenreq_jsonObj = new JSONObject(tokenreqMap);
        System.out.println(tokenreq_jsonObj);
        String tokenkey = tokenreq_jsonObj.toString();
        return tokenkey;
    }

    public String PostIDverification(String CaseNumber, String SessionId, String Address1, String Address2,
            String Address3, String City, String CountryCode, String FirstName, String IsDefault, String LastName,
            String PostalCode, String State, String Type, String SocialSecurityNumber, String DateOfBirth) {
        String id_data = null;
        JSONObject jsonObjectReq = new JSONObject();
        JSONObject OuterjsonObjectReq = new JSONObject();
        Map<String, String> mapp = new HashMap<>();
        OuterjsonObjectReq.put("CaseNumber", CaseNumber);
        OuterjsonObjectReq.put("SessionId", SessionId);
        mapp.put("Address1", Address1);
        mapp.put("Address2", Address2);
        mapp.put("Address3", Address3);
        mapp.put("City", City);
        mapp.put("CountryCode", CountryCode);
        mapp.put("FirstName", FirstName);
        mapp.put("IsDefault", IsDefault);
        mapp.put("LastName", LastName);
        mapp.put("PostalCode", PostalCode);
        mapp.put("State", State);
        mapp.put("Type", Type);

        JSONArray Addresses = new JSONArray();
        Addresses.add(mapp);
        jsonObjectReq.put("Addresses", Addresses);
        OuterjsonObjectReq.put("Customer", jsonObjectReq);
        jsonObjectReq.put("FirstName", FirstName);
        jsonObjectReq.put("LastName", LastName);
        jsonObjectReq.put("SocialSecurityNumber", SocialSecurityNumber);
        jsonObjectReq.put("DateOfBirth", DateOfBirth);
        System.out.println("JSON file created: " + OuterjsonObjectReq.toString().replace("\\/", "/"));
        return OuterjsonObjectReq.toString().replace("\\/", "/");
    }

    public String GETClientDetails() {

        return "";
    }
}