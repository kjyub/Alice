package db;


import java.util.*;

public class DBConnectData {
    static String dbURL = "jdbc:mysql://www.antsstory.site:23306/aliceDB";
    Properties properties = new Properties();
    DBConnectData(){
        properties.setProperty("user","kjy");
        properties.setProperty("password","alice1234");
        properties.setProperty("characterEncoding","utf-8");
        properties.setProperty("serverTimezone","UTC");
    }
}
