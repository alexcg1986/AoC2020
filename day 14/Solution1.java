import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// --- Day 14: Docking Data ---
// As your ferry approaches the sea port, the captain asks for your help again.
// The computer system that runs this port isn't compatible with the docking
// program on the ferry, so the docking parameters aren't being correctly
// initialized in the docking program's memory.

// After a brief inspection, you discover that the sea port's computer system
// uses a strange bitmask system in its initialization program. Although you
// don't have the correct decoder chip handy, you can emulate it in software!

// The initialization program (your puzzle input) can either update the bitmask
// or write a value to memory. Values and memory addresses are both 36-bit
// unsigned integers. For example, ignoring bitmasks for a moment, a line like
// mem[8] = 11 would write the value 11 to memory address 8.

// The bitmask is always given as a string of 36 bits, written with the most
// significant bit (representing 2^35) on the left and the least significant bit
// (2^0, that is, the 1s bit) on the right. The current bitmask is applied to
// values immediately before they are written to memory: a 0 or 1 overwrites the
// corresponding bit in the value, while an X leaves the bit in the value
// unchanged.

// For example, consider the following program:

// mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
// mem[8] = 11
// mem[7] = 101
// mem[8] = 0
// This program starts by specifying a bitmask (mask = ....). The mask it
// specifies will overwrite two bits in every written value: the 2s bit is
// overwritten with 0, and the 64s bit is overwritten with 1.

// The program then attempts to write the value 11 to memory address 8. By
// expanding everything out to individual bits, the mask is applied as follows:

// value: 000000000000000000000000000000001011 (decimal 11)
// mask: XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
// result: 000000000000000000000000000001001001 (decimal 73)
// So, because of the mask, the value 73 is written to memory address 8 instead.
// Then, the program tries to write 101 to address 7:

// value: 000000000000000000000000000001100101 (decimal 101)
// mask: XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
// result: 000000000000000000000000000001100101 (decimal 101)
// This time, the mask has no effect, as the bits it overwrote were already the
// values the mask tried to set. Finally, the program tries to write 0 to
// address 8:

// value: 000000000000000000000000000000000000 (decimal 0)
// mask: XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
// result: 000000000000000000000000000001000000 (decimal 64)
// 64 is written to address 8 instead, overwriting the value that was there
// previously.

// To initialize your ferry's docking program, you need the sum of all values
// left in memory after the initialization program completes. (The entire 36-bit
// address space begins initialized to the value 0 at every address.) In the
// above example, only two values in memory are not zero - 101 (at address 7)
// and 64 (at address 8) - producing a sum of 165.

// Execute the initialization program. What is the sum of all values left in
// memory after it completes? (Do not truncate the sum to 36 bits.)

public class Solution1 {
    public static void main(String[] args) {
        System.out.println(getSum(getInput()));
    }

    static List<List<String>> getInput() {
        List<List<String>> input = new ArrayList<>();
        List<String> mems = new ArrayList<>();
        String mask = "";
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            while ((line = br.readLine()) != null) {
                Matcher matcher = Pattern.compile("([0-9X]+)").matcher(line);
                while (matcher.find()) {
                    if (line.contains("mask"))
                        mask = matcher.group(1);
                    else
                        mems.add(matcher.group(1));
                }
                if (mems.size() > 1) { // we add mask to each mem instruction
                    mems.add(mask);
                    input.add(new ArrayList<>(mems));
                    mems.clear();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return input;
    }

    private static BigInteger getSum(List<List<String>> input) {
        Map<String, BitSet> mems = new HashMap<>();
        for (List<String> instruction : input) {
            final String MEM_POSITION = instruction.get(0);
            final String VALUE = Integer.toBinaryString(Integer.valueOf(instruction.get(1)));
            final String MASK = instruction.get(2);
            final BitSet value = new BitSet();

            for (int i = 0; i < MASK.length(); ++i) {
                switch (MASK.charAt(i)) { // asign values
                case '1' -> value.set(MASK.length() - 1 - i); // compensate binary weights
                case 'X' -> {
                    // compensate VALUE offset and binary weights
                    if (i >= MASK.length() - VALUE.length()
                            && VALUE.charAt(i - (MASK.length() - VALUE.length())) == '1')
                        value.set(MASK.length() - 1 - i);
                }
                }
            }
            mems.put(MEM_POSITION, value);
        }

        return calculateValue(mems);
    }

    private static BigInteger calculateValue(final Map<String, BitSet> mems) {
        return mems.keySet().stream().map(key -> {
            final BitSet VALUE = mems.get(key);
            BigInteger sum = BigInteger.ZERO;
            for (int i = 0; i < VALUE.length(); ++i) {
                if (VALUE.get(i))
                    // we shift values based on binary weight and perform logical '+=' <=> '|='
                    // we need BigInteger to handle big values
                    sum = sum.add(BigInteger.ONE.shiftLeft(i));
            }
            return sum;
        }).reduce(BigInteger.ZERO, BigInteger::add);
    }
}