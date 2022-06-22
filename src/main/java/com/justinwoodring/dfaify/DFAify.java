package com.justinwoodring.dfaify;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;

import com.justinwoodring.dfaify.controllers.AppController;
import com.justinwoodring.dfaify.models.AppModel;
import com.justinwoodring.dfaify.models.dfa.DFA;
import com.justinwoodring.dfaify.views.AppView;

import org.apache.commons.io.IOUtils;
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

        InputStream is = DFAify.class.getResourceAsStream("/com/justinwoodring/dfaify/dfaify.png");
        final File tempFile = File.createTempFile("tempImage", ".png");
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(is, out);
        }

        Image icon = new Image("file:"+tempFile.toString());
        stage.getIcons().add(icon);

        AppModel appModel = new AppModel(DFAify.class.getResourceAsStream("/com/justinwoodring/dfaify/examples/one-bit-adder.xml"));
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
        return "v1.0.1a";
    }
}
