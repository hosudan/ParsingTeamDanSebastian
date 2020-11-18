package ubbcluj.Model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Grammar {
    private List<String> nonTerminals;
    private Set<String> terminals;
    private List<Rule> rules;
    private String startingSymbol;


    public Grammar() {
    nonTerminals = new LinkedList<>();
    terminals = new HashSet<>();
    rules = new ArrayList<>();
    getGrammarFromFile();
    }

    public boolean isnonterminal(String nt){
        return nonTerminals.contains(nt);
    }

    public boolean isterminal(String t){
        return terminals.contains(t);
    }

    public void Menu(){
        while(true) {
            System.out.println("1 - Print Non-Terminals");
            System.out.println("2 - Print Terminals");
            System.out.println("3 - Print Start");
            System.out.println("4 - Print rules");
            Scanner inScanner = new Scanner(System.in);
            int option = Integer.parseInt(inScanner.next().trim());
            switch (option) {
                case 0:
                    System.exit(0);
                case 1:
                    System.out.println(nonTerminals.toString());
                    System.out.println();
                    break;
                case 2:
                    System.out.println(terminals.toString());
                    System.out.println();
                    break;
                case 3:
                    System.out.println(startingSymbol);
                    System.out.println();
                    break;
                case 4:
                    System.out.println(rules.toString());
                    System.out.println();
                    break;
                default:
                    System.out.println("not an option.");
            }
        }
    }

    private void getGrammarFromFile() {
        try {
            int i = 0;
            for (String line : Files.readAllLines(Paths.get("src/resources/grammar.txt"))) {
                if (i <= 2){
                    String[] lst = line.split(" ");
                    for (int j = 0; j < lst.length; j++) {
                        if (i == 0) {
                            if (!nonTerminals.contains(lst[j])) {
                                nonTerminals.add(lst[j]);
                            }
                        }
                        if (i == 1) {
                            if (!terminals.contains(lst[j])) {
                                terminals.add(lst[j]);
                            }
                        }
                        if (i == 2) {
                            if(isnonterminal(lst[j])) {
                                startingSymbol = lst[j];
                            }
                            else { System.out.println(startingSymbol + " is not a non terminal from the given set."); }
                        }
                    }
                }
                if (i > 2) {
                    String[] lst = line.split(" -> ");
                    List<List<String>> productions = new ArrayList<>();

                    for ( String rule: lst[1].split(" \\| "))
                        productions.add(Arrays.asList(rule.split(" ")));
                    boolean valid = true;
                    for(List<String> prod : productions){
                        for (String pr: prod){
                            if (!isnonterminal(pr) && !isterminal(pr)){
                                valid = false;
                            }
                        }
                    }
                    if (!isnonterminal(lst[0])) valid = false;
                    if (valid) {
                        rules.add(new Rule(lst[0], productions));
                    }
                    else { System.out.println("invalid rules"); }
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getNonTerminals() {
        return nonTerminals;
    }

    public Set<String> getTerminals() {
        return terminals;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public String getStartingSymbol() {
        return startingSymbol;
    }

    public String toString() {
        return "G =( " + nonTerminals.toString() + ", " + terminals.toString() + ", " +
                rules.toString() + ", " + startingSymbol + " )";

    }
}
