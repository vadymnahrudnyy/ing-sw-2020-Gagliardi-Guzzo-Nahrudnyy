package it.polimi.ingsw.PSP30.Utils;

import it.polimi.ingsw.PSP30.Messages.Message;

import java.util.NoSuchElementException;

/**
 *The {@code Utils.QueueOfEvents} class is used to manage all the events between client e server by implementing a FIFO queue.
 * @version 1.0
 */
public class QueueOfEvents {
    private static final int MAX_EVENTS = 100;
    private int numOfEvents;
    private Node firstEvent;
    private Node lastEvent;


    /**
     * Defines the structure of the nodes.
     */
    private static class Node {
        private Message message;
        private Node nextEvent;
    }

    /**
     * Constructor which creates an empty queue.
     */
    public QueueOfEvents() {
        firstEvent = null;
        lastEvent = null;
        numOfEvents = 0;
    }

    /**
     * Tells if the queue is empty.
     * @return true if the queue is empty or false if it has at least a node.
     */
    public boolean isEmpty() {
        return firstEvent == null;
    }

    /**
     * Allows to create a new node to insert in the queue.
     * @param message specifies the message to insert into the queue.
     */
    public synchronized void enqueueEvent(Message message) {
        Node oldLastEvent = lastEvent;
        lastEvent = new Node();
        lastEvent.message = message;
        lastEvent.nextEvent = null;
        if (isEmpty()) firstEvent = lastEvent;
        else oldLastEvent.nextEvent = lastEvent;
        numOfEvents++;
    }

    /**
     * Dequeues the first inserted node in message queue.
     * @return the first message of the queue.
     */
    public Message dequeueEvent() throws NoSuchElementException {
        if (isEmpty()) return null;
        Message message = firstEvent.message;
        firstEvent = firstEvent.nextEvent;
        numOfEvents--;
        if (isEmpty()) lastEvent = null;
        return message;
    }


    /**
     * Flushes the queue by deleting all the nodes.
     */
    public void flushQueue() {
        do {
            if (isEmpty()) throw new NoSuchElementException("The queue is already empty");
            Message message = firstEvent.message;
            firstEvent = firstEvent.nextEvent;
            numOfEvents--;
            if (isEmpty()) lastEvent = null;
        }while (numOfEvents == 0) ;

    }
}