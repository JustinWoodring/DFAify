package com.booglejr.dfaify.controllers;
import java.io.File;

import com.booglejr.dfaify.models.AppModel;
import com.booglejr.dfaify.models.dfa.Connection;
import com.booglejr.dfaify.models.dfa.error.StringContainsLettersNotInAlphabetError;
import com.booglejr.dfaify.models.dfa.error.UnindexedStateError;
import com.booglejr.dfaify.views.AppView;

import javafx.animation.Animation.Status;
import javafx.stage.Stage;

public class AppController {
    private AppModel model;
    private AppView view;
    private Stage stage;

    public AppController(AppModel model, Stage stage){
        this.model = model;
        this.stage = stage;
        this.view = null;
    }

    public void attachView(AppView view){
        this.view=view;
    }

    public void loadNewDFA(){
        log("Loading new DFA...");
        File file = view.fc.showOpenDialog(stage);
 
        if (file != null) {
            log((model.loadNewDFA(file)?"Loading successful":"Errors were encountered loading the file specified."));
        }
        reset();
    }

    public void reset(){
        log("Resetting...");
        model.dfa.failed=false;
        model.dfa.currentState=0;
        view.timeline.stop();
        view.playButton.setText("Play");
        setString("");
        stepDfa();
    }

    public boolean stepDfa(){
        for(int i =0;i<model.dfa.currentString.length();i++){
            if(!model.dfa.alphabet.contains(String.valueOf(model.dfa.currentString.charAt(i)))){
                log("String contains errors not in the DFA's alphabet...");
                throw new StringContainsLettersNotInAlphabetError();
            }
        }

        if(model.dfa.currentString.equals("")){
            view.statusDisplay.setText((!model.dfa.failed && model.dfa.getCurrentState().isFinalState())?"Accepted String":"Bad String");
            view.stepForward.setDisable(true);
            view.playButton.setDisable(true);
            if(view.timeline!=null){
                view.timeline.stop();
                view.playButton.setText("Play");
            }
        }
        view.stringDisplay.setText(model.dfa.currentString);
        if (model.dfa.currentString.length()!=0){
            char lastChar = model.dfa.currentString.charAt(0);
            model.dfa.currentString = (model.dfa.currentString.substring(1, model.dfa.currentString.length()));
            view.stringDisplay.setText(model.dfa.currentString);

            for (Connection conn : model.dfa.getCurrentState().getConnections()){
                if(lastChar==conn.getTakesChar()){
                    try {
                        log("Taking " + lastChar + " to get from " + model.dfa.getCurrentState().getName() + " to " + conn.getToState().getName());
                        model.dfa.currentState=conn.getToState().getId();
                        if(model.dfa.currentString.equals("")){
                            view.statusDisplay.setText((!model.dfa.failed && model.dfa.getCurrentState().isFinalState())?"Accepted String":"Bad String");
                            view.stepForward.setDisable(true);
                            view.playButton.setDisable(true);
                            if(view.timeline!=null){
                                view.timeline.stop();
                                view.playButton.setText("Play");
                            }
                        }
                        if(model.dfa.currentString!=null){
                            return true;
                        }
                    } catch (UnindexedStateError e) {
                        e.printStackTrace();
                    }
                }
            }
            model.dfa.failed=true;
            return false;
        }
        return false;
    }

    public void setString(String string){
        model.dfa.failed=false;
        view.stepForward.setDisable(false);
        view.playButton.setDisable(false);
        model.dfa.currentString=string;
        view.stringDisplay.setText(model.dfa.currentString);
        view.statusDisplay.setText("");
        //stepDfa();
    }

    public void togglePlaybackTimeline(){
        if(view.timeline.getStatus()==Status.STOPPED){
            view.timeline.setCycleCount(model.dfa.currentString.length()+1);
            view.stepForward.setDisable(true);
            view.timeline.play();
            view.playButton.setText("Pause");
        }else{
            view.timeline.stop();
            view.playButton.setText("Play");
            view.stepForward.setDisable(false);
        }
    }

    public void log(String string){
        model.logString(string);
        view.textArea.setText(model.log);
    }

    public void showAbout(){
        view.about.show();
    }
}
