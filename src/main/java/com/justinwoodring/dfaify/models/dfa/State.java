package com.justinwoodring.dfaify.models.dfa;

import java.util.ArrayList;

import com.justinwoodring.dfaify.models.dfa.error.TooManyConnectionsTakingSameCharError;
import com.justinwoodring.dfaify.models.dfa.error.UnindexedStateError;

public class State {
    private Integer id;
    private String name;
    private boolean isFinalState;
    private ArrayList<Connection> connections;

    State(String name, boolean isFinalState){
        this.id=-1;
        this.name = name;
        this.isFinalState = isFinalState;
        this.connections = new ArrayList<Connection>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<Connection> getConnections() {
        return connections;
    }

    public boolean isFinalState() {
        return isFinalState;
    }

    public void connect(State toState, char accepts) throws TooManyConnectionsTakingSameCharError{
        boolean isOk = true;
        for (Connection connection : connections) {
            if(accepts==connection.getTakesChar()){
                isOk=false;
            }
        }

        if (isOk){
            connections.add(new Connection(toState, accepts));
        }else{
            throw new TooManyConnectionsTakingSameCharError();
        }
}


    public boolean isIndexed(){
        return (id>=0) ? true : false;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() throws UnindexedStateError {
        if(id==-1){
            throw new UnindexedStateError();
        }
        return id;
    }
}
