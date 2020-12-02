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
        if (production.size() == 1 && production.get(0).equals("epsilon")) {
            finished = true;
        }
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
        return null;
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

    @Override
    public String toString() {
        String str = startState + " -> ";
        for (int i = 0; i < production.size(); i++) {
            if (i == index) {
                str += ".";
            }
            str += production.get(i);
            if(i != production.size() - 1){
                str+= " ";
            }
        }
        if (production.size() == index) {
            str += ".";
        }
        return str;
    }

    public int getIndex(){
        return this.index;
    }
    public void setIndex(int index) {
        this.index = index;
    }


}
