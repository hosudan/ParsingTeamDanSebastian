package ubbcluj;

import ubbcluj.Model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
    /*    HHashTable<Integer, String> map = new HHashTable();
        map.put (512, "pizza");
        map.put (2, "dwadwadawdadsdz");
        map.put (510, "junge");
        System.out.println(map.get(2));
        System.out.println(map.get(510));
        map.remove(510);
        System.out.println(map.get(510)); */

      /*  SScanner sc = new SScanner("p1example.txt","codificationtable.txt");
        System.out.println(sc.isIdentifier("1"));
        System.out.println(sc.isCharacter("\"a\""));
        sc.outputSTPIF();*/
      /*  FiniteAutomata fa = new FiniteAutomata("FAconstant.txt");
        fa.Menu(); */

        Grammar gr = new Grammar();
       // gr.Menu();

        Parser p = new Parser(gr,true);
        //p.printAllStates();
        System.out.println(p.TableStr());
        ArrayList<String> test =
                new ArrayList<String>(Arrays.asList("a","b","b","c"));
        //System.out.println(p.findIndexRule(3));
        System.out.println(p.accept(test));
        //p.printAllTransitions();
    }
}
