import java.util.Random;

public class Matrix {
    public final int rows;
    public final int cols;
    public final long[][] data;
    public final boolean triangular;

    public Matrix(int rows, int cols) {
        this.rows = rows; this.cols = cols;
        this.data = new long[rows][cols];
        this.triangular = false;
    }

    public Matrix(int size) {
        this.rows = size; this.cols = size;
        this.data = new long[size][];
        for (int i = 0; i < size; i++) this.data[i] = new long[i + 1];
        this.triangular = true;
    }

    public static Matrix random(int rows, int cols, Random rnd) {
        Matrix m = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++) m.data[i][j] = rnd.nextInt(10);
        return m;
    }

    public static Matrix randomTriangular(int size, Random rnd) {
        Matrix m = new Matrix(size);
        for (int i = 0; i < size; i++)
            for (int j = 0; j <= i; j++) m.data[i][j] = rnd.nextInt(10);
        return m;
    }

    public long checksum() {
        long sum = 0;
        for (long[] row : data) for (long v : row) sum += v;
        return sum;
    }
}