package ubbcluj.Model;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class SScanner {
    public static final List<String> separators = Collections.unmodifiableList(Arrays.asList("(",")","[","]","{","}",";",","," ","\n","\t","\"","'"));
    public static final List<String> possibleOperators = Collections.unmodifiableList(Arrays.asList("&","|","<",">","="));
    public static final List<String> operators = Collections.unmodifiableList(Arrays.asList("&&","||","+","-","*","/","=","<","<=","==","!=",">=",">","!","%"));
    public static final List<String> reservedWords = Collections.unmodifiableList(Arrays.asList("ARRAY","INT","CHAR","STRING","READ","PRINT","IF","ELSE","WHILE"));

    private String fileName;
    private String fileTable;

    private HHashTable<String,Integer> codificationTable;
    private List<Pair<Integer,Integer>> PIF;
    private SymbolTable ST;
    private boolean error;
    private FiniteAutomata FAide;
    private FiniteAutomata FAcons;

    public SScanner(String fileName, String fileTable){
        this.fileName = fileName;
        this.fileTable = fileTable;
        this.codificationTable = readCodificationTable();
        this.PIF = new ArrayList<>();
        this.ST = new SymbolTable();
        this.error = false;
        this.FAide = new FiniteAutomata("FAidentifier.txt");
        this.FAcons = new FiniteAutomata("FAconstant.txt");
    }

    public HHashTable<String, Integer> readCodificationTable(){
        HHashTable<String, Integer> table = new HHashTable<>();
        try{
            File file = new File("/Users/Dan/IdeaProjects/FLCD/src/resources/" + this.fileTable);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String str;
            String[] list;
            int  i = 0;
            while ((str = br.readLine()) != null) {
                list = str.split(" ");
                table.put(list[0], Integer.parseInt(list[1]));
            }
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Exception while reading from table file!");
        }
        return table;
    }

    public String parseFile(){
        String content = "";
        try{
            File file = new File("/Users/Dan/IdeaProjects/FLCD/src/resources/" + this.fileName);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String str;
            while ((str = br.readLine()) != null) {
                content += str+ "\n";
            }
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Exception while reading from file!");
        }
        return content;
    }

    public boolean isSeparator(String ch){
        return separators.contains(ch);
    }

    public boolean isOperator(String ch){
        return operators.contains(ch);
    }

    public boolean isReservedWord(String ch){
        return reservedWords.contains(ch.toUpperCase());
    }

    public boolean isIdentifier(String ch){
        Pattern pattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9]{0,249}");
       /* if (ch.length() > 250) {
            return false;
        }*/
        return (pattern.matcher(ch).matches());
    }

    public boolean isConstant(String ch){
        return (isCharacter(ch) || isInteger(ch) || isString(ch));
    }

    public boolean isString(String ch){
        Pattern pattern = Pattern.compile("\"(\\.|[^\"])*\"");
        return (pattern.matcher(ch).matches());
        //return true;
    }

    public boolean isCharacter(String ch){
        Pattern pattern = Pattern.compile("^'[a-zA-Z0-9]*'$");
        return (pattern.matcher(ch).matches());
    }

    public boolean isInteger(String ch){
        Pattern pattern = Pattern.compile("[-]?\\d+");
        return (pattern.matcher(ch).matches());
    }


    public List<String> detectTokens(){
        String content = parseFile();
        //System.out.println(content);
        String tokenChar = "";
        String tokenOp = "";
        String tokenSTR = "";
        String tokenCHR = "";
        List<String> tokens = new ArrayList<String>();
        int i = 0;
        while(i < content.length()){
            String ch = Character.toString(content.charAt(i));
            //System.out.println(ch);
            while (!isSeparator(ch) && !isOperator(ch)){
                if(possibleOperators.contains(ch) && i+1 < content.length()){ // maybe I have a complex operator of 2 chars
                    String nextch = Character.toString(content.charAt(i+1));
                    if (isOperator(ch+nextch)){
                        tokens.add(ch+nextch);  // if i do, add it to the token list
                        i++; i++;  // jump over the next character
                        break;
                    }
                }
                tokenChar += ch;
                i++;
                if (i < content.length()) {
                    ch = Character.toString(content.charAt(i));
                }
                else{
                    break;
                }
            }
            if (tokenChar != ""){
                //System.out.println(i + " " + tokenChar);
                tokens.add(tokenChar);
                tokenChar = "";
            }
            while(isSeparator(ch)){
                if(ch.equals("\"")){  // string detection
                    tokenSTR+= ch;
                    i++;
                    if (i<content.length()){
                        String nextch = Character.toString(content.charAt(i));

                        while(!nextch.equals("\"") && i<content.length()){
                            tokenSTR+=nextch;
                            i++;
                            nextch = Character.toString(content.charAt(i));
                        }
                        tokenSTR+=nextch;
                        tokens.add(tokenSTR);
                        i++;
                        if (i < content.length()) {
                            ch = Character.toString(content.charAt(i));
                        } else {
                            break;
                        }
                    }
                    else {
                            tokenSTR+=Character.toString(content.charAt(i));
                            tokens.add(tokenSTR);
                            break;
                    }
                    tokenSTR="";
                }
                else if(ch.equals("'")){ //char detection
                    tokenCHR+=ch;
                    i++;
                    if (i<content.length()) {
                        String nextch = Character.toString(content.charAt(i));
                      /*  if(!nextch.equals("'") && isSeparator(nextch)){
                            tokens.add(ch); break;
                        }*/
                        if (nextch.equals("'")) {
                            tokenCHR += nextch;
                            tokens.add(tokenCHR);
                        } else {
                            tokenCHR += nextch;
                            i++;
                            if (i<content.length()){
                                nextch = Character.toString(content.charAt(i));
                                if (nextch.equals("'")) {
                                    tokenCHR += nextch;
                                    tokens.add(tokenCHR);
                                } else{ tokens.add(tokenCHR); i--; }
                            } else{ tokens.add(tokenCHR); tokenCHR=""; break;}
                        }
                    }
                    else{tokens.add(ch); break;}
                    tokenCHR="";
                    i++;
                    if (i < content.length()) {
                        ch = Character.toString(content.charAt(i));
                    } else {
                        break;
                    }
                }
                else {  // if not string or char
                    tokens.add(ch);
                    i++;
                    if (i < content.length()) {
                        ch = Character.toString(content.charAt(i));
                    } else {
                        break;
                    }
                }
            }
            while(isOperator(ch)){
                tokenOp += ch;
                i++;
                if (i < content.length()) {
                    ch = Character.toString(content.charAt(i));
                }
                else{
                    break;
                }
            }
            if (tokenOp != ""){
                tokens.add(tokenOp);
                tokenOp = "";
            }
        }
        tokens = removeWhitespaces(tokens);
        //System.out.println(tokens);
        return tokens;
    }

    public List<String> removeWhitespaces(List<String> str){
        List<String> output  = new ArrayList<String>();
        Pattern pattern = Pattern.compile("[^\\S\\r\\n]+");
        for (int i = 0; i < str.size(); i++){
            if (!pattern.matcher(str.get(i)).matches()){
                output.add(str.get(i));
            }
        }
        return output;
    }

    public List<Pair<String,Integer>> addToST(){
        List<String> tokens = detectTokens();
        List<Pair<String,Integer>> printCollector = new ArrayList<>();
        //System.out.println(tokens.isEmpty());
        int i=1;
        for (String token : tokens){
            //System.out.println(token);
            if (isReservedWord(token) || isOperator(token) || isSeparator(token)){
                continue;
            }
            else if (/*isIdentifier(token)*/FAide.check(token)){
                boolean ex = ST.TIcontains(token);
                if(!ex){
                    ST.addtoTI(token,i);
                    //System.out.println("STI: " + token);
                    printCollector.add(new Pair(token,i));
                    i++;
                }
            }
            else if (/*isConstant(token)*/FAcons.check(token)){
                    boolean ex = ST.TCcontains(token);
                    if(!ex) {
                        ST.addtoTC(token, i);
                        //System.out.println("STC: " + token);
                        printCollector.add(new Pair(token, i));
                        i++;
                    }
            }
        }
        //System.out.println(ST);
        return printCollector;
    }

    public List<Pair<Integer,Integer>> addToPIF(){
        List<String> tokens = detectTokens();
        Integer line = 0;
        for (String token : tokens){
            if (isReservedWord(token) || isOperator(token) || isSeparator(token) && !token.equals("'") && !token.equals("\"")){
                //if(codificationTable.get(token)==null) System.out.println(token);
                if (!token.equals("\n")){
                this.PIF.add(new Pair(codificationTable.get(token.toUpperCase()),0));} else {line++;}
            }
            else{
                if (/*isIdentifier(token)*/FAide.check(token)){
                    Integer stp = ST.getfromTI(token);
                    this.PIF.add(new Pair(1, stp));
                }
                else if (/*isConstant(token)*/FAcons.check(token)){
                    Integer stp = ST.getfromTC(token);
                    this.PIF.add(new Pair(2, stp));
                }
                else{
                    this.error = true;
                    System.out.println("Lexical Error: "+ token + " is not defined @ line: "+line);
                    break;
                }
            }
        }
        //System.out.println(PIF);
        return this.PIF;
    }

    public void outputSTPIF(){
        List<Pair<String,Integer>> STprint = addToST();
        List<Pair<Integer,Integer>> PIFprint = addToPIF();
        try {


            File STfile = new File("/Users/Dan/IdeaProjects/FLCD/src/resources/ST.out");
            if (STfile.createNewFile()) {
                System.out.println("File created: " + STfile.getName());
            } else {
                System.out.println("File already exists: " + STfile.getName());
            }
            FileWriter fileWriter = new FileWriter(STfile);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            for(Pair p : STprint){
                String key =(String) p.getKey();
                Integer value = (Integer) p.getValue();
                printWriter.println(key+" "+ value);
            }
            printWriter.close();
            fileWriter.close();


            File PIFfile = new File("/Users/Dan/IdeaProjects/FLCD/src/resources/PIF.out");
            if (PIFfile.createNewFile()) {
                System.out.println("File created: " + STfile.getName());
            } else {
                System.out.println("File already exists: " + PIFfile.getName());
            }
            fileWriter = new FileWriter(PIFfile);
            printWriter = new PrintWriter(fileWriter);
            for(Pair p : PIFprint){
                Integer key =(Integer) p.getKey();
                Integer value = (Integer) p.getValue();
                printWriter.println(key+" "+ value);
            }
            printWriter.close();
            fileWriter.close();


        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
