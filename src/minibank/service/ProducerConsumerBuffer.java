package minibank.service;

import minibank.model.Command;

public class ProducerConsumerBuffer {
    private final Command[] buffer;
    private int count = 0;
    private int in = 0;
    private int out = 0;
    private boolean stopped = false;

    public ProducerConsumerBuffer(int capacity) {
        this.buffer = new Command[capacity];
    }

    public synchronized void produce(Command cmd) throws InterruptedException {
        while (count == buffer.length && !stopped) {
            wait();
        }
        if (stopped) return;

        buffer[in] = cmd;
        in = (in + 1) % buffer.length;
        count++;
        notifyAll();
    }

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
        notifyAll();
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
