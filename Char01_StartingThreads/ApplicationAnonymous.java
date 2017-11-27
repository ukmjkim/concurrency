public class ApplicationAnonymous {
  public static void main(String... args) {
    Thread t = new Thread(new Runnable() {
      @Override
      public void run() {
        for (int i = 0; i < 5; i++) {
          System.out.println("Hello: " + i + " Thread: " + Thread.currentThread().getName());
          try {
            Thread.sleep(100);
          } catch (InterruptedException ignored) {
          }
        }
      }
    }, "myThread");

    t.start();
  }
}

