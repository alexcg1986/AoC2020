import java.util.HashMap;
import java.util.List;
import java.util.Map;

// --- Part Two ---
// To completely determine whether you have enough adapters, you'll need to
// figure out how many different ways they can be arranged. Every arrangement
// needs to connect the charging outlet to your device. The previous rules about
// when adapters can successfully connect still apply.

// The first example above (the one that starts with 16, 10, 15) supports the
// following arrangements:

// (0), 1, 4, 5, 6, 7, 10, 11, 12, 15, 16, 19, (22)
// (0), 1, 4, 5, 6, 7, 10, 12, 15, 16, 19, (22)
// (0), 1, 4, 5, 7, 10, 11, 12, 15, 16, 19, (22)
// (0), 1, 4, 5, 7, 10, 12, 15, 16, 19, (22)
// (0), 1, 4, 6, 7, 10, 11, 12, 15, 16, 19, (22)
// (0), 1, 4, 6, 7, 10, 12, 15, 16, 19, (22)
// (0), 1, 4, 7, 10, 11, 12, 15, 16, 19, (22)
// (0), 1, 4, 7, 10, 12, 15, 16, 19, (22)
// (The charging outlet and your device's built-in adapter are shown in
// parentheses.) Given the adapters from the first example, the total number of
// arrangements that connect the charging outlet to your device is 8.

// The second example above (the one that starts with 28, 33, 18) has many
// arrangements. Here are a few:

// (0), 1, 2, 3, 4, 7, 8, 9, 10, 11, 14, 17, 18, 19, 20, 23, 24, 25, 28, 31,
// 32, 33, 34, 35, 38, 39, 42, 45, 46, 47, 48, 49, (52)

// (0), 1, 2, 3, 4, 7, 8, 9, 10, 11, 14, 17, 18, 19, 20, 23, 24, 25, 28, 31,
// 32, 33, 34, 35, 38, 39, 42, 45, 46, 47, 49, (52)

// (0), 1, 2, 3, 4, 7, 8, 9, 10, 11, 14, 17, 18, 19, 20, 23, 24, 25, 28, 31,
// 32, 33, 34, 35, 38, 39, 42, 45, 46, 48, 49, (52)

// (0), 1, 2, 3, 4, 7, 8, 9, 10, 11, 14, 17, 18, 19, 20, 23, 24, 25, 28, 31,
// 32, 33, 34, 35, 38, 39, 42, 45, 46, 49, (52)

// (0), 1, 2, 3, 4, 7, 8, 9, 10, 11, 14, 17, 18, 19, 20, 23, 24, 25, 28, 31,
// 32, 33, 34, 35, 38, 39, 42, 45, 47, 48, 49, (52)

// (0), 3, 4, 7, 10, 11, 14, 17, 20, 23, 25, 28, 31, 34, 35, 38, 39, 42, 45,
// 46, 48, 49, (52)

// (0), 3, 4, 7, 10, 11, 14, 17, 20, 23, 25, 28, 31, 34, 35, 38, 39, 42, 45,
// 46, 49, (52)

// (0), 3, 4, 7, 10, 11, 14, 17, 20, 23, 25, 28, 31, 34, 35, 38, 39, 42, 45,
// 47, 48, 49, (52)

// (0), 3, 4, 7, 10, 11, 14, 17, 20, 23, 25, 28, 31, 34, 35, 38, 39, 42, 45,
// 47, 49, (52)

// (0), 3, 4, 7, 10, 11, 14, 17, 20, 23, 25, 28, 31, 34, 35, 38, 39, 42, 45,
// 48, 49, (52)
// In total, this set of adapters can connect the charging outlet to your device
// in 19208 distinct arrangements.

// You glance back down at your bag and try to remember why you brought so many
// adapters; there must be more than a trillion valid ways to arrange them!
// Surely, there must be an efficient way to pathCounter the arrangements.

// What is the total number of distinct ways you can arrange the adapters to
// connect the charging outlet to your device?

public class Solution2 extends Solution1 {
    public static void main(String[] args) {
        final List<Integer> input = getInput();
        System.out.println(countArrangements(input, new HashMap<>(), input.get(0), input.get(input.size() - 1)));
    }

    private static long countArrangements(final List<Integer> input, final Map<Integer, Long> memoization,
            final int CURRENT, final int MAX) {
        if (CURRENT + 3 == MAX) // our device
            return 1;
        // return stored value if we had already visited
        else if (memoization.containsKey(CURRENT))
            return memoization.get(CURRENT);

        long pathCounter = 0;
        if (input.contains(CURRENT + 1))
            pathCounter += countArrangements(input, memoization, CURRENT + 1, MAX);
        if (input.contains(CURRENT + 2))
            pathCounter += countArrangements(input, memoization, CURRENT + 2, MAX);
        if (input.contains(CURRENT + 3))
            pathCounter += countArrangements(input, memoization, CURRENT + 3, MAX);
        // per each recursion return, overwrite the existing counter with the new one
        memoization.put(CURRENT, pathCounter);
        return pathCounter;
    }
}