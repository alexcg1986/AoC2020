import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

// --- Part Two ---
// As you finish the last group's customs declaration, you notice that you
// misread one word in the instructions:

// You don't need to identify the questions to which anyone answered "yes"; you
// need to identify the questions to which everyone answered "yes"!

// Using the same example as above:

// abc

// a
// b
// c

// ab
// ac

// a
// a
// a
// a

// b
// This list represents answers from five groups:

// In the first group, everyone (all 1 person) answered "yes" to 3 questions: a,
// b, and c.
// In the second group, there is no question to which everyone answered "yes".
// In the third group, everyone answered yes to only 1 question, a. Since some
// people did not answer "yes" to b or c, they don't count.
// In the fourth group, everyone answered yes to only 1 question, a.
// In the fifth group, everyone (all 1 person) answered "yes" to 1 question, b.
// In this example, the sum of these counts is 3 + 0 + 1 + 1 + 1 = 6.

// For each group, count the number of questions to which everyone answered
// "yes". What is the sum of those counts?

public class Solution2 {
    public static void main(String[] args) {
        final List<HashSet<Character>> input = getInput();
        System.out.println(totalCount(input));
    }

    private static List<HashSet<Character>> getInput() {
        // we use HashSet again to also control duplicate char values per line
        final List<HashSet<Character>> input = new ArrayList<>();
        HashSet<Character> choices = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            boolean control = true;
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    if (control) { // control if 'choices' is empty because no matching characters left
                        if (choices.isEmpty())
                            for (int i = 0; i < line.length(); i++)
                                choices.add(line.charAt(i));
                        else {
                            HashSet<Character> temp = new HashSet<>();
                            for (int i = 0; i < line.length(); i++)
                                temp.add(line.charAt(i));
                            // this helper is to allow us to iterate over our choices and bypass
                            // ConcurrentModificationException
                            HashSet<Character> choicesHelper = new HashSet<>(choices);
                            choicesHelper.forEach(c -> {
                                if (!temp.contains(c))
                                    choices.remove(c);
                            });
                            if (choices.isEmpty())
                                control = false; // we control 'choices' emptiness
                        }
                    }
                } else {
                    if (!choices.isEmpty()) {
                        input.add(new HashSet<>(choices));
                        choices.clear();
                    }
                    control = true;
                }
            }
            if (!choices.isEmpty()) // add values left after EOF
                input.add(choices);
        } catch (Exception e) {
            System.out.println(e);
        }
        return input;
    }

    private static int totalCount(List<HashSet<Character>> input) {
        int totalCount = 0;
        for (HashSet<Character> choices : input)
            totalCount += choices.size();
        return totalCount;
    }
}