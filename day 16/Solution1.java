import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// --- Day 16: Ticket Translation ---
// As you're walking to yet another connecting flight, you realize that one of
// the legs of your re-routed trip coming up is on a high-speed train. However,
// the train ticket you were given is in a language you don't understand. You
// should probably figure out what it says before you get to the train station
// after the next flight.

// Unfortunately, you can't actually read the words on the ticket. You can,
// however, read the numbers, and so you figure out the tickets these tickets
// must have and the valid ranges for tickets in those tickets.

// You collect the rules for ticket tickets, the numbers on your ticket, and the
// numbers on other nearby tickets for the same train service (via the airport
// security cameras) together into a single document you can reference (your
// puzzle input).

// The rules for ticket tickets specify a list of tickets that exist somewhere on
// the ticket and the valid ranges of tickets for each field. For example, a rule
// like class: 1-3 or 5-7 means that one of the tickets in every ticket is named
// class and can be any value in the ranges 1-3 or 5-7 (inclusive, such that 3
// and 5 are both valid in this field, but 4 is not).

// Each ticket is represented by a single line of comma-separated tickets. The
// tickets are the numbers on the ticket in the order they appear; every ticket
// has the same format. For example, consider this ticket:

// .--------------------------------------------------------.
// | ????: 101 ?????: 102 ??????????: 103 ???: 104 |
// | |
// | ??: 301 ??: 302 ???????: 303 ??????? |
// | ??: 401 ??: 402 ???? ????: 403 ????????? |
// '--------------------------------------------------------'
// Here, ? represents text in a language you don't understand. This ticket might
// be represented as 101,102,103,104,301,302,303,401,402,403; of course, the
// actual train tickets you're looking at are much more complicated. In any
// case, you've extracted just the numbers in such a way that the first number
// is always the same specific field, the second number is always a different
// specific field, and so on - you just don't know what each position actually
// means!

// Start by determining which tickets are completely invalid; these are tickets
// that contain tickets which aren't valid for any field. Ignore your ticket for
// now.

// For example, suppose you have the following notes:

// class: 1-3 or 5-7
// row: 6-11 or 33-44
// seat: 13-40 or 45-50

// your ticket:
// 7,1,14

// nearby tickets:
// 7,3,47
// 40,4,50
// 55,2,20
// 38,6,12
// It doesn't matter which position corresponds to which field; you can identify
// invalid nearby tickets by considering only whether tickets contain tickets
// that are not valid for any field. In this example, the tickets on the first
// nearby ticket are all valid for at least one field. This is not true of the
// other three nearby tickets: the tickets 4, 55, and 12 are are not valid for
// any field. Adding together all of the invalid tickets produces your ticket
// scanning error rate: 4 + 55 + 12 = 71.

// Consider the validity of the nearby tickets you scanned. What is your ticket
// scanning error rate?

// (?:(\d+)(?:-)(\d+)) (?:or) (?:(\d+)(?:-)(\d+)) params (group 1 to 4 in order)
// (\\d+)(?:,+)(\\d+) tickets (10 times of group 1 and group 2)

public class Solution1 {
    public static void main(String[] args) {
        System.out.println(getErrorRate(getInput()));
    }

    private static List<List<Integer>> getInput() {
        List<List<Integer>> input = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            input = br.lines().map(line -> {
                final List<Integer> params = new ArrayList<>();
                final Matcher paramsMatcher = Pattern.compile("(?:(\\d+)(?:-)(\\d+)) (?:or) (?:(\\d+)(?:-)(\\d+))")
                        .matcher(line);
                while (paramsMatcher.find()) {
                    params.addAll(Arrays.asList(Integer.valueOf(paramsMatcher.group(1)),
                            Integer.valueOf(paramsMatcher.group(2)), Integer.valueOf(paramsMatcher.group(3)),
                            Integer.valueOf(paramsMatcher.group(4))));
                }
                final List<Integer> ticket = new ArrayList<>();
                final Matcher ticketsMatcher = Pattern.compile("(\\d+)(?:,+)(\\d+)").matcher(line);
                while (ticketsMatcher.find()) {
                    ticket.addAll(Arrays.asList(Integer.valueOf(ticketsMatcher.group(1)),
                            Integer.valueOf(ticketsMatcher.group(2))));
                }
                return params.size() > 0 ? params : ticket.size() > 0 ? ticket : null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println(e);
        }
        return input;
    }

    private static long getErrorRate(final List<List<Integer>> input) {
        final List<List<Integer>> params = input.stream().filter(list -> list.size() <= 4).collect(Collectors.toList());
        final List<List<Integer>> tickets = input.stream().filter(list -> list.size() > 4).collect(Collectors.toList());
        final List<Integer> invalids = new ArrayList<>();
        boolean valid = false;

        for (List<Integer> ticket : tickets)
            for (Integer value : ticket) {
                for (List<Integer> parameters : params)
                    if (parameters.get(0) <= value && value <= parameters.get(1)
                            || parameters.get(2) <= value && value <= parameters.get(3)) {
                        valid = !valid;
                        break;
                    }
                if (!valid)
                    invalids.add(value);
                else
                    valid = !valid;
            }

        return calculateErrorRate(invalids);
    }

    private static long calculateErrorRate(final List<Integer> invalids) {
        return invalids.stream().reduce(0, Integer::sum);
    }
}