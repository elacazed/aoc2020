package fr.ela.aoc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class D11 {

    private char[][] positions;

    private final int rows;
    private final int cols;


    public static void main(String[] args) {
        try {
            Path file = Paths.get("target/classes/D11-test.txt");
            List<String> lines = Files.readAllLines(file);
            D11 test = new D11(lines);
            test.part("One", false, test::countAdjacent, 4);
            test = new D11(lines);
            test.part("Two", true, test::countVisible, 5);

            file = Paths.get("target/classes/D11.txt");
            lines = Files.readAllLines(file);
            D11 real = new D11(lines);
            real.part("One", false, real::countAdjacent, 4);
            real = new D11(lines);
            real.part("Two", false, real::countVisible, 5);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private enum Direction {
        RIGHT(1, 0),
        TOP(0, -1),
        LEFT(-1, 0),
        BOTTOM(0, 1),
        TOPRIGHT(1, -1),
        TOPLEFT(-1, -1),
        BOTTOMLEFT(-1, 1),
        BOTTOMRIGHT(1, 1);


        final int dx;
        final int dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        public static Stream<Direction> stream() {
            return EnumSet.allOf(Direction.class).stream();
        }
    }

    private class Position {
        int x;
        int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public D11(List<String> lines) {
        rows = lines.size();
        cols = lines.get(0).length();
        char[][] positions = new char[rows][];

        for (int i = 0; i < rows; i++) {
            char[] row = new char[cols];
            System.arraycopy(lines.get(i).toCharArray(), 0, row, 0, cols);
            positions[i] = row;
        }
        this.positions = positions;
    }

    boolean isOccupied(Position pos) {
        return get(pos) == '#';
    }

    boolean isSeat(Position pos) {
        char c = get(pos);
        return c == 'L' || c == '#';
    }

    char get(Position pos) {
        return positions[pos.x][pos.y];
    }

    public long countAdjacent(Position position) {
        return Direction.stream().map(d -> move(position, d, 1))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(this::isOccupied).count();
    }

    public long countVisible(Position position) {
        int count = 0;
        for (Direction d : Direction.values()) {
            Optional<Position> pos = move(position, d, 1);
            while (pos.isPresent() && !isSeat(pos.get())) {
                pos = move(pos.get(), d, 1);
            }
            if (pos.isPresent() && isOccupied(pos.get())) {
                count++;
            }
        }
        return count;
    }

    Optional<Position> move(Position pos, Direction d, int distance) {
        int x = pos.x + (d.dx * distance);
        int y = pos.y + (d.dy * distance);
        if (x < 0 || y < 0 || x >= rows || y >= cols) {
            return Optional.empty();
        } else {
            return Optional.of(new Position(x, y));
        }
    }


    public static long countOccupiedSeats(char[][] positions) {
        return display(positions).chars().filter(c -> c == '#').count();
    }

    private static String display(char[][] positions) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < positions.length; i++) {
            sb.append(new String(positions[i], 0, positions[i].length)).append("\n");
        }
        return sb.toString();
    }

    private static String displayTransition(char[][] positions, char[][] newPos) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < positions.length; i++) {
            sb.append(new String(positions[i], 0, positions[i].length)).append("   ").append(new String(newPos[i], 0, newPos[i].length)).append("\n");
        }
        return sb.toString();
    }


    private void part(String name, boolean displaySteps, Function<Position, Long> countFunction, int nb) {
        char[][] pos = positions;
        int changes = 0;
        do {
            changes = nextRound(displaySteps, countFunction, nb);
        } while (changes != 0);

        System.out.println("Part " + name + " done! Seats " + countOccupiedSeats(positions));
        System.out.println("Final positions : \n" + display(positions));
        System.out.println("===========================================================");
    }


    public int nextRound(boolean display, Function<Position, Long> countFunction, int number) {
        char[][] newPos = new char[rows][];
        int changes = 0;
        for (int i = 0; i < rows; i++) {
            newPos[i] = new char[cols];
            for (int j = 0; j < cols; j++) {
                Position p = new Position(i, j);
                char c = get(p);
                char n = nextState(p, countFunction, number);
                if (c != n) {
                    changes++;
                }
                newPos[i][j] = n;
            }
        }
        if (display) {
            System.out.println("------------ Step ------------\n" + displayTransition(positions, newPos));
        }
        positions = newPos;
        return changes;
    }

    public char nextState(Position p, Function<Position, Long> countFunction, int number) {
        char c = get(p);
        switch (c) {
            case '.':
                break;
            case 'L':
                if (countFunction.apply(p) == 0) {
                    return '#';
                }
                break;
            case '#':
                if (countFunction.apply(p) < number) {
                    break;
                } else {
                    return 'L';
                }
            default:
                throw new IllegalArgumentException("Error at position " + p + " : " + c);
        }
        return c;
    }
}
