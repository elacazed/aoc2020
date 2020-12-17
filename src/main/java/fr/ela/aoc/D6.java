package fr.ela.aoc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;


/*
n's answers are on a single line. For example:

abc

a
b
c

ab
ac

a
a
a
a

b

This list represents answers from five groups:

    The first group contains one person who answered "yes" to 3 questions: a, b, and c.
    The second group contains three people; combined, they answered "yes" to 3 questions: a, b, and c.
    The third group contains two people; combined, they answered "yes" to 3 questions: a, b, and c.
    The fourth group contains four people; combined, they answered "yes" to only 1 question, a.
    The last group contains one person who answered "yes" to only 1 question, b.

 */
public class D6 {


    public static void main(String[] args) {
        try {
            Path file = Paths.get("target/classes/input_6.txt");

            List<String> lines = Files.readAllLines(file);
            partOne(lines);
            partTwo(lines);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void partTwo(List<String> lines) {
        Integer[] answers = new Integer[26];
        Arrays.fill(answers, 0);

        int i = 0;
        int total = 0;
        int groupSize = 0;
        for (String line : lines) {
            if (line.trim().length() == 0) {
                final int size = groupSize;
                long group = Arrays.stream(answers).filter(Objects::nonNull).filter(v -> v == size).count();
                System.out.println("Group " + i + "(size "+groupSize+") : " + group);
                total += group;
                groupSize = 0;
                Arrays.fill(answers, 0);
                i++;
            } else {
                groupSize++;
                for (char c : line.toCharArray()) {
                    answers[(int) c - 97]++;
                }
            }
        }
        final int size = groupSize;
        long group = Arrays.stream(answers).filter(Objects::nonNull).filter(v -> v == size).count();
        System.out.println("Group " + i + " : " + group);
        total += group;
        System.out.println("Total : " + total);
    }

    public static void partOne(List<String> lines) {
        Set<Character> answers = new HashSet<>();
        int i = 0;
        int total = 0;
        for (String line : lines) {
            if (line.trim().length() == 0) {
                System.out.println("Group " + i + " : " + answers.size());
                total += answers.size();
                answers.clear();
                i++;
            } else {
                for (char c : line.toCharArray()) {
                    answers.add(c);
                }
            }
        }
        System.out.println("Group " + i + " : " + answers.size());
        total += answers.size();
        System.out.println("Total : " + total);
    }
}
