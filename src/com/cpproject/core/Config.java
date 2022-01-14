package com.cpproject.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;

public class Config {
    public Random random = new Random();
    public HashMap<String, Integer> storage = new HashMap<>();
    public Config () {
        this.storage.put("minimalNumberOfOpenedCheckouts", 2);
        this.storage.put("numberOfCheckouts", 5);
        this.storage.put("clientsPerCheckout", 2);
        this.storage.put("cashTimeMin", 1500);
        this.storage.put("cashTimeMax", 4000);
        this.storage.put("clientMin", 2000);
        this.storage.put("clientMax", 3000);
        this.storage.put("clientGenerateMin", 200);
        this.storage.put("clientGenerateMax", 1000);
        this.storage.put("clientGenerateOn", 1);
    }
    public synchronized void setValue(String key, int value) {
        this.storage.put(key, value);
    }
    public synchronized Integer getValue(String key) {
        if (key.equals("cashTime")) {
            int min = this.storage.get("cashTimeMin");
            int max = this.storage.get("cashTimeMax");
            return this.random.nextInt(max - min) + min;
        }
        if (key.equals("client")) {
            int min = this.storage.get("clientMin");
            int max = this.storage.get("clientMax");
            return this.random.nextInt(max - min) + min;
        }
        if (key.equals("clientGenerate")) {
            int min = this.storage.get("clientGenerateMin");
            int max = this.storage.get("clientGenerateMax");
            return this.random.nextInt(max - min) + min;
        }
        return this.storage.get(key);
    }
    private Integer getProperty (Properties prop, String key, Integer def) {
        try {
            return Integer.parseInt(prop.getProperty(key, def.toString()));
        } catch (NumberFormatException e) {
            return def;
        }
    }
    public void loadFromProperties (InputStream stream) throws IOException {
        Properties appProps = new Properties();
        appProps.load(stream);
        this.storage.put("minimalNumberOfOpenedCheckouts", this.getProperty(appProps, "minimalNumberOfOpenedCheckouts", 2));
        this.storage.put("numberOfCheckouts", this.getProperty(appProps, "numberOfCheckouts", 5));
        this.storage.put("clientsPerCheckout", this.getProperty(appProps, "clientsPerCheckout", 2));
        this.storage.put("cashTimeMin", this.getProperty(appProps, "clientMin", 200));
        this.storage.put("cashTimeMax", this.getProperty(appProps, "clientMax", 1000));
        this.storage.put("clientMin", this.getProperty(appProps,"cashTimeMin", 200));
        this.storage.put("clientMax", this.getProperty(appProps,"cashTimeMax", 1000));
        this.storage.put("clientGenerateMin", this.getProperty(appProps,"clientGenerateMin", 200));
        this.storage.put("clientGenerateMax", this.getProperty(appProps,"clientGenerateMax", 1000));
        this.storage.put("clientGenerateOn", this.getProperty(appProps, "clientGenerateOn", 1));
    }
}
