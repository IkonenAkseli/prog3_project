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
        
        
        
        
        ComboBox comboBox = getInitialData(courseDataMap);
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
                    JSONArray jsonArr = new JSONArray(APISearch(address));
                    
                    rootItem.setValue(name);
                    
                    try {
                        // Luodaan treeview, VBox ja scene, asetetaan scene
                        // nakymaan
                        TreeView treeView = new TreeView();
                        // Haetaan koko rakenne
                        moduleInfo(jsonArr, rootItem);
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
    
    
    private String APISearch(String url) throws MalformedURLException,
                                                IOException {
        //Otetaan yhteys apiin ja haetaan sielta tietoja
        URL address = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) address.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        StringBuilder sb = new StringBuilder();
        //Talletetaan tiedot Stringiin
        try (Scanner scanner = new Scanner(address.openStream())) {
            
            while(scanner.hasNext()) {
                sb.append(scanner.nextLine());
            }
        }
       
        return sb.toString();
    }  

    
    private ComboBox getInitialData(HashMap<String, JSONObject> courseDataMap)
            throws IOException{
        
        // Toistaiseksi paljon turhaa koodia!
        // Rakentaa comboboxin ja palauttaa sen, seka paivittaa kartan kaikista
        // moduuleista
        ArrayList<TreeItem<String>> items = new ArrayList<>();
        ComboBox<String> comboBox = new ComboBox<>();
        
        String dataString = APISearch("https://sis-tuni.funidata.fi/kori/api/"
                + "module-search?curriculumPeriodId=uta-lvv-2021&universityId"
                + "=tuni-university-root-id&moduleType=DegreeProgramme&limit="
                + "1000");
        
        JSONArray jsonData = createJson(dataString);
        
        for (Object module : jsonData){
            JSONObject obj = new JSONObject(module.toString());
            String name = obj.get("name").toString();
            
            courseDataMap.put(name, obj);
            
            TreeItem<String> item = new TreeItem<>(name);
            items.add(item);
            comboBox.getItems().add(name);
        }
        
        
        return comboBox;
    }



    
    
    private JSONArray createJson(String data){
        // Vain tutkinto-ohjelman alustamista varten
        JSONObject jsonObject = new JSONObject(data);
        JSONArray jsonArray = jsonObject.getJSONArray("searchResults");
        return jsonArray;
    }
    
    //Haetaan kaikki rekursiivisesti apista
    private TreeItem moduleInfo(JSONArray jsonData, TreeItem treeItem)
                                throws IOException {          
                       
        for(Object module : jsonData) {

            JSONObject obj = new JSONObject(module.toString());
            String type = obj.get("type").toString();
           
            if(!type.equals("CourseUnitRule")) {

                if(type.equals("ModuleRule")) {
                   
                    //Seuraava osoite
                    String nextModuleId = obj.get("moduleGroupId").toString();
                    String address = "https://sis-tuni.funidata.fi/kori/api/modules/"
                                + "by-group-id?groupId=" + nextModuleId
                                + "&universityId=tuni-university-root-id";

                    JSONObject moduleData = new JSONObject(
                                            new JSONArray(APISearch(address)).get(0)
                                                          .toString());

                    TreeItem newTreeItem = new TreeItem(getItemInfo(moduleData, type));                    
                    treeItem.getChildren().add(newTreeItem);
                   
                    //Haetaan seuraava kutsumalla rekursiivisesti
                    JSONArray ruleModules = new JSONArray(APISearch(address));
                    moduleInfo(ruleModules, newTreeItem);
                   
                } else if(type.equals("CompositeRule")) {
                    //Seuraava, tasta ei lisata mitaan
                    JSONArray ruleModules = obj.getJSONArray("rules");
                   
                    moduleInfo(ruleModules, treeItem);

                } else if(this.modules.contains(type)) {
                    //Seuraava, tasta ei lisata mitaan
                    JSONObject nextModule = obj.getJSONObject("rule");
                   
                    JSONArray ruleModules = new JSONArray();
                    moduleInfo(ruleModules.put(nextModule), treeItem);
                }
            } else {
                //Kurssi, tasta ei paase alemmaksi
                String nextModuleId = obj.get("courseUnitGroupId").toString();
                String s = "https://sis-tuni.funidata.fi/kori/api/course-units/"
                            + "by-group-id?groupId=" + nextModuleId
                            + "&universityId=tuni-university-root-id";
                JSONObject courseData = new JSONObject(
                                        new JSONArray(APISearch(s)).get(0)
                                                      .toString());              
               
                TreeItem item = new TreeItem(getItemInfo(courseData, type));                  
                treeItem.getChildren().add(item);
            }
        }
        return treeItem;
    }




    
    



    private final ArrayList<String> modules = new ArrayList<String>() {
        {
            add("CreditsRule");
            add("StudyModule");
            add("DegreeProgramme");
            add("GroupingModule");
        }
    };
    

    
    private String getItemInfo(JSONObject object, String type){

        String completeString = "";
        String name = "";
        String description = "";

        // Haetaan moduulin tiedot riippuen moduulin tyypista
        if(type.equals("ModuleRule")) {
            try {
                byte[] sBytes = new JSONObject(object.get("name").toString())
                                               .get("fi").toString().getBytes();
                completeString = new String(sBytes, StandardCharsets.UTF_8);
            } catch(JSONException e) {
                byte[] sBytes = new JSONObject(object.get("name").toString())
                                               .get("en").toString().getBytes();
                completeString = new String(sBytes, StandardCharsets.UTF_8);
            }
        } else if(type.equals("CourseUnitRule")) {

            try {
                name = new JSONObject(object.get("name").toString())
                                            .get("fi").toString(); 
            } catch(JSONException e) {
                name = new JSONObject(object.get("name").toString())
                                            .get("en").toString();
            } try {
                description = new JSONObject(object.get("content").toString())
                                                   .get("fi").toString();
            } catch(JSONException e) {

                try {
                    description = new JSONObject(object.get("content")
                                              .toString()).get("en").toString();
                } catch(JSONException exc) {

                    try {
                        description = new JSONObject(object.get("tweetText")
                                        .toString()).getString("fi");
                    } catch(JSONException exc2) {
                        description = "Description not available";
                    } 
                }
            }
            finally {
                int credits = Integer.parseInt(
                              new JSONObject(object.get("credits").toString())
                                                   .get("min").toString());
                
                completeString = name + "\n" + description + "\nOpintopisteet: " + credits;
            }
        }
        return completeString;
    }


}