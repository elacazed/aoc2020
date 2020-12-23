package fr.ela.aoc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class D16 {

    protected static Pattern RANGE_PATTERN = Pattern.compile("([0-9]+)-([0-9]+)");

    protected static final String TICKET = "179,101,223,107,127,211,191,61,199,193,181,131,89,109,197,59,227,53,103,97";

    protected static final String[] RULES = {"departure location: 25-863 or 882-957",
                                          "departure station: 50-673 or 690-972",
                                          "departure platform: 25-312 or 321-959",
                                          "departure track: 48-337 or 358-971",
                                          "departure date: 31-458 or 476-957",
                                          "departure time: 32-800 or 821-973",
                                          "arrival location: 34-502 or 528-951",
                                          "arrival station: 30-650 or 662-957",
                                          "arrival platform: 50-148 or 160-966",
                                          "arrival track: 27-572 or 587-969",
                                          "class: 46-893 or 913-964",
                                          "duration: 36-161 or 179-962",
                                          "price: 38-294 or 311-965",
                                          "route: 26-391 or 397-962",
                                          "row: 28-111 or 122-967",
                                          "seat: 48-65 or 84-973",
                                          "train: 33-827 or 839-960",
                                          "type: 47-436 or 454-959",
                                          "wagon: 45-136 or 147-959",
                                          "zone: 36-252 or 275-957"};

    protected static final String[] TEST_RULES = {"class: 1-3 or 5-7",
                                               "row: 6-11 or 33-44",
                                               "seat: 13-40 or 45-50"};

    protected static final String[] TEST_TICKETS = {"7,3,47",
                                                 "40,4,50",
                                                 "55,2,20",
                                                 "38,6,12"};

    protected static final String[] TEST_RULES_2 = {"class: 0-1 or 4-19",
                                                 "row: 0-5 or 8-19",
                                                 "seat: 0-13 or 16-19"};


    public static void main(String[] args) {

        System.out.println("Test : ");
        List<Rule> testRules = Arrays.stream(TEST_RULES).map(Rule::new).collect(Collectors.toList());
        partOne(testRules, Arrays.stream(TEST_TICKETS).map(Ticket::new).collect(Collectors.toList()));
        System.out.println("-----------------------------------------");

        try {
            List<Rule> rules = Arrays.stream(RULES).map(Rule::new).collect(Collectors.toList());
            Path file = Paths.get("target/classes/D16.txt");
            List<Ticket> tickets = Files.readAllLines(file).stream().map(Ticket::new).collect(Collectors.toList());
            List<Ticket> validTickets = partOne(rules, tickets);
            Ticket ticket = new Ticket(TICKET);
            partTwo(ticket.size(), rules, validTickets, ticket, r -> r.name.startsWith("departure"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Ticket> partOne(List<Rule> rules, List<Ticket> tickets) {
        IntPredicate matchOneRule = rules.stream().map(rule -> (IntPredicate) rule).reduce(n -> false, IntPredicate::or);
        Map<Integer, List<Ticket>> sorted = tickets.stream().collect(Collectors.groupingBy(t -> t.invalidSum(matchOneRule)));
        int sum = sorted.entrySet().stream().filter(e -> e.getKey() != -1).map(e -> e.getKey() * e.getValue().size()).reduce(Integer::sum).orElse(0);
        System.out.println("Part One : " + sum);
        return sorted.getOrDefault(-1, Collections.emptyList());
    }

    public static void partTwo(int nbFields, List<Rule> rules, List<Ticket> tickets, Ticket myTicket, Predicate<Rule> ruleFilter) {
        // Construction d'une map index -> liste des valeurs de chaque ticket valide.
        Map<Integer, List<Integer>> valuesPerField = new HashMap<>();
        for (int i = 0; i < nbFields; i++) {
            final int index = i;
            tickets.forEach(t -> valuesPerField.computeIfAbsent(index, in -> new ArrayList<>(tickets.size())).add(t.values[index]));
        }

        // Map des listes d'index candidats pour une règle donnée (key = nom de la règle) : ça évite de recalculer qu'une règle matche pour un index.
        Map<Rule, List<Integer>> candidates = new HashMap<>();
        long result = 1L;

        // Tant qu'on n'a pas associé toutes les colonnes à une règle...
        while (!valuesPerField.isEmpty()) {
            System.out.println(rules.size()+" rules remaining....");

            Iterator<Rule> it = rules.iterator();
            while (it.hasNext()) {
                Rule rule = it.next();
                // Calcul des règles
                List<Integer> positions = candidates.computeIfAbsent(rule, r -> r.getPositions(valuesPerField));
                if (positions.size() == 1) {
                    int index = positions.get(0);
                    System.out.println("\tFound index of "+rule+" : "+index);
                    if (ruleFilter.test(rule)) {
                        result = result * myTicket.values[index];
                    }
                    valuesPerField.remove(index);
                    it.remove();
                    candidates.values().forEach(l -> l.remove((Integer) index));
                }
            }
        }
        System.out.println("Part two : "+result);
    }


    private static Predicate<Integer> range(String range) {
        Matcher m = RANGE_PATTERN.matcher(range.trim());
        if (m.matches()) {
            int min = Integer.parseInt(m.group(1));
            int max = Integer.parseInt(m.group(2));

            return i -> i >= min && i <= max;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static class Ticket {
        final int[] values;

        public Ticket(String value) {
            String[] vals = value.split(",");
            values = new int[vals.length];
            for (int i = 0; i < vals.length; i++) {
                values[i] = Integer.parseInt(vals[i]);
            }
        }

        public int size() {
            return values.length;
        }

        public int invalidSum(IntPredicate matchOneRule) {
            List<Integer> invalid = Arrays.stream(values).filter(i -> ! matchOneRule.test(i)).boxed().collect(Collectors.toList());
            return invalid.isEmpty() ? -1 : invalid.stream().mapToInt(Integer::intValue).sum();
        }

    }


    public static class Rule implements IntPredicate {
        final String name;
        final String description;
        final Predicate<Integer> test;

        public Rule(String value) {
            String[] parts = value.split(":");
            this.name = parts[0];
            this.description = parts[1];
            Iterator<String> it = Arrays.stream(parts[1].split("or")).iterator();
            Predicate<Integer> pred = range(it.next());
            while (it.hasNext()) {
                pred = pred.or(range(it.next()));
            }
            this.test = pred;
        }

        public int hashCode() {
            return name.hashCode();
        }

        public boolean equald(Object other) {
            if (other instanceof Rule) {
                return ((Rule) other).name.equals(name);
            }
            return false;
        }

        public boolean test(int value) {
            return test.test(value);
        }

        public String toString() {
            return "[" + name + "] :" + description;
        }

        public List<Integer> getPositions(Map<Integer, List<Integer>> valuesPerFields) {
            return valuesPerFields.entrySet().stream()
                    .filter(e -> e.getValue().stream().allMatch(this::test))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }
    }

}
