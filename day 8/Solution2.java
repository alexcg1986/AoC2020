import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

// --- Part Two ---
// After some careful analysis, you believe that exactly one instruction is
// corrupted.

// Somewhere in the program, either a jmp is supposed to be a nop, or a nop is
// supposed to be a jmp. (No acc instructions were harmed in the corruption of
// this boot code.)

// The program is supposed to terminate by attempting to execute an instruction
// immediately after the last instruction in the file. By changing exactly one
// jmp or nop, you can repair the boot code and make it terminate correctly.

// For example, consider the same program from above:

// nop +0
// acc +1
// jmp +4
// acc +3
// jmp -3
// acc -99
// acc +1
// jmp -4
// acc +6
// If you change the first instruction from nop +0 to jmp +0, it would create a
// single-instruction infinite loop, never leaving that instruction. If you
// change almost any of the jmp instructions, the program will still eventually
// find another jmp instruction and loop forever.

// However, if you change the second-to-last instruction (from jmp -4 to nop
// -4), the program terminates! The instructions are visited in this order:

// nop +0 | 1
// acc +1 | 2
// jmp +4 | 3
// acc +3 |
// jmp -3 |
// acc -99 |
// acc +1 | 4
// nop -4 | 5
// acc +6 | 6
// After the last instruction (acc +6), the program terminates by attempting to
// run the instruction below the last instruction in the file. With this change,
// after the program terminates, the accumulator contains the value 8 (acc +1,
// acc +1, acc +6).

// Fix the program so that it terminates normally by changing exactly one jmp
// (to nop) or nop (to jmp). What is the value of the accumulator after the
// program terminates?

public class Solution2 {
    public static void main(String[] args) {
        final BitSet visited = new BitSet();
        final BitSet tempVisited = new BitSet();
        System.out.println(getAccumulator(getInput(), visited, 0, 0, tempVisited, 0, 0));
    }

    private static List<String[]> getInput() {
        final List<String[]> input = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            br.lines().forEach(line -> input.add(line.trim().split("[ ]")));
        } catch (Exception e) {
            System.out.println(e);
        }
        return input;
    }

    private static int getAccumulator(final List<String[]> input, final BitSet visited, int index, int accumulator,
            final BitSet tempVisited, int tempIndex, int tempAccumulator) {
        if (index >= input.size()) // Scape clause
            return accumulator;

        if (visited.get(index) != true && tempIndex != 0) {
            visited.flip(index);
            tempVisited.flip(index);
        } else if (visited.get(index) != true)
            visited.flip(index);
        else {
            // revert changes
            index = tempIndex;
            tempIndex = 0;
            accumulator -= tempAccumulator;
            tempAccumulator = 0;
            visited.andNot(tempVisited);
            tempVisited.clear();
        }

        switch (input.get(index)[0]) {
        case "acc":
            if (tempIndex != 0) {
                accumulator += Integer.valueOf(input.get(index)[1]);
                tempAccumulator += Integer.valueOf(input.get(index)[1]);
            } else
                accumulator += Integer.valueOf(input.get(index)[1]);
            index++;
            break;
        case "jmp":
            if (!visited.get(index)) {
                visited.flip(index);
                index += Integer.valueOf(input.get(index)[1]);
            } else if (tempVisited.get(index)) {
                index += Integer.valueOf(input.get(index)[1]);
            } else {
                tempIndex = index;
                tempVisited.flip(index);
                index++;
            }
            break;
        case "nop":
            if (!visited.get(index)) {
                visited.flip(index);
                index++;
            } else if (tempVisited.get(index)) {
                index++;
            } else {
                tempIndex = index;
                tempVisited.flip(index);
                index += Integer.valueOf(input.get(index)[1]);
            }
        }
        return getAccumulator(input, visited, index, accumulator, tempVisited, tempIndex, tempAccumulator);
    }
}