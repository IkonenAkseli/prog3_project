package fi.tuni.prog3.sisu;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.application.Application;
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
import org.json.JSONObject;


public class App extends Application{

    @Override
    public void start(Stage stage) throws IOException {
        

        var button = new Button("SISU");
        
        
        
        
        var scene = new Scene(new StackPane(button), 640, 480);
        
        
        
        ComboBox comboBox = getInitialData();
        TreeItem<String> rootItem = new TreeItem<>("Tutkinto-ohjelmat");
        //treeView.setRoot(rootItem);
        
        //ArrayList<TreeItem<String>> firstChildren = getInitialData();
        /*
        firstChildren.forEach(item -> {
            rootItem.getChildren().add(item);
        });
        */
        
        VBox rootBox = new VBox();
        rootBox.getChildren().add(comboBox);
        
        Scene scene2 = new Scene(rootBox,1920,1080);
        
        stage.setScene(scene2);
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

    
    private ComboBox getInitialData() throws IOException{
        
        // Toistaiseksi paljon turhaa koodia!
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
            
            TreeItem<String> item = new TreeItem<>(name);
            items.add(item);
            comboBox.getItems().add(name);
        }
        
        
        return comboBox;
    }



    
    
    private JSONArray createJson(String data){
        JSONObject jsonObject = new JSONObject(data);
        JSONArray jsonArray = jsonObject.getJSONArray("searchResults");
        return jsonArray;
    }
    
    
    



}