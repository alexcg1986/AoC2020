import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

// --- Part Two ---
// The line is moving more quickly now, but you overhear airport security
// talking about how passports with invalid data are getting through. Better add
// some data validation, quick!

// You can continue to ignore the cid field, but each other field has strict
// rules about what values are valid for automatic validation:

// byr (Birth Year) - four digits; at least 1920 and at most 2002.
// iyr (Issue Year) - four digits; at least 2010 and at most 2020.
// eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
// hgt (Height) - a number followed by either cm or in:
// If cm, the number must be at least 150 and at most 193.
// If in, the number must be at least 59 and at most 76.
// hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
// ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
// pid (Passport ID) - a nine-digit number, including leading zeroes.
// cid (Country ID) - ignored, missing or not.
// Your job is to count the passports where all required fields are both present
// and valid according to the above rules. Here are some example values:

// byr valid: 2002
// byr invalid: 2003

// hgt valid: 60in
// hgt valid: 190cm
// hgt invalid: 190in
// hgt invalid: 190

// hcl valid: #123abc
// hcl invalid: #123abz
// hcl invalid: 123abc

// ecl valid: brn
// ecl invalid: wat

// pid valid: 000000001
// pid invalid: 0123456789
// Here are some invalid passports:

// eyr:1972 cid:100
// hcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926

// iyr:2019
// hcl:#602927 eyr:1967 hgt:170cm
// ecl:grn pid:012533040 byr:1946

// hcl:dab227 iyr:2012
// ecl:brn hgt:182cm pid:021572410 eyr:2020 byr:1992 cid:277

// hgt:59cm ecl:zzz
// eyr:2038 hcl:74454a iyr:2023
// pid:3556412378 byr:2007
// Here are some valid passports:

// pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980
// hcl:#623a2f

// eyr:2029 ecl:blu cid:129 byr:1989
// iyr:2014 pid:896056539 hcl:#a97842 hgt:165cm

// hcl:#888785
// hgt:164cm byr:2001 iyr:2015 cid:88
// pid:545766238 ecl:hzl
// eyr:2022

// iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719
// Count the number of valid passports - those that have all required fields and
// valid values. Continue to treat cid as optional. In your batch file, how many
// passports are valid?

public class Solution2 {
    public static void main(String[] args) {
        List<List<String>> input = getInput();
        System.out.println(countValid(input));
    }

    private static List<List<String>> getInput() {
        List<String> fields = new ArrayList<>();
        List<List<String>> input = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            br.lines().forEach(line -> {
                if (!line.isEmpty())
                    fields.addAll(Arrays.asList(line.trim().split("[ ]+"))); // populate fields with values
                else {
                    input.add(new ArrayList<>(fields)); // add those fields to the input if the line is empty
                    fields.clear();
                }
            });
            if (!fields.isEmpty()) // check if there were values left after EOF
                input.add(fields);
        } catch (Exception e) {
            System.out.println(e);
        }
        return input;
    }

    private static int countValid(List<List<String>> input) {
        int validPassports = 0;
        for (List<String> fields : input) {
            if (fields.size() >= 7)
                if (checkValid(fields))
                    validPassports++;
        }
        return validPassports;
    }

    private static boolean checkValid(List<String> fields) {
        final List<String> validValues = Arrays.asList("pid:", "ecl:", "eyr:", "iyr:", "hgt:", "hcl:", "byr:");
        boolean control;
        for (String param : validValues) {
            control = false;
            for (String field : fields) {
                if (field.contains(param) && checkField(field, param)) {
                    control = true;
                    break;
                }
            }
            if (!control)
                return false;
        }
        return true;
    }

    private static boolean checkField(String field, String param) {
        // We need to filter the input field because the pattern is strict ^ & $
        final String FIELD = field.split(param)[1];
        switch (param) {
        case "pid:":
            return Pattern.compile("(?:^[0-9]{9}$)").matcher(FIELD).find();
        case "ecl:":
            return Pattern.compile("(?:^(amb|blu|brn|gry|grn|hzl|oth)$)").matcher(FIELD).find();
        case "eyr:":
            return Pattern.compile("(?:^(202[0-9]|2030)$)").matcher(FIELD).find();
        case "iyr:":
            return Pattern.compile("(?:^(201[0-9]|2020)$)").matcher(FIELD).find();
        case "hgt:":
            return Pattern.compile("(?:^((1[5-8][0-9]|19[0-3])cm|(59|6[0-9]|7[0-6])in)$)").matcher(FIELD).find();
        case "hcl:":
            return Pattern.compile("(?:^(#[0-9a-f]{6})$)").matcher(FIELD).find();
        case "byr:":
            return Pattern.compile("(?:^(19[2-9][0-9]|200[0-2])$)").matcher(FIELD).find();
        default:
            return false;
        }
    }
}