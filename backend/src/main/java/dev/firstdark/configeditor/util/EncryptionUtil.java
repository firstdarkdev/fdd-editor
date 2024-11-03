package dev.firstdark.configeditor.util;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;

public final class EncryptionUtil {

    // Instance of the Encryptor Used
    private final StandardPBEStringEncryptor encryptor;

    public EncryptionUtil(String encCode) {
        encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(encCode);
    }

    /**
     * Will Encrypt the string passed into it, if it's not already encrypted
     *
     * @param input The string to be encrypted
     * @return The encrypted string
     */
    public String encrypt(String input) {
        if (isEncrypted(input)) {
            return input;
        }

        input = "enc:" + input;
        return encryptor.encrypt(input);
    }

    /**
     * Decrypts an encrypted string
     *
     * @param input The encrypted String
     * @return The Plain Text String
     */
    public String decrypt(String input) {
        if (!isEncrypted(input))
            return input;

        input = internalDecrypt(input);

        if (input.startsWith("enc:")) {
            input = input.replaceFirst("enc:", "");
        }
        return input;
    }

    // Used internally
    private String internalDecrypt(String input) {
        return encryptor.decrypt(input);
    }

    // Test if String is encrypted
    private boolean isEncrypted(String input) {
        try {
            String temp = internalDecrypt(input);
            return temp.startsWith("enc:");
        } catch (EncryptionOperationNotPossibleException ignored) {
            // String is likely not encrypted
        }
        return false;
    }
}