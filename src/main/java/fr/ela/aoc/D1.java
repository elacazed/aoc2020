package fr.ela.aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class D1 {

    public static void main(String[] args) {
        try {
            Integer[] records = getRecords();
            System.out.println("Part one : " + getResult(records));
            System.out.println("Part two : " + getResult2(records));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final int getResult(Integer[] records) {
        int first;
        int second;
        for (int i = 0; i < records.length; i++) {
            first = records[i];
            for (int j = i + 1; j < records.length; j++) {
                second = records[j];
                int k = first + second;
                if (k == 2020) {
                    return first * second;
                }
            }
        }
        return 0;
    }

    private static final int getResult2(Integer[] records) {
        int first;
        int second;
        for (int i = 0; i < records.length; i++) {
            first = records[i];
            for (int j = i + 1; j < records.length; j++) {
                second = records[j];
                int k = first + second;
                if (k < 2020) {
                    for (int l = 0; l < records.length; l++) {
                        int third = records[l];
                        if (third != first && third != second && third + k == 2020) {
                            return first * second * third;
                        }
                    }
                }
            }
        }
        return 0;
    }


    private static Integer[] getRecords() throws IOException {
        Path file = Paths.get("target/classes/D1.txt");
        return Files.readAllLines(file).stream().map(Integer::parseInt).collect(Collectors.toList()).toArray(Integer[]::new);
    }


}
