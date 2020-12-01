package ubbcluj.Model;

import java.util.ArrayList;
import java.util.List;

public class State {
    List<Item> listItems;

    public State(){
        listItems = new ArrayList<Item>();
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
}
