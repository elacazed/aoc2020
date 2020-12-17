package fr.ela.aoc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.BinaryOperator;

public class D5 {

    public static void main(String[] args) {
        try {
            Path file = Paths.get("target/classes/input_5.txt");
            List<String> lines = Files.readAllLines(file);
            MyOperator myOperator = new MyOperator();
            int seat = lines.stream().map(D5::getSeatId).sorted().reduce(myOperator).orElse(0) +1;
            System.out.println("Max seat ID : " + myOperator.max);
            System.out.println("My seat ID : " + seat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class MyOperator implements BinaryOperator<Integer> {
        int max = 0;

        @Override
        public Integer apply(Integer integer, Integer integer2) {
            max = Math.max(max, Math.max(integer, integer2));
            return integer2 == integer + 1 ? integer2 : integer;
        }
    }

    public static int getSeatId(String value) {
        String val = value.substring(0, 10);
        int row = getValue(val.substring(0, 7), 'F');
        int col = getValue(val.substring(7, 10), 'L');
        return row * 8 + col;
    }

    private static int getValue(String value, char low) {
        int min = 0;
        int val = 0;
        int max = (int) Math.pow(2, value.length()) - 1;

        for (int i = 0; i < value.length(); i++) {
            if (low == value.charAt(i)) {
                max = max - (max - min) / 2 - 1;
                val = min;
            } else {
                min = min + (max - min) / 2 + 1;
                val = max;
            }
        }
        return val;
    }

}
