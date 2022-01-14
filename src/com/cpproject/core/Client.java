package com.cpproject.core;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class Client extends Thread {
    public CountDownLatch latch = new CountDownLatch(1);
    final public Shop shop;
    public int id;
    public volatile ClientState clientState;
    public synchronized ClientState getClientState () {
        return this.clientState;
    }
    public synchronized void setClientState (ClientState state) {
        this.clientState = state;
    }
    Client(Shop shop, int id) {
        super(String.format("client#%d", id));
        this.shop = shop;
        this.id = id;
    }
    @Override
    public void run () {
        try {
            System.out.printf("%s wchodzi do sklepu i zaczyna wybierać zakupy%n", this.getName());
            this.setClientState(ClientState.IN_SHOP);
            Thread.sleep(this.shop.config.getValue("client"));
            Cashier cashier = this.findCashWithShortestQueue();
            cashier.queue.add(this);
            System.out.printf("%s zrobił zakupy i staje w kolejce do kasy #%d [ilosc klientow w kolejce: %d]%n", this.getName(), cashier.id, cashier.queue.size());
            this.setClientState(ClientState.IN_QUEUE);
            this.latch.await();
            this.setClientState(ClientState.LEAVING);
            Thread.sleep(1000);
            this.shop.clients.remove(this);
            System.out.printf("%s wychodzi ze sklepu%n", this.getName());
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
    }
    public Cashier findCashWithShortestQueue () {
        return Arrays.stream(this.shop.cashiers).filter(Cashier::isActive).reduce(this.shop.cashiers[0], (prev, now) -> prev.queue.size() < now.queue.size() ? prev : now);
    }
}
