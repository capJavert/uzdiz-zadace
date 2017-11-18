package hr.foi.uzdiz.antbaric.zadaca.components;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SyntaxValidator {

    /**
     * Return rules
     *
     * @return Returns list of rules
     */
    private static List<String> rules() {
        return Stream.of(
                "^\\d{3,} [^\\s]{1,} [^\\s]{1,} [^\\s]{1,} (Sequential|Aplhabetical|Random) \\d{1,} \\d{1,} [^\\s]{1,}$"
        ).collect(Collectors.toList());
    }

    /**
     *
     * @param match String to match
     * @return Matcher
     */
    public static Boolean validate(Object match) {
        for(String rule : rules()) {
            Pattern pattern = Pattern.compile(rule);
            Matcher m = pattern.matcher(match.toString());

            if(m.matches()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if rule is valid
     *
     * @param args Command
     * @return Returns true if valid
     */
    public static Boolean validateArguments(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg).append(" ");
        }
        String p = sb.toString().trim();

        return validate(p);
    }
}
