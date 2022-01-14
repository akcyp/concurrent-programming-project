package com.cpproject.core;

import java.util.concurrent.LinkedBlockingDeque;

public class Shop extends Thread {
    public Config config;
    public final LinkedBlockingDeque<Client> clients = new LinkedBlockingDeque<>();
    public Cashier[] cashiers;
    public int clientCounter = 0;
    public volatile boolean started = false;
    public Shop(Config config) {
        this.config = config;
    }
    @Override
    public void start () {
        this.cashiers = new Cashier[config.getValue("numberOfCheckouts")];
        for (int i = 0; i < this.cashiers.length; i++) {
            this.cashiers[i] = new Cashier(this, i);
            if (i < 2) {
                this.openCash(i);
            }
        }
        this.started = true;
        while (true) {
            this.manage();
            try {
                Thread.sleep(this.config.getValue("clientGenerate"));
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            if (this.config.getValue("clientGenerateOn") == 1) {
                this.addNewCustomer();
            }
        }
    }
    public void addNewCustomer () {
        Client customer = new Client(this, this.clientCounter++);
        this.clients.add(customer);
        customer.start();
    }
    public synchronized void manage () {
        int minimal = this.config.getValue("minimalNumberOfOpenedCheckouts");
        int perCheckout = this.config.getValue("clientsPerCheckout");
        int clientsInShop = this.clients.stream().filter(client -> client.getClientState() != ClientState.LEAVING).toList().size();
        int checkoutsNeeded = (int) Math.max(minimal, clientsInShop / perCheckout);
        for(int i = 0; i < this.cashiers.length; i++) {
            if (i < checkoutsNeeded) {
                if (!this.cashiers[i].isActive()) {
                    System.out.printf("Otwarcie kasy #%d%n", i);
                    this.openCash(i);
                }
            } else {
                if (this.cashiers[i].isActive()) {
                    System.out.printf("Zamknięcie kasy #%d%n", i);
                    this.closeCash(i);
                }
            }
        }
    }
    public void closeCash (int i) {
        this.cashiers[i].active = false;
        System.out.printf("Kasa #%d zostaje zamknięta, ale kasjer obsłuży jeszcze %d klientów%n", this.cashiers[i].id, this.cashiers[i].queue.size());
    }
    public void openCash (int i) {
        if (!this.cashiers[i].isAlive()) {
            int id = this.cashiers[i].id;
            this.cashiers[i] = new Cashier(this, id);
            this.cashiers[i].active = true;
            this.cashiers[i].start();
        }
    }
}
