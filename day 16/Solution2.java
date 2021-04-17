import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// --- Part Two ---
// Now that you've identified which tickets contain invalid values, discard
// those tickets entirely. Use the remaining valid tickets to determine which
// field is which.

// Using the valid ranges for each field, determine what fieldOrder the fields appear
// on the tickets. The fieldOrder is consistent between all tickets: if seat is the
// third field, it is the third field on every ticket, including your ticket.

// For example, suppose you have the following notes:

// class: 0-1 or 4-19
// row: 0-5 or 8-19
// seat: 0-13 or 16-19

// your ticket:
// 11,12,13

// nearby tickets:
// 3,9,18
// 15,1,5
// 5,14,9
// Based on the nearby tickets in the above example, the first position must be
// row, the second position must be class, and the third position must be seat;
// you can conclude that in your ticket, class is 12, row is 11, and seat is 13.

// Once you work out which field is which, look for the six fields on your
// ticket that start with the word departure. What do you get if you multiply
// those six values together?

public class Solution2 {
    public static void main(String[] args) {
        System.out.println(getValues(getInput()));
    }

    private static List<List<String>> getInput() {
        List<List<String>> input = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            input = br.lines().map(line -> {
                List<String> params = new ArrayList<>();
                if (line.contains("-")) {
                    params = Arrays.stream(line.trim().split("(?:[ ,:-]|or )+")).collect(Collectors.toList());
                }
                final List<String> ticket = new ArrayList<>();
                final Matcher ticketsMatcher = Pattern.compile("(\\d+)(?:,+)(\\d+)").matcher(line);
                while (ticketsMatcher.find()) {
                    ticket.addAll(Arrays.asList(ticketsMatcher.group(1), ticketsMatcher.group(2)));
                }
                return params.size() > 0 ? params : ticket.size() > 0 ? ticket : null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println(e);
        }
        return input;
    }

    private static long getValues(final List<List<String>> input) {
        final List<List<String>> params = input.stream().filter(list -> list.size() <= 6).collect(Collectors.toList());
        final List<List<Integer>> tickets = input.stream().filter(list -> list.size() > 6)
                .map(ticket -> ticket.stream().map(Integer::valueOf).collect(Collectors.toList()))
                .collect(Collectors.toList());
        boolean valid = false;

        for (int i = 0; i < tickets.size(); ++i) { // tickets
            for (Integer fields : tickets.get(i)) { // fields
                for (List<String> parameters : params) { // params
                    final int LENGTH = parameters.size();
                    if (Integer.valueOf(parameters.get(LENGTH - 4)) <= fields
                            && fields <= Integer.valueOf(parameters.get(LENGTH - 3))
                            || Integer.valueOf(parameters.get(LENGTH - 2)) <= fields
                                    && fields <= Integer.valueOf(parameters.get(LENGTH - 1))) {
                        valid = !valid;
                        break;
                    }
                }
                if (valid)
                    valid = !valid;
                else {
                    tickets.remove(i--); // compensate the ticket rearrange for the iteration
                    break;
                }
            }
        } // invalid tickets removed

        final int FIELD_SIZE = tickets.get(0).size();
        String tempFieldName = "";
        int tempPosition = Integer.MIN_VALUE;
        Map<String, Integer> fieldOrder = new HashMap<>();
        HashSet<Integer> filtered = new HashSet<>();
        valid = true;

        do {
            for (int i = 0; i < params.size(); ++i) { // params
                for (int k = 0; k < FIELD_SIZE; ++k) { // fields
                    if (filtered.contains(k)) // we don't check known positions again
                        continue;
                    for (int j = 0; j < tickets.size(); ++j) { // tickets
                        final int LENGTH = params.get(i).size();
                        if (tickets.get(j).get(k) < Integer.valueOf(params.get(i).get(LENGTH - 4))
                                || Integer.valueOf(params.get(i).get(LENGTH - 3)) < tickets.get(j).get(k)
                                        && tickets.get(j).get(k) < Integer.valueOf(params.get(i).get(LENGTH - 2))
                                || Integer.valueOf(params.get(i).get(LENGTH - 1)) < tickets.get(j).get(k)) {
                            valid = false; // if out of boundaries
                            break;
                        }
                    }
                    if (!valid)
                        valid = true;
                    else {
                        if (tempFieldName.isEmpty()) { // check uniqueness of the boundaries
                            tempFieldName = params.get(i).size() == 6
                                    ? params.get(i).get(0) + " " + params.get(i).get(1)
                                    : params.get(i).get(0);
                            tempPosition = k;
                        } else { // discard if repeated
                            tempFieldName = "";
                            tempPosition = Integer.MIN_VALUE;
                            break;
                        }
                    }
                }
                if (!tempFieldName.isEmpty()) { // if only one field is satisfied
                    fieldOrder.put(tempFieldName, tempPosition);
                    params.remove(i--); // remove matched param and compensate rearrange
                    filtered.add(tempPosition); // we don't check this position again
                    tempFieldName = "";
                    tempPosition = Integer.MIN_VALUE;
                }
            }
        } while (params.size() != 0);

        return calculateValue(fieldOrder, tickets.get(0));
    }

    private static long calculateValue(final Map<String, Integer> fieldOrder, final List<Integer> ticket) {
        return fieldOrder.keySet().stream().filter(key -> key.contains("departure"))
                .map(key -> Long.valueOf(ticket.get(fieldOrder.get(key)))).reduce(1L, Math::multiplyExact);
    }
}