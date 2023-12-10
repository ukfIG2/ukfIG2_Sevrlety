package _03_AIS;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TestHash {
    public static void main(String[] args) {
        String password = "BlablaBlaBla";

        try {
            // Create a MessageDigest object
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Update the message digest with the bytes of the password
            md.update(password.getBytes());

            // Get the hash value
            byte[] hashedBytes = md.digest();

            // Convert the byte array to a hexadecimal string
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }

            String hashedPassword = sb.toString();
            System.out.println("Hashed Password: " + hashedPassword);
            System.out.println(hashedPassword.length());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
