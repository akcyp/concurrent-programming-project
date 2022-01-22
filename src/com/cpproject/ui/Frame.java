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

        JMenu configMenu = new JMenu("Config");

        JMenuItem modifyClientsPerCheckoutButton = new JMenuItem("clientsPerCheckout");
        modifyClientsPerCheckoutButton.addActionListener(this);
        configMenu.add(modifyClientsPerCheckoutButton);

        JMenuItem modifyClientGenerateMinButton = new JMenuItem("clientGenerateMin");
        modifyClientGenerateMinButton.addActionListener(this);
        configMenu.add(modifyClientGenerateMinButton);

        JMenuItem modifyClientGenerateMaxButton = new JMenuItem("clientGenerateMax");
        modifyClientGenerateMaxButton.addActionListener(this);
        configMenu.add(modifyClientGenerateMaxButton);

        JMenuItem modifyClientMinButton = new JMenuItem("clientMin");
        modifyClientMinButton.addActionListener(this);
        configMenu.add(modifyClientMinButton);

        JMenuItem modifyClientMaxButton = new JMenuItem("clientMax");
        modifyClientMaxButton.addActionListener(this);
        configMenu.add(modifyClientMaxButton);

        JMenuItem modifyCashTimeMinButton = new JMenuItem("cashTimeMin");
        modifyCashTimeMinButton.addActionListener(this);
        configMenu.add(modifyCashTimeMinButton);

        JMenuItem modifyCashTimeMaxButton = new JMenuItem("cashTimeMax");
        modifyCashTimeMaxButton.addActionListener(this);
        configMenu.add(modifyCashTimeMaxButton);

        JMenuBar menu = new JMenuBar();
        menu.add(optionsMenu);
        menu.add(configMenu);
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
            case "clientsPerCheckout" -> {
                new InputPanel("number of clients per one checkout", this.shop.config.getValue("clientsPerCheckout"), (val) -> {
                    this.shop.config.setValue("clientsPerCheckout", val);
                    return null;
                });
            }
            case "clientGenerateMin" -> {
                new InputPanel("minimum time between generating new people", this.shop.config.getValue("clientGenerateMin"), (val) -> {
                    if (val > this.shop.config.getValue("clientGenerateMax")) {
                        return "clientGenerateMin cannot be greater than clientGenerateMax";
                    } else {
                        this.shop.config.setValue("clientGenerateMin", val);
                        return null;
                    }
                });
            }
            case "clientGenerateMax" -> {
                new InputPanel("maximum time between generating new people", this.shop.config.getValue("clientGenerateMax"), (val) -> {
                    if (val < this.shop.config.getValue("clientGenerateMin")) {
                        return "clientGenerateMax cannot be less than clientGenerateMin";
                    } else {
                        this.shop.config.setValue("clientGenerateMax", val);
                        return null;
                    }
                });
            }
            case "clientMin" -> {
                new InputPanel("minimum shopping time", this.shop.config.getValue("clientMin"), (val) -> {
                    if (val > this.shop.config.getValue("clientMax")) {
                        return "clientMin cannot be greater than clientMax";
                    } else {
                        this.shop.config.setValue("clientMin", val);
                        return null;
                    }
                });
            }
            case "clientMax" -> {
                new InputPanel("maximum shopping time", this.shop.config.getValue("clientMax"), (val) -> {
                    if (val < this.shop.config.getValue("clientMin")) {
                        return "clientMax cannot be less than clientMin";
                    } else {
                        this.shop.config.setValue("clientMax", val);
                        return null;
                    }
                });
            }
            case "cashTimeMin" -> {
                new InputPanel("minimum customer service time by the cashier", this.shop.config.getValue("cashTimeMin"), (val) -> {
                    if (val > this.shop.config.getValue("cashTimeMax")) {
                        return "cashTimeMin cannot be greater than cashTimeMax";
                    } else {
                        this.shop.config.setValue("cashTimeMin", val);
                        return null;
                    }
                });
            }
            case "cashTimeMax" -> {
                new InputPanel("maximum customer service time by the cashier", this.shop.config.getValue("cashTimeMax"), (val) -> {
                    if (val < this.shop.config.getValue("cashTimeMin")) {
                        return "cashTimeMax cannot be less than cashTimeMin";
                    } else {
                        this.shop.config.setValue("cashTimeMax", val);
                        return null;
                    }
                });
            }
        }
    }
}
