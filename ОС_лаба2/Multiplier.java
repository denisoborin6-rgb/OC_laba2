import java.util.concurrent.atomic.AtomicInteger;

public class Multiplier {
    public static void multiplyStatic(Matrix A, Matrix B, Matrix C, int numThreads, Progress progress) throws InterruptedException {
        Thread[] threads = new Thread[numThreads];
        int rowsPerThread = A.rows / numThreads;
        int remainder = A.rows % numThreads;
        int startRow = 0;
        for (int t = 0; t < numThreads; t++) {
            int endRow = startRow + rowsPerThread + (t < remainder ? 1 : 0);
            final int from = startRow, to = endRow;
            threads[t] = new Thread(() -> {
                for (int i = from; i < to; i++) {
                    for (int j = 0; j < B.cols; j++) {
                        long sum = 0;
                        for (int k = 0; k < B.rows; k++) sum += A.data[i][k] * B.data[k][j];
                        C.data[i][j] = sum;
                    }
                    progress.increment();
                }
            });
            threads[t].start();
            startRow = endRow;
        }
        for (Thread thread : threads) thread.join();
    }

    public static void multiplyDynamic(Matrix A, Matrix B, Matrix C, int numThreads, Progress progress) throws InterruptedException {
        AtomicInteger nextRow = new AtomicInteger(0);
        Thread[] threads = new Thread[numThreads];
        for (int t = 0; t < numThreads; t++) {
            threads[t] = new Thread(() -> {
                while (true) {
                    int i = nextRow.getAndIncrement();
                    if (i >= A.rows) break;
                    for (int j = 0; j < B.cols; j++) {
                        long sum = 0;
                        for (int k = 0; k < B.rows; k++) sum += A.data[i][k] * B.data[k][j];
                        C.data[i][j] = sum;
                    }
                    progress.increment();
                }
            });
            threads[t].start();
        }
        for (Thread thread : threads) thread.join();
    }

    public static void multiplyTriangularStatic(Matrix A, Matrix B, Matrix C, int numThreads, Progress progress) throws InterruptedException {
        Thread[] threads = new Thread[numThreads];
        int rowsPerThread = A.rows / numThreads;
        int remainder = A.rows % numThreads;
        int startRow = 0;
        for (int t = 0; t < numThreads; t++) {
            int endRow = startRow + rowsPerThread + (t < remainder ? 1 : 0);
            final int from = startRow, to = endRow;
            threads[t] = new Thread(() -> {
                for (int i = from; i < to; i++) {
                    for (int j = 0; j < B.cols; j++) {
                        long sum = 0;
                        for (int k = 0; k < A.data[i].length; k++) sum += A.data[i][k] * B.data[k][j];
                        C.data[i][j] = sum;
                    }
                    progress.increment();
                }
            });
            threads[t].start();
            startRow = endRow;
        }
        for (Thread thread : threads) thread.join();
    }

    public static void multiplyTriangularDynamic(Matrix A, Matrix B, Matrix C, int numThreads, Progress progress) throws InterruptedException {
        AtomicInteger nextRow = new AtomicInteger(0);
        Thread[] threads = new Thread[numThreads];
        for (int t = 0; t < numThreads; t++) {
            threads[t] = new Thread(() -> {
                while (true) {
                    int i = nextRow.getAndIncrement();
                    if (i >= A.rows) break;
                    for (int j = 0; j < B.cols; j++) {
                        long sum = 0;
                        for (int k = 0; k < A.data[i].length; k++) sum += A.data[i][k] * B.data[k][j];
                        C.data[i][j] = sum;
                    }
                    progress.increment();
                }
            });
            threads[t].start();
        }
        for (Thread thread : threads) thread.join();
    }
}