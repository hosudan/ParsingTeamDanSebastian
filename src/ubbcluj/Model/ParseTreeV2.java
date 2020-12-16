package ubbcluj.Model;

import java.util.ArrayList;
import java.util.List;

public class ParseTreeV2 {
    class Node{
        public String element;
        public int leftChild, rightSibling;
        int currentIndex;

        public Node(String element, int index){
            this.currentIndex = index;
            this.leftChild = -1;
            this.rightSibling = -1;
            this.element = element;
        }
        public Node(String element, int index,  int leftChild, int rightSibling){
            this.currentIndex = index;
            this.element = element;
            this.leftChild = leftChild;
            this.rightSibling = rightSibling;
        }

        int getLeftChild(){ return this.leftChild; }
        int getRightSibling(){ return this.rightSibling; }
        String getElement(){ return this.element; }
        int getIndex(){ return this.currentIndex; }

        void setLeftChild(int index){ this.leftChild = index; }
        void setRightSibling(int index){ this.rightSibling = index; }
        public String toString(){
            return "Idx: " + Integer.toString(currentIndex) + ": " +
                    "symbol: " + element + ", left child: " + Integer.toString(leftChild) +
                    ", right sibling: " + Integer.toString(rightSibling);
        }
    }

    Grammar grammar;
    List<Node> listNodes;
    String indexesUsed;

    public ParseTreeV2(String indexesUsed, Grammar grammar){
        this.grammar = grammar;
        this.indexesUsed = indexesUsed;
        this.listNodes = new ArrayList<>();

        if(indexesUsed != null) {
            //first node
            System.out.println(this.indexesUsed);
            normalizeIndexes();
            System.out.println(this.indexesUsed);
            Node firstNode = new Node(grammar.getStartingSymbol(), 0);
            listNodes.add(firstNode);
            createTree();
        }
    }

    public void normalizeIndexes(){
        String indexesUsedNormalized = "";
        int currentIndex = 0;
        List<Integer> listProdIndexes = new ArrayList<>();
        //convert indexes from string to int
        for(String indexAsString : this.indexesUsed.split(" ")){
            if(indexAsString.equals("") == false){
                int indexAsInt = Integer.parseInt(indexAsString);
                listProdIndexes.add(indexAsInt);
            }
        }

        //change the position of some indexes
        for(int i=listProdIndexes.size()-1; i>=0; i--){
            int currentProdIndex = listProdIndexes.get(i);
            List<String> prod = findProductionByIndex(currentProdIndex);
            int countNonterminals = 0;
            for(String symbol : prod){
                if(this.grammar.isnonterminal(symbol)){
                    countNonterminals++;
                }
            }
            //reverse the elements starting from i+1 until i+countNonTerminals
            List<Integer> subList = new ArrayList<>();
            for(int j=0; j<countNonterminals; j++){
                subList.add(listProdIndexes.get(i+countNonterminals-j));
            }
            for(int j=0; j<countNonterminals; j++){
                listProdIndexes.set(i+j+1, subList.get(j));
            }
        }

        //retransformm listProdIndexes to this.indexesUsed
        for(int el : listProdIndexes){
            indexesUsedNormalized += Integer.toString(el) + " ";
        }

        this.indexesUsed = indexesUsedNormalized;
    }

    public List<String> findProductionByIndex(int index){
        for(Rule rule : grammar.getRules()){
            for(List<String> prod : rule.getProductions()){
                index--;
                if(index == 0)
                    return prod;
            }
        }
        return null;
    }

    public void createTree(){
        int maxIndex = 1;
        for(String indexAsString : indexesUsed.split(" ")){
            if(indexAsString.equals("")) continue;

            int indexAsInt = Integer.parseInt(indexAsString);
            List<String> production = findProductionByIndex(indexAsInt);
            List<Node> batchOfNodes = new ArrayList<>(); //or current level from parseTree
            //creating new nodes
            for(String symbol : production){
                Node newNode = new Node(symbol, maxIndex);
                maxIndex++;
                batchOfNodes.add(newNode);
            }
            //connecting the nodes
            for(int i=0; i<batchOfNodes.size()-1; i++){
                Node currentNode = batchOfNodes.get(i);
                int currentNodeIndex = currentNode.getIndex();
                int nextNodeIndex = batchOfNodes.get(i+1).getIndex();

                currentNode.setRightSibling(nextNodeIndex);
            }

            //check the list of nodes so far and assign all this nodes to the first node that is a nonterminal and
            //does not have a left child so far
            Node previousNonterminalNode = null;
            for(Node node : this.listNodes){
                if(node.leftChild != -1){
                    previousNonterminalNode = node;
                }
                if(this.grammar.isnonterminal(node.getElement()) && node.leftChild == -1){
                    //set the left child
                    int firstNodeFromBatch = batchOfNodes.get(0).getIndex();
                    node.leftChild = firstNodeFromBatch;
                    //set the first node from currentLine as a right sibling if it is the case
                    int currentNodeIndex = node.getIndex();
                    int previousNodeNonterminalIndex = -1;
                    if(previousNonterminalNode != null){
                        previousNodeNonterminalIndex = previousNonterminalNode.getIndex();
                    }
                    if(previousNodeNonterminalIndex > 0){
                        if(onSameLevel(previousNodeNonterminalIndex, currentNodeIndex) == true){
                            //if previousNodeNonterminalIndex and currentNodeIndex are on same line
                            //then their children must be related as siblings
                            this.listNodes.get(firstNodeFromBatch-1).setRightSibling(firstNodeFromBatch);
                        }
                    }
                    break;
                }
            }

            //adding all batch of nodes to the list of nodes
            this.listNodes.addAll(batchOfNodes);
        }
    }

    public boolean onSameLevel(int nodeIndexLeft, int nodeIndexRight){
        Node currentNode = this.listNodes.get(nodeIndexLeft);
        while(currentNode.getRightSibling() != -1){
            if(currentNode.getRightSibling() == nodeIndexRight) return true;
            currentNode = this.listNodes.get(currentNode.getRightSibling());
        }
        return false;
    }

    public String toString(){
        if(this.indexesUsed == null){
            return null;
        }
        String output = "";
        for(Node node : this.listNodes){
            output += node.toString() + "\n";
        }
        return output;
    }
}
