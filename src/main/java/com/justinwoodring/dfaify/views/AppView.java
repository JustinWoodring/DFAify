package com.justinwoodring.dfaify.views;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Base64;

import com.justinwoodring.dfaify.controllers.AppController;
import com.justinwoodring.dfaify.models.AppModel;

import org.w3c.dom.Document;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Animation.Status;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public class AppView extends StackPane{
    private AppModel model;
    private AppController controller;

    public AboutView about;

    public Label statusDisplay;
    public Label stringDisplay;

    public Button openFile;
    public Button resetButton;
    public Button loadString;
    public Button playButton;
    public Button makeFast;
    public Button makeSlow;
    public Button stepForward;
    public Button showAbout;

    public Timeline timeline;
    public WebEngine webEngine;
    public TextArea textArea;

    public FileChooser fc;

    public AppView(AppModel model, AppController controller){
        this.about = new AboutView();
        WebView webView = new WebView();
        this.webEngine = webView.getEngine();
        
        webEngine.getLoadWorker().stateProperty().addListener(
            new ChangeListener<javafx.concurrent.Worker.State>() {
                public void changed(ObservableValue ov, javafx.concurrent.Worker.State oldState, javafx.concurrent.Worker.State newState) {
                    if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {


                        Document doc = webEngine.getDocument();
                        doc.getElementById("mermaiddivinput").setTextContent(
                            Base64.getEncoder().encodeToString(
                                model.dfa.getMermaidMd().getBytes()
                            )
                        );


                        controller.stepDfa();
                        timeline = new Timeline(new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                controller.stepDfa();
                                Document doc = webEngine.getDocument();
                                doc.getElementById("mermaiddivinput").setTextContent(
                                    Base64.getEncoder().encodeToString(
                                        model.dfa.getMermaidMd().getBytes()
                                    )
                                );
                            }
                        }));

                    }
                }
            }
        );

        URL url = this.getClass().getResource("/com/justinwoodring/dfaify/webView/gui.html");
        webEngine.load(url.toString());

        //Scene Declaration
        fc = new FileChooser();

        TextInputDialog td = new TextInputDialog();
        td.setHeaderText("Enter a DFA string");
        td.setTitle("DFA Input");
        td.setGraphic(null);

        this.openFile = new Button("Open DFA");
        openFile.setPrefWidth(150);
        openFile.setOnAction((event)  -> {
            controller.loadNewDFA();
            Document doc = webEngine.getDocument();
                doc.getElementById("mermaiddivinput").setTextContent(
                    Base64.getEncoder().encodeToString(
                        model.dfa.getMermaidMd().getBytes()
                    )
                );   
        });

        this.resetButton = new Button("Reset");
        resetButton.setPrefWidth(150);
        resetButton.setOnAction((event)  -> {
            controller.reset();
            Document doc = webEngine.getDocument();
                doc.getElementById("mermaiddivinput").setTextContent(
                    Base64.getEncoder().encodeToString(
                        model.dfa.getMermaidMd().getBytes()
                    )
                );   
        });

        this.loadString = new Button("Load String");
        loadString.setPrefWidth(150);
        loadString.setOnAction((event)  -> {
            td.showAndWait();
            controller.setString(td.getEditor().getText());
        });

        this.stepForward = new Button("Step");
        stepForward.setPrefWidth(150);
        stepForward.setOnAction((event)  -> {
            if(timeline.getStatus()==Status.STOPPED){            
                controller.stepDfa();
                Document doc = webEngine.getDocument();
                doc.getElementById("mermaiddivinput").setTextContent(
                    Base64.getEncoder().encodeToString(
                        model.dfa.getMermaidMd().getBytes()
                    )
                ); 
            }
        });

        this.playButton = new Button("Play");
        playButton.setPrefWidth(150);
        playButton.setOnAction((event) -> {
            controller.togglePlaybackTimeline();
        });

        this.makeFast = new Button("Increase Rate");
        makeFast.setPrefWidth(150);
        makeFast.setOnAction((event) -> {
            timeline.setRate(timeline.getRate()*.5+timeline.getRate());
        });

        this.makeSlow = new Button("Decrease Rate");
        makeSlow.setPrefWidth(150);
        makeSlow.setOnAction((event) -> {
            timeline.setRate(-timeline.getRate()*.5+timeline.getRate());
        });

        this.showAbout = new Button("About DFAify");
        showAbout.setPrefWidth(150);
        showAbout.setOnAction((event) -> {
            controller.showAbout();
        });

        this.statusDisplay = new Label("");
        this.stringDisplay = new Label("");

        VBox buttonPanel = new VBox(openFile, resetButton,new Separator(Orientation.HORIZONTAL),loadString, stepForward, new Separator(Orientation.HORIZONTAL),makeSlow,playButton, makeFast,new Separator(Orientation.HORIZONTAL), showAbout);
        buttonPanel.setSpacing(10);
        buttonPanel.setPadding(new Insets(5));
        buttonPanel.setFillWidth(true);
        buttonPanel.setAlignment(Pos.TOP_CENTER);

        textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);
        try {
            textArea.setFont(Font.loadFont(this.getClass().getResource("/com/justinwoodring/dfaify/fonts/DejaVuSansMono.ttf").toURI().toString(), 12));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        SplitPane split = new SplitPane(webView, textArea);
        BorderPane bpane = new BorderPane(split);
        bpane.setLeft(buttonPanel);

        VBox vbox = new VBox(stringDisplay, bpane, statusDisplay);
        vbox.setPrefHeight(Integer.MAX_VALUE);
        vbox.setAlignment(Pos.CENTER);
        vbox.setBackground(new Background(new BackgroundFill(Color.rgb(225, 225, 225), CornerRadii.EMPTY, Insets.EMPTY)));

        VBox.setVgrow(bpane, Priority.ALWAYS);

        this.getChildren().add(vbox);
    }
}
