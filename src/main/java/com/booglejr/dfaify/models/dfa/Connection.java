package com.booglejr.dfaify.models.dfa;

public class Connection {
    private State toState;
    private char takenChar;

    Connection(State toState, char takenChar){
        this.toState = toState;
        this.takenChar = takenChar;
    }

    public State getToState() {
        return toState;
    }

    public char getTakesChar() {
        return takenChar;
    }
}
