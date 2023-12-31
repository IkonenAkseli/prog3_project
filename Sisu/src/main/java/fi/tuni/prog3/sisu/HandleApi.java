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
import javafx.scene.input.MouseEvent;
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
    
    private HashMap<String, Course> allCourses = new HashMap<>();
    private HashMap<String, JSONObject> bachDegrees = new HashMap<>();
    private HashMap<String, JSONObject> mastDegrees = new HashMap<>();
    private HashMap<String, JSONObject> doctDegrees = new HashMap<>();
    private HashMap<String, JSONObject> miscDegrees = new HashMap<>();
    
    public HandleApi(){
        
    }
	
    /**
     * Fetches specific JSON-data from api and returns it as a String.
     * @param url Address from which to fetch data.
     * @return Fetched data as a String.
     * @throws MalformedURLException
     * @throws IOException 
     */
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
            scanner.close();
        }
        
        connection.disconnect();
        
        return sb.toString();
    } 
    
    
    /**
     * Fetches every programme from sisu api and stores it in the HashMap that
     * is provided as a parameter. Also saves programmes within the class by
     * their level.
     * Returns a ComboBox with all of the
     * programme names stored in it.
     * @param courseDataMap HashMap in which all programmes are stored.
     * @return ComboBox containing all programme names.
     * @throws IOException 
     */
    public ComboBox getInitialData(HashMap<String, JSONObject> courseDataMap)
            throws IOException{
        
        // Toistaiseksi paljon turhaa koodia!
        // Rakentaa comboboxin ja palauttaa sen, seka paivittaa kartan kaikista
        // moduuleista
        
        ComboBox<String> comboBox = new ComboBox<>();
        
        
        
        String dataString = getApiData("https://sis-tuni.funidata.fi/kori/api/"
                + "module-search?curriculumPeriodId=uta-lvv-2021&universityId"
                + "=tuni-university-root-id&moduleType=DegreeProgramme&limit="
                + "1000");
        
        JSONArray jsonData = createJson(dataString);
        
        for (Object module : jsonData){
            JSONObject obj = new JSONObject(module.toString());
            String name = obj.get("name").toString();
            String code = obj.get("code").toString();
            
            courseDataMap.put(name, obj);
            
            // Lisää tutkinto-ohjelmat omiin datamappeihin tutkinnon tason perusteella (kandi, maisteri jne.)
            if (code.endsWith("K"))
            {
                bachDegrees.put(name, obj);
            }
            else if (code.endsWith("M"))
            {
                mastDegrees.put(name, obj);
            }
            else if (code.endsWith("0") || code.endsWith("5"))
            {
                miscDegrees.put(name, obj);
            }
            else {
                doctDegrees.put(name, obj);
            }
            
            
            comboBox.getItems().add(name);
        }
        
        
        return comboBox;
    }

    /**
     * Returns a HashMap of all stored courses.
     * @return a HashMap of all stored courses.
     */
    public HashMap<String, Course> getAllCourses(){
            return allCourses;
    }

    /**
     * Returns a HashMap of all bachelors degrees.
     * @return a HashMap of all bachelors degrees.
     */
    public HashMap<String, JSONObject> getBachDegrees(){
            return bachDegrees;
    }

    /**
     * Returns a HashMap of all masters degrees.
     * @return a HashMap of all masters degrees.
     */
    public HashMap<String, JSONObject> getMastDegrees(){
            return mastDegrees;
    }

    /**
     * Returns a HashMap of all doctorate degrees.
     * @return a HashMap of all doctorate degrees.
     */
    public HashMap<String, JSONObject> getDoctDegrees(){
            return doctDegrees;
    }

    /**
     * Returns a HashMap of all degrees that aren't placed by level.
     * @return a HashMap of all degrees that aren't placed by level.
     */
    public HashMap<String, JSONObject> getMiscDegrees(){
            return miscDegrees;
    }
    
    private JSONArray createJson(String data){
        // Vain tutkinto-ohjelman alustamista varten
        JSONObject jsonObject = new JSONObject(data);
        JSONArray jsonArray = jsonObject.getJSONArray("searchResults");
        return jsonArray;
    }
    
    
    /**
     * Recursively fetches the structure and courses of a given programme and
     * stores it in the given TreeItem.
     * 
     * @param jsonData JSONArray of the programme.
     * @param treeItem TreeItem to which store the structure.
     * @throws IOException 
     */
    void getStructureData(JSONArray jsonData, TreeItem treeItem)
                                throws IOException {          
                       
        for(Object module : jsonData) {

            JSONObject obj = new JSONObject(module.toString());
            String type = obj.get("type").toString();
           
            if(!type.equals("CourseUnitRule")) {

                if(type.equals("ModuleRule")) {
                   
                    //Build next Address
                    String nextModuleId = obj.get("moduleGroupId").toString();
                    String address = "https://sis-tuni.funidata.fi/kori/api/modules/"
                                + "by-group-id?groupId=" + nextModuleId
                                + "&universityId=tuni-university-root-id";

                    JSONObject moduleData = new JSONObject(
                                            new JSONArray(getApiData(address)).get(0)
                                                          .toString());

                    TreeItem newTreeItem = new TreeItem(getItemInfo(moduleData, type));                    
                    treeItem.getChildren().add(newTreeItem);
                   
                    //Recursively get next modules
                    JSONArray ruleModules = new JSONArray(getApiData(address));
                    getStructureData(ruleModules, newTreeItem);
                   
                } else if(type.equals("CompositeRule")) {
                    //Go deeper, nothing added here
                    JSONArray ruleModules = obj.getJSONArray("rules");
                   
                    getStructureData(ruleModules, treeItem);

                } else if(this.modules.contains(type)) {
                    //Go deeper, nothing added here
                    JSONObject nextModule = obj.getJSONObject("rule");
                   
                    JSONArray ruleModules = new JSONArray();
                    getStructureData(ruleModules.put(nextModule), treeItem);
                }
            } else {
                //Course, this is a leaf of the tree
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
        
    }




    
    


    // Different module types
    private final ArrayList<String> modules = new ArrayList<String>() {
        {
            add("CreditsRule");
            add("StudyModule");
            add("DegreeProgramme");
            add("GroupingModule");
        }
    };
    
    // For getting the name or name and other info of a JSON module.
    private String getItemInfo(JSONObject object, String type){

        String completeString = "";
        String name = "";
        String description = "";

        // Fetch module info depending on the module type using charset UTF_8
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
                } catch(JSONException ex) {

                    try {
                        description = new JSONObject(object.get("tweetText")
                                        .toString()).getString("fi");
                    } catch(JSONException exc) {
                        description = "Kuvausta ei saatavilla!";
                    } 
                }
            }
            finally {
                int credits = Integer.parseInt(
                              new JSONObject(object.get("credits").toString())
                                                   .get("min").toString());
                
                // Create course object, add to all courses and finalize
                // the returning string
                
                Course course = new Course(name, description, credits);
                allCourses.put(name, course);
                description = course.html2text(description);
                completeString = name + "\n" + description + "\nOpintopisteet: "
                        + credits;
            }
        }
        return completeString;
    }
    
    
}