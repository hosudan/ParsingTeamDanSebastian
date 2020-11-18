package ubbcluj.Model;

public class SymbolTable {
    private HHashTable<String,Integer> TableConstants;
    private HHashTable<String,Integer> TableIdentifiers;

    public HHashTable<String, Integer> getTableConstants() {
        return TableConstants;
    }

    public void setTableConstants(HHashTable<String, Integer> tableConstants) {
        TableConstants = tableConstants;
    }

    public HHashTable<String, Integer> getTableIdentifiers() {
        return TableIdentifiers;
    }

    public void setTableIdentifiers(HHashTable<String, Integer> tableIdentifiers) {
        TableIdentifiers = tableIdentifiers;
    }

    public SymbolTable(){
        this.TableConstants= new HHashTable<>();
        this.TableIdentifiers=new HHashTable<>();
    }

    public SymbolTable(HHashTable<String, Integer> TC,HHashTable<String, Integer> TI){
        this.TableConstants=TC;
        this.TableIdentifiers=TI;
    }

    public boolean TCcontains(String key){ return TableConstants.contains(key);}
    public boolean TIcontains(String key){ return TableIdentifiers.contains(key);}

    public void addtoTC(String key, Integer value){
        TableConstants.put(key,value);
    }
    public void addtoTI(String key, Integer value){
        TableIdentifiers.put(key,value);
    }

    public Integer getfromTI(String key){
        return TableIdentifiers.get(key);
    }
    public Integer getfromTC(String key){
        return TableConstants.get(key);
    }

    public void removefromTC(String key){
        TableConstants.remove((key));
    }
    public void removefromTI(String key){
        TableIdentifiers.remove((key));
    }

    @Override
    public String toString() {
        return "SymbolTable{" +
                "TableConstants=" + TableConstants +
                ", TableIdentifiers=" + TableIdentifiers +
                '}';
    }
}
