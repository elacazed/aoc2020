package fr.ela.aoc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class D14 {

    private static final Pattern MEM_PATTERN = Pattern.compile("mem\\[([0-9]+)\\] = ([0-9]+)");

    public static void main(String[] args) {
        try {
            Path file = Paths.get("target/classes/D14-test.txt");
            partOne(Files.readAllLines(file), 165L);
            List<String> lines = new ArrayList<>();
            lines.add("mask = 000000000000000000000000000000X1001X");
            lines.add("mem[42] = 100");
            lines.add("mask = 00000000000000000000000000000000X0XX");
            lines.add("mem[26] = 1");
            partTwo(lines, 208L);
            // 208
            file = Paths.get("target/classes/D14.txt");
            partOne(Files.readAllLines(file), 6317049172545L);
            partTwo(Files.readAllLines(file), 3434009980379L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static char[] asCharArray(long value) {
        return asCharArray(value, 36);
    }

    private static char[] asCharArray(long value, int size) {
        String binary = Long.toBinaryString(value);
        char[] masked = new char[size];
        Arrays.fill(masked, '0');
        System.arraycopy(binary.toCharArray(), 0, masked, size - binary.length(), binary.length());
        return masked;
    }

    // ===================== Part one

    public static void partOne(List<String> lines, long expected) {
        MaskOne mask = MaskOne.build("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        long sum = 0;
        Map<Long, Long> memory = new HashMap<>();
        for (String line : lines) {
            if (line.startsWith("mask")) {
                mask = MaskOne.build(line.substring(7));
            } else {
                Matcher m = MEM_PATTERN.matcher(line);
                if (m.matches()) {
                    long pos = Long.parseLong(m.group(1));
                    long val = mask.apply(Long.parseLong(m.group(2)));
                    sum -= memory.getOrDefault(pos, 0L);
                    memory.put(pos, val);
                    sum += val;
                } else {
                    throw new IllegalArgumentException("Error " + line);
                }
            }
        }
        String ok = expected == sum ? "ok" : "ko";
        System.out.println("Part one "+ok+ " : "+ sum+" Expected : "+expected);
    }

    public static class MaskOne {
        Map<Integer, Character> map = new HashMap<>();

        public static MaskOne build(String value) {
            MaskOne m = new MaskOne();
            for (int i = 0; i < value.length(); i++) {
                char c = value.charAt(i);
                if (c != 'X') {
                    m.map.put(i, c);
                }
            }
            return m;
        }

        public long apply(long value) {
            char[] masked = asCharArray(value);
            map.forEach((key, value1) -> masked[key] = value1);
            return Long.parseLong(new String(masked), 2);
        }
    }

    // ===================== Part Two
    public static void partTwo(List<String> lines, long expected) {
        MaskTwo mask = MaskTwo.build("000000000000000000000000000000000000");

        Map<String, Long> memory = new HashMap<>();
        for (String line : lines) {
            if (line.startsWith("mask")) {
                mask = MaskTwo.build(line.substring(7));
            } else {
                Matcher m = MEM_PATTERN.matcher(line);
                if (m.matches()) {
                    List<String> adresses = mask.getAdresses(Long.parseLong(m.group(1)));
                    long val = Long.parseLong(m.group(2));
                    adresses.forEach(a -> memory.put(a, val));
                } else {
                    throw new IllegalArgumentException("Error " + line);
                }
            }
        }
        long sum = memory.values().stream().reduce(Long::sum).orElse(-1L);
        String ok = expected == sum ? "ok" : "ko";
        System.out.println("Part Two "+ok+ " : "+ sum+" Expected : "+expected);
    }


    public static class MaskTwo {
        List<Integer> floatingBits = new ArrayList<>();
        List<Integer> oneBits = new ArrayList<>();

        public static MaskTwo build(String mask) {
            MaskTwo m = new MaskTwo();
            for (int i = 0; i < mask.length(); i++) {
                char c = mask.charAt(i);
                switch (c) {
                    case 'X':
                        m.floatingBits.add(i);
                        break;
                    case '1':
                        m.oneBits.add(i);
                }
            }
            return m;
        }

        public List<String> getAdresses(long adress) {
            char[] binaryAdress = asCharArray(adress);
            oneBits.forEach(i -> binaryAdress[i] = '1');

            List<String> adresses = new ArrayList<>();

            for (long i = 0; i < Math.pow(2, floatingBits.size()); i++) {
                char[] binaryFloating = asCharArray(i, floatingBits.size());

                 for (int j = 0; j < floatingBits.size(); j++) {
                    binaryAdress[floatingBits.get(j)] = binaryFloating[j];
                }
                adresses.add(new String(binaryAdress));
            }
            return adresses;
        }
    }
}