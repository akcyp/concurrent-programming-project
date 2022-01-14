package com.cpproject.core;

import java.util.concurrent.LinkedBlockingDeque;

public class Cashier extends Thread {
    public LinkedBlockingDeque<Client> queue = new LinkedBlockingDeque<>();
    public volatile boolean active = false;
    public int id;
    public Shop shop;
    Cashier(Shop shop, int id) {
        super(String.format("cashier#%d", id));
        this.id = id;
        this.shop = shop;
    }
    @Override
    public void run() {
        System.out.printf("%s rozpoczyna prace%n", this.getName());
        while (true) {
            if (this.queue.size() > 0) {
                Client first = this.queue.peekFirst();
                try {
                    this.handle(first);
                    System.out.printf("Odblokowanie wątku %s przez %s%n", first.getName(), Thread.currentThread().getName());
                    first.latch.countDown();
                } catch (InterruptedException e) {
                    System.out.print(e.toString());
                }
            } else if (!this.isActive()) {
                break;
            }
        }
        System.out.printf("%s konczy prace%n", this.getName());
    }
    public void handle (Client client) throws InterruptedException {
        System.out.printf("%s zaczyna obsługiwać klienta #%d%n", this.getName(), client.id);
        client.setClientState(ClientState.CASHING);
        Thread.sleep(this.shop.config.getValue("cashTime")); // skasowanie rzeczy w kasie
        System.out.printf("%s kończy obsługiwać klienta #%d%n", this.getName(), client.id);
        this.queue.remove(client);
    }
    public boolean isActive () {
        return this.active;
    }
}
