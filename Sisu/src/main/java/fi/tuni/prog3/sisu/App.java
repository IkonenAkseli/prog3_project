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
import javafx.geometry.Pos;
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
        
        // Talletetaan kaikki opintosuunnat tanne
        HashMap<String, JSONObject> courseDataMap = new HashMap<>();
        Button btnClose = new Button(); 
        btnClose.setText("Sulje"); 
        btnClose.setOnAction( e -> stage.close() );
        
        VBox startBox = new VBox(button, btnClose);
        startBox.setAlignment(Pos.CENTER);
        
        var scene = new Scene(startBox, 1280, 720);
        
        
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
        Button btnBack = new Button("Takaisin");
        
        
        
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
                        Button btnCloseScene3 = new Button();
                        btnCloseScene3.setText("Sulje"); 
                        btnCloseScene3.setOnAction( e3 -> stage.close() );
                        
                        
        
        
                        Scene scene3 = new Scene(treeBox, 1280, 720);
                        
                        treeView.setRoot(rootItem);
                        treeBox.getChildren().add(treeView);
                        treeBox.getChildren().addAll(btnBack, btnCloseScene3);
                        
                        
                        
                        stage.setScene(scene3);
                    } catch (IOException ex) {
                        Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    
                } catch (IOException ex) {
                    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        Button btnCloseScene2 = new Button();
        btnCloseScene2.setText("Sulje"); 
        btnCloseScene2.setOnAction( e -> stage.close() );
        
        
        VBox rootBox = new VBox();
        rootBox.getChildren().addAll(comboBox, btnCloseScene2);
        
        Scene scene2 = new Scene(rootBox,1280,720);
        
        // Takaisin valitsemaan tutkinto-ohjelmaa
        btnBack.setOnAction((ActionEvent e) -> {
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