package fr.ela.aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class D2 {


    public static void main(String[] args) {
        try {
            long result = getResult();
            System.out.println("Result : " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static final long getResult() throws IOException {
        Path file = Paths.get("target/classes/input_2.txt");
        return Files.lines(file).map(Item::new).filter(Item::check).count();
    }

    private static class Item {
        PasswordChecker checker;
        String password;

        ///7-8 j: jjjjjfvh
        public Item(String value) {
            Pattern pat = Pattern.compile("([0-9]+)-([0-9]+) ([a-z]): ([a-z]+)");
            Matcher m = pat.matcher(value);
            if (m.matches()) {
                this.checker = new PasswordChecker(m.group(3).charAt(0), Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
                this.password = m.group(4);
            }
        }

        public boolean check() {
            return checker.check(password);
        }

    }

    private static class PasswordChecker {
        final char c;
        final int min;
        final int max;

        private PasswordChecker(char c, int min, int max) {
            this.c = c;
            this.min = min;
            this.max = max;
        }

        public boolean check(String password) {

            int count = password.charAt(min - 1) == c ? 1 : 0;
            count += password.charAt(max - 1) == c ? 1 : 0;
            return count == 1;
        }
    }

}
