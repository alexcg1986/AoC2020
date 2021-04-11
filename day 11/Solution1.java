import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// --- Day 11: Seating System ---
// Your plane lands with plenty of time to spare. The final leg of your journey
// is a ferry that goes directly to the tropical island where you can finally
// start your vacation. As you reach the waiting area to board the ferry, you
// realize you're so early, nobody else has even arrived yet!

// By modeling the process people use to choose (or abandon) their seat in the
// waiting area, you're pretty sure you can predict the best place to sit. You
// make a quick map of the seat layout (your puzzle input).

// The seat layout fits neatly on a grid. Each position is either floor (.), an
// empty seat (L), or an occupied seat (#). For example, the initial seat layout
// might look like this:

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
// Now, you just need to model the people who will be arriving shortly.
// Fortunately, people are entirely predictable and always follow a simple set
// of rules. All decisions are based on the number of occupied seats adjacent to
// a given seat (one of the eight positions immediately up, down, left, right,
// or diagonal from the seat). The following rules are applied to every seat
// simultaneously:

// If a seat is empty (L) and there are no occupied seats adjacent to it, the
// seat becomes occupied.
// If a seat is occupied (#) and four or more seats adjacent to it are also
// occupied, the seat becomes empty.
// Otherwise, the seat's state does not change.
// Floor (.) never changes; seats don't move, and nobody sits on the floor.

// After one round of these rules, every seat in the example layout becomes
// occupied:

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
// After a second round, the seats with four or more occupied adjacent seats
// become empty again:

// #.LL.L#.##
// #LLLLLL.L#
// L.L.L..L..
// #LLL.LL.L#
// #.LL.LL.LL
// #.LLLL#.##
// ..L.L.....
// #LLLLLLLL#
// #.LLLLLL.L
// #.#LLLL.##
// This process continues for three more rounds:

// #.##.L#.##
// #L###LL.L#
// L.#.#..#..
// #L##.##.L#
// #.##.LL.LL
// #.###L#.##
// ..#.#.....
// #L######L#
// #.LL###L.L
// #.#L###.##
// #.#L.L#.##
// #LLL#LL.L#
// L.L.L..#..
// #LLL.##.L#
// #.LL.LL.LL
// #.LL#L#.##
// ..L.L.....
// #L#LLLL#L#
// #.LLLLLL.L
// #.#L#L#.##
// #.#L.L#.##
// #LLL#LL.L#
// L.#.L..#..
// #L##.##.L#
// #.#L.LL.LL
// #.#L#L#.##
// ..L.L.....
// #L#L##L#L#
// #.LLLLLL.L
// #.#L#L#.##
// At this point, something interesting happens: the chaos stabilizes and
// further applications of these rules cause no seats to change state! Once
// people stop moving around, you count 37 occupied seats.

// Simulate your seating area by applying the seating rules repeatedly until no
// seats change state. How many seats end up occupied?

// input.get(i-1)[j-1]   input.get(i-1)[j]    input.get(i-1)[j+1]
// input.get(i)[j-1]             x            input.get(i)[j+1] 
// input.get(i+1)[j-1]   input.get(i+1)[j]    input.get(i+1)[j+1]

public class Solution1 {
    public static void main(String[] args) {
        System.out.println(getOccupiedSeats(getInput()));
    }

    static List<char[]> getInput() {
        List<char[]> input = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            input = br.lines().map(String::toCharArray).collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println(e);
        }
        return input;
    }

    private static int getOccupiedSeats(List<char[]> input) {
        final int MAX_COLUMNS = input.get(0).length;
        final int MAX_ROWS = input.size();
        boolean occupiedAdjacent = false;
        int occupiedAdjacentCount = 0;
        boolean stabilized;
        do {
            List<char[]> mutated = new ArrayList<>();
            for (int i = 0; i < MAX_ROWS; i++) { // deep copied array
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
                                if (input.get(i + k)[j + n] == '#') {
                                    occupiedAdjacent = true;
                                    break;
                                }
                            }
                            if (occupiedAdjacent)
                                break;
                        }
                        if (!occupiedAdjacent) {
                            mutated.get(i)[j] = '#';
                            stabilized = false;
                        } else
                            occupiedAdjacent = !occupiedAdjacent;
                    } else if (input.get(i)[j] == '#') {
                        for (int k = i == 0 ? 0 : -1; k <= tempK; k++) { // row relative
                            for (int n = j == 0 ? 0 : -1; n <= tempN; n++) { // column relative
                                if (n == 0 && k == 0)
                                    continue;
                                if (input.get(i + k)[j + n] == '#')
                                    occupiedAdjacentCount++;
                                if (occupiedAdjacentCount >= 4)
                                    break;
                            }
                            if (occupiedAdjacentCount >= 4) {
                                occupiedAdjacentCount = 0;
                                mutated.get(i)[j] = 'L';
                                stabilized = false;
                                break;
                            }
                        }
                        occupiedAdjacentCount = 0; // reset counter
                    }
                }
            }
            input = new ArrayList<>(mutated); // we don't deep copy because mutated dies here
        } while (!stabilized);

        return countOccupiedSeats(input);
    }

    static int countOccupiedSeats(final List<char[]> input) {
        int occupied = 0;
        for (char[] arr : input) {
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] == '#')
                    occupied++;
                System.out.print(arr[i]);
            }
            System.out.println();
        }
        return occupied;
    }
}