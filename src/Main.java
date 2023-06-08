import java.util.Arrays;
import java.util.Random;

public class Main {

    /**
     * 링버퍼 구현 클래스
     */
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

        // 버퍼에 data를 넣음
        public synchronized void put(T element) throws InterruptedException {
            if (isFull()) {
                System.out.println("##### Buffer is full #####");
                wait();
            }

            buffer[back] = element;
            back = (back + 1) % bufsize;
            numberOfEntries++;
            notify();
        }

        // 버퍼에서 data를 가져옴
        public synchronized T get() throws InterruptedException {
            if (isEmpty()) {
                System.out.println("##### Buffer is empty #####");
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


    public static void main(String[] args) throws InterruptedException {
        CircularBuffer circularBuffer = new CircularBuffer<Integer>(10);
        Random rand = new Random();

        int producerAvg = 100;      // 생산자 평균 속도
        int consumerAvg = 100;      // 소비자 평균 속도
        int dispersion = 10;        // 분산


        // 데이터를 생성하여 버퍼에 넣는 스레드
        final int[] data = {0};   // 발생 데이터
        Thread producerThread = new Thread(() -> {
            while(true) {
                try {
                    // data를 넣음
                    circularBuffer.put(data[0]);
//                    System.out.println("put: " + data[0]);

                    // 평균 100ms + 분산10 * 가우시안분포  시간만큼 sleep
                    int t = producerAvg + (int)(dispersion * rand.nextGaussian());
                    Thread.sleep(t);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                data[0]++;
            }
        });

        // 데이터를 소비하는 스레드
        Thread consumerThread = new Thread(() -> {
            int count = 0;
            while(true) {
                try {
                    // data를 가져옴
                    int get = (int) circularBuffer.get();
                    // 처리, 누락 출력
                    if (get == count) {
                        System.out.println("data " + get + " 처리 완료");
                    }else{
                        System.out.println("data " + count + " 누락 발생");
                    }
                    count++;

                    // 평균 100ms + 분산10 * 가우시안분포  시간만큼 sleep
                    int t = consumerAvg + (int)(dispersion * rand.nextGaussian());
                    Thread.sleep(t);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        producerThread.start();
        consumerThread.start();

      return;
    }
}