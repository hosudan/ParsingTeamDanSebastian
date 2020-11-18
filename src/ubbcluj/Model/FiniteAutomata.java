package ubbcluj.Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class FiniteAutomata {
    private List<String> States;
    private List<String> input;
    private String Start;
    private Hashtable<Pair<String,String>,List<String>> transitions;
    private List<String> finalstate;
    private String FAname;

    public FiniteAutomata(String Fname){
        this.FAname=Fname;
        States = new ArrayList<>();
        input = new ArrayList<>();
        finalstate = new ArrayList<>();
        transitions = new Hashtable<>();
        readFA();
    }

    public void Menu(){
        while(true) {
            System.out.println("1 - Print States");
            System.out.println("2 - Print Variables");
            System.out.println("3 - Print Q0");
            System.out.println("4 - Print final states");
            System.out.println("5 - Check DFA sequence");
            Scanner inScanner = new Scanner(System.in);
            int option = Integer.parseInt(inScanner.next().trim());
            switch (option) {
                case 0:
                    System.exit(0);
                case 1:
                    System.out.println(this.States.toString());
                    System.out.println();
                    break;
                case 2:
                    System.out.println(this.input.toString());
                    System.out.println();
                    break;
                case 3:
                    System.out.println(this.Start);
                    System.out.println();
                    break;
                case 4:
                    System.out.println(this.finalstate.toString());
                    System.out.println();
                    break;
                case 5:
                    String st = inScanner.next().trim();
                    System.out.println(check(st));
                    break;
                default:
                    System.out.println("not an option.");
            }
        }
    }

    public boolean checkDFA(){
        //System.out.println(transitions.toString());
        for(Map.Entry<Pair<String,String>,List<String>> entry : transitions.entrySet()) {
            Pair k = entry.getKey();
            List v = entry.getValue();
            if(v.size()>1){
                return false;
            }
        }
        return true;
    }

    public static void addtransition(Hashtable<Pair<String,String>,List<String>> table, Pair<String,String> key,String value) {
        for(Map.Entry<Pair<String,String>,List<String>> entry : table.entrySet()) {
            Pair k = entry.getKey();
            List v = entry.getValue();
            if((k.getValue().equals(key.getValue())) && (k.getKey().equals(key.getKey()))){
                v.add(value);
            }
        }
      /*  List<String> list = table.get(key);
        if (list == null) {
            list = new ArrayList<String>();
            table.put(key, list);
        }
        list.add(value); */
    }

    public static List<String> gettransitionlist(Hashtable<Pair<String,String>,List<String>> table, Pair<String,String> key) {
        for(Map.Entry<Pair<String,String>,List<String>> entry : table.entrySet()) {
            Pair k = entry.getKey();
            List v = entry.getValue();
            if((k.getValue().equals(key.getValue())) && (k.getKey().equals(key.getKey()))){
                return v;
            }
        }
        return new ArrayList<String>();
    }

    public void printStates(){
        System.out.println(this.States.toString());
    }

    public void printinput(){
        System.out.println(this.input.toString());
    }

    public void printStart(){
        System.out.println(this.Start);
    }

    public void printfinalStates(){
        System.out.println(this.finalstate.toString());
    }

    public boolean check(String value){
        if(!checkDFA()){ return false; }
        String curr = this.Start;
        for (char symbol: value.toCharArray()) {
            Pair<String,String> p = new Pair(curr,""+symbol);
            //System.out.println(gettransitionlist(transitions,new Pair("CHR2","'")));
            List lst = gettransitionlist(transitions,p);
            //System.out.println(p.toString() + " " + lst.toString());
            if(lst.size()>0){
                curr = (String) lst.get(0);
            }else return false;
        }
        if (finalstate.contains(curr)){
            return true;
        } else { return false; }
    }

    public void readFA(){
        try{
            File file = new File("/Users/Dan/IdeaProjects/FLCD/src/resources/" + this.FAname);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String str; String[] list;
            str = br.readLine();
            list = str.split(" ");
            for (String s: list){
                States.add(s);
            }
            //System.out.println(States.toString());
            str = br.readLine();
            list = str.split(" ");
            for (String s: list){
                input.add(s);
            }

            for (String s : States){
                for (String i: input){
                    Pair<String,String> p = new Pair(s,i);
                    transitions.put(p, new ArrayList<String>());
                }
            }


            //System.out.println(input.toString());

            str = br.readLine();
            Start = str;

            str = br.readLine();
            list = str.split(" ");
            for (String s: list){
                finalstate.add(s);
            }
            int  i = 0; int j = 0;
            while ((str = br.readLine()) != null) {
                list = str.split(" ");
                Pair p = new Pair(list[0],list[1]);
                addtransition(transitions,p,list[2]);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Exception while reading from FA!");
        }
    }
}
