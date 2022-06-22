package com.justinwoodring.dfaify.models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import com.justinwoodring.dfaify.models.dfa.DFA;
import com.justinwoodring.dfaify.models.dfa.DFAReader;
import com.justinwoodring.dfaify.models.dfa.error.TooManyConnectionsTakingSameCharError;

import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

public class AppModel {
    public DFA dfa;
    public String log;

    public AppModel(InputStream is) throws FileNotFoundException, IOException{
        final File tempFile = File.createTempFile("temp", ".xml");
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(is, out);
        }
        loadNewDFA(tempFile);
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
