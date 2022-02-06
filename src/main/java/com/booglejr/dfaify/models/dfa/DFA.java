package com.booglejr.dfaify.models.dfa;

import java.util.ArrayList;

import com.booglejr.dfaify.models.dfa.error.StringContainsLettersNotInAlphabetError;
import com.booglejr.dfaify.models.dfa.error.UnindexedStateError;

public class DFA {
    public String currentString;
    public int currentState;
    public int idCounter;
    public ArrayList<State> states;
    public String alphabet;
    public boolean failed;

    public DFA(State entrypoint){
        idCounter=0;
        currentState=0;
        alphabet=new String();
        states=new ArrayList<State>();
        entrypoint.setId(0);
        states.add(entrypoint);
        currentString="";
        beginIndexing();
    }

    public void sortAlphabet(){
        StringBuilder alphabet = new StringBuilder();
        alphabet.append(this.alphabet);
        int n = alphabet.length();  
        char temp = (char)0;  
         for(int i=0; i < n; i++){  
                 for(int j=1; j < (n-i); j++){  
                          if((int)alphabet.charAt(j-1) > (int)alphabet.charAt(j)){  
                                 //swap elements  
                                 temp = alphabet.charAt(j-1);  
                                 alphabet.setCharAt(j-1,alphabet.charAt(j));  
                                 alphabet.setCharAt(j,temp);  
                         }  
                          
                 }  
         }  
        this.alphabet = alphabet.toString();
    }

    public void beginIndexing() {
        State indexPoint = this.states.get(0);

        for (Connection conn : indexPoint.getConnections()) {
            index(conn);   
        }
    }

    public Integer getNewId(){
        return ++idCounter;
    }

    public State getCurrentState(){
        for (State state : states) {
            try {
                if(currentState == state.getId()){
                    return state;
                }
            } catch (UnindexedStateError e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void index(Connection conn){
        if (!alphabet.contains(String.valueOf(conn.getTakesChar()))){
            alphabet += conn.getTakesChar();
            sortAlphabet();
        }
        if (!conn.getToState().isIndexed()){
            conn.getToState().setId(this.getNewId());
            states.add(conn.getToState());
            for (Connection sub_conn : conn.getToState().getConnections()) {
                index(sub_conn);   
            }
        }
    }

    public ArrayList<State> getFinalStates(){
        ArrayList<State> statesToReturn = new ArrayList<State>();
        for(State state: this.states){
            if (state.isFinalState()){
                statesToReturn.add(state);
            }
        }

        return statesToReturn;
    }

    public String getMermaidMd(){
        StringBuilder md = new StringBuilder();
        md.append("flowchart TD\n");
        md.append("\nstart[Entrypoint]==>id0\n");
        for (State state : this.states){
            md.append("id"+state.getId()+"(("+state.getName()+"))"+(state.isFinalState()?":::final":"")+"\n");
            for(Connection conn: state.getConnections()){
                md.append("id"+state.getId()+"-->|"+conn.getTakesChar()+"| id"+conn.getToState().getId()+"\n");
            }
        }

        md.append("\nclassDef default fill:#4ECDC4,stroke:#1A535C,stroke-width:4px\n");
        md.append("\nclassDef entrypoint stroke-width:4px\n");
        md.append("\nclassDef final stroke-dasharray: 3 3\n");
        md.append("\nclassDef active fill:#FFE66D \n");
        
        md.append("\nclass id0 entrypoint\n");
        md.append("\nclass id"+this.getCurrentState().getId()+" active\n");
        return md.toString();

    }

    public boolean run(String string){
        currentString=string;
        for(int i =0;i<string.length();i++){
            if(!alphabet.contains(String.valueOf(string.charAt(i)))){
                throw new StringContainsLettersNotInAlphabetError();
            }
        }

        currentState=0;
        System.out.println("DFA Specifications");
        System.out.println("---------------------------");
        System.out.println("DFA Alphabet: " + alphabet);
        System.out.println("Entrypoint: '" + getCurrentState().getName()+"'");
        System.out.print(  "Final States: {");
        for (State state : getFinalStates()) {
            System.out.print(" '"+state.getName()+"' ");
        }
        System.out.println("}");
        System.out.println("---------------------------");
        System.out.println("Running against String: " + currentString);
        System.out.println("===========================");
        System.out.println("");
        boolean keepRunning = true;
        while(keepRunning){
            keepRunning = step();
        }

        System.out.println("===========================");
        System.out.println("Result: "+(!failed && getCurrentState().isFinalState()));
        return !failed && getCurrentState().isFinalState();
    }


    public boolean step() {
        System.out.println("Current String: " + currentString);
        System.out.println("Current State: " + getCurrentState().getName());
        System.out.println("stepping...");
        System.out.println("");
        if (currentString.length()!=0){
            char lastChar = currentString.charAt(0);
            currentString = (currentString.substring(1, currentString.length()));

            for (Connection conn : getCurrentState().getConnections()){
                if(lastChar==conn.getTakesChar()){
                    try {
                        currentState=conn.getToState().getId();
                        if(currentString!=null){
                            return true;
                        }
                    } catch (UnindexedStateError e) {
                        e.printStackTrace();
                    }
                }
            }
            failed=true;
            return false;
        }
        return false;

    }

}
