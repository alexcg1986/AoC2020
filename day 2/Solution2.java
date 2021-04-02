import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

// --- Part Two ---
// While it appears you validated the passwords correctly, they don't seem to be what the Official Toboggan Corporate Authentication System is expecting.

// The shopkeeper suddenly realizes that he just accidentally explained the password policy rules from his old job at the sled rental place down the street!
// The Official Toboggan Corporate Policy actually works a little differently.

// Each policy actually describes two positions in the password, where 1 means the first character, 2 means the second character, and so on. 
// (Be careful; Toboggan Corporate Policies have no concept of "index zero"!) Exactly one of these positions must contain the given letter. 
// Other occurrences of the letter are irrelevant for the purposes of policy enforcement.

// Given the same example list from above:

// 1-3 a: abcde is valid: position 1 contains a and position 3 does not.
// 1-3 b: cdefg is invalid: neither position 1 nor position 3 contains b.
// 2-9 c: ccccccccc is invalid: both position 2 and position 9 contain c.
// How many passwords are valid according to the new interpretation of the policies?

public class Solution2 {
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
        return checkMatch(parameters[3], parameters[2].charAt(0), Integer.valueOf(parameters[0]) - 1)
                ^ checkMatch(parameters[3], parameters[2].charAt(0), Integer.valueOf(parameters[1]) - 1);
    }

    private static boolean checkMatch(String string, Character character, Integer index) {
        return string.charAt(index) == character;
    }
}
