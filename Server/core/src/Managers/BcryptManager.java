package Managers;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class BcryptManager {

    //https://github.com/patrickfav/bcrypt
    public static String Encrypt(String open){
        return BCrypt.withDefaults().hashToString(12, open.toCharArray());
    }

    public static boolean verify(String verify, String password){
        return BCrypt.verifyer().verify(verify.toCharArray(), password).verified;
    }

}
