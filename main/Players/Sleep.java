package Players;


/**
 * This class is used to make the thread sleep for a specified amount of time.
 */
public class Sleep extends Thread {

  /**
   * This constructor is used to make the thread sleep for a specified amount of time.
   * @param timeoutMs The amount of time to sleep for in milliseconds.
   */
  public Sleep(int timeoutMs) {
    try {
      Thread.sleep(timeoutMs);
    } catch (InterruptedException e) {
      System.out.println(e);
    }
  }
}