package fr.ela.aoc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class D8 {

    public static void main(String[] args) {

        try {
            Path file = Paths.get("target/classes/D8-test.txt");
            List<String> lines = Files.readAllLines(file);
            List<Command> commands = new ArrayList<>(lines.size());

            for (int i = 0; i < lines.size(); i++) {
                commands.add(Command.build(i, lines.get(i)));
            }
            partOne(commands);
            partTwo(commands);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public enum Operation {
        ACC(Integer::sum, (p, v) -> p + 1),
        NOP((c, v) -> c, (p, v) -> p + 1),
        JMP((c, v) -> c, Integer::sum);

        private final BiFunction<Integer, Integer, Integer> function;
        private final BiFunction<Integer, Integer, Integer> next;

        Operation(BiFunction<Integer, Integer, Integer> function, BiFunction<Integer, Integer, Integer> next) {
            this.function = function;
            this.next = next;
        }

    }

    public static class Command {

        private static final Pattern PATTERN = Pattern.compile("([a-z]{3}) (\\+|\\-)([0-9]+)");

        private final int index;
        private Operation op;
        private final int value;
        private boolean executed;

        public Command(int index, Operation op, int value) {
            this.op = op;
            this.index = index;
            this.value = value;
            this.executed = false;
        }

        public static Command build(int index, String cmd) {
            Matcher m = PATTERN.matcher(cmd);
            if (m.matches()) {
                String op = m.group(1);
                int sign = m.group(2).equals("+") ? 1 : -1;
                int value = Integer.parseInt(m.group(3));
                return new Command(index, Operation.valueOf(op.toUpperCase()), sign * value);
            }
            throw new IllegalArgumentException(cmd + " : not recognized");
        }

        public int apply(int count) {
            executed = true;
            return op.function.apply(count, value);
        }

        public boolean isExecuted() {
            return executed;
        }

        public void reset() {
            executed = false;
        }

        public String toString() {
            return index + " " + op.name() + " " + value;
        }

        public Command next(List<Command> commands) {
            int next = op.next.apply(index, value);
            return (next > 0 && next < commands.size()) ? commands.get(next) : null;
        }


        public void switchNopJmp() {
            if (op == Operation.NOP) {
                op = Operation.JMP;
            } else if (op == Operation.JMP) {
                op = Operation.NOP;
            }
        }

        public boolean switchable() {
            return EnumSet.of(Operation.NOP, Operation.JMP).contains(op);
        }
    }

    public static class Result {
        final int value;
        final boolean infiniteLoop;
        final List<Command> executed;

        public Result(int value, boolean infiniteLoop, List<Command> executed) {
            this.value = value;
            this.infiniteLoop = infiniteLoop;
            this.executed = executed;
        }
    }

    public static Result run(List<Command> commands) {
        return run(0, commands);
    }


    public static Result run(int start, List<Command> commands) {
        int res = start;
        commands.forEach(Command::reset);
        Command cmd = commands.get(0);
        List<Command> executed = new ArrayList<>();
        while (cmd != null && !cmd.isExecuted()) {
            res = cmd.apply(res);
            executed.add(cmd);
            cmd = cmd.next(commands);
        }
        return new Result(res, cmd != null, executed);
    }


    private static void partTwo(List<Command> lines) {
        lines.forEach(Command::reset);
        Result r = run(lines);
        int runs = 1;
        Command switchCmd = null;
        if (r.infiniteLoop) {
            List<Command> switches = r.executed.stream().filter(Command::switchable).collect(Collectors.toList());
            int i = 0;
            while (r.infiniteLoop && i < switches.size()) {
                i++;
                switchCmd = switches.get(switches.size() - i);
                switchCmd.switchNopJmp();
                r = run(lines);
                runs++;
                switchCmd.switchNopJmp();
            }
        }
        System.out.println("Part two : " + (r.infiniteLoop ? "Infinite Loop! " : "End ") + r.value + " (" + runs + " runs), switched command "+switchCmd);
    }


    private static void partOne(List<Command> lines) {
        Result r = run(lines);
        System.out.println("Part one : " + (r.infiniteLoop ? "Infinite Loop! " : "End ") + r.value);
    }

}
