package com.booglejr.dfaify;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;

import com.booglejr.dfaify.controllers.AppController;
import com.booglejr.dfaify.models.AppModel;
import com.booglejr.dfaify.views.AppView;

import org.xml.sax.SAXException;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class DFAify extends Application{
    private static HostServices hostServices ;

    public static HostServices getStaticHostServices() {
        return hostServices ;
    }
    
    @Override
    public void start(Stage stage) throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
        hostServices = getHostServices();

        stage.setTitle("DFAify");
        stage.getIcons().add(new Image(this.getClass().getResource("/com/booglejr/dfaify/dfaify.png").toString()));

        AppModel appModel = new AppModel(new File(this.getClass().getResource("/com/booglejr/dfaify/examples/one-bit-adder.xml").toURI()));
        AppController appController = new AppController(appModel, stage);
        AppView appView = new AppView(appModel, appController);

        appController.attachView(appView);
        
        Scene scene = new Scene(appView, 640,480);
        stage.setScene(scene);
        stage.show();

        stage.setMinWidth(640);
        stage.setMinHeight(480);
    }

    public static void main(String[] args) {
        launch();
    }

    public static class JavaApp{
        public void loadHttpAddr(String string) {
            getStaticHostServices().showDocument(string);
        }
    }

    public static String getVersion() {
        return "v1.0.0a";
    }
}
