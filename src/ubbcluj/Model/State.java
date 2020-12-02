package ubbcluj.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class State {
    List<Item> listItems;
    HashMap<String,State> transitions;

    public State(Grammar grammar, HashSet<Item> initialitems){
        listItems = new ArrayList<Item>();
        this.listItems.addAll(initialitems);
        transitions = new HashMap<>();
        closure(grammar);
    }

    public State(Grammar grammar, Item initialItem){
        listItems = new ArrayList<Item>();
        listItems.add(initialItem);
        transitions = new HashMap<>();
        closure(grammar);
    }

    public State(Grammar grammar){
        listItems = new ArrayList<Item>();
        transitions = new HashMap<>();
    }

    private void closure(Grammar grammar) {
       // System.out.println("Log: closure initial "+ this.toString());
        boolean changeFlag = false;
        do {
            changeFlag = false;
            HashSet<Item> temp = new HashSet<>();
            for (Item item : listItems) {

                if (item.getCurrentSymbol()!=null && grammar.isnonterminal(item.getCurrentSymbol())) {
                    HashSet<Rule> rules = grammar.getRulesByLeftSymbol(item.getCurrentSymbol());
                    temp.addAll(createItem(rules));
                }
            }
            temp = this.returnNonExistingItems(temp);
            if(!temp.isEmpty()){
                listItems.addAll(temp);
                changeFlag = true;
            }
        } while (changeFlag);
       // System.out.println("Log: closure "+ this.toString());
    }


    private HashSet<Item> returnNonExistingItems(HashSet<Item> temp){
        HashSet<Item> nonExisting = new HashSet<>();
        for(Item tempItem : temp){
            boolean exists = false;
            for(Item existingItem : this.listItems){
                if(tempItem.equal(existingItem)){
                    exists = true;
                    break;
                }
            }
            if(exists == false)
                nonExisting.add(tempItem);
        }
        return nonExisting;
    }

    private HashSet<Item> createItem(HashSet<Rule> rules) {
        HashSet<Item> results = new HashSet<>();
        for (Rule rule : rules) {
            for(List<String> oneProduction : rule.getProductions())
                results.add(new Item(rule.getStart(), oneProduction));
        }
        return results;
    }

    public State(List<Item> listItems){
        this.listItems = listItems;
    }

    public void addItem(Item newItem){
        listItems.add(newItem);
    }

    public Item getItem(int index){
        if(index >= this.getSize()){
            return null;
        }
        return listItems.get(index);
    }
    public void addTransition(String s, State state){
        transitions.put(s, state);
    }
    public HashMap<String, State> getTransitions() {
        return transitions;
    }

    public boolean searchItem(Item item){
        for(Item i : this.listItems){
            if(item.equal(i) == true){
                return true;
            }
        }
        return false;
    }

    public List<Item> getListItems(){
        return this.listItems;
    }

    public int getSize(){
        return this.listItems.size();
    }

    public boolean equal(State other){
        for(Item item : this.getListItems()){
            boolean equals = false;
            for(Item otherItem : other.getListItems()){
                if(item.equal(otherItem)){
                    equals = true;
                }
            }
            if(equals == false)
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String s = "[";
        for(int i = 0; i<listItems.size()-1; i++){
            s += listItems.get(i) + " | ";
        }
        s+= listItems.get(listItems.size()-1) + "]";
        return s;
    }
}
