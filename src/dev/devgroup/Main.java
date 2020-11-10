package dev.devgroup;

public class Main {

    public static void main(String[] args) {
        ForwardStatsError forwardStatsError = new ForwardStatsError();

        forwardStatsError.statsCalc();

        System.out.println("Press any key + Enter to close!");
        new java.util.Scanner(System.in).next();
    }

}
