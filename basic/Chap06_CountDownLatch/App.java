class Processor implements Runnable {
  private CountDownLatch latch;

  public Processor(CountDownLatch latch) {
    this.latch = latch;
  }

  public void run() {
    System.out.println("Started");
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
    }

    latch.countDown();
  }
}

public class App {
  public static void main(String... args) {
    CountDownLatch latch = new CountDownLatch(100);

    ExecutorService executor = Executors.newFixedThreadPool(3);
    for (int i = 0; i < 3; i++) {
      executor.submit(new Processor(latch));
    }
    executor.shutdown();

    try {
      latch.await();
    } catch (InterruptedException e) {
    }

    System.out.println("Completed.");
  }
}
