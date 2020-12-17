package fr.ela.aoc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class D9 {


    public static void main(String[] args) {

        try {
            Path file = Paths.get("target/classes/D9-test.txt");
            List<Long> lines = Files.readAllLines(file).stream().map(Long::parseLong).collect(Collectors.toList());

            long testNumber = partOne(5, lines);
            partTwo(lines, testNumber);
            file = Paths.get("target/classes/D9.txt");
            lines = Files.readAllLines(file).stream().map(Long::parseLong).collect(Collectors.toList());
            long number = partOne(25, lines);
            partTwo(lines, number);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static long partOne(int size, List<Long> lines) {
        long solution = getInvalidNumber(size, lines);
        System.out.println("part one : "+(solution == -1 ? "No Solution :'(": solution));
        return solution;
    }


    public static long getInvalidNumber(int size, List<Long> lines) {
        long solution = -1;
        for (int i = size; i < lines.size(); i++) {
            List<Long> window = lines.subList(i - size, i);
            solution = lines.get(i);
            if (! isValid(solution, window)) {
                break;
            }
        }
        return solution;
    }

    private static boolean isValid(final long number, List<Long> numbers) {
        for (long n : numbers) {
            if (numbers.stream().filter(x -> x != n).anyMatch(x -> x+n == number)) {
                return true;
            }
        }
        return false;
    }

    private static void partTwo(List<Long> lines, long number) {
        Range r = findRange(number, lines);
        System.out.println("Part two : "+r);
    }

    private static Range findRange(final long number, List<Long> numbers) {

        for (int i = 0; i <  numbers.size(); i++) {
            long value = numbers.get(i);
            if (value < number) {
                Range r = Range.start(i, value);
                int j = i + 1;
                while (r.sum < number) {
                    r.add(j, numbers.get(j));
                    j++;
                }
                if (r.sum == number) {
                    return r;
                }
            }
        }
        return null;
    }

    private static class Range {
        int startIndex;
        int endIndex;

        long min;
        long max;
        long sum = 0;

        static Range start(int startIndex, long value) {
            Range r = new Range();
            r.startIndex = startIndex;
            r.sum += value;
            r.min = value;
            r.max = value;
            return r;
        }

        void add(int index, long value) {
            sum += value;
            endIndex = index;
            min = Math.min(min, value);
            max = Math.max(max, value);
        }

        public long getWeakness() {
            return min + max;
        }

        public String toString() {
            return "Range : "+startIndex+"-"+endIndex+" : "+getWeakness();
        }
    }

}
