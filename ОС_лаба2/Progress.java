import java.util.concurrent.atomic.AtomicInteger;

public class Progress {
    private final AtomicInteger completedRows = new AtomicInteger(0);
    private final int totalRows;

    public Progress(int totalRows) { this.totalRows = totalRows; }

    public void increment() { completedRows.incrementAndGet(); }

    public void printLoop() {
        int lastPercent = -1;
        while (completedRows.get() < totalRows) {
            int percent = (int) ((completedRows.get() * 100.0) / totalRows);
            if (percent != lastPercent) {
                System.out.printf("Прогресс: %d%%\n", percent);
                lastPercent = percent;
            }
            try { Thread.sleep(200); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); break; }
        }
        System.out.println("Прогресс: 100%");
    }
}