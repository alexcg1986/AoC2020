import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// --- Part Two ---
// Ding! The "fasten seat belt" signs have turned on. Time to find your seat.

// It's a completely full flight, so your seat should be the only missing
// boarding pass in your list. However, there's a catch: some of the seats at
// the very front and back of the plane don't exist on this aircraft, so they'll
// be missing from your list as well.

// Your seat wasn't at the very front or back, though; the seats with IDs +1 and
// -1 from yours will be in your list.

// What is the ID of your seat?

public class Solution2 {
    public static void main(String[] args) {
        List<String> input = getInput();
        System.out.println(findSeat(input));
    }

    private static List<String> getInput() {
        List<String> input = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            br.lines().forEach(line -> input.add(line));
        } catch (Exception e) {
            System.out.println(e);
        }
        return input;
    }

    private static int findSeat(List<String> input) {
        List<Integer> seatIds = new ArrayList<>();
        for (String position : input) {
            final int ROW = getValue(position.substring(0, 7));
            final int COLUMN = getValue(position.substring(7, 10));
            final int SEAT_ID = ROW * 8 + COLUMN;
            seatIds.add(SEAT_ID);
        }
        return getMySeat(seatIds);
    }

    private static int getValue(String subPosition) {
        int value = 0;
        final char[] arrChar = subPosition.toCharArray();
        for (int i = 0; i < arrChar.length; i++)
            if (arrChar[i] == 'B' || arrChar[i] == 'R')
                // we need to shift index according to the real binary value
                value |= (1 << arrChar.length - 1 - i); // '|=' Logical Or or Equal -> '+=' in this case
        return value;
    }

    private static int getMySeat(List<Integer> seatIds) {
        Collections.sort(seatIds);
        int mySeatId = Integer.MIN_VALUE;
        for (int i = 0; i < seatIds.size() - 1; i++)
            if (seatIds.get(i) != seatIds.get(i + 1) - 1)
                mySeatId = seatIds.get(i) + 1;
        return mySeatId;
    }
}