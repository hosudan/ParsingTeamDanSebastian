package ubbcluj.Model;

public class Node<K,V> {
    private K key;
    private V value;
    Node<K,V> next;

    public void replacecurrent(Node nos){
        this.key = (K)nos.key;
        this.value = (V) nos.value;
        this.next = nos.next;
    }

    public Node(K nkey,V nvalue){
        this.key = nkey;
        this.value=nvalue;
        this.next = null;
    }

    public Node getNext(){
        return next;
    }
    public void setNext(Node n){
        next = n;
    }

    public void addnode(Node no){
        Node start = this;
        while(true){
            if (start.equals(no)) {
                start.setValue(no.getValue());
                break;

            } else if (start.getNext()==null){
                start.setNext(no);
                break;
            }
            else {
                start = start.getNext();
            }
        }
    }

    public void remove(K key){
        Node start = this;
        while (true){
            if (start.getKey().equals(key)){
                replacecurrent(start.getNext());
                break;
            }
            else if (start.getNext()==null){
                break;
            }
            else { start =  start.getNext(); }
        }
    }

    public void put(K key, V value){
        Node node = new Node(key,value);
        addnode(node);
    }

    public Boolean contains(K key){
        Node start = this;
        while (true){
            if (start.getKey().equals(key)){
                return true;
            }
            else if (start.getNext()==null){
                return false;
            }
            else { start =  start.getNext(); }
        }
    }

    public V get(K key){
        Node start = this;
        while (true){
            if (start.getKey().equals(key)){
                return (V) start.getValue();
            }
            else if (start.getNext()==null){
                return null;
            }
            else { start =  start.getNext(); }
        }
    }


    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;

        Node node = (Node) obj;
        return key.equals(node.getKey());
    }

    @Override
    public String toString() {
        if (next ==null){ return "Node{" +
                "key=" + key +
                ", value=" + value +'}'; }
        else
        return "Node{" +
                "key=" + key +
                ", value=" + value +
                ", next=" + next.toString() +
                '}';
    }
}
