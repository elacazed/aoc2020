package fr.ela.aoc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class D15 {


    public static void main(String[] args) {
        try {
            partOneTest(2020, 436,0, 3, 6);
            partOneTest(2020, 1,1, 3, 2);
            partOneTest(2020, 10,2, 1,3);
            partOneTest(2020, 27, 1,2,3);
            partOneTest(2020, 78, 2,3,1);
            partOneTest(2020, 438, 3,2,1);
            partOneTest(2020, 1836, 3,1,2);
            Long time = System.currentTimeMillis();
            System.out.println("Part one : " + play(2020, 8,13,1,0,18,9) + " (" + (System.currentTimeMillis() - time) + ")");

            time = System.currentTimeMillis();
            System.out.println("Part two : " + play(30000000, 8,13,1,0,18,9) + " (" + (System.currentTimeMillis() - time) + ")");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void partOneTest(int target, int expected, int ... start) {
        int real = play(target, start);

        System.out.println("PartOne Test : "+Arrays.toString(start)+" "+real+(real == expected ? " OK" : " KO : expected "+expected));
    }

    public static int play(int target, int... start) {
        Map<Integer, LastTurns> numbers = new HashMap<>();
        int spoken = -1;
        int round = 0;

        for (; round < start.length; round++) {
            spoken = start[round];
            numbers.computeIfAbsent(spoken, s -> new LastTurns()).play(round);
        }
        while (round < target) {
            spoken = numbers.computeIfAbsent(spoken, s -> new LastTurns()).diff();
            numbers.computeIfAbsent(spoken, s -> new LastTurns()).play(round);
            round++;
        }
        return spoken;
    }

    private static class LastTurns {
        int lastTurn = -1;
        int ante = -1;

        public void play(int turn) {
            ante = lastTurn;
            lastTurn = turn;
        }

        public int diff() {
            return ante == -1 ? 0 : lastTurn - ante;
        }
    }


}
