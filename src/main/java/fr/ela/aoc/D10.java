package fr.ela.aoc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class D10 {
    /*
        attery is dead.

                You'll need to plug it in. There's only one problem: the charging outlet near your seat produces the wrong number of jolts. Always prepared, you make a list of all of the joltage adapters in your bag.

        Each of your joltage adapters is rated for a specific output joltage (your puzzle input). Any given adapter can take an input 1, 2, or 3 jolts lower than its rating and still produce its rated output joltage.

        In addition, your device has a built-in joltage adapter rated for 3 jolts higher than the highest-rated adapter in your bag. (If your adapter list were 3, 9, and 6, your device's built-in adapter would be rated for 12 jolts.)

        Treat the charging outlet near your seat as having an effective joltage rating of 0.

        Since you have some time to kill, you might as well test all of your adapters. Wouldn't want to get to your resort and realize you can't even charge your device!

        If you use every adapter in your bag at once, what is the distribution of joltage differences between the charging outlet, the adapters, and your device?

        For example, suppose that in your bag, you have adapters with the following joltage ratings:

                16
                10
                15
                5
                1
                11
                7
                19
                6
                12
                4

        With these adapters, your device's built-in joltage adapter would be rated for 19 + 3 = 22 jolts, 3 higher than the highest-rated adapter.

        Because adapters can only connect to a source 1-3 jolts lower than its rating, in order to use every adapter, you'd need to choose them like this:

        The charging outlet has an effective rating of 0 jolts, so the only adapters that could connect to it directly would need to have a joltage rating of 1, 2, or 3 jolts. Of these, only one you have is an adapter rated 1 jolt (difference of 1).
        From your 1-jolt rated adapter, the only choice is your 4-jolt rated adapter (difference of 3).
        From the 4-jolt rated adapter, the adapters rated 5, 6, or 7 are valid choices. However, in order to not skip any adapters, you have to pick the adapter rated 5 jolts (difference of 1).
        Similarly, the next choices would need to be the adapter rated 6 and then the adapter rated 7 (with difference of 1 and 1).
        The only adapter that works with the 7-jolt rated adapter is the one rated 10 jolts (difference of 3).
        From 10, the choices are 11 or 12; choose 11 (difference of 1) and then 12 (difference of 1).
        After 12, only valid adapter has a rating of 15 (difference of 3), then 16 (difference of 1), then 19 (difference of 3).
        Finally, your device's built-in adapter is always 3 higher than the highest adapter, so its rating is 22 jolts (always a difference of 3).

        In this example, when using every adapter, there are 7 differences of 1 jolt and 5 differences of 3 jolts.

     */
    public static void main(String[] args) {
        try {
            Path file = Paths.get("target/classes/D10-test.txt");
            List<Long> lines = Files.readAllLines(file).stream().map(Long::parseLong).collect(Collectors.toList());

            Sequence seq = partOne(lines);
            partTwo(seq);
            lines.sort(Comparator.naturalOrder());

            file = Paths.get("target/classes/D10-test2.txt");
            lines = Files.readAllLines(file).stream().map(Long::parseLong).collect(Collectors.toList());

            lines.sort(Comparator.naturalOrder());
            System.out.println("ordered  : "+toString(lines));
            seq = partOne(lines);
            partTwo(seq);

            file = Paths.get("target/classes/D10.txt");
            lines = Files.readAllLines(file).stream().map(Long::parseLong).collect(Collectors.toList());

            lines.sort(Comparator.naturalOrder());

            System.out.println("ordered  : "+toString(lines));
            seq = partOne(lines);
            partTwo(seq);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Sequence partOne(List<Long> lines) {
        long start = 0;
        Sequence sequence = getSequence(start, lines);
        System.out.println(sequence);
        return sequence;
    }

    private static void partTwo(Sequence sequence) {
        System.out.println("Résultat : "+sequence.countPermutableSequences());
    }

    public static boolean adapted(long input, long joltage) {
        return joltage > input && joltage - input < 4;
    }

    public static List<Long> getCandidates(long value, List<Long> adapters) {
        return adapters.stream().filter(j -> adapted(value, j)).sorted().collect(Collectors.toList());
    }

    public static Sequence getSequence(long value, List<Long> adapters) {
        Sequence sequence = new Sequence(value);
        long next = value;
        while (!adapters.isEmpty()) {
            List<Long> candidates = getCandidates(next, adapters);
            if (candidates.size() == 1) {
                next = candidates.get(0);
                sequence.add(next);
                adapters.remove(next);
            } else {
                if (candidates.isEmpty()) {
                    return null;
                }
                for (long val : candidates) {
                    adapters.remove(val);
                    Sequence suite = getSequence(val, adapters);
                    if (suite == null) {
                        adapters.add(val);
                    } else {
                        sequence.add(val);
                        sequence.concat(suite);
                        return sequence;
                    }
                }
            }
        }
        return sequence;
    }

    public static class Sequence {

        private final LinkedList<Long> list = new LinkedList<>();
        private final Map<Integer, Integer> gaps = new HashMap<>();

        final long start;

        long result = 0;

        public Sequence(long start) {
            this.start = start;
        }

        public long getOutput() {
            Long last = list.getLast();
            result = countGap(1) * (countGap(3) + 1);
            return last + 3;
        }

        private long countGap(int i) {
            return gaps.getOrDefault(i, 0);
        }

        public boolean add(Long value) {
            long last = list.isEmpty() ? start : list.getLast();
            int gap = (int) (value - last);
            gaps.compute(gap, (k, v) -> (v == null) ? 1 : v + 1);

            return list.add(value);
        }

        public void concat(Sequence sequence) {
            sequence.forEach(this::add);
        }

        private void forEach(Consumer<Long> action) {
            list.forEach(action);
        }

        public String toString() {
            return "Sequence : " + D10.toString(list) + "\n  Output : " + getOutput() + ", Result : " + result;
        }

        public long countPermutableSequences() {
            Map<Integer, Integer> subs = new HashMap<>();
            long start = this.start;
            Iterator<Long> it = list.iterator();
            long next;
            LinkedList<Long> sub = new LinkedList<>();
            long startOfSub = -1;
            while (it.hasNext()) {
                next = it.next();
                if (next - start == 3) {
                    if (sub.size() > 1) {
                        endSubSequence(start, sub, subs);
                    }
                    sub = new LinkedList<>();
                    startOfSub = -1;
                } else {
                    startOfSub = startOfSub == -1 ? start : startOfSub;
                    sub.add(start);
                }
                start = next;
            }
            if (sub.size() > 1) {
                endSubSequence(start, sub, subs);
            }
            return subs.entrySet().stream()
                    .map(e -> Math.pow(countPermutations(e.getKey()), e.getValue()))
                    .reduce((x, y) -> x*y).orElse(0d).longValue();
        }

        private void endSubSequence(long start, LinkedList<Long> sub, Map<Integer, Integer> subs) {
            sub.add(start);
            System.out.println(D10.toString(sub));
            subs.compute(sub.size(), (k, v) -> (v == null) ? 1 : v + 1);
        }

    }



    private static String toString(List<Long> list, long start) {
        return list.stream().map(i -> i+start).map(Object::toString).collect(Collectors.joining(","));
    }
    private static String toString(List<Long> list) {
        return toString(list, 0);
    }

    public static long countPermutations(int size) {
        if (size == 2) {
            return 1;
        }
        if (size < 5) {
            return (long) Math.pow(2, size - 2);
        }
        if (size == 5) {
            return 7;
        }
        return 0;
    }

}
