package fr.ela.aoc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class D4 {


    /*

    byr (Birth Year)
    iyr (Issue Year)
    eyr (Expiration Year)
    hgt (Height)
    hcl (Hair Color)
    ecl (Eye Color)
    pid (Passport ID)
    cid (Country ID)

     */


    public static void main(String[] args) {
        try {
            Path file = Paths.get("target/classes/input_4.txt");
            List<String> lines = Files.readAllLines(file);
            int validPasswords = 0;
            StringBuilder sb = new StringBuilder();
            for (String line : lines) {
                if (line.trim().length() == 0) {
                    if (Password.isValid(sb.toString())) {
                        validPasswords++;
                    }
                    sb = new StringBuilder();
                } else {
                    sb.append(line).append(" ");
                }
            }
            if (Password.isValid(sb.toString())) {
                validPasswords++;
            }

            System.out.println("Tested " + Password.counter + " passwords, " + validPasswords + " valid");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static class Password {

        private static int counter = 0;

        private Map<String, String> fields;
        private static Map<String, Predicate<String>> requiredFields = buildMap();

        private static Map<String, Predicate<String>> buildMap() {
            Map<String, Predicate<String>> map = new HashMap<>();
            map.put("byr", v -> validInt(v, Pattern.compile("([0-9]{4})"), 1920, 2002));
            map.put("iyr", v -> validInt(v, Pattern.compile("([0-9]{4})"), 2010, 2020));
            map.put("eyr", v -> validInt(v, Pattern.compile("([0-9]{4})"), 2020, 2030));
            map.put("hgt", v -> validHeight(v));
            map.put("hcl", v -> v.matches("#[0-99a-f]{6}"));
            map.put("ecl",v -> v.matches("(amb|blu|brn|gry|grn|hzl|oth)"));
            map.put("pid",v -> v.matches("[0-9]{9}"));
            return map;

        }

        public Password(String data) {
            counter++;
            fields = new HashMap<>();
            String[] fieldsStr = data.split("\\s");
            for (String field : fieldsStr) {
                String[] value = field.split(":");
                fields.put(value[0], value[1]);
            }

        }

        public static boolean isValid(String str) {
            return new Password(str).isValid();
        }

        public boolean isValid() {
            return requiredFields.entrySet().stream().allMatch(e -> test(e, fields));
        }
    }

    private static boolean test(Map.Entry<String, Predicate<String>> fields, Map<String, String> values) {
        String fieldName = fields.getKey();
        String value = values.get(fieldName);
        if (value == null) {
            System.out.println("Invalid "+fields.getKey()+" : missing");
            return false;
        }
        if (fields.getValue().test(value)) {
            return true;
        } else {
            System.out.println("Invalid "+fields.getKey()+" : "+value);
            return false;
        }
    }

    private static boolean validInt(String value, Pattern regexp, int min, int max) {
        Matcher m = regexp.matcher(value);
        if (m.matches()) {
            int val = Integer.parseInt(m.group(1));
            return val <= max && val >= min;
        } else {
            return false;
        }
    }


    private static boolean validHeight(String value) {
        Pattern regexp = Pattern.compile("([0-9]+)(in|cm)");
        Matcher m = regexp.matcher(value);
        if (m.matches()) {
            int val = Integer.parseInt(m.group(1));
            if ("in".equals(m.group(2))) {
                return val <= 76 && val >= 59;
            } else {
                return val <= 193 && val >= 150;
            }
        } else {
            return false;
        }
    }

}
