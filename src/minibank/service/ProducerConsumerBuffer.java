package minibank.service;

import minibank.model.Command;

/**
 * Shared bounded array buffer for producer-consumer transaction coordination
 * using wait() and notifyAll() inside synchronized blocks (Practical 10 Part B).
 */
public class ProducerConsumerBuffer {
    private final Command[] buffer;
    private int count = 0;
    private int in = 0;
    private int out = 0;
    private boolean stopped = false;

    public ProducerConsumerBuffer(int capacity) {
        this.buffer = new Command[capacity];
    }

    /**
     * Producer method adds a transaction command to buffer.
     * Waits when buffer is full.
     */
    public synchronized void produce(Command cmd) throws InterruptedException {
        while (count == buffer.length && !stopped) {
            wait();
        }
        if (stopped) return;

        buffer[in] = cmd;
        in = (in + 1) % buffer.length;
        count++;
        notifyAll(); // Notify consumer
    }

    /**
     * Consumer method retrieves and removes transaction command from buffer.
     * Waits when buffer is empty.
     */
    public synchronized Command consume() throws InterruptedException {
        while (count == 0 && !stopped) {
            wait();
        }
        if (count == 0 && stopped) {
            return null;
        }

        Command cmd = buffer[out];
        out = (out + 1) % buffer.length;
        count--;
        notifyAll(); // Notify producer
        return cmd;
    }

    public synchronized void stop() {
        this.stopped = true;
        notifyAll();
    }

    public synchronized int getCount() {
        return count;
    }
}
