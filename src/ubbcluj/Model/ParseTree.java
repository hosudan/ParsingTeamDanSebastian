package ubbcluj.Model;

import java.util.List;
import java.util.Stack;

/*

        IGNORE THIS FOR THE MOMENT
        (not completed. Change of methods, see PraseTreeV2)

 */

public class ParseTree {
    private Grammar grammar;

    enum ParsingState{
        q, //normal
        b, //back
        f, //final
        e //error
    }

    public ParseTree(Grammar grammar) {
        this.grammar = grammar;
    }

    //get the number of productions associated with a nonterminal
    public int getRuleLength(String symbol) {
        for(Rule rule : this.grammar.getRules()){
            if(rule.getStart().equals(symbol)) {
                return rule.getProductions().size();
            }
        }
        return -1; //if no producion found having the given symbol as a left stide
    }

    //it gets the production with number 'productionIndex' associated with the given symbol (if productionIndex = 1, it
    //returns the first production associated with the symbol)
    public List<String> getProduction(String symbol, int productionIndex){
        //if porductionIndex is greater then the number of productions associated with the given rule
        if(productionIndex > getRuleLength(symbol)){
            return null;
        }
        //iterate through the rules untill a rule with the left side = symbol is found. Then return the requested production
        for(Rule rule : this.grammar.getRules()){
            if(rule.getStart().equals(symbol)){
                return rule.getProductions().get(productionIndex - 1); //since production index start from 1
            }
        }
        //if no rule is found, return null
        return null;
    }

    public void createParseTree(String word){
        //creation and initialization
        ParsingState parseState = ParsingState.q;
        int index = 1;
        Stack<WorkingStackItem> workingStack = new Stack<>();
        Stack<String> inputStack = new Stack<>();
        inputStack.push(this.grammar.getStartingSymbol() + Integer.toString(1));

        while(parseState != ParsingState.e && parseState != ParsingState.f){
            //get tot symbol
            String topSymbol = inputStack.peek();
            //if nonterminal, then expand
            if(grammar.isnonterminal(topSymbol)){
                expand(workingStack, inputStack);
            }

        }
    }

    public void expand(Stack<WorkingStackItem> workingStack, Stack<String> inputStack){
        String topSymbol = inputStack.peek();
        //pop the symbol to prepare to
        inputStack.pop();
        if(workingStack.empty()){
            //add the symbol and it's production index to the working stack
            WorkingStackItem newItem = new WorkingStackItem(topSymbol,1);
            workingStack.push(newItem);
            //replace the
        }
        else{

        }
    }

    public class WorkingStackItem{
        public String symbol;
        public int index;

        public WorkingStackItem(String symbol){
            this.symbol = symbol;
            this.index = 1;
        }
        public WorkingStackItem(String symbol, int index){
            this.symbol = symbol;
            this.index = index;
        }
    }
}
