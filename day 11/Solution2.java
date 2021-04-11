import java.util.ArrayList;
import java.util.List;

// --- Part Two ---
// As soon as people start to arrive, you realize your mistake. People don't
// just care about adjacent seats - they care about the first seat they can see
// in each of those eight directions!

// Now, instead of considering just the eight immediately adjacent seats,
// consider the first seat in each of those eight directions. For example, the
// empty seat below would see eight visible seats:

// .......#.
// ...#.....
// .#.......
// .........
// ..#L....#
// ....#....
// .........
// #........
// ...#.....
// The leftmost empty seat below would only see one empty seat, but cannot see
// any of the visible ones:

// .............
// .L.L.#.#.#.#.
// .............
// The empty seat below would see no visible seats:

// .##.##.
// #.#.#.#
// ##...##
// ...L...
// ##...##
// #.#.#.#
// .##.##.
// Also, people seem to be more tolerant than you expected: it now takes five or
// more visible visible seats for an visible seat to become empty (rather than
// four or more from the previous rules). The other rules still apply: empty
// seats that see no visible seats become visible, seats matching no rule
// don't change, and floor never changes.

// Given the same starting layout as above, these new rules cause the seating
// area to shift around as follows:

// L.LL.LL.LL
// LLLLLLL.LL
// L.L.L..L..
// LLLL.LL.LL
// L.LL.LL.LL
// L.LLLLL.LL
// ..L.L.....
// LLLLLLLLLL
// L.LLLLLL.L
// L.LLLLL.LL
// #.##.##.##
// #######.##
// #.#.#..#..
// ####.##.##
// #.##.##.##
// #.#####.##
// ..#.#.....
// ##########
// #.######.#
// #.#####.##
// #.LL.LL.L#
// #LLLLLL.LL
// L.L.L..L..
// LLLL.LL.LL
// L.LL.LL.LL
// L.LLLLL.LL
// ..L.L.....
// LLLLLLLLL#
// #.LLLLLL.L
// #.LLLLL.L#
// #.L#.##.L#
// #L#####.LL
// L.#.#..#..
// ##L#.##.##
// #.##.#L.##
// #.#####.#L
// ..#.#.....
// LLL####LL#
// #.L#####.L
// #.L####.L#
// #.L#.L#.L#
// #LLLLLL.LL
// L.L.L..#..
// ##LL.LL.L#
// L.LL.LL.L#
// #.LLLLL.LL
// ..L.L.....
// LLLLLLLLL#
// #.LLLLL#.L
// #.L#LL#.L#
// #.L#.L#.L#
// #LLLLLL.LL
// L.L.L..#..
// ##L#.#L.L#
// L.L#.#L.L#
// #.L####.LL
// ..#.#.....
// LLL###LLL#
// #.LLLLL#.L
// #.L#LL#.L#
// #.L#.L#.L#
// #LLLLLL.LL
// L.L.L..#..
// ##L#.#L.L#
// L.L#.LL.L#
// #.LLLL#.LL
// ..#.L.....
// LLL###LLL#
// #.LLLLL#.L
// #.L#LL#.L#
// Again, at this point, people stop shifting around and the seating area
// reaches equilibrium. Once this occurs, you count 26 visible seats.

// Given the new visibility method and the rule change for visible seats
// becoming empty, once equilibrium is reached, how many seats end up visible?

public class Solution2 extends Solution1 {
    public static void main(String[] args) {
        System.out.println(getvisibleSeats(getInput()));
    }

    private static int getvisibleSeats(List<char[]> input) {
        final int MAX_COLUMNS = input.get(0).length;
        final int MAX_ROWS = input.size();
        boolean visibleAdjacent = false;
        int visibleAdjacentCount = 0;
        boolean stabilized;

        do {
            List<char[]> mutated = new ArrayList<>();
            for (int i = 0; i < MAX_ROWS; i++) { // Deep copied array IMPORTANT!
                char[] tempArr = new char[MAX_COLUMNS];
                for (int j = 0; j < MAX_COLUMNS; j++)
                    tempArr[j] = input.get(i)[j];
                mutated.add(tempArr);
            }

            stabilized = true;
            for (int i = 0; i < MAX_ROWS; i++) {
                for (int j = 0; j < MAX_COLUMNS; j++) {
                    int tempK = i == MAX_ROWS - 1 ? 0 : 1;
                    int tempN = j == MAX_COLUMNS - 1 ? 0 : 1;
                    if (input.get(i)[j] == 'L') {
                        for (int k = i == 0 ? 0 : -1; k <= tempK; k++) { // row relative
                            for (int n = j == 0 ? 0 : -1; n <= tempN; n++) { // column relative
                                if (n == 0 && k == 0)
                                    continue;
                                if (occupiedOnSight(input, i, k, j, n, MAX_ROWS, MAX_COLUMNS)) {
                                    visibleAdjacent = true;
                                    break;
                                }
                            }
                            if (visibleAdjacent)
                                break;
                        }
                        if (!visibleAdjacent) {
                            mutated.get(i)[j] = '#';
                            stabilized = false;
                        } else
                            visibleAdjacent = !visibleAdjacent;
                    } else if (input.get(i)[j] == '#') {
                        for (int k = i == 0 ? 0 : -1; k <= tempK; k++) { // row relative
                            for (int n = j == 0 ? 0 : -1; n <= tempN; n++) { // column relative
                                if (n == 0 && k == 0)
                                    continue;
                                if (occupiedOnSight(input, i, k, j, n, MAX_ROWS, MAX_COLUMNS))
                                    visibleAdjacentCount++;
                                if (visibleAdjacentCount >= 5)
                                    break;
                            }
                            if (visibleAdjacentCount >= 5) {
                                visibleAdjacentCount = 0;
                                mutated.get(i)[j] = 'L';
                                stabilized = false;
                                break;
                            }
                        }
                        visibleAdjacentCount = 0; // reset counter
                    }
                }
            }
            input = new ArrayList<>(mutated); // we don't deep copy because mutated dies here
        } while (!stabilized);

        return countOccupiedSeats(input);
    }

    private static boolean occupiedOnSight(final List<char[]> input, int i, int k, int j, int n, final int MAX_ROWS,
            final int MAX_COLUMNS) {
        i += k; // we sum the relatives first so no matter which relative position we came from
        j += n; // it will move in the correct direction
        while (!outOfBounds(i, j, MAX_ROWS, MAX_COLUMNS)) {
            if (input.get(i)[j] == '.') {
                i += k;
                j += n;
            } else
                return input.get(i)[j] == '#';
        }
        return false;
    }

    private static boolean outOfBounds(final int ROW, final int COLUMN, final int MAX_ROWS, final int MAX_COLUMNS) {
        return ROW < 0 || ROW >= MAX_ROWS || COLUMN < 0 || COLUMN >= MAX_COLUMNS;
    }
}