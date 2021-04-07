import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// --- Part Two ---
// It's getting pretty expensive to fly these days - not because of ticket
// prices, but because of the ridiculous number of bags you need to buy!

// Consider again your shiny gold bag and the rules from the above example:

// faded blue bags contain 0 other bags.
// dotted black bags contain 0 other bags.
// vibrant plum bags contain 11 other bags: 5 faded blue bags and 6 dotted black
// bags.
// dark olive bags contain 7 other bags: 3 faded blue bags and 4 dotted black
// bags.
// So, a single shiny gold bag must contain 1 dark olive bag (and the 7 bags
// within it) plus 2 vibrant plum bags (and the 11 bags within each of those): 1
// + 1*7 + 2 + 2*11 = 32 bags!

// Of course, the actual rules have a small chance of going several levels
// deeper than this example; be sure to count all of the bags, even if the
// nesting becomes topologically impractical!

// Here's another example:

// shiny gold bags contain 2 dark red bags.
// dark red bags contain 2 dark orange bags.
// dark orange bags contain 2 dark yellow bags.
// dark yellow bags contain 2 dark green bags.
// dark green bags contain 2 dark blue bags.
// dark blue bags contain 2 dark violet bags.
// dark violet bags contain no other bags.
// In this example, a single shiny gold bag must contain 126 other bags.

// How many individual bags are required inside your single shiny gold bag?

public class Solution2 {
    public static void main(String[] args) {
        List<String> input = getInput();
        System.out.println(getBags(input, "shiny gold"));
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

    private static int getBags(final List<String> input, final String KEYWORD) {
        return countBags(getMap(input), KEYWORD) - 1;
    }

    private static Map<String, Map<String, Integer>> getMap(final List<String> input) {
        Map<String, Map<String, Integer>> map = new HashMap<>();
        input.forEach(line -> {
            // group 1 -> Number
            // group 2 -> Bag color
            // group 3 -> "bag(s)" (we use this just as a matcher finisher)
            Matcher matcher = Pattern.compile("(\\d+) (.+?) (bags?)").matcher(line);
            while (matcher.find()) {
                // line.trim().split(" bag")[0] -> parent node
                // map.computeIfAbsent(key, k -> new HashSet<V>()).add(v); [Documentation]
                map.computeIfAbsent(line.trim().split(" bag")[0], match -> new HashMap<>()).put(matcher.group(2),
                        Integer.valueOf(matcher.group(1)));
            }
        });
        return map;
    }

    private static int countBags(final Map<String, Map<String, Integer>> map, final String KEYWORD) {
        // map access to collection of values per key and recursive child count
        return map.getOrDefault(KEYWORD, Map.of()).entrySet().stream()
                .mapToInt(child -> child.getValue() * countBags(map, child.getKey())).sum() + 1;
    }
}