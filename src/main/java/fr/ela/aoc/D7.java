package fr.ela.aoc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class D7 {

    public static void main(String[] args) {

        try {
            Path file = Paths.get("target/classes/input_7.txt");
            List<Rule> rules = Files.readAllLines(file).stream().map(Rule::new).collect(Collectors.toList());

            partOne(rules);
            partTwo(rules);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void partOne(List<Rule> rules) {

        Set<String> containers = new HashSet<>();
        Set<String> types = new HashSet<>();
        types.add("shiny gold");
        do {
            types = types.stream().map(t -> getContainers(t, rules)).flatMap(Collection::stream).collect(Collectors.toSet());
            containers.addAll(types);
        } while (!types.isEmpty());

        System.out.println("Result Part 1 : " + containers.size());
    }

    private static void partTwo(List<Rule> rules) {
        Map<String, Rule> rulesMap = new HashMap<>();
        rules.stream().forEach(r -> rulesMap.put(r.container, r));
        System.out.println("Result 2 : " + rulesMap.get("shiny gold").count(rulesMap));

    }


    private static List<String> getContainers(String type, List<Rule> rules) {
        return rules.stream().filter(r -> r.contained.containsKey(type)).map(r -> r.container).collect(Collectors.toList());
    }


    private static class Rule {
        private static final Pattern PAT = Pattern.compile("([a-zA-Z ]+) bags contain (.*)");
        private static final Pattern PAT2 = Pattern.compile("([0-9]+) ([a-zA-Z ]+) (bag|bags)\\.?");
        String container;

        Map<String, Integer> contained;

        public Rule(String value) {
            Matcher m = PAT.matcher(value);
            if (m.matches()) {
                container = m.group(1);
                contained = new HashMap<>();
                Arrays.stream(m.group(2).split(", ")).forEach(s -> addContained(s, contained));
            }
            System.out.println(this);
        }

        public int count(Map<String, Rule> rules) {
            if (contained.isEmpty()) {
                return 0;
            } else {
                return contained.entrySet().stream().map(e -> e.getValue() * (1 + rules.get(e.getKey()).count(rules))).reduce(Integer::sum).orElse(0);
            }
        }

        public String toString() {
            return "Rule : " + container + " => " + contained.entrySet().stream().map(e -> e.getValue() + " " + e.getKey()).collect(Collectors.joining(","));
        }

        private static void addContained(String value, Map<String, Integer> map) {
            Matcher m = PAT2.matcher(value);
            if (m.matches()) {
                map.put(m.group(2), Integer.parseInt(m.group(1)));
            }
        }
    }
}
