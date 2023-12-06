package tests;

import org.apache.commons.codec.binary.StringUtils;

public class TestResults {
    public static void main(String[] args) {
        String test = "[r0 - IN]\n" +
                "[r0 - IN, r0 - STAY]\n" +
                "[r0 - IN, r0 - STAY, r4 - IN]\n" +
                "[r0 - IN, r0 - STAY, r4 - IN, r0 - STAY]\n" +
                "[r0 - IN, r0 - STAY, r4 - IN, r0 - STAY, r4 - OUT]\n" +
                "[r0 - STAY, r4 - IN, r0 - STAY, r4 - OUT, r0 - OUT]\n" +
                "[r4 - IN, r0 - STAY, r4 - OUT, r0 - OUT, MOVE]\n" +
                "[r0 - STAY, r4 - OUT, r0 - OUT, MOVE, r2 - IN]\n" +
                "[r4 - OUT, r0 - OUT, MOVE, r2 - IN, r2 - STAY]\n" +
                "[r0 - OUT, MOVE, r2 - IN, r2 - STAY, r2 - OUT]\n" +
                "[MOVE, r2 - IN, r2 - STAY, r2 - OUT, MOVE]\n" +
                "[r2 - IN, r2 - STAY, r2 - OUT, MOVE, r0 - IN]\n" +
                "[r2 - STAY, r2 - OUT, MOVE, r0 - IN, r0 - STAY]\n" +
                "[r2 - OUT, MOVE, r0 - IN, r0 - STAY, r4 - IN]\n" +
                "[MOVE, r0 - IN, r0 - STAY, r4 - IN, r0 - OUT]\n" +
                "[r0 - IN, r0 - STAY, r4 - IN, r0 - OUT, r4 - STAY]\n" +
                "[r0 - STAY, r4 - IN, r0 - OUT, r4 - STAY, r1 - IN]\n" +
                "[r4 - IN, r0 - OUT, r4 - STAY, r1 - IN, r4 - STAY]\n" +
                "[r0 - OUT, r4 - STAY, r1 - IN, r4 - STAY, r4 - OUT]\n" +
                "[r4 - STAY, r1 - IN, r4 - STAY, r4 - OUT, r1 - STAY]\n" +
                "[r1 - IN, r4 - STAY, r4 - OUT, r1 - STAY, r4 - IN]\n" +
                "[r4 - STAY, r4 - OUT, r1 - STAY, r4 - IN, r1 - STAY]\n" +
                "[r4 - OUT, r1 - STAY, r4 - IN, r1 - STAY, r4 - OUT]\n" +
                "[r1 - STAY, r4 - IN, r1 - STAY, r4 - OUT, r1 - STAY]\n" +
                "[r4 - IN, r1 - STAY, r4 - OUT, r1 - STAY, r4 - IN]\n" +
                "[r1 - STAY, r4 - OUT, r1 - STAY, r4 - IN, r1 - STAY]\n" +
                "[r4 - OUT, r1 - STAY, r4 - IN, r1 - STAY, r4 - OUT]\n" +
                "[r1 - STAY, r4 - IN, r1 - STAY, r4 - OUT, r1 - STAY]\n" +
                "[r4 - IN, r1 - STAY, r4 - OUT, r1 - STAY, r4 - IN]\n" +
                "[r1 - STAY, r4 - OUT, r1 - STAY, r4 - IN, r1 - STAY]\n" +
                "[r4 - OUT, r1 - STAY, r4 - IN, r1 - STAY, r1 - OUT]\n" +
                "[r1 - STAY, r4 - IN, r1 - STAY, r1 - OUT, r4 - STAY]\n" +
                "[r4 - IN, r1 - STAY, r1 - OUT, r4 - STAY, r0 - IN]\n" +
                "[r1 - STAY, r1 - OUT, r4 - STAY, r0 - IN, r4 - STAY]\n" +
                "[r1 - OUT, r4 - STAY, r0 - IN, r4 - STAY, r4 - OUT]";

        String trueValue = "[r0 - IN]\n" +
                "[r0 - IN, r0 - STAY]\n" +
                "[r0 - IN, r0 - STAY, r4 - IN]\n" +
                "[r0 - IN, r0 - STAY, r4 - IN, r0 - STAY]\n" +
                "[r0 - IN, r0 - STAY, r4 - IN, r0 - STAY, r4 - OUT]\n" +
                "[r0 - STAY, r4 - IN, r0 - STAY, r4 - OUT, r0 - OUT]\n" +
                "[r4 - IN, r0 - STAY, r4 - OUT, r0 - OUT, MOVE]\n" +
                "[r0 - STAY, r4 - OUT, r0 - OUT, MOVE, r2 - IN]\n" +
                "[r4 - OUT, r0 - OUT, MOVE, r2 - IN, r2 - STAY]\n" +
                "[r0 - OUT, MOVE, r2 - IN, r2 - STAY, r2 - OUT]\n" +
                "[MOVE, r2 - IN, r2 - STAY, r2 - OUT, MOVE]\n" +
                "[r2 - IN, r2 - STAY, r2 - OUT, MOVE, r0 - IN]\n" +
                "[r2 - STAY, r2 - OUT, MOVE, r0 - IN, r0 - STAY]\n" +
                "[r2 - OUT, MOVE, r0 - IN, r0 - STAY, r4 - IN]\n" +
                "[MOVE, r0 - IN, r0 - STAY, r4 - IN, r0 - STAY]\n" +
                "[r0 - IN, r0 - STAY, r4 - IN, r0 - STAY, r0 - OUT]\n" +
                "[r0 - STAY, r4 - IN, r0 - STAY, r0 - OUT, r4 - STAY]\n" +
                "[r4 - IN, r0 - STAY, r0 - OUT, r4 - STAY, r1 - IN]\n" +
                "[r0 - STAY, r0 - OUT, r4 - STAY, r1 - IN, r4 - STAY]\n" +
                "[r0 - OUT, r4 - STAY, r1 - IN, r4 - STAY, r4 - OUT]\n" +
                "[r4 - STAY, r1 - IN, r4 - STAY, r4 - OUT, r1 - STAY]\n" +
                "[r1 - IN, r4 - STAY, r4 - OUT, r1 - STAY, r4 - IN]\n" +
                "[r4 - STAY, r4 - OUT, r1 - STAY, r4 - IN, r1 - STAY]\n" +
                "[r4 - OUT, r1 - STAY, r4 - IN, r1 - STAY, r4 - OUT]\n" +
                "[r1 - STAY, r4 - IN, r1 - STAY, r4 - OUT, r1 - STAY]\n" +
                "[r4 - IN, r1 - STAY, r4 - OUT, r1 - STAY, r4 - IN]\n" +
                "[r1 - STAY, r4 - OUT, r1 - STAY, r4 - IN, r1 - STAY]\n" +
                "[r4 - OUT, r1 - STAY, r4 - IN, r1 - STAY, r4 - OUT]\n" +
                "[r1 - STAY, r4 - IN, r1 - STAY, r4 - OUT, r1 - STAY]\n" +
                "[r4 - IN, r1 - STAY, r4 - OUT, r1 - STAY, r4 - IN]\n" +
                "[r1 - STAY, r4 - OUT, r1 - STAY, r4 - IN, r1 - STAY]\n" +
                "[r4 - OUT, r1 - STAY, r4 - IN, r1 - STAY, r1 - OUT]\n" +
                "[r1 - STAY, r4 - IN, r1 - STAY, r1 - OUT, r4 - STAY]\n" +
                "[r4 - IN, r1 - STAY, r1 - OUT, r4 - STAY, r0 - IN]\n" +
                "[r1 - STAY, r1 - OUT, r4 - STAY, r0 - IN, r0 - STAY]\n" +
                "[r1 - OUT, r4 - STAY, r0 - IN, r0 - STAY, r4 - OUT]";

        System.out.println(trueValue.equals(test));
    }
}
