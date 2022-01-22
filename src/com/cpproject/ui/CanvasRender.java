package com.cpproject.ui;

import com.cpproject.core.Cashier;
import com.cpproject.core.Client;
import com.cpproject.core.ClientState;
import com.cpproject.core.Shop;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

class CanvasRender extends JComponent {
    static int checkoutSize = 100;
    static int clientSize = 70;
    final public Shop shop;
    CanvasRender(Shop shop) {
        this.shop = shop;
        Timer timer = new Timer(1000 / 60, evt -> this.repaint()); // 60 FPS
        timer.setRepeats(true);
        timer.start();
    }
    @Override
    public void paintComponent (Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.GRAY);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        if (this.shop.started) {
            for (int i = 0; i < this.shop.cashiers.length; i++) {
                // Print checkout
                int left = this.getWidth() - checkoutSize - 10;
                int top = 10 + i * (checkoutSize + 10);
                Rectangle box = new Rectangle(left, top + 5, checkoutSize, checkoutSize);
                this.printCashier(g2, box, this.shop.cashiers[i]);
                // Print checkout queue
                int index = 1;
                for (Client client: this.shop.cashiers[i].queue) {
                    Rectangle clientBox = new Rectangle(box.x - (clientSize + 4) * index, box.y, clientSize, clientSize);
                    this.printClient(g2, clientBox, client);
                    index++;
                }
            }
            this.printShopClients(g2);
        }
    }
    private void printShopClients (Graphics2D g2) {
        int index = 0;
        int maxClientsPerHeight = (this.getHeight() - 10) / (clientSize + 10);
        for (Client client: this.shop.clients) {
            ClientState state = client.getClientState();
            if (state != ClientState.IN_SHOP && state != ClientState.LEAVING) continue;
            int left = 5 + (int) Math.floor(index / maxClientsPerHeight) * (clientSize + 5);
            int top = 5 + (clientSize + 5) * (index % maxClientsPerHeight);
            Rectangle box = new Rectangle(left, top, clientSize, clientSize);
            this.printClient(g2, box, client);
            index++;
        }
    }
    private void centerString(Graphics2D g2, Rectangle r, String text) {
        String[] lines = text.split("\n");
        int totalHeight = Arrays.stream(lines).map(str -> (int) g2.getFontMetrics().getStringBounds(str, g2).getHeight()).reduce(0, Integer::sum);
        int dy = 0;
        for (String line: lines) {
            Rectangle2D fontBox = g2.getFontMetrics().getStringBounds(line, g2);
            int fontHeight = (int) Math.round(fontBox.getHeight());
            int fontWidth = (int) Math.round(fontBox.getWidth());
            int fX = (int) Math.round(fontBox.getX());
            int fY = (int) Math.round(fontBox.getY());
            int a = (r.width / 2) - (fontWidth / 2) - fX;
            int b = (r.height / 2) - (totalHeight / 2) - fY + dy;
            g2.drawString(line, r.x + a, r.y + b);
            dy += fontHeight;
        }
    }
    private void printClient (Graphics2D g2, Rectangle box, Client client) {
        g2.setColor(Color.WHITE);
        g2.fillRect(box.x, box.y, box.width, box.height);
        g2.setStroke(new BasicStroke(2));
        g2.setColor(this.getClientColor(client));
        g2.drawRect(box.x, box.y, box.width, box.height);
        g2.setColor(Color.BLACK);
        this.centerString(g2, box, String.format("Client #%d", client.id));
    }
    private Color getClientColor (Client client) {
        return switch (client.getClientState()) {
            case IN_SHOP -> Color.GREEN;
            case IN_QUEUE, CASHING -> Color.BLACK;
            case LEAVING -> Color.RED;
        };
    }
    private void printCashier (Graphics2D g2, Rectangle box, Cashier cashier) {
        g2.setColor(Color.WHITE);
        g2.fillRect(box.x, box.y, box.width, box.height);
        g2.setStroke(new BasicStroke(2));
        g2.setColor(this.getCashierColor(cashier));
        g2.drawRect(box.x, box.y, box.width, box.height);
        g2.setColor(Color.BLACK);
        this.centerString(g2, box, String.format("Cash #%d\nQueue = %d", cashier.id, cashier.queue.size()));
    }
    private Color getCashierColor (Cashier cashier) {
        return cashier.isAlive() ?
            cashier.isActive() ? Color.GREEN : Color.BLUE
            : Color.RED;
    }
}