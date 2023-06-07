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

        public synchronized void put(T element) {
            if (isFull()) {
                throw new IllegalStateException("Buffer is full");
            }

            buffer[back] = element;
            back = (back + 1) % bufsize;
            numberOfEntries++;
        }

        public synchronized T get() {
            if (isEmpty()) {
                throw new IllegalStateException("Buffer is empty");
            }

            T element = buffer[front];
            buffer[front] = null;
            front = (front + 1) % bufsize;
            numberOfEntries--;
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

        for (int i = 0; i < 5; i++){
            circularBuffer.put(i);
        }
        System.out.println(circularBuffer);

        for (int i = 5; i < 20; i++){
            circularBuffer.put(i);
            System.out.println(circularBuffer);
            int get = (int) circularBuffer.get();
            System.out.println("get = " + get + '\n');
        }

        return;
    }
}