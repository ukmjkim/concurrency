import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Processor implements Runnable {
  private int id;

  public Processor(int id) {
    this.id = id;
  }

  public void run() {
    System.out.println("Starting: " + id);
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
    }
    System.out.println("Completed: " + id);
  }
}

public class App {
  public static void main(String[] args) {
    long start = System.currentTimeMillis();
    ExecutorService executor = Executors.newFixedThreadPool(2);

    for (int i = 0; i < 5; i++) {
      executor.submit(new Processor(i));
    }

    executor.shutdown();

    System.out.println("All tasks submitted.");

    try {
    executor.awaitTermination(20, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
    }

    System.out.println("All tasks completed.");

    long end = System.currentTimeMillis();
    System.out.println("Running Time: " + (end - start));
  }
}
