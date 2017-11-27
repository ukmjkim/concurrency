public class Worker {
  // volatile cannot fix this problem because it is not cache problem
  // this variable must be atomic int
  private int count = 0;

  public synchronized void increment() {
    count++;
  }

  public static void main(String...args) {
    Worker worker = new Worker();
    worker.doWork();
  }

  public void doWork() {
    Thread t1 = new Thread(new Runnable() {
      public void run() {
        for (int i = 0; i < 1000; i++) {
          // Synchronization Problem
          // two threads do this at the same time
          // get variable and set variable
          // count = count + 1;
          // count++;
          increment();
        }
      }
    });

    Thread t2 = new Thread(new Runnable() {
      public void run() {
        for (int i = 0; i < 1000; i++) {
          // count++;
          increment();
        }
      }
    });

    t1.start();
    t2.start();

    /* need to wait until two threads finish their job */
    try {
      t1.join();
      t2.join();
    } catch (InterruptedException e) {
    }

    System.out.println("Count is: " + count);
  }
}

/*

Synchronization Problem  ==========================================
Mijungs-MBP-2:Char03_SynchronizeThread MijungKimMacPro$ java Worker
Count is: 1590
Mijungs-MBP-2:Char03_SynchronizeThread MijungKimMacPro$ java Worker
Count is: 1640
Mijungs-MBP-2:Char03_SynchronizeThread MijungKimMacPro$ java Worker
Count is: 1369
Mijungs-MBP-2:Char03_SynchronizeThread MijungKimMacPro$ java Worker
Count is: 1737
Mijungs-MBP-2:Char03_SynchronizeThread MijungKimMacPro$ java Worker
Count is: 1322

After fixing this problem =========================================
Mijungs-MBP-2:Char03_SynchronizeThread MijungKimMacPro$ java Worker
Count is: 2000

*/
