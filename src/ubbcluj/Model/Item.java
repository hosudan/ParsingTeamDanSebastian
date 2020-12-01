package ubbcluj.Model;

import java.util.List;

public class Item {
    String startState;
    List<String> production;
    int index;
    boolean finished;

    public Item(String startState, List<String> production){
        this.index = 0;
        this.finished = false;
        this.startState = startState;
        this.production = production;
    }

    /*
        def: return the symbol from the right side of the dot. The positiion of the dot is given by the index.
        return: the symbol - if index if not at the end | the null string if the symbol is at the end
    */
    public String getCurrentSymbol(){
        if(index < this.production.size())
            return this.production.get(index);
        return "";
    }

    /*
        def: moves the dot one step. If the dot gets at the end of the string, return false and set finished flag to false
     */
    public boolean next(){
        if(this.index < production.size())
            this.index++;
        if(this.index < production.size())
            return true;
        this.finished = true;
        return false;
    }

    public String getStartState(){
        return this.startState;
    }

    public List<String> getProduction(){
        return this.production;
    }

    public boolean isFinished(){
        return this.finished;
    }

    boolean equal(Item other){
        if(other.startState != this.startState ||
                other.index != this.index ||
                other.getProduction() != this.getProduction()) {
            return false;
        }
        return true;
    }

}
