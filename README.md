# Multithreaded Rootfinder
a multithreaded program capable of interacting with thread safe data structures to compute a large amount of polynomials.
#

### Developer Documentation
This program is using the java ExecutorService in order to create threads. The executor service executes runnables which in this program are 10 Rootfinders, and 1 PolynomialGenerator. This lets the input buffer be added to and taken from at the same time.

Polynomial Generator adds random polynomials to the input buffer.

Rootfinder takes from the input buffer, solves the polynomial, and adds the resulting roots to the output buffer in a string.

Both the input and output buffer were adapted from from textbook example CH23-FIG23_18-19 circular buffer and are thread safe. 

![UML](../umls/rootfinder.png)


### User Documentation
Run the driver class to start the program. This will create 10 workers, and then give you the option to either solve 30 random polynomials or solve 3000 random polynomials and receive some data about each thread.