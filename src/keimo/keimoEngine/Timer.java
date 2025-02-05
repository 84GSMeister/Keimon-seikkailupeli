package keimo.keimoEngine;

public class Timer {
    public static double getTime() {
        return (double) System.nanoTime() / (double) 1_000_000_000;
    }
}
