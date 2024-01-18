import java.util.HashMap;
import java.util.Map;

public class Rootfinder implements Runnable {
  private final InputBuffer polynomials;
  private final OutputBuffer roots;
  private static final Map<String, Integer> numSolved = new HashMap<>();
  private static boolean finished;


  public Rootfinder(InputBuffer polynomials, OutputBuffer roots) {
    this.polynomials = polynomials;
    this.roots = roots;
  }

  @Override
  public void run() {
    while (!finished || !polynomials.isEmpty()) {
      try {
        double[] p = polynomials.blockingGet();
        double a = p[0];
        double b = p[1];
        double c = p[2];
        double det = Math.pow(b, 2) - 4 * a * c;
        String rs = String.format("%.0fx^2 + %.0fx +%.0f ---> Roots: ",a,b,c);
        if (det > 0) {
          double[] sol = new double[]{(-b + Math.sqrt(det)) / (2 * a), (-b - Math.sqrt(det)) / (2 * a)};
          // two real and distinct roots
          rs += String.format("%.3f, %.3f", sol[0], sol[1]);
        }

        // check if determinant is equal to 0
        else if (det == 0) {
          // two real and equal roots
          // determinant is equal to 0
          // so -b + 0 == -b
          double sol = -b / (2 * a);
          rs += String.format("%.3f", sol);
        } else {
          double real = -b / (2 * a);
          double imaginary = Math.sqrt(-det) / (2 * a);
          rs += String.format("%.3f + %.3fi, %.3f - %.3fi", real, imaginary, real, imaginary);
        }
        String currThread = Thread.currentThread().getName();
        roots.blockingPut(rs+'\n');

        if (numSolved.containsKey(currThread)) {
          numSolved.put(currThread, numSolved.get(currThread)+1);
        }
        else {
          numSolved.put(Thread.currentThread().getName(), 1);
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }

    }
  }
  public static void setFinished(boolean bool) {
    finished = bool;
  }

  public static Map<String, Integer> getNumSolved() {
    return numSolved;
  }




}
