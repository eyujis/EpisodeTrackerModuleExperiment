public class TestResults {
    public static void main(String[] args) {
        String test = "[r1 - IN]\n" +
                "[r1 - IN, r1 - STAY]\n" +
                "[r1 - IN, r1 - STAY, r1 - OUT]\n" +
                "[r1 - IN, r1 - STAY, r1 - OUT, MOVE]\n" +
                "[r1 - IN, r1 - STAY, r1 - OUT, MOVE, r2 - IN]\n" +
                "[r1 - STAY, r1 - OUT, MOVE, r2 - IN, r2 - STAY]\n" +
                "[r1 - OUT, MOVE, r2 - IN, r2 - STAY, r2 - OUT]\n" +
                "[MOVE, r2 - IN, r2 - STAY, r2 - OUT, MOVE]\n" +
                "[r2 - IN, r2 - STAY, r2 - OUT, MOVE, r2 - IN]\n" +
                "[r2 - STAY, r2 - OUT, MOVE, r2 - IN, r2 - STAY]\n" +
                "[r2 - OUT, MOVE, r2 - IN, r2 - STAY, r2 - OUT]\n" +
                "[MOVE, r2 - IN, r2 - STAY, r2 - OUT, MOVE]\n" +
                "[r2 - IN, r2 - STAY, r2 - OUT, MOVE, r1 - IN]\n" +
                "[r2 - STAY, r2 - OUT, MOVE, r1 - IN, r1 - STAY]\n" +
                "[r2 - OUT, MOVE, r1 - IN, r1 - STAY, r1 - OUT]\n" +
                "[MOVE, r1 - IN, r1 - STAY, r1 - OUT, MOVE]\n" +
                "[r1 - IN, r1 - STAY, r1 - OUT, MOVE, r1 - IN]";

        String trueValue = "[r1 - IN]\n" +
                "[r1 - IN, r1 - STAY]\n" +
                "[r1 - IN, r1 - STAY, r1 - OUT]\n" +
                "[r1 - IN, r1 - STAY, r1 - OUT, MOVE]\n" +
                "[r1 - IN, r1 - STAY, r1 - OUT, MOVE, r2 - IN]\n" +
                "[r1 - STAY, r1 - OUT, MOVE, r2 - IN, r2 - STAY]\n" +
                "[r1 - OUT, MOVE, r2 - IN, r2 - STAY, r2 - OUT]\n" +
                "[MOVE, r2 - IN, r2 - STAY, r2 - OUT, MOVE]\n" +
                "[r2 - IN, r2 - STAY, r2 - OUT, MOVE, r2 - IN]\n" +
                "[r2 - STAY, r2 - OUT, MOVE, r2 - IN, r2 - STAY]\n" +
                "[r2 - OUT, MOVE, r2 - IN, r2 - STAY, r2 - OUT]\n" +
                "[MOVE, r2 - IN, r2 - STAY, r2 - OUT, MOVE]\n" +
                "[r2 - IN, r2 - STAY, r2 - OUT, MOVE, r1 - IN]\n" +
                "[r2 - STAY, r2 - OUT, MOVE, r1 - IN, r1 - STAY]\n" +
                "[r2 - OUT, MOVE, r1 - IN, r1 - STAY, r1 - OUT]\n" +
                "[MOVE, r1 - IN, r1 - STAY, r1 - OUT, MOVE]\n" +
                "[r1 - IN, r1 - STAY, r1 - OUT, MOVE, r1 - IN]";

        System.out.println(trueValue.equals(test));
    }
}
