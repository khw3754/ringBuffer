import java.util.Arrays;

public class Main {

    public static class CircularBuffer<T> {
        @Override
        public String toString() {
            return "CircularBuffer{" +
                    "buffer=" + Arrays.toString(buffer) +
                    ", capacity=" + capacity +
                    ", front=" + front +
                    ", back=" + back +
                    ", size=" + bufsize +
                    '}';
        }

        private T[] buffer;
        private int capacity;
        private int front;
        private int back;
        private int bufsize;

        public CircularBuffer(int capacity) {
            this.capacity = capacity;
            buffer = (T[]) new Object[capacity];
            front = 0;
            back = 0;
            bufsize = 0;    // 저장된 데이터 개수
        }

        public synchronized void put(T element) {
            if (isFull()) {
                throw new IllegalStateException("Buffer is full");
            }

            buffer[back] = element;
            back = (back + 1) % capacity;
            bufsize++;
        }

        public synchronized T get() {
            if (isEmpty()) {
                throw new IllegalStateException("Buffer is empty");
            }

            T element = buffer[front];
            buffer[front] = null;
            front = (front + 1) % capacity;
            bufsize--;
            return element;
        }

        public boolean isFull() {
            return bufsize == capacity;
        }

        public boolean isEmpty() {
            return bufsize == 0;
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