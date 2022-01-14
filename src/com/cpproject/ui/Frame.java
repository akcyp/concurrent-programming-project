package com.cpproject.ui;

import com.cpproject.core.Shop;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Frame extends JFrame implements ActionListener {
    public Shop shop;
    public CanvasRender canvas;
    private final JMenuItem toggleShopButton;
    public Frame(Shop shop) {
        this.shop = shop;
        this.setTitle("Shop Simulator");
        this.setResizable(false);
        // this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.setSize(1680, 860);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenu optionsMenu = new JMenu("Options");

        JMenuItem addClientButton = new JMenuItem("Add client");
        addClientButton.addActionListener(this);
        optionsMenu.add(addClientButton);

        JMenuItem toggleShopButton = new JMenuItem(this.shop.config.getValue("clientGenerateOn").equals(1) ? "Close shop" : "Open shop");
        this.toggleShopButton = toggleShopButton;
        toggleShopButton.addActionListener(this);
        optionsMenu.add(toggleShopButton);

        JMenuBar menu = new JMenuBar();
        menu.add(optionsMenu);
        this.setJMenuBar(menu);

        this.canvas = new CanvasRender(shop);
        this.canvas.setSize(this.getWidth(), this.getHeight());
        this.canvas.setFocusable(false);
        this.add(this.canvas);
    }
    public void actionPerformed(ActionEvent evt) {
        System.out.printf("[Event] %s\n", evt.getActionCommand());
        switch (evt.getActionCommand()) {
            case "Add client" -> this.shop.addNewCustomer();
            case "Close shop" -> {
                this.shop.config.setValue("clientGenerateOn", 0);
                this.toggleShopButton.setText("Open shop");
            }
            case "Open shop" -> {
                this.shop.config.setValue("clientGenerateOn", 1);
                this.toggleShopButton.setText("Close shop");
            }
        }
    }
}
