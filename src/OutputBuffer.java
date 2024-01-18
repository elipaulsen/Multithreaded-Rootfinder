public class OutputBuffer {
  private final String[] buffer = new String[30]; // shared buffer
  private int occupiedCells = 0; // count number of buffers used
  private int writeIndex = 0; // index of next element to write to
  private int readIndex = 0; // index of next element to read

  // place value into buffer
  public synchronized void blockingPut(String results)
      throws InterruptedException {

    // wait until buffer has space available, then write value;
    // while no empty locations, place thread in blocked state
    while (occupiedCells == buffer.length) {
      wait(); // wait until a buffer cell is free
    }

    buffer[writeIndex] = results; // set new buffer value

    // update circular write index
    writeIndex = (writeIndex + 1) % buffer.length;

    ++occupiedCells; // one more buffer cell is full
    notifyAll(); // notify threads waiting to read from buffer
  }

  // return value from buffer
  public synchronized String blockingGet() throws InterruptedException {
    // wait until buffer has data, then read value;
    // while no data to read, place thread in waiting state
    while (occupiedCells == 0) {
      wait(); // wait until a buffer cell is filled
    }

    String readValue = buffer[readIndex]; // read value from buffer

    // update circular read index
    readIndex = (readIndex + 1) % buffer.length;

    --occupiedCells; // one fewer buffer cells are occupied
    notifyAll(); // notify threads waiting to write to buffer

    return readValue;
  }

  public String[] getBuffer() {
    return buffer;
  }

  public boolean notEmpty() {
    return occupiedCells != 0;
  }
}



