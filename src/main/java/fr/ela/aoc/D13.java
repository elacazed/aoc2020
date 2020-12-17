package fr.ela.aoc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class D13 {

    private final long startTime;
    private final List<Long> busIds;

    public static void main(String[] args) {
        try {
            Path file = Paths.get("target/classes/D13-test.txt");
            D13 test = new D13(Files.readAllLines(file));

            test.partOne();
            test.parTwo();
            D13 real = new D13(Files.readAllLines(Paths.get("target/classes/D13.txt")));
            real.partOne();
            // 222
            real.parTwo();
            //408270049879073
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public D13(List<String> lines) {
        startTime = Long.parseLong(lines.get(0));
        busIds = Arrays.stream(lines.get(1).split(",")).map(s -> "x".equals(s) ? "-1" : s).map(Long::parseLong).collect(Collectors.toList());

    }

    private void partOne() {
        Bus bus = busIds.stream().filter(i -> i > 0).map(id -> new Bus(id, startTime)).min(Comparator.comparing(b -> b.wait)).orElseThrow();
        System.out.println("Bus " + bus.id + " : " + bus.wait + ", result = " + (bus.id * bus.wait));
    }

    private class Bus {
        final long id;
        final long wait;

        private Bus(long id, long startTime) {
            this.id = id;
            this.wait = ((startTime / id) + 1) * id - startTime;
        }
    }

    private void parTwo() {
        List<Bus2> busses = new ArrayList<>();
        for (int i = 0; i < busIds.size(); i++) {
            long id = busIds.get(i);
            if (id > 0) {
                busses.add(new Bus2(id, i));
            }
        }
        Iterator<Bus2> it = busses.iterator();
        Bus2 bus = it.next();
        long lcm = bus.id;
        long time = bus.id - bus.offset;
        bus = it.next();

        while (true) {
            if (bus.isDepartureTime(time)) {
                System.out.println("Bus "+bus.id+" can depart at "+time);
                lcm *= bus.id;
                if (! it.hasNext()) {
                    break;
                }
                bus = it.next();
                continue;
            }
            time += lcm;
        }

        System.out.println("Time : " + time);
    }


    private class Bus2 {
        final long id;
        final long offset;

        private Bus2(long id, long offset) {
            this.id = id;
            this.offset = offset;
        }

        public boolean isDepartureTime(final long timestamp) {
            return (timestamp + offset) % id == 0;
        }


    }
}
