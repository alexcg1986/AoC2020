import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

// --- Part Two ---
// Time to check the rest of the slopes - you need to minimize the probability
// of a sudden arboreal stop, after all.

// Determine the number of trees you would encounter if, for each of the
// following slopes, you start at the top-left corner and traverse the map all
// the way to the bottom:

// Right 1, down 1.
// Right 3, down 1. (This is the slope you already checked.)
// Right 5, down 1.
// Right 7, down 1.
// Right 1, down 2.
// In the above example, these slopes would find 2, 7, 3, 4, and 2 tree(s)
// respectively; multiplied together, these produce the answer 336.

// What do you get if you multiply together the number of trees encountered on
// each of the listed slopes?

public class Solution2 {

    public static void main(String[] args) {
        List<String> input = getInput();
        System.out.println(calculate(getTrees(input)));
    }

    private static List<String> getInput() {
        List<String> input = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            br.lines().forEach(line -> input.add(line));
            br.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return input;
    }

    private static int[] getTrees(List<String> input) {
        final int[] arrTrees = { 0, 0, 0, 0, 0 };
        final int[] arrColumns = { 0, 0, 0, 0 };
        final int WIDTH = input.get(0).length();

        for (int row = 1; row < input.size(); row++) {
            arrColumns[0] = arrColumns[0] + 1 <= WIDTH - 1 ? arrColumns[0] + 1 : arrColumns[0] + 1 - WIDTH;
            if (input.get(row).charAt(arrColumns[0]) == '#')
                arrTrees[0]++;

            arrColumns[1] = arrColumns[1] + 3 <= WIDTH - 1 ? arrColumns[1] + 3 : arrColumns[1] + 3 - WIDTH;
            if (input.get(row).charAt(arrColumns[1]) == '#')
                arrTrees[1]++;

            arrColumns[2] = arrColumns[2] + 5 <= WIDTH - 1 ? arrColumns[2] + 5 : arrColumns[2] + 5 - WIDTH;
            if (input.get(row).charAt(arrColumns[2]) == '#')
                arrTrees[2]++;

            arrColumns[3] = arrColumns[3] + 7 <= WIDTH - 1 ? arrColumns[3] + 7 : arrColumns[3] + 7 - WIDTH;
            if (input.get(row).charAt(arrColumns[3]) == '#')
                arrTrees[3]++;

            // We can use the column value of the first slope and the relative to the row as
            // double of this value in the same iteration
            if (row < input.size() - row)
                if (input.get(row + row).charAt(arrColumns[0]) == '#')
                    arrTrees[4]++;
        }
        return arrTrees;
    }

    private static int calculate(int[] arrTrees) {
        int output = 1;
        for (int trees : arrTrees) {
            output *= trees;
        }
        return output;
    }
}