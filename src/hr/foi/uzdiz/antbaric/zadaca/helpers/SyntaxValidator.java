package hr.foi.uzdiz.antbaric.zadaca.helpers;

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
                "-br (2[4-9]|3[0-9]|40)",
                "-bs ([8-9][0-9]|1[0-5][0-9]|160)",
                "-brk ([2-5])",
                "-pi (100|[1-9][0-9]|\\d)",
                "-g \\d{3,5}",
                "-m [^\\s]{1,}",
                "-s [^\\s]{1,}",
                "-a [^\\s]{1,}",
                "-r [^\\s]{1,}",
                "-tcd \\d{1,}"
        ).collect(Collectors.toList());
    }

    /**
     *
     * @param match String to match
     * @return List<Matcher>
     */
    private static List<Matcher> validate(Object match) {
        List<Matcher> matchers = new ArrayList<>();
        
        for(String rule : rules()) {
            Pattern pattern = Pattern.compile(rule);
            Matcher m = pattern.matcher(match.toString());

            if(m.matches()) {
                matchers.add(m);
            }
        }

        if(matchers.size() > 0) {
            return matchers;
        } else {
            return null;
        }
    }

    /**
     * Check if rule is valid
     *
     * @param args Command
     * @return Returns true if valid
     */
    public static List<Matcher> validateArguments(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg).append(" ");
        }
        String p = sb.toString().trim();

        return SyntaxValidator.validate(p);
    }
}
