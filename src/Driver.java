import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Driver {

  public static void main(String[] args) throws InterruptedException {
    ExecutorService executorService = Executors.newCachedThreadPool();

    InputBuffer polynomials = new InputBuffer();
    OutputBuffer roots = new OutputBuffer();

    Random r = new Random();

    for(int i=0; i<10; i++) {
      executorService.execute(new Rootfinder(polynomials, roots));
    }

    System.out.println("***MultiThreaded Rootfinder***");
    System.out.println("1. Solve 30 random polynomials");
    System.out.println("2. View thread statistics for 3000 random solved polynomials");

    Scanner in = new Scanner(System.in);
    int choice = in.nextInt();
    while (choice != 1 && choice != 2){
      System.out.println("1. Solve 30 random polynomials");
      System.out.println("2. View thread statistics for 3000 random solved polynomials");
      choice = in.nextInt();
    }
    if(choice == 1) {
      for(int i=0; i < 30; i++) {
        try {
          polynomials.blockingPut(new double[]{r.nextInt(2001)-1000, r.nextInt(2001)-1000, r.nextInt(2001)-1000});
          System.out.println((i+1)+". "+roots.blockingGet());
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }
      Rootfinder.setFinished(true);
    }
    else {
      long begin = System.currentTimeMillis();
      for(int i=0; i < 3000; i++) {
        try {
          polynomials.blockingPut(new double[]{r.nextInt(2001)-1000, r.nextInt(2001)-1000, r.nextInt(2001)-1000});
          roots.blockingGet();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }
      Rootfinder.setFinished(true);
      System.out.println("time: " + (System.currentTimeMillis() - begin));
      Map<String, Integer> m = Rootfinder.getNumSolved();
      System.out.println("# of solutions per thread");
      for(String k: m.keySet()){
        System.out.printf("%s solved %d polynomials\n",k,m.get(k));
      }
    }

    executorService.shutdown();
    if(!executorService.awaitTermination(1, TimeUnit.SECONDS)){
      executorService.shutdownNow();
    }
  }



}
