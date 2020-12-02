package ubbcluj.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Parser {
    private List<State> canonicalCollection;
    private Grammar grammar;
    private Boolean log;

    public Parser(Grammar grammar, boolean log){
        this.grammar = grammar;
        this.canonicalCollection = new ArrayList<>();
        this.log = log;
        //creating the expanded state, that regarding "S' -> S"
        createAllStates();
    }

    public State goTo(State s, String currSymbol){
        State newState = null;
        if(log){ System.out.println("Log: goto (State: "+ s.toString() + " Symbol:"+ currSymbol + ")");}
        for(Item r : s.getListItems()){
            //get current symbol from item
            String itemCurrentSymbol = r.getCurrentSymbol();
            //if current symbol is the same as the parameter currSymbol, that means we can move the dot
            if(itemCurrentSymbol != null && itemCurrentSymbol.equals(currSymbol)){
                //copy the item to do a next() on it, then create a new state with it.
                // Leave the initial item alone, since it belongs to the current state.
                Item initialItem = new Item(r.getStartState(), r.getProduction());
                initialItem.setIndex(r.getIndex());
                initialItem.next();
                if(log) {System.out.println("Log: -> goto = (new State: "+ initialItem + ")"); }
                newState = new State(this.grammar, initialItem);
                if(!this.hasState(newState)) {
                    //s.addTransition(currSymbol,newState);
                    this.canonicalCollection.add(newState);
                    if(log){System.out.println("Log: -> closure = " + newState);}
                }
                else { if(log) {System.out.println("Log: -> State not added as it already exists.");} }
            }
        }
        return newState;
    }

    public void createAllStates(){
        this.init();
        for (int i = 0; i < canonicalCollection.size(); i++) {
                HashSet<String> currentsymbolsforstate = new HashSet<>();
                for (Item item : canonicalCollection.get(i).getListItems()) {
                    if (item.getCurrentSymbol() != null) {
                        currentsymbolsforstate.add(item.getCurrentSymbol());
                    }
                }
                State temp;
                for (String str : currentsymbolsforstate) {
                    temp = goTo(canonicalCollection.get(i), str);
                    if (temp != null) {
                        canonicalCollection.get(i).addTransition(str, temp);
                    }
                }

            }
            if(log) { printAllTransitions(); }
        }

//    public State goTo(State s, String symbol){
//        //create new state
//        State newState = new State(grammar);
//        //iterate thorugh every items in the current state s
//        for(Item item : s.getListItems()){
//            //if the current symbol of the item (that symbol standind at the right side of the dot)
//            String currentSymbol = item.getCurrentSymbol();
//            //if currentSymbol is the same as the symbol from the goTo method
//            if(currentSymbol.equals(symbol)){
//                //move the dot (aka the index)
//                item.next();
//                //get new currentSymbol
//                String currentSymbolAfterNext = item.getCurrentSymbol();
//                //check if it is a nonterminal
//                if(grammar.isnonterminal(currentSymbolAfterNext)){
//                    //apply closure (aka add avery rule regarding the nonterminal as the startSymbol of the rule)
//                    //this.closure(newState, currentSymbolAfterNext);
//                }
//            }
//        }
//        //return the new state
//        return newState;
//    }

    public boolean hasState(State someState){
        for(State s : this.canonicalCollection){
            if(s.equal(someState))
                return true;
        }
        return false;
    }


    /*
        def: it adds the rules that have as starting symbol, the given 'currentSymbol'. This method is used when moving the point,
             to add the new rules to the given state s.
     */
 /*   void closure(State s, String currentSymbol){
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
    } */

    void init(){
        String extendedStartState = grammar.getStartingSymbol()+"'";
        ArrayList<String> extendedProduction = new ArrayList<String>();
        extendedProduction.add(grammar.getStartingSymbol());
        Item extendedItem = new Item(extendedStartState, extendedProduction);

        State s0 = new State(grammar,extendedItem);

        this.canonicalCollection.add(s0);
        if(log){System.out.println("init done: " + s0);}
    }

    public void printState(State s){
        String output = "";
        for(Item item : s.getListItems()){
            output += item.toString();
            output += "\n";
        }
        System.out.println(output);
    }

    public void printAllTransitions(){
        System.out.println("-----------All the transitions between the states-----------");
        int i = 0;
        for ( State st : this.canonicalCollection){
            System.out.println("State #" + i + " -> " + st);
            for (HashMap.Entry<String, State> entry : st.getTransitions().entrySet()) {
                System.out.println(entry.getKey() + ":" + entry.getValue().toString());
            }
            System.out.println("--------------------");
            i++;
        }
    }

    public void printAllStates(){
        String output = "";
        for(State currS : this.canonicalCollection){
            printState(currS);
            System.out.println("------------");
        }
    }
}
