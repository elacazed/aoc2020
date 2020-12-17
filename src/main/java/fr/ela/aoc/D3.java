package fr.ela.aoc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class D3 {


    public static void main(String[] args) {
        try {
            Path file = Paths.get("target/classes/D3.txt");
            List<String> lines = Files.readAllLines(file);

            int[][] slopes = new int[][]{{1, 1}, {3, 1}, {5, 1}, {7, 1}, {1, 2}};

            long result = Arrays.stream(slopes).map(slope -> getResult(lines, slope[0], slope[1])).reduce(1L, (i1, i2) -> i1*i2);

            System.out.println("Result : " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
        Right 1, down 1.
        Right 3, down 1. (This is the slope you already checked.)
        Right 5, down 1.
        Right 7, down 1.
        Right 1, down 2
    */
    private static final long getResult(List<String> lines, int right, int down) {
        List<boolean[]> map = lines.stream().map(D3::getTrees).collect(Collectors.toList());

        int curX = 0;
        int curY = 0;

        int treesCounter = 0;
        while (curY < map.size() - 1) {
            curX += right;
            curY += down;
            if (hasTree(map, curX, curY)) {
                treesCounter++;
            }
        }
        System.out.println("Slope "+right+", "+down+" : "+treesCounter);
        return (long) treesCounter;
    }


    private static boolean hasTree(List<boolean[]> map, int x, int y) {
        boolean[] row = map.get(y);
        int xmod = x % row.length;
        return row[xmod];
    }

    private static boolean[] getTrees(String s) {
        boolean[] result = new boolean[s.length()];
        for (int i = 0; i < s.length(); i++) {
            result[i] = s.charAt(i) == '#';
        }
        return result;
    }


}
