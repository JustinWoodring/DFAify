package com.booglejr.dfaify.models;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import com.booglejr.dfaify.models.dfa.DFA;
import com.booglejr.dfaify.models.dfa.DFAReader;
import com.booglejr.dfaify.models.dfa.error.TooManyConnectionsTakingSameCharError;

import org.xml.sax.SAXException;

public class AppModel {
    public DFA dfa;
    public String log;

    public AppModel(File file){
        loadNewDFA(file);
        log = new String();
    }

    public boolean loadNewDFA(File file){
        try {
            this.dfa = new DFA(DFAReader.getEntrypoint(file));
            return true;
        } catch (ParserConfigurationException | SAXException | IOException | TooManyConnectionsTakingSameCharError e ) {
            e.printStackTrace();
            return false;
        }
    }

    public String getLog(){
        return this.log;
    }

    public void logString(String input){
        this.log+="\n"+input+"\n";
    }
}
