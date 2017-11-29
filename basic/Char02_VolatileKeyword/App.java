import java.util.Scanner;


/* How you can terminate the processes */
/* There are two threads running, main thread and new thread. */
/* Some system use cach memory for optimization, while (running) is not always checked in loop */
/* To prevent thread caching variable when they are not changed in the thread */
/* Thread Synchronization will be another way */
class Processor extends Thread {
  private volatile boolean running = true;

  public void run() {
    while (running) {
      System.out.println("Hello");
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
      }
    }
  }

  public void shutdown() {
    running = false;
  }
}

public class App {
  public static void main(String... args) {
    Processor proc1 = new Processor();
    proc1.start();

    System.out.println("Press return to stop...");
    Scanner scanner = new Scanner(System.in);
    scanner.nextLine();

    proc1.shutdown();
  }
}
