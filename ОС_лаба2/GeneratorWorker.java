import java.io.*;
import java.util.Random;

public class GeneratorWorker {
    public static void main(String[] args) throws Exception {
        int n = Integer.parseInt(args[0]);
        int m = Integer.parseInt(args[1]);
        int k = Integer.parseInt(args[2]);
        String filename = args[3];
        boolean triangular = Boolean.parseBoolean(args[4]);

        Random rnd = new Random(42);
        Matrix A = triangular ? Matrix.randomTriangular(n, rnd) : Matrix.random(n, m, rnd);
        Matrix B = Matrix.random(m, k, rnd);

        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(filename))) {
            out.writeBoolean(triangular);
            out.writeInt(A.rows); out.writeInt(A.cols);
            for (long[] row : A.data) for (long v : row) out.writeLong(v);
            out.writeInt(B.rows); out.writeInt(B.cols);
            for (long[] row : B.data) for (long v : row) out.writeLong(v);
        }
    }
}