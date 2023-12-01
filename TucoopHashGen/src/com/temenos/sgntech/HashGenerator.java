package com.temenos.sgntech;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * TODO: Document me!
 *
 * @author Peter
 *
 */
public class HashGenerator {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String mytimestamp;
        String username = null;
        String Nusername = null;
        String username1 = null;
        String Nusername1 = null;
        String USERname = null;
        String PASSword = null;
        StringBuilder UhexString = new StringBuilder();
       StringBuilder PhexString = new StringBuilder();
       StringBuilder UhexString1 = new StringBuilder();
       StringBuilder PhexString1 = new StringBuilder();
        final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String[] tempm = sdf2.format(timestamp).toString().toString().split("\\+");
        mytimestamp = (tempm[0] + "Z");

        String mytimestamp1 = "2023-11-29T12:08:59.094Z";
        String Usern = "Cooperativa_Ahorro_y_Credito_TestSite";
        String Psswd = "830058098b40451d8ccb366a3c88a372";
        String secret = "ae9361c550484a838ca5b6ef0c578488";
        String Newinput;
        String input;
        String Newinput1;
        String input1;
        

        String[] var = new String[3];
        var[0] = Usern;
        var[1] = Psswd;
        var[2] = secret;
        MessageDigest md = null;
        MessageDigest mdn = null;
        MessageDigest md1 = null;
        MessageDigest mdn1 = null;

            String Variable = mytimestamp1 + "." + var[0].toString();   
            try {
                input = Variable;
                md = MessageDigest.getInstance("SHA-256");
                byte[] hash = md.digest(input.getBytes());
                for (byte b : hash) {
                    UhexString.append(String.format("%02x", b));
                    username = UhexString.toString();
                }
            } catch (NoSuchAlgorithmException e1) {
            }
            try {
                Newinput = username + "." + var[2];
                mdn = MessageDigest.getInstance("SHA-256");
                byte[] nhash = mdn.digest(Newinput.getBytes());
                for (byte c : nhash) {
                    PhexString.append(String.format("%02x", c));
                    Nusername = PhexString.toString();
                }
            } catch (NoSuchAlgorithmException e2) {
            }
                  
            String Variable1 = mytimestamp1 + "." + var[1].toString();   
            try {
                input1 = Variable1;
                md1 = MessageDigest.getInstance("SHA-256");
                byte[] hash1 = md1.digest(input1.getBytes());
                for (byte d : hash1) {
                    UhexString1.append(String.format("%02x", d));
                    username1 = UhexString1.toString();
                }
            } catch (NoSuchAlgorithmException e3) {
            }
            try {
                Newinput1 = username1 + "." + var[2];
                mdn1 = MessageDigest.getInstance("SHA-256");
                byte[] nhash = mdn1.digest(Newinput1.getBytes());
                for (byte f : nhash) {
                    PhexString1.append(String.format("%02x", f));
                    Nusername1 = PhexString1.toString();
                }
            } catch (NoSuchAlgorithmException e2) {
            }
    
            System.out.println("Username  Value Second ......." + Nusername);
            System.out.println("Password  Value Second ......." + Nusername1);
    }
}
