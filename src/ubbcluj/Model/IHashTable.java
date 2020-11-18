package ubbcluj.Model;

public interface IHashTable<K,V> {
    public Integer hashcode(K key);
    public void put(K key, V value);
    public V get(K key);
    public Boolean contains(K key);
    public Boolean isEmpty();
    public void remove(K key);
}
