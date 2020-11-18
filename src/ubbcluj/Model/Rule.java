package ubbcluj.Model;

import java.util.List;

public class Rule {
    private String start;
    private List<List<String>> productions;

    Rule(String start, List<List<String>> rules) {
        this.start = start;
        this.productions = rules;
    }

    List<List<String>> getProductions() {
        return productions;
    }

    String getStart() {
        return start;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(start + " -> ");
        for (List<String> pr : productions) {
            for (String element: pr)
                sb.append(element).append(" ");
            sb.append("| ");
        }
        sb.replace(sb.length() - 3, sb.length() - 1, "");
        return sb.toString();
    }
}
