package com.cpproject;

import com.cpproject.core.Config;
import com.cpproject.core.Shop;
import com.cpproject.ui.Frame;

import java.io.IOException;

public class Main {
    public static void main (String[] args) throws IOException {
        Config config = new Config();
        config.loadFromProperties(Main.class.getClassLoader().getResourceAsStream("config.properties"));
        Shop shop = new Shop(config);
        Frame frame = new Frame(shop);
        frame.setVisible(true);
        shop.start();
    }
}
