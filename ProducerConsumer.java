import java.util.LinkedList;
import java.util.concurrent.Semaphore;

class SharedBuffer {
    private LinkedList<Integer> buffer;
    private int bufferSize;
    private Semaphore producerSemaphore;
    private Semaphore consumerSemaphore;

    public SharedBuffer(int bufferSize) {
        this.bufferSize = bufferSize;
        this.buffer = new LinkedList<>();
        this.producerSemaphore = new Semaphore(bufferSize);
        this.consumerSemaphore = new Semaphore(0);
    }

    public void produce() throws InterruptedException {
        producerSemaphore.acquire();
        int item = (int) (Math.random() * 100);
        buffer.add(item);
        System.out.println("Produced: " + item);
        consumerSemaphore.release();
    }

    public void consume() throws InterruptedException {
        consumerSemaphore.acquire();
        int item = buffer.remove();
        System.out.println("Consumed: " + item);
        producerSemaphore.release();
    }
}

public class ProducerConsumer {
    public static void main(String[] args) {
        SharedBuffer buffer = new SharedBuffer(5);

        Thread producer = new Thread(() -> {
            try {
                while (true) {
                    buffer.produce();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                while (true) {
                    buffer.consume();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
    }
}
