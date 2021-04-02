import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

// --- Day 2: Password Philosophy ---
// Your flight departs in a few days from the coastal airport; the easiest way down to the coast from here is via toboggan.

// The shopkeeper at the North Pole Toboggan Rental Shop is having a bad day. "Something's wrong with our computers; we can't log in!"
// You ask if you can take a look.

// Their password database seems to be a little corrupted: some of the passwords wouldn't have been allowed by the 
// Official Toboggan Corporate Policy that was in effect when they were chosen.

// To try to debug the problem, they have created a list (your puzzle input) of passwords (according to the corrupted database) 
// and the corporate policy when that password was set.

// For example, suppose you have the following list:

// 1-3 a: abcde
// 1-3 b: cdefg
// 2-9 c: ccccccccc
// Each line gives the password policy and then the password. The password policy indicates the lowest and highest number of times a given
// letter must appear for the password to be valid. For example, 1-3 a means that the password must contain a at least 1 time and at most 3 times.

// In the above example, 2 passwords are valid. The middle password, cdefg, is not; it contains no instances of b, but needs at least 1.
// The first and third passwords are valid: they contain one a or nine c, both within the limits of their respective policies.

// How many passwords are valid according to their policies?

class Solution1 {

    public static void main(String[] args) {
        final List<String> input = getInput();
        int validPasswords = 0;
        for (String line : input) {
            final String[] parameters = lineBreakdown(line);
            if (checkValues(parameters))
                validPasswords++;
        }
        System.out.println("There are: " + validPasswords + " valid passwords.");
    }

    private static List<String> getInput() {
        final List<String> input = new ArrayList<>();
        try (final BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            br.lines().forEach(line -> input.add(line));
            br.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return input;
    }

    private static String[] lineBreakdown(String line) {
        final String[] parameters = line.trim().split("[-: ]+");
        return parameters;
    }

    private static boolean checkValues(String[] parameters) {
        return !parameters[3].contains(parameters[2]) ? false : checkMatches(parameters);
    }

    private static boolean checkMatches(String[] parameters) {
        int matches = 0;
        for (int i = 0; i < parameters[3].length(); i++)
            if (parameters[3].charAt(i) == parameters[2].charAt(0))
                matches++;
        return matches >= Integer.valueOf(parameters[0]) && matches <= Integer.valueOf(parameters[1]);
    }
}