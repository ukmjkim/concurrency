public class App {
  public static void main(String...args) {
    System.out.println("Synchronized Objects: ");
    new Worker().main();

    System.out.println("Synchronized Methods: ");
    // new WorkerMethodsSynchronized().main();
  }
}
