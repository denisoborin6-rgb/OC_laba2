public class Benchmark {
    public static void main(String[] args) throws Exception {
        int[] sizes = {500, 1000, 1500};
        int[] threadsList = {1, 2, 4, 8, 16};
        int repeats = 3;

        System.out.println("Размер;Треугольная;Режим;Потоки;Время_с;Ускорение");

        for (int size : sizes) {
            System.out.println("--- Размер " + size + " ---");
            for (int tri = 0; tri < 2; tri++) {
                boolean isTriangular = (tri == 1);
                String fname = "mat_" + size + "_" + isTriangular + ".bin";

                // ДЕМОНСТРАЦИЯ IPC: запускаем отдельный процесс-генератор
                GeneratorProcess.generate(size, size, size, isTriangular, fname);
                // Загружаем данные из файла
                Matrix[] loaded = GeneratorProcess.load(fname);
                Matrix A = loaded[0], B = loaded[1], C = new Matrix(size, size);

                for (int modeIdx = 0; modeIdx < 2; modeIdx++) {
                    String mode = (modeIdx == 0) ? "static" : "dynamic";
                    double baseTime = 0;

                    for (int t : threadsList) {
                        double[] times = new double[repeats];
                        for (int r = 0; r < repeats; r++) {
                            for (int i = 0; i < size; i++) java.util.Arrays.fill(C.data[i], 0);
                            Progress progress = new Progress(size);
                            long start = System.nanoTime();
                            if (isTriangular) {
                                if ("static".equals(mode)) Multiplier.multiplyTriangularStatic(A, B, C, t, progress);
                                else Multiplier.multiplyTriangularDynamic(A, B, C, t, progress);
                            } else {
                                if ("static".equals(mode)) Multiplier.multiplyStatic(A, B, C, t, progress);
                                else Multiplier.multiplyDynamic(A, B, C, t, progress);
                            }
                            long elapsed = System.nanoTime() - start;
                            times[r] = elapsed / 1_000_000_000.0;
                        }

                        java.util.Arrays.sort(times);
                        double sum = 0;
                        for (int r = 1; r < repeats - 1; r++) sum += times[r];
                        double avg = sum / (repeats - 2);

                        if (t == 1) baseTime = avg;
                        double speedup = baseTime / avg;

                        System.out.printf("%d;%b;%s;%d;%.3f;%.2f\n", size, isTriangular, mode, t, avg, speedup);
                    }
                }
            }
        }
        System.out.println("--- Замеры завершены ---");
    }
}