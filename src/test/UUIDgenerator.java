import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class UUIDgenerator {


    @Test
    public void generateUUID() {
        System.out.println(UUID.randomUUID().toString());
    }

    @Test
    public void generateUUID_SHA() {
//        Let’s generate a unique key using ‘SHA-256’ and a random UUID:

        MessageDigest salt = null;
        try {
            salt = MessageDigest.getInstance("SHA-256");
            salt.update(UUID.randomUUID().toString().getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println(salt.digest());
        String digest = Base64.encodeBase64String(salt.digest());
        System.out.println(digest);
//        bytesToHex(salt.digest());
    }

}
