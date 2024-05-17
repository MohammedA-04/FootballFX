package com.example.textfieldfx;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// Read APIKEY.txt from text file in gitIgnore
// This APIKEY.txt is then used in other classes
public class APIConfig {

    private static final String API_KEY_FROM_FILE = "APIKEY.txt";
    private static String APIKEY;

    static {

        try (BufferedReader r = new BufferedReader(new FileReader(API_KEY_FROM_FILE))) {
            APIKEY = r.readLine();

        }
        catch (IOException e) {
            System.out.println("Error:"+ e.getMessage());
            APIKEY = null;
            throw new RuntimeException(e);
        }
    }

    // returns APIkey
    public static String getAPIKEY(){
        return APIKEY;
    }
}
