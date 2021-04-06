import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;

// --- Part Two ---
// The Elves in accounting are thankful for your help; one of them even offers you a starfish coin they had left over from a past vacation.
// They offer you a second one if you can find three numbers in your expense report that meet the same criteria.
// Using the above example again, the three entries that sum to 2020 are 979, 366, and 675. Multiplying them together produces the answer, 241861950.
// In your expense report, what is the product of the three entries that sum to 2020?

class Solution2 {
    public static void main(String[] args) {
        LinkedList<Integer> values = getValues();
        while (!checkValues(values) && values.size() > 2)
            values.pollFirst();
    }

    private static LinkedList<Integer> getValues() {
        LinkedList<Integer> linkedList = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            // populate linkedList
            br.lines().forEach(line -> linkedList.push(Integer.valueOf(line)));
            br.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return linkedList;
    }

    // iterate over the linkedList and check conditions
    private static boolean checkValues(LinkedList<Integer> linkedList) {
        for (int i = 1; i < linkedList.size(); i++) {
            int temporal;
            if ((temporal = linkedList.get(i) + linkedList.getFirst()) < 2020) {
                for (int j = i + 1; j < linkedList.size(); j++) {
                    if (temporal + linkedList.get(j) == 2020) {
                        System.out.println(
                                "The answer is: " + linkedList.get(j) * linkedList.get(i) * linkedList.getFirst());
                        return true;
                    }
                }
            }
        }
        return false;
    }
}