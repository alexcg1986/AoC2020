import java.util.regex.Pattern;

public class pruebas {
    public static void main(String[] args) {
        System.out.println(Pattern.compile("(?:#[0-9a-f]{6})$").matcher("#efcc98").find());
    }
}
