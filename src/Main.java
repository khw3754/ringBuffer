import java.util.Arrays;

public class Main {

    public static class CircularBuffer<T> {
        @Override
        public String toString() {
            return "CircularBuffer{" +
                    "buffer=" + Arrays.toString(buffer) +
                    ", bufsize=" + bufsize +
                    ", front=" + front +
                    ", back=" + back +
                    ", numberOfEntries=" + numberOfEntries +
                    '}';
        }

        private T[] buffer;
        private int bufsize;
        private int front;
        private int back;
        private int numberOfEntries;

        public CircularBuffer(int capacity) {
            this.bufsize = capacity;
            buffer = (T[]) new Object[capacity];
            front = 0;
            back = 0;
            numberOfEntries = 0;    // 저장된 데이터 개수
        }

        public synchronized void put(T element) throws InterruptedException {
            if (isFull()) {
                System.out.println("Buffer is full");
                wait();
            }

            buffer[back] = element;
            back = (back + 1) % bufsize;
            numberOfEntries++;
            notify();
        }

        public synchronized T get() throws InterruptedException {
            if (isEmpty()) {
                System.out.println("Buffer is empty");
                wait();
            }

            T element = buffer[front];
            buffer[front] = null;
            front = (front + 1) % bufsize;
            numberOfEntries--;
            notify();
            return element;
        }

        public boolean isFull() {
            return numberOfEntries == bufsize;
        }

        public boolean isEmpty() {
            return numberOfEntries == 0;
        }
    }


    public static void main(String[] args)
    {
        CircularBuffer circularBuffer = new CircularBuffer<Integer>(10);

        final int[] data = {0};   // 발생 데이터
        Thread producerThread = new Thread(() -> {
            while(true) {
                try {
                    circularBuffer.put(data[0]);
                    System.out.println("put: " + data[0]);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                data[0]++;
            }
        });

        Thread consumerThread = new Thread(() -> {
            while(true) {
                try {
                    int get = (int) circularBuffer.get();
                    System.out.println("get: " + get);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        producerThread.start();
        consumerThread.start();

//      return;
    }
}