import java.util.HashMap;
import java.util.List;
import java.util.Map;

// --- Part Two ---
// Before you can give the destination to the captain, you realize that the
// actual action meanings were printed on the back of the instructions the whole
// time.

// Almost all of the actions indicate how to move a waypoint which is relative
// to the ship's position:

// Action N means to move the waypoint north by the given value.
// Action S means to move the waypoint south by the given value.
// Action E means to move the waypoint east by the given value.
// Action W means to move the waypoint west by the given value.
// Action L means to rotate the waypoint around the ship left
// (counter-clockwise) the given number of degrees.
// Action R means to rotate the waypoint around the ship right (clockwise) the
// given number of degrees.
// Action F means to move forward to the waypoint a number of times equal to the
// given value.
// The waypoint starts 10 units east and 1 unit north relative to the ship. The
// waypoint is relative to the ship; that is, if the ship moves, the waypoint
// moves with it.

// For example, using the same instructions as above:

// F10 moves the ship to the waypoint 10 times (a total of 100 units east and 10
// units north), leaving the ship at east 100, north 10. The waypoint stays 10
// units east and 1 unit north of the ship.
// N3 moves the waypoint 3 units north to 10 units east and 4 units north of the
// ship. The ship remains at east 100, north 10.
// F7 moves the ship to the waypoint 7 times (a total of 70 units east and 28
// units north), leaving the ship at east 170, north 38. The waypoint stays 10
// units east and 4 units north of the ship.
// R90 rotates the waypoint around the ship clockwise 90 degrees, moving it to 4
// units east and 10 units south of the ship. The ship remains at east 170,
// north 38.
// F11 moves the ship to the waypoint 11 times (a total of 44 units east and 110
// units south), leaving the ship at east 214, south 72. The waypoint stays 4
// units east and 10 units south of the ship.
// After these operations, the ship's Manhattan distance from its starting
// position is 214 + 72 = 286.

// Figure out where the navigation instructions actually lead. What is the
// Manhattan distance between that location and the ship's starting position?

public class Solution2 extends Solution1 {
    public static void main(String[] args) {
        System.out.println(getManhattanDistance(getInput()));
    }

    private static double getManhattanDistance(final List<String> input) {
        final int CONST = 90;
        final Map<Character, Integer> waypoints = new HashMap<>() { // waypoint stored values
            private static final long serialVersionUID = 1L;
            {
                put('E', 10);
                put('N', 1);
            }
        };
        final Map<Character, Integer> movements = new HashMap<>(); // ship stored values
        final Map<Character, Integer> cardinalToNumber = Map.of('N', 1, 'E', 2, 'S', 3, 'W', 4); // immutable map
        final Map<Integer, Character> numberToCardinal = Map.of(1, 'N', 2, 'E', 3, 'S', 4, 'W'); // immutable map

        for (String instruction : input)
            switch (instruction.charAt(0)) {
            case 'N' -> waypoints.merge('N', Integer.valueOf(instruction.substring(1)),
                    (oldValue, newValue) -> oldValue + newValue);
            case 'E' -> waypoints.merge('E', Integer.valueOf(instruction.substring(1)),
                    (oldValue, newValue) -> oldValue + newValue);
            case 'S' -> waypoints.merge('S', Integer.valueOf(instruction.substring(1)),
                    (oldValue, newValue) -> oldValue + newValue);
            case 'W' -> waypoints.merge('W', Integer.valueOf(instruction.substring(1)),
                    (oldValue, newValue) -> oldValue + newValue);
            case 'R' -> {
                Map<Character, Integer> temp = new HashMap<>(waypoints);
                waypoints.clear();
                temp.keySet().forEach(key -> {
                    final int VALUE_TO_REASIGN = temp.get(key);
                    final int ROTATED_NUMBER = cardinalToNumber.get(key)
                            + Integer.valueOf(instruction.substring(1)) / CONST;
                    final char ROTATED_CARDINAL = ROTATED_NUMBER > 4 ? numberToCardinal.get(ROTATED_NUMBER - 4)
                            : numberToCardinal.get(ROTATED_NUMBER);
                    waypoints.put(ROTATED_CARDINAL, VALUE_TO_REASIGN);
                });
            }
            case 'L' -> {
                Map<Character, Integer> temp = new HashMap<>(waypoints);
                waypoints.clear();
                temp.keySet().forEach(key -> {
                    final int VALUE_TO_REASIGN = temp.get(key);
                    final int ROTATED_NUMBER = cardinalToNumber.get(key)
                            - Integer.valueOf(instruction.substring(1)) / CONST;
                    final char ROTATED_CARDINAL = ROTATED_NUMBER < 1 ? numberToCardinal.get(ROTATED_NUMBER + 4)
                            : numberToCardinal.get(ROTATED_NUMBER);
                    waypoints.put(ROTATED_CARDINAL, VALUE_TO_REASIGN);
                });
            }
            case 'F' -> waypoints.keySet()
                    .forEach(key -> movements.merge(key, waypoints.get(key) * Integer.valueOf(instruction.substring(1)),
                            (oldValue, newValue) -> oldValue + newValue));
            }

        return calculateManhattanDistance(movements);
    }
}