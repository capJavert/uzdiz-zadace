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
            "^($^)|(-g \\d{3,5})? ?(-m [^\\s]{1,}) (-s [^\\s]{1,}) (-a [^\\s]{1,}) (-alg hr\\.foi\\.uzdiz\\.antbaric\\.zadaca\\.iterators\\.(Sequential|Random|Index)) ?(-tcd \\d{1,})? ?(-bcd \\d{1,})? ?(-i [^\\s]{1,})? ?(-brl \\d{1,})?$"
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
