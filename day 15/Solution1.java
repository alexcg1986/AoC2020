import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// --- Day 15: Rambunctious Recitation ---
// You catch the airport shuttle and try to book a new flight to your vacation
// island. Due to the storm, all direct flights have been cancelled, but a route
// is available to get around the storm. You take it.

// While you wait for your flight, you decide to check in with the Elves back at
// the North Pole. They're playing a memory game and are ever so excited to
// explain the rules!

// In this game, the players take turns saying numbers. They begin by taking
// turns reading from a list of starting numbers (your puzzle input). Then, each
// turn consists of considering the most recently spoken number:

// If that was the first time the number has been spoken, the current player
// says 0.
// Otherwise, the number had been spoken before; the current player announces
// how many turns apart the number is from when it was TEMPly spoken.
// So, after the starting numbers, each turn results in that player speaking
// aloud either 0 (if the last number is new) or an age (if the last number is a
// repeat).

// For example, suppose the starting numbers are 0,3,6:

// Turn 1: The 1st number spoken is a starting number, 0.
// Turn 2: The 2nd number spoken is a starting number, 3.
// Turn 3: The 3rd number spoken is a starting number, 6.
// Turn 4: Now, consider the last number spoken, 6. Since that was the first
// time the number had been spoken, the 4th number spoken is 0.
// Turn 5: Next, again consider the last number spoken, 0. Since it had been
// spoken before, the next number to speak is the difference between the turn
// number when it was last spoken (the TEMP turn, 4) and the turn number of
// the time it was most recently spoken before then (turn 1). Thus, the 5th
// number spoken is 4 - 1, 3.
// Turn 6: The last number spoken, 3 had also been spoken before, most recently
// on turns 5 and 2. So, the 6th number spoken is 5 - 2, 3.
// Turn 7: Since 3 was just spoken twice in a row, and the last two turns are 1
// turn apart, the 7th number spoken is 1.
// Turn 8: Since 1 is new, the 8th number spoken is 0.
// Turn 9: 0 was last spoken on turns 8 and 4, so the 9th number spoken is the
// difference between them, 4.
// Turn 10: 4 is new, so the 10th number spoken is 0.
// (The game ends when the Elves get sick of playing or dinner is ready,
// whichever comes first.)

// Their question for you is: what will be the 2020th number spoken? In the
// example above, the 2020th number spoken will be 436.

// Here are a few more examples:

// Given the starting numbers 1,3,2, the 2020th number spoken is 1.
// Given the starting numbers 2,1,3, the 2020th number spoken is 10.
// Given the starting numbers 1,2,3, the 2020th number spoken is 27.
// Given the starting numbers 2,3,1, the 2020th number spoken is 78.
// Given the starting numbers 3,2,1, the 2020th number spoken is 438.
// Given the starting numbers 3,1,2, the 2020th number spoken is 1836.
// Given your starting numbers, what will be the 2020th number spoken?

public class Solution1 {
    public static void main(String[] args) {
        System.out.println(getNumber(getInput(), 2020));
    }

    static List<Integer> getInput() {
        List<Integer> input = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            input = Arrays.stream(br.readLine().trim().split(",")).map(Integer::valueOf).collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println(e);
        }
        return input;
    }

    // Van Eck Sequence is defined as:

    // Starts with 0.
    // If the last term is the first occurrence of that term the next term is 0.
    // If the last term has occurred previously the next term is how many steps back
    // was the most recent occurrence.
    static long getNumber(final List<Integer> input, final int MAX_TURNS) {
        final int[] STORE = new int[MAX_TURNS]; // the array to store values
        int turn;
        int lastSpoken = Integer.MIN_VALUE;

        for (turn = 1; turn <= input.size(); ++turn) {
            lastSpoken = input.get(turn - 1); // turn offset compensation
            STORE[lastSpoken] = turn; // we keep track of lastSpoken values
        }

        for (; turn <= MAX_TURNS; ++turn) {
            final int STORED_TURN = STORE[lastSpoken];
            STORE[lastSpoken] = turn - 1;
            // if it has been spoken before, we return the current value
            // minus the stored one, else 0;
            // we also -1 each turn to compensate offset
            final int TEMP = STORED_TURN == 0 ? 0 : turn - 1 - STORED_TURN;
            lastSpoken = TEMP;
        }

        return lastSpoken;
    }
}
