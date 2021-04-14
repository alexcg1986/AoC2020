import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

// --- Part Two ---
// For some reason, the sea port's computer system still can't communicate with
// your ferry's docking program. It must be using version 2 of the decoder chip!

// A version 2 decoder chip doesn't modify the values being written at all.
// Instead, it acts as a memory address decoder. Immediately before a value is
// written to memory, each bit in the bitmask modifies the corresponding bit of
// the destination memory address in the following way:

// If the bitmask bit is 0, the corresponding memory address bit is unchanged.
// If the bitmask bit is 1, the corresponding memory address bit is overwritten
// with 1.
// If the bitmask bit is X, the corresponding memory address bit is floating.
// A floating bit is not connected to anything and instead fluctuates
// unpredictably. In practice, this means the floating bits will take on all
// possible values, potentially causing many memory addresses to be written all
// at once!

// For example, consider the following program:

// mask = 000000000000000000000000000000X1001X
// mem[42] = 100
// mask = 00000000000000000000000000000000X0XX
// mem[26] = 1
// When this program goes to write to memory address 42, it first applies the
// bitmask:

// address: 000000000000000000000000000000101010 (decimal 42)
// mask: 000000000000000000000000000000X1001X
// result: 000000000000000000000000000000X1101X
// After applying the mask, four bits are overwritten, three of which are
// different, and two of which are floating. Floating bits take on every
// possible combination of values; with two floating bits, four actual memory
// addresses are written:

// 000000000000000000000000000000011010 (decimal 26)
// 000000000000000000000000000000011011 (decimal 27)
// 000000000000000000000000000000111010 (decimal 58)
// 000000000000000000000000000000111011 (decimal 59)
// Next, the program is about to write to memory address 26 with a different
// bitmask:

// address: 000000000000000000000000000000011010 (decimal 26)
// mask: 00000000000000000000000000000000X0XX
// result: 00000000000000000000000000000001X0XX
// This results in an address with three floating bits, causing writes to eight
// memory addresses:

// 000000000000000000000000000000010000 (decimal 16)
// 000000000000000000000000000000010001 (decimal 17)
// 000000000000000000000000000000010010 (decimal 18)
// 000000000000000000000000000000010011 (decimal 19)
// 000000000000000000000000000000011000 (decimal 24)
// 000000000000000000000000000000011001 (decimal 25)
// 000000000000000000000000000000011010 (decimal 26)
// 000000000000000000000000000000011011 (decimal 27)
// The entire 36-bit address space still begins initialized to the value 0 at
// every address, and you still need the sum of all values left in memory at the
// end of the program. In this example, the sum is 208.

// Execute the initialization program using an emulator for a version 2 decoder
// chip. What is the sum of all values left in memory after it completes?

public class Solution2 extends Solution1 {
    public static void main(String[] args) {
        System.out.println(getSum(getInput()));
    }

    private static Long getSum(List<List<String>> input) {
        Map<BitSet, Long> mems = new HashMap<>();
        for (List<String> instruction : input) {
            final String MEM_POSITION = Integer.toBinaryString(Integer.valueOf(instruction.get(0)));
            final long VALUE = Long.valueOf(instruction.get(1));
            final String MASK = instruction.get(2);
            final HashSet<BitSet> memPos = new HashSet<BitSet>() {
                private static final long serialVersionUID = 1L;
                {
                    add(new BitSet());
                }
            };

            for (int i = 0; i < MASK.length(); ++i) {
                switch (MASK.charAt(i)) { // asign values
                case '1' -> { // compensate binary weights
                    for (BitSet position : memPos)
                        position.set(MASK.length() - 1 - i);
                }
                case '0' -> { // compensate VALUE offset and binary weights
                    if (i >= MASK.length() - MEM_POSITION.length()
                            && MEM_POSITION.charAt(i - (MASK.length() - MEM_POSITION.length())) == '1')
                        for (BitSet position : memPos)
                            position.set(MASK.length() - 1 - i);
                }
                case 'X' -> {
                    HashSet<BitSet> helper = new HashSet<>(memPos); // ConcurrentModificationException
                    for (BitSet position : helper) {
                        BitSet temp = (BitSet) position.clone(); // create 2 childs
                        temp.set(MASK.length() - 1 - i); // 1
                        position.set(MASK.length() - 1 - i, false); // 0
                        memPos.add(temp);
                        memPos.add(position);
                    }
                }
                }
            }
            memPos.forEach(position -> mems.put(position, VALUE));
        }

        return calculateValue(mems);
    }

    private static Long calculateValue(final Map<BitSet, Long> mems) {
        return mems.keySet().stream().map(key -> mems.get(key)).reduce(0L, Long::sum);
    }
}