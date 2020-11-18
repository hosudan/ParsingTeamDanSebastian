package ubbcluj.Model;

import java.util.Arrays;
import java.util.Objects;

public class HHashTable<K,V> implements IHashTable<K,V> {
    private Integer n;
    Node[] buckets;
    public HHashTable(Integer bucketno){
        n = bucketno;
        this.buckets = new Node[n];
    }

    public HHashTable(){
        this(64);
    }

    @Override
    public Integer hashcode(K key){
        return Objects.hash(key); //generic hash function in-built -> could've done a string hashfunction if it was not generic (hash*31 + charAt(i) ex, where i reiteratres through str chars)
    }

    @Override
    public void put(K key, V value){
        if (key == null || value == null){
            throw new RuntimeException("only non null values/keys accepted in the hash table.");
        }
        Integer hash = hashcode(key);
        int id = hash & (n-1);
        if (buckets[id]==null){
            Node node = new Node(key,value);
            buckets[id]= node;
        }
        else{
            buckets[id].put(key,value);
        }
    }
    @Override
    public Boolean contains(K key){
        Integer hash = hashcode(key);
        int id = hash & (n-1);
        if (buckets[id]==null){
            return false;
        }
        else { return buckets[id].contains(key); }
    }

    @Override
    public Boolean isEmpty(){
        for (int i=0; i< buckets.length; i++){
            if (buckets[i]!=null)
                return false;
        }
        return true;
    }
    @Override
    public void remove(K key){
        Integer hash = hashcode(key);
        int id = hash & (n-1);
        if (buckets[id]!=null) {
            if ((buckets[id].getNext() == null) && (buckets[id].getKey().equals(key))) {
                buckets[id] = null;
            } else {
                buckets[id].remove(key);
            }
        }
    }
    @Override
    public V get(K key){
        Integer hash = hashcode(key);
        int id = hash & (n-1);
        if (buckets[id]==null){
            return null;
        }
        else { return (V) buckets[id].get(key); }
    }

    @Override
    public String toString() {
        String result = "";
        for(int i=0;i< buckets.length;i++){
            if (buckets[i] == null){continue;}else
                result+=buckets[i].toString();
        }
        return result;
    }
}
