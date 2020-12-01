package ubbcluj.Model;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    List<State> states;
    Grammar grammar;

    public Parser(Grammar grammar){
        this.grammar = grammar;
        this.states = new ArrayList<State>();

        //creating the expanded state, that regarding "S' -> S"
        this.init();
    }

    public void createAllStates(){
        for(State s : this.states){
            //call goTo for the state s with every nonterminal
            State newState = new State();
            for(String nonTerminal : this.grammar.getNonTerminals()){
                
            }
        }
    }

    public State goTo(State s, String symbol){
        //create new state
        State newState = new State();
        //iterate thorugh every items in the current state s
        for(Item item : s.getListItems()){
            //if the current symbol of the item (that symbol standind at the right side of the dot)
            String currentSymbol = item.getCurrentSymbol();
            //if currentSymbol is the same as the symbol from the goTo method
            if(currentSymbol.equals(symbol)){
                //move the dot (aka the index)
                item.next();
                //get new currentSymbol
                String currentSymbolAfterNext = item.getCurrentSymbol();
                //check if it is a nonterminal
                if(grammar.isnonterminal(currentSymbolAfterNext)){
                    //apply closure (aka add avery rule regarding the nonterminal as the startSymbol of the rule)
                    this.closure(newState, currentSymbolAfterNext);
                }
            }
        }
        //return the new state
        return newState;
    }


    /*
        def: it adds the rules that have as starting symbol, the given 'currentSymbol'. This method is used when moving the point,
             to add the new rules to the given state s.
     */
    void closure(State s, String currentSymbol){
        //iterate through the rules
        for(Rule r : this.grammar.getRules()){
            //check if the rule starts from the wanted starting symbol
            String ruleStartingSymbol = r.getStart();
            if(ruleStartingSymbol.equals(currentSymbol)){
                //iterate thorugh all produtions and create new items based on thems
                //(a production of "S-> a b | C" is either the lsit [a,b] or the list [C])
                for(List<String> production : r.getProductions()){
                    Item newItem = new Item(r.getStart(), production);
                    s.addItem(newItem);
                }
            }
        }
    }

    void init(){
        String extendedStartState = "S'";
        ArrayList<String> extendedProduction = new ArrayList<String>();
        extendedProduction.add("S");
        Item extendedItem = new Item(extendedStartState, extendedProduction);

        State s0 = new State();
        s0.addItem(extendedItem);

        this.states.add(s0);
    }

    public void printState(State s){
        String output = "";
        for(Item item : s.getListItems()){
            output += item.startState;
            output += " -> ";
            for(String production : item.getProduction()){
                output += production;
            }
            output += "\n";
        }
        System.out.println(output);
    }

    public void printAllStates(){
        String output = "";
        for(State currS : this.states){
            printState(currS);
        }
    }
}
