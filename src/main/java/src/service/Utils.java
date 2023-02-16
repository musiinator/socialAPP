package src.service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Random;

public class Utils {

    /**
     * Method that applies Depth First Search on the graph of the adjacency matrix
     * @param start - starting node
     * @param visited - vector of visited nodes
     * @param m - adjacency matrix
     * @param numberOfUsers - number of nodes in the graph
     */
    public void DFS(int start, int []visited, int [][]m, int numberOfUsers) {
        visited[start] = 1;
        for(int i=0; i<numberOfUsers; i++)
        {
            if (m[start][i] == 1 && visited[i] == 0){
                DFS(i, visited, m, numberOfUsers);
            }
        }
    }


    public void DFS1(int start, int [] visited, int [][] m, int numberOfUsers, int numbers_of_comunities){
        visited[start] = numbers_of_comunities;
        for(int i=0; i<numberOfUsers; i++)
        {
            if(m[start][i] == 1 && visited[i] == 0){
                DFS1(i,visited,m,numberOfUsers,numbers_of_comunities);
            }
        }
    }

    public static String encrypt(String password, byte[] salt) {

        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

}

