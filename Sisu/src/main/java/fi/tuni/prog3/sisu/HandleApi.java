package fi.tuni.prog3.sisu;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TreeItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * A class for Handling API requests
 * 
 * 
 */

public class HandleApi {
    
    public HandleApi(){
        
    }
    
    public String getApiData(String url) throws MalformedURLException,
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
    
    
    public ComboBox getInitialData(HashMap<String, JSONObject> courseDataMap)
            throws IOException{
        
        // Toistaiseksi paljon turhaa koodia!
        // Rakentaa comboboxin ja palauttaa sen, seka paivittaa kartan kaikista
        // moduuleista
        ArrayList<TreeItem<String>> items = new ArrayList<>();
        ComboBox<String> comboBox = new ComboBox<>();
        
        
        
        String dataString = getApiData("https://sis-tuni.funidata.fi/kori/api/"
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
    public TreeItem getStructureData(JSONArray jsonData, TreeItem treeItem)
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
                                            new JSONArray(getApiData(address)).get(0)
                                                          .toString());

                    TreeItem newTreeItem = new TreeItem(getItemInfo(moduleData, type));                    
                    treeItem.getChildren().add(newTreeItem);
                   
                    //Haetaan seuraava kutsumalla rekursiivisesti
                    JSONArray ruleModules = new JSONArray(getApiData(address));
                    getStructureData(ruleModules, newTreeItem);
                   
                } else if(type.equals("CompositeRule")) {
                    //Seuraava, tasta ei lisata mitaan
                    JSONArray ruleModules = obj.getJSONArray("rules");
                   
                    getStructureData(ruleModules, treeItem);

                } else if(this.modules.contains(type)) {
                    //Seuraava, tasta ei lisata mitaan
                    JSONObject nextModule = obj.getJSONObject("rule");
                   
                    JSONArray ruleModules = new JSONArray();
                    getStructureData(ruleModules.put(nextModule), treeItem);
                }
            } else {
                //Kurssi, tasta ei paase alemmaksi
                String nextModuleId = obj.get("courseUnitGroupId").toString();
                String s = "https://sis-tuni.funidata.fi/kori/api/course-units/"
                            + "by-group-id?groupId=" + nextModuleId
                            + "&universityId=tuni-university-root-id";
                JSONObject courseData = new JSONObject(
                                        new JSONArray(getApiData(s)).get(0)
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