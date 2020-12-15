package ubbcluj;

import ubbcluj.Model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

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

        ArrayList<String> program = new ArrayList<>();
//        program.add("if");
//        program.add("(");
//        program.add("identifier");
//        program.add(">");
//        program.add("identifier");
//        program.add(")");
//        program.add("{");
//        program.add("identifier");
//        program.add("=");
//        program.add("constant");
//        program.add(";");
//        program.add("}");
        program.add("{");
        program.add("int");
        program.add("identifier");
        //program.add("");
        program.add(";");
        program.add("}");

        Parser p = new Parser(gr,true);
        //p.printAllStates();
        System.out.println(p.TableStr());
        ArrayList<String> test = new ArrayList<String>(Arrays.asList("a","b","c","d"));
        //System.out.println(p.findIndexRule(3));
        System.out.println(p.accept(test));
        //p.printAllTransitions();

        System.out.println("\n\n");
        ParseTreeV2 pt2 = new ParseTreeV2(p.getLastWordIndexesUsed(), gr);
        //ParseTreeV2 pt2 = new ParseTreeV2("1 2 3 4", gr);

        System.out.println(pt2.toString());

    }
}
