package ubbcluj.Model;

import java.util.*;

public class Parser {
    private List<State> canonicalCollection;
    private Grammar grammar;
    private Boolean log;
    private HashMap<String, Integer>[] goToTable;
    private Action[] actionTable;
    private String lastWordIndexesUsed;

    public Parser(Grammar grammar, boolean log){
        this.grammar = grammar;
        this.canonicalCollection = new ArrayList<>();
        this.log = log;
        //creating the expanded state, that regarding "S' -> S"
        createAllStates();
        createGoToTable();
        createActionTable();
        //printactiontable();
    }

    public void printactiontable(){
        for(int i = 0; i<actionTable.length; i++){
            System.out.println(i + ": "  + actionTable[i].getType().toString() + " " +  actionTable[i].getStindex());
        }
    }

    public State goTo(State s, String currSymbol){
        State newState = null;
        HashSet<Item> nextState = new HashSet<>();
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
                nextState.add(initialItem);
                if(log) {System.out.println("Log: -> goto = (new State: "+ initialItem + ")"); }
            }
        }
        newState = new State(this.grammar, nextState);
        if(!this.hasState(newState)) {
            //s.addTransition(currSymbol,newState);
            this.canonicalCollection.add(newState);
            if(log){System.out.println("Log: -> closure = " + newState);}
        }
        else { if(log) {System.out.println("Log: -> State not added as it already exists.");} }
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

    protected void createGoToTable() {
        goToTable = new HashMap[canonicalCollection.size()];
        for (int i = 0; i < goToTable.length; i++) {
            goToTable[i] = new HashMap<>();
        }
        for (int i = 0; i < canonicalCollection.size(); i++) {
            for (String s : canonicalCollection.get(i).getTransitions().keySet()) {
                    goToTable[i].put(s, findStateIndex(canonicalCollection.get(i).getTransitions().get(s)));
            }
        }
    }

    public String TableStr() {
        String str = "LR(0) Table: \n";
        str += "          ";
        for (String variable : grammar.getNonTerminals()) {
            str += String.format("%-6s",variable);
        }
        for (String variable : grammar.getTerminals()) {
            str += String.format("%-6s",variable);
        }
        str += "\n";

        for (int i = 0; i < goToTable.length; i++) {
            for (int j = 0; j < (grammar.getNonTerminals().size()+grammar.getTerminals().size()+1)*6+2; j++) {
                str += "-";
            }
            str += "\n";
            str += String.format("|%-6s|",i);
            for (String variable : grammar.getNonTerminals()) {
                str += String.format("%6s",(goToTable[i].get(variable) == null ? "|" : goToTable[i].get(variable)+"|"));
            }
            for (String variable : grammar.getTerminals()) {
                str += String.format("%6s",(goToTable[i].get(variable) == null ? "|" : goToTable[i].get(variable)+"|"));
            }
            if(actionTable[i].getType().equals(ActionType.REDUCE)){
            str+= String.format("%9s",(actionTable[i].getType().toString() + " " +  actionTable[i].getStindex())); }
            else { str+=String.format("%7s",(actionTable[i].getType().toString())); }
            str += "\n";
        }
        for (int j = 0; j < (grammar.getNonTerminals().size()+grammar.getTerminals().size()+1)*6+2; j++) {
            str += "-";
        }
        str += "\n";
        return str;
    }


    private boolean createActionTable() {
        actionTable = new Action[canonicalCollection.size()];
        for(int i = 0; i< actionTable.length; i++) {
              actionTable[i] = new Action(ActionType.ERROR, -1);
        }
        for(int i = 0; i< canonicalCollection.size(); i++){
            State st = canonicalCollection.get(i);
            if (st.getTransitions().size() != 0){
                // conflict shift-reduce
                List<Item> itlst = st.getListItems();
                for(Item it : itlst){
                    if (it.isFinished()){
                        System.out.println("Conflict shift-reduce at state: " + findStateIndex(st));
                        return false;
                    }
                }
                //actionTable[i] = new Action(ActionType.SHIFT,-1);
                actionTable[i].setType(ActionType.SHIFT);
            }
            else {
                List<Item> itlst = st.getListItems();
                if (itlst.get(0).getStartState().equals(grammar.getStartingSymbol()+"'") && itlst.get(0).getProduction().get(0).equals(grammar.getStartingSymbol())){
                    //actionTable[i] = new Action(ActionType.ACCEPT,-1);
                    actionTable[i].setType(ActionType.ACCEPT);
                }
                else{
                    Item redItem = st.getListItems().get(0);
                    int finished = 0; // conflict reduce-reduce
                    for(Item it : itlst){
                        if (it.isFinished()){
                            finished++;
                        }
                    }
                    if (finished > 1){
                        System.out.println("Conflict reduce-reduce at state: " + findStateIndex(st));
                        return false;
                    }else if(finished == 1){
                        //actionTable[i] = new Action(ActionType.REDUCE, findRuleIndex(redItem));
                        actionTable[i].setType(ActionType.REDUCE);
                        actionTable[i].setStindex(findRuleIndex(redItem));
                    }
                }
            }
        }
        return true;
    }

    private int findRuleIndex(Item it){
        int index = 1;
        for(Rule rule: grammar.getRules()){
            if(rule.getStart().equals(it.getStartState())){
                for(List<String> prod : rule.getProductions())
                {
                    if(prod.equals(it.getProduction())){
                            return index;
                    }
                    index++;
                }
                index--;
            }
            index+=rule.getProductions().size();
        }
        return -1;
    }

    private Item findIndexRule(int ind){
        int index = 1;
        for(Rule rule: grammar.getRules()){
            //System.out.println(rule + " " + index);
                for(List<String> prod : rule.getProductions())
                {
                    if(index == ind){
                        Item ret = new Item(rule.getStart(),prod);
                        return ret;
                    }
                    index++;
                }
                //System.out.println("index after productions" + index);
        }
        return null;
    }

    private int findStateIndex(State state) {
        for (int i = 0; i < canonicalCollection.size(); i++) {
            if (canonicalCollection.get(i).equal(state)) {
                return i;
            }
        }
        return -1;
    }

    public boolean accept(ArrayList<String> input){
        //sequence validation
        for( String s : input){
            if(!grammar.isnonterminal(s) && !grammar.isterminal(s)){
                return false;
            }
        }
        Integer stindex = 0;
        String Output = "";
        boolean end = false;
        input.add("$");
        Stack<String> inputSt = new Stack();
        Stack<String> workingSt = new Stack();
        for (int i = input.size()-1; i>=0; i--){
            inputSt.push(input.get(i));
        }
        //System.out.println(inputSt);
        workingSt.add("$");
        workingSt.add("0");
        do{
            if(log) {
                System.out.println("----------BEFORE--------");
                System.out.println("Input:" + inputSt);
                System.out.println("Working:" + workingSt);
                System.out.println("Output:" + Output);
            }
            if (stindex == null) { return false; }
            if(actionTable[stindex].getType() == ActionType.SHIFT){
                String in = inputSt.pop();
                stindex = goToTable[stindex].get(in);
                workingSt.push(in);
                workingSt.push(stindex+"");
            }
            else if (actionTable[stindex].getType() == ActionType.REDUCE){
                int ruleIndex = actionTable[stindex].getStindex();
                System.out.println(ruleIndex+ "");
                Item itm = findIndexRule(ruleIndex);
                //System.out.println(itm);
                String leftSide = itm.getStartState();
                int rightSideLength = itm.getProduction().size();
                for(int i=0; i <2*rightSideLength ; i++){
                    workingSt.pop();
                }
                int nextState= 0;
                try {
                    nextState = Integer.parseInt(workingSt.peek());
                } catch (NumberFormatException e ){ System.out.println(" ER " + workingSt.peek()); }
                workingSt.push(leftSide);
                int variableState = goToTable[nextState].get(leftSide);
                workingSt.push(variableState+"");
                Output += ruleIndex +" ";
                stindex = variableState;
            }
            else {
                if (actionTable[stindex].getType() == ActionType.ACCEPT){
                            workingSt.removeAllElements();
                            workingSt.add("acc");
                            StringBuilder out = new StringBuilder();
                            out.append(Output);
                            out.reverse();
                            Output = out.toString();
                            end = true;
                            if (inputSt.size()>1) { return false; }
                            //return true;
                }
                if (actionTable[stindex].getType() == ActionType.ERROR){
                        workingSt.removeAllElements();
                        workingSt.add("error");
                        System.out.println("Error before token: " + inputSt.toString());
                        this.lastWordIndexesUsed = null;
                        return false;
                }
            }
            if (log) {
                System.out.println("----------AFTER--------");
                System.out.println("Input:" + inputSt);
                System.out.println("Working:" + workingSt);
                System.out.println("Output:" + Output);
            }
        } while(!end);

        //for parseTree building
        this.lastWordIndexesUsed = Output;

        return true;
    }

    public String getLastWordIndexesUsed(){
        return this.lastWordIndexesUsed;
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
                System.out.println(entry.getKey() + ":" + entry.getValue().toString() + "State:" + findStateIndex(entry.getValue()));
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
