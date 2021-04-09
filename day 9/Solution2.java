import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

// --- Part Two ---
// The final step in breaking the XMAS encryption relies on the invalid number
// you just found: you must find a contiguous set of at least two numbers in
// your list which sum to the invalid number from step 1.

// Again consider the above example:

// 35
// 20
// 15
// 25
// 47
// 40
// 62
// 55
// 65
// 95
// 102
// 117
// 150
// 182
// 127
// 219
// 299
// 277
// 309
// 576
// In this list, adding up all of the numbers from 15 through 40 produces the
// invalid number from step 1, 127. (Of course, the contiguous set of numbers in
// your actual list might be much longer.)

// To find the encryption weakness, add together the smallest and largest number
// in this contiguous range; in this example, these are 15 and 47, producing 62.

// What is the encryption weakness in your XMAS-encrypted list of numbers?

public class Solution2 extends Solution1 {
    public static void main(String[] args) {
        final long RESULT = getWeakness(getInput(25), 0, new LinkedList<Long>());
        System.out.println(RESULT == -1 ? "All the values are valid."
                : RESULT == 0 ? "There's not set meeting the criteria." : "The weakness is: " + RESULT);
    }

    private static long getWeakness(List<List<Long>> input, final int INDEX, final LinkedList<Long> deque) {
        if (INDEX >= input.get(1).size()) // Scape clause
            return -1;

        boolean valid = false;
        for (int i = 0; i < input.get(0).size() - 1; i++) {
            for (int j = i + 1; j < input.get(0).size(); j++)
                if (input.get(1).get(INDEX) == input.get(0).get(i) + input.get(0).get(j)
                        && input.get(0).get(i) != input.get(0).get(j)) {
                    deque.push(input.get(0).get(0)); // we stack the values for later
                    input.get(0).remove(0);
                    input.get(0).add(input.get(1).get(INDEX));
                    valid = true;
                    break;
                }
            if (valid)
                break;
        }
        return valid ? getWeakness(input, INDEX + 1, deque)
                : calculateWeakness(input.get(0), input.get(1).get(INDEX), deque);
    }

    private static long calculateWeakness(final List<Long> preamble, final long VALUE, final LinkedList<Long> deque) {
        if (deque.size() <= 0) // Scape clause
            return 0;

        final long TEMP;
        if ((TEMP = preamble.stream().mapToLong(Long::longValue).sum()) < VALUE) {
            if (TEMP + deque.peek() == VALUE) {
                preamble.add(deque.poll());
                Collections.sort(preamble);
                return preamble.get(0) + preamble.get(preamble.size() - 1);
            } else
                preamble.add(0, deque.poll()); // add lower value
        } else {
            int low = 0;
            int high = 1;
            long sum = preamble.get(low) + preamble.get(high);
            while (high < preamble.size() - 1) {
                if (sum == VALUE) {
                    List<Long> weaknessSet = new ArrayList<>(preamble.subList(low, high + 1));
                    Collections.sort(weaknessSet);
                    return weaknessSet.get(0) + weaknessSet.get(weaknessSet.size() - 1);
                }
                if (sum < VALUE) // we simulate "accordion" behaviour :D
                    sum += preamble.get(++high);
                else {
                    sum -= preamble.get(low++);
                    if (low == high)
                        sum += preamble.get(++high);
                }
            }
            preamble.remove(high); // remove highest value
        }
        return calculateWeakness(preamble, VALUE, deque);
    }
}
