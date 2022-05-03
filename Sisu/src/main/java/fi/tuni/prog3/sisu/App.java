package fi.tuni.prog3.sisu;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class App extends Application{
    
    

    @Override
    public void start(Stage stage) throws IOException {
        

        var button = new Button("Sisään SISUun");
        stage.setTitle("Melkein parempi SISU");
        
        // Talletetaan kaikki moduulit tanne
        HashMap<String, JSONObject> courseDataMap = new HashMap<>();
        
        
        
        
        var scene = new Scene(new StackPane(button), 640, 480);
        
        
        HandleApi apiHandler = new HandleApi();
        
        ComboBox comboBox = apiHandler.getInitialData(courseDataMap);
        comboBox.setPromptText("Valitse tutkinto-ohjelma");
        
        //treeView.setRoot(rootItem);
        
        //ArrayList<TreeItem<String>> firstChildren = getInitialData();
        /*
        firstChildren.forEach(item -> {
            rootItem.getChildren().add(item);
        });
        */
        Button btn1 = new Button("Takaisin");
        
        
        
        // Valitaan
        comboBox.setOnAction(new EventHandler() {
            @Override
            public void handle(Event e) {
                String clickedName = comboBox.getValue().toString();
                var jsonObj = courseDataMap.get(clickedName);
                
                //System.out.println(jsonObj);
                
                String groupId = jsonObj.getString("groupId");
                String name = jsonObj.getString("name");
                
                // Asetetaan osoite, josta lahdetaan hakemaan
                String address = "https://sis-tuni.funidata.fi/kori/api/modules/"
                        + "by-group-id?groupId=" + groupId
                        + "&universityId=tuni-university-root-id";
                
                
                try {
                    TreeItem<String> rootItem = new TreeItem<>("Tutkinto-ohjelma");
                    JSONArray jsonArr = new JSONArray(apiHandler.getApiData(address));
                    
                    rootItem.setValue(name);
                    
                    try {
                        // Luodaan treeview, VBox ja scene, asetetaan scene
                        // nakymaan
                        TreeView treeView = new TreeView();
                        // Haetaan koko rakenne
                        apiHandler.getStructureData(jsonArr, rootItem);
                        //moduleInfo(jsonArr, rootItem);
                        VBox treeBox = new VBox();
                        treeBox.getChildren().add(btn1);
        
        
                        Scene scene3 = new Scene(treeBox, 1280, 720);
                        
                        treeView.setRoot(rootItem);
                        treeBox.getChildren().add(treeView);
                        
                        
                        
                        
                        stage.setScene(scene3);
                    } catch (IOException ex) {
                        Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    
                } catch (IOException ex) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        VBox rootBox = new VBox();
        rootBox.getChildren().add(comboBox);
        
        Scene scene2 = new Scene(rootBox,1280,720);
        
        // Takaisin valitsemaan tutkinto-ohjelmaa
        btn1.setOnAction((ActionEvent e) -> {
            stage.setScene(scene2);
            
        });
        
        button.setOnAction(e -> stage.setScene(scene2));
        
        
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
    
    


}