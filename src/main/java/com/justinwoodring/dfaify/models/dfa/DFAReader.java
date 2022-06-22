package com.justinwoodring.dfaify.models.dfa;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DFAReader {

    public static State getEntrypoint(File file) throws ParserConfigurationException, SAXException, IOException{
        ArrayList<StateWrapper> state_list = new ArrayList<StateWrapper>();
        ArrayList<ConnWrapper> conn_list = new ArrayList<ConnWrapper>();
        ArrayList<State> states = new ArrayList<State>();

        //Load file
        File xmlFile = file;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);

        //Process nodes.
        NodeList stateNodes = doc.getElementsByTagName("state");
        for(int i=0; i<stateNodes.getLength(); i++)
        {
            Node stateNode = stateNodes.item(i);
            if(stateNode.getNodeType() == Node.ELEMENT_NODE)
            {
                Element stateElement = (Element) stateNode;
                state_list.add(new StateWrapper(
                    stateElement.getAttribute("name"),
                    stateElement.getAttribute("ref"),
                    (stateElement.getAttribute("final").equals("true")?true:false),
                    (stateElement.getAttribute("entry").equals("true")?true:false)
                ));

                NodeList connNodes = stateElement.getElementsByTagName("conn");
                for(int j=0; j<connNodes.getLength(); j++)
                {
                    Node connNode = connNodes.item(j);
                    Element connElement = (Element) connNode;
                    conn_list.add(new ConnWrapper(
                        stateElement.getAttribute("ref"),
                        connElement.getAttribute("to"),
                        connElement.getAttribute("takes").charAt(0)
                    ));
                }
            }
        }

        for (StateWrapper stateDesc : state_list) {
            State state = new State(stateDesc.name,stateDesc.isFinal);
            states.add(state);
        }

        for (ConnWrapper connDesc : conn_list){
            System.out.println("Connecting " + connDesc.from + " to " + connDesc.to + " with " + connDesc.takes);

            State fromState = null;
            int x = 0;
            for (StateWrapper stateDesc: state_list){
                if(connDesc.from.equals(stateDesc.ref)){
                    fromState = states.get(x);
                }
                x++;
            }

            State toState = null;
            x = 0;
            for (StateWrapper stateDesc: state_list){
                if(connDesc.to.equals(stateDesc.ref)){
                    toState = states.get(x);
                }
                x++;
            }

            System.out.println("Actually Connecting " + fromState.getName() + " to " + toState.getName() + " with " + connDesc.takes);
            try {
                fromState.connect(toState, connDesc.takes);
            } catch (Exception e) {
                throw e;
            }
        }



        int x = 0;
        for(StateWrapper stateDesc:state_list){
            if(stateDesc.isEntry){
                return states.get(x);
            }
            x++;
        }
        return null;


    }
}

class StateWrapper{
    public String name;
    public String ref;
    public boolean isFinal;
    public boolean isEntry;

    StateWrapper(String name, String ref, boolean isFinal, boolean isEntry){
        this.name=name;
        this.ref=ref;
        this.isFinal=isFinal;
        this.isEntry=isEntry;
    }
}

class ConnWrapper{
    public String from;
    public String to;
    public char takes;

    ConnWrapper(String from, String to, char takes){
        this.from = from;
        this.to = to;
        this.takes = takes;
    }
}