import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// --- Day 12: Rain Risk ---
// Your ferry made decent progress toward the island, but the storm came in
// faster than anyone expected. The ferry needs to take evasive actions!

// Unfortunately, the ship's navigation computer seems to be malfunctioning;
// rather than giving a route directly to safety, it produced extremely
// circuitous instructions. When the captain uses the PA system to ask if anyone
// can help, you quickly volunteer.

// The navigation instructions (your puzzle input) consists of a sequence of
// single-character actions paired with integer input values. After staring at
// them for a few minutes, you work out what they probably mean:

// Action N means to move north by the given value.
// Action S means to move south by the given value.
// Action E means to move east by the given value.
// Action W means to move west by the given value.
// Action L means to turn left the given number of degrees.
// Action R means to turn right the given number of degrees.
// Action F means to move forward by the given value in the direction the ship
// is currently facing.
// The ship starts by facing east. Only the L and R actions change the direction
// the ship is facing. (That is, if the ship is facing east and the next
// instruction is N10, the ship would move north 10 units, but would still move
// east if the following action were F.)

// For example:

// F10
// N3
// F7
// R90
// F11
// These instructions would be handled as follows:

// F10 would move the ship 10 units east (because the ship starts by facing
// east) to east 10, north 0.
// N3 would move the ship 3 units north to east 10, north 3.
// F7 would move the ship another 7 units east (because the ship is still facing
// east) to east 17, north 3.
// R90 would cause the ship to turn right by 90 degrees and face south; it
// remains at east 17, north 3.
// F11 would move the ship 11 units south to east 17, south 8.
// At the end of these instructions, the ship's Manhattan distance (sum of the
// absolute values of its east/west position and its north/south position) from
// its starting position is 17 + 8 = 25.

// Figure out where the navigation instructions lead. What is the Manhattan
// distance between that location and the ship's starting position?

public class Solution1 {
    public static void main(String[] args) {
        System.out.println(getManhattanDistance(getInput()));
    }

    static List<String> getInput() {
        List<String> input = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            input = br.lines().collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println(e);
        }
        return input;
    }

    private static double getManhattanDistance(final List<String> input) {
        char direction = 'E'; // starting value;
        final int CONST = 90;
        final Map<Character, Integer> movements = new HashMap<>(); // stored values
        final Map<Character, Integer> cardinalToNumber = Map.of('N', 1, 'E', 2, 'S', 3, 'W', 4); // immutable mapper
        final Map<Integer, Character> numberToCardinal = Map.of(1, 'N', 2, 'E', 3, 'S', 4, 'W'); // immutable mapper

        for (String instruction : input)
            switch (instruction.charAt(0)) {
            case 'N' -> movements.merge('N', Integer.valueOf(instruction.substring(1)),
                    (oldValue, newValue) -> oldValue + newValue);
            case 'E' -> movements.merge('E', Integer.valueOf(instruction.substring(1)),
                    (oldValue, newValue) -> oldValue + newValue);
            case 'S' -> movements.merge('S', Integer.valueOf(instruction.substring(1)),
                    (oldValue, newValue) -> oldValue + newValue);
            case 'W' -> movements.merge('W', Integer.valueOf(instruction.substring(1)),
                    (oldValue, newValue) -> oldValue + newValue);
            case 'R' -> {
                final int NEW_DIRECTION = cardinalToNumber.get(direction)
                        + Integer.valueOf(instruction.substring(1)) / CONST;
                direction = NEW_DIRECTION > 4 ? numberToCardinal.get(NEW_DIRECTION - 4)
                        : numberToCardinal.get(NEW_DIRECTION);
            }
            case 'L' -> {
                final int NEW_DIRECTION = cardinalToNumber.get(direction)
                        - Integer.valueOf(instruction.substring(1)) / CONST;
                direction = NEW_DIRECTION < 1 ? numberToCardinal.get(NEW_DIRECTION + 4)
                        : numberToCardinal.get(NEW_DIRECTION);
            }
            case 'F' -> movements.merge(direction, Integer.valueOf(instruction.substring(1)),
                    (oldValue, newValue) -> oldValue + newValue);
            }

        return calculateManhattanDistance(movements);
    }

    static double calculateManhattanDistance(final Map<Character, Integer> movements) {
        return Math.abs(movements.getOrDefault('E', 0) - movements.getOrDefault('W', 0))
                + Math.abs(movements.getOrDefault('N', 0) - movements.getOrDefault('S', 0));
    }
}