import java.io.*;

public class GeneratorProcess {
    public static void generate(int n, int m, int k, boolean triangular, String filename) throws Exception {
        // Берем текущий classpath проекта (папку out/production/Laba2_OS), чтобы новый процесс java нашел GeneratorWorker
        String classpath = System.getProperty("java.class.path");

        ProcessBuilder pb = new ProcessBuilder(
                "java", "-cp", classpath, "GeneratorWorker",
                String.valueOf(n), String.valueOf(m), String.valueOf(k), filename, String.valueOf(triangular)
        );
        pb.redirectErrorStream(true);
        Process p = pb.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[Генератор] " + line);
            }
        }

        int exitCode = p.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Генератор упал с кодом: " + exitCode);
        }
    }

    public static Matrix[] load(String filename) throws Exception {
        try (DataInputStream in = new DataInputStream(new FileInputStream(filename))) {
            boolean triangular = in.readBoolean();
            int aRows = in.readInt(); int aCols = in.readInt();
            Matrix A = triangular ? new Matrix(aRows) : new Matrix(aRows, aCols);
            for (int i = 0; i < aRows; i++)
                for (int j = 0; j < A.data[i].length; j++) A.data[i][j] = in.readLong();

            int bRows = in.readInt(); int bCols = in.readInt();
            Matrix B = new Matrix(bRows, bCols);
            for (int i = 0; i < bRows; i++)
                for (int j = 0; j < bCols; j++) B.data[i][j] = in.readLong();
            return new Matrix[]{A, B};
        }
    }
}