package com.justinwoodring.dfaify.views;

import java.net.URL;

import com.justinwoodring.dfaify.DFAify;
import com.justinwoodring.dfaify.DFAify.JavaApp;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Popup;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
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
import javafx.concurrent.Worker.State;
import javafx.concurrent.Worker;

public class AboutView extends Stage {
    AboutView(){
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends State> ov, State t, State t1) {
                if (t1 == Worker.State.SUCCEEDED) {
                    JSObject window = (JSObject) webEngine.executeScript("window");
                    window.setMember("app", new JavaApp());
                    webEngine.executeScript("document.getElementById('version').textContent = '"+DFAify.getVersion()+"';");
                }
            }
        });
        

        URL url = this.getClass().getResource("/com/justinwoodring/dfaify/webView/about.html");
        webEngine.load(url.toString());

        Button hide = new Button("Hide");
        hide.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                hide();
            }
        });

        HBox hbox = new HBox(hide);
        hbox.setAlignment(Pos.TOP_CENTER);
        hbox.setPadding(new Insets(10));
        VBox vbox = new VBox(webView, hbox);
        
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setSpacing(20);
        vbox.setBackground(new Background(new BackgroundFill(Color.rgb(255,255,255), CornerRadii.EMPTY, Insets.EMPTY)));
        VBox.setVgrow(webView, Priority.ALWAYS);
        StackPane container = new StackPane(vbox);
        Scene scene = new Scene(container, 360,420);
        this.setScene(scene);

        this.setTitle("About DFAify");
    }
}