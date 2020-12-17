package fr.ela.aoc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class D12 {


    public static void main(String[] args) {
        try {
            Path file = Paths.get("target/classes/D12-test.txt");
            List<String> lines = Files.readAllLines(file);
            partOne(lines, false);
            partTwo(lines, false);

            file = Paths.get("target/classes/D12.txt");
            lines = Files.readAllLines(file);
            partOne(lines, false);
            partTwo(lines, false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void partOne(List<String> lines, boolean display) {
        Ship ship = new Ship();
        lines.forEach(l -> ship.move(l, display));
        System.out.println("Part one : Final Position "+ship.getXShip()+","+ship.getYShip()+" Manhattan : "+ ship.getManhattanDistance());
    }


    private static void partTwo(List<String> lines, boolean display) {
        WayPointShip ship = new WayPointShip(10, 1);
        lines.forEach(l -> ship.move(l, display));
        System.out.println("Part two : Final Position "+ship.getXShip()+","+ship.getYShip()+" Manhattan : "+ ship.getManhattanDistance());
    }


    public static class WayPointShip extends Ship {
        private int xShip;
        private int yShip;

        public WayPointShip(int x, int y) {
            super(x, y);
            this.xShip = 0;
            this.yShip = 0;
        }

        @Override
        public void turn(int degrees) {
            if (degrees < 0) {
                turnRight(360 + degrees);
            } else {
                turnRight(degrees);
            }
        }

        public void turnRight(int degrees) {
            int x = xPos;
            int y = yPos;
            switch (degrees) {
                case 90:
                    xPos = y;
                    yPos = - x;
                    break;
                case 180:
                    xPos = - x;
                    yPos = - y;
                    break;
                case 270:
                    xPos = -y;
                    yPos = x;
                    break;
                default:
                    break;
            }
        }

        public String toString(String line) {
            return super.toString(", waypoint : "+xPos+","+yPos+" ("+line+")");
        }

        public int getXShip() {
            return xShip;
        }
        public int getYShip() {
            return yShip;
        }

        @Override
        public void forward(int amount) {
            int dx = xPos * amount;
            int dy = yPos * amount;

            xShip += dx;
            yShip += dy;
        }
    }

    public static class Ship {

        protected int direction = 0;
        protected int xPos;
        protected int yPos;

        public Ship() {
            this(0,0);
        }

        public Ship(int x, int y) {
            this.xPos = x;
            this.yPos = y;
        }

        public int getXShip() {
            return xPos;
        }
        public int getYShip() {
            return yPos;
        }

        public void move(String line, boolean display) {
            int amount = Integer.parseInt(line.substring(1));
            switch (line.charAt(0)) {
                case 'N':
                    goNorth(amount);
                    break;
                case 'S':
                    goSouth(amount);
                    break;
                case 'E':
                    goEast(amount);
                    break;
                case 'W':
                    goWest(amount);
                    break;
                case 'L':
                    turn(-1 * amount);
                    break;
                case 'R':
                    turn(amount);
                    break;
                case 'F':
                    forward(amount);
                    break;
                default:
                    throw new IllegalArgumentException(line);
            }
            if (display) {
                System.out.println(toString(line));
            }
        }

        public String toString(String line) {
            return "Position " + getXShip() + "," + getYShip() + " " + line;
        }

        public void goNorth(int amount) {
            yPos += amount;
        }

        public void goSouth(int amount) {
            yPos -= amount;
        }

        public void goEast(int amount) {
            xPos += amount;
        }

        public void goWest(int amount) {
            xPos -= amount;
        }

        public void turn(int degrees) {
            direction = direction + degrees;
        }

        public long getManhattanDistance() {
            return Math.abs(getXShip()) + Math.abs(getYShip());
        }

        public void forward(int amount) {
            switch ((direction / 90) % 4) {
                case 0:
                    xPos += amount;
                    break;
                case 1:
                case -3:
                    yPos -= amount;
                    break;
                case 2:
                case -2:
                    xPos -= amount;
                    break;
                case 3:
                case -1:
                    yPos += amount;
                    break;
                default:
                    throw new IllegalArgumentException("Direction à la con : " + direction);
            }
        }
    }
}
