package itis.khabibullina;

import itis.khabibullina.net.NetSample;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        NetSample net = new NetSample();
        Map<String, String> map = new HashMap<>();
        map.put("name", "Sen. Anala Iyer");
        map.put("email", "sen_aRa_iyer143683@stroman-leannon.test");
        map.put("gender", "female");
        map.put("status", "active");

        net.get("https://gorest.co.in/public/v2/users", map);
        net.post("https://gorest.co.in/public/v2/users", map);
        net.put("https://gorest.co.in/public/v2/users", map);
        net.get("https://gorest.co.in/public/v2/users", map);
        net.delete("https://gorest.co.in/public/v2/users", map);
    }
}