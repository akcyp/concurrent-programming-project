# Project for concurrent programming classes

Report on the semester project - winter 2021/2022

## Description

In the task we face the problem of several (N > 2) queues for programs that handle other threads. Number of queues is determined by a separate - single instance of a supervisor thread, which depends on the number of waiting threads (clients). Based on this number, the instance creates or terminates the processing processes (queues).

In such an assumption, there is no possibility of jamming or lack of viability because each thread will be served at some point. The algorithm by applying FIFO (first in - first out) provides the fairness of receiving access.

A similar - a more realistic example of a similar problem could be found in programs performing high complexity computing - for example, this could be image processing. Website threads websites could entrust such processing to separate processes to increase efficiency. However in the case of many requests at short intervals, such processing should be queued or new ones should be created processing processes. Such an example resembles the problem contained in the project I am developing.

## Ways of synchronization

Customer threads are synchronized by blocking them until they are handled by the cashier thread. This is happening for using `java.util.concurrent.CountDownLatch`. Cashier thread by changing the value of this object (after successfully serving the customer who was first in queue) unblocks the thread of the client, allowing him to performing the next steps (in our case, leaving the store).

Also I use the `java.util.concurrent.LinkedBlockingDeque` object to synchronize the queue. which allows
keeping client instances in sync.

## Config

We can modify the default program values by editing the `config.properties` file.

```js
numberOfCheckouts=6 // the number of available cash registers in the store
minimalNumberOfOpenedCheckouts=2 // minimum number of open cash registers
clientsPerCheckout=2 // number of customers per checkout
clientGenerateMin=200 // minimum time between the arrival of new customers
clientGenerateMax=1000 // maximum time between the arrival of new customers
clientMin=2000 // minimum time for the customer to make purchases
clientMax=3000 // maximum shopping time by the customer
cashTimeMin=1500 // min. time needed for the customer to be served by the cashier
cashTimeMax=4000 // max. time needed for the customer to be served by the cashier
clientGenerateOn=0 // automatic generation of clients 0 - off, 1 - on
```

The same values can be modified while the program is running through the appropriate menu.
![Menu](https://user-images.githubusercontent.com/67923777/152676217-dfb3bc10-4b82-4b67-ae4b-d8ec3a0a15c8.png)

## Program operation

<div>
  <img align="left" src="https://user-images.githubusercontent.com/67923777/152676236-e180c534-1682-4b25-b862-7eeab5d7fe8b.png" style="margin: 5px" />
  Customers staying in the store are represented by found objects on the left side of the graphical interface. Every such facility has assigned color depending on the state it is in.
  <ul>
    <li>Green - means that the customer is in the store and chooses to buy</li>
    <li>Red - the customer was served at the checkout and leaves the store</li>
    <li>Black - the customer is in the queue at the checkout</li>
  </ul>
  Each customer passes through each of these states in that order.
</div>
</br></br></br></br></br></br></br></br>
<div>
  <img align="right" src="https://user-images.githubusercontent.com/67923777/152676255-19259e46-dfa6-4b92-82bc-d1d52310e92c.png" style="margin: 5px" />
  The checkouts located in the store are represented by objects located on the right side of the GUI. Anyone like that an object is assigned a color depending on its state of affairs is located.
  <ul>
    <li>Green - the cash register is open and is serving or waiting for customers</li>
    <li>Red - the ticket office is closed and does not serve customers</li>
    <li>Blue - the cash register is closed but it still has to serve customers in the queue.</li>
  </ul>

  Cash registers are opened or closed by the cashier supervisor, who in calculates the quantity depending on the number of customers in the store needed to open the cash registers. When, in a short period of time, customers arrived in the store, the manager creates a cashier thread.
  </br></br>
  Conversely, when customers decreased, the cash register is closed. There may be situations where the cash register is closed (and ends serve customers from the queue - blue color) however due a sudden increase in the number of people in the store, the cashier must cancel the break and reopen the cash register.
</div>
