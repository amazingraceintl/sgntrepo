package com.temenos.sgntech;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
/**
 * TODO: Document me!
 *
 * @author Peter Ajayi
 *
 */
public class HashGenerator {

    String mytimestamp1;
    String Usern;
    String Psswd;
    String secret;

    public String HashGenerator(String mytimestamp1, String usern, String psswd, String secret) {
        this.mytimestamp1 = mytimestamp1;
        Usern = usern; Psswd = psswd;
        this.secret = secret;
        String username = null; String Nusername = null; String username1 = null;
        String Nusername1 = null; String USERname = null; String PASSword = null;
        String Newinput; String input; String Newinput1; String input1;

        String[] var = new String[3];
        StringBuilder UhexString = new StringBuilder();
        StringBuilder PhexString = new StringBuilder();
        StringBuilder UhexString1 = new StringBuilder();
        StringBuilder PhexString1 = new StringBuilder();

        var[0] = Usern;
        var[1] = Psswd;
        var[2] = this.secret;
        MessageDigest md = null;
        MessageDigest mdn = null;
        MessageDigest md1 = null;
        MessageDigest mdn1 = null;

        String Variable = this.mytimestamp1 + "." + var[0].toString();
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

        String Variable1 = this.mytimestamp1 + "." + var[1].toString();
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
        return this.mytimestamp1 + "|" + Nusername + "|" + Nusername1;
    }

    public HashGenerator() {
        super();
        // TODO Auto-generated constructor stub

    }

}
