public class Main {
    public static void main(String[] args) throws Exception {
        int n = 500, m = 500, k = 500, threads = 4;
        String mode = "static";
        boolean triangular = false;
        String filename = null;

        if (args.length >= 4) {
            n = Integer.parseInt(args[0]); m = Integer.parseInt(args[1]); k = Integer.parseInt(args[2]);
            threads = Integer.parseInt(args[3]);
            if (args.length >= 5) mode = args[4];
            if (args.length >= 6) triangular = Boolean.parseBoolean(args[5]);
            if (args.length >= 7) filename = args[6];
        }

        Matrix A, B, C;
        if (filename != null) {
            Matrix[] loaded = GeneratorProcess.load(filename);
            A = loaded[0]; B = loaded[1];
            C = new Matrix(n, k);
        } else {
            java.util.Random rnd = new java.util.Random(42);
            if (triangular) { A = Matrix.randomTriangular(n, rnd); B = Matrix.random(n, k, rnd); }
            else { A = Matrix.random(n, m, rnd); B = Matrix.random(m, k, rnd); }
            C = new Matrix(n, k);
        }

        Progress progress = new Progress(n);
        Thread progressThread = new Thread(progress::printLoop);
        progressThread.start();

        long start = System.nanoTime();
        if (triangular) {
            if ("dynamic".equals(mode)) Multiplier.multiplyTriangularDynamic(A, B, C, threads, progress);
            else Multiplier.multiplyTriangularStatic(A, B, C, threads, progress);
        } else {
            if ("dynamic".equals(mode)) Multiplier.multiplyDynamic(A, B, C, threads, progress);
            else Multiplier.multiplyStatic(A, B, C, threads, progress);
        }
        long elapsed = System.nanoTime() - start;
        progressThread.join();

        System.out.println("Время умножения: " + String.format("%.3f", elapsed / 1_000_000_000.0).replace(",", ".") + " с");
        System.out.println("Контрольная сумма: " + C.checksum());
    }
}