
package fi.tuni.prog3.sisu;

import org.jsoup.Jsoup;

/**
 *
 * Class for representing a course
 */
public class Course {
    
    private String name;
    private String description;
    private int credits;
    
    /**
     * Constructor for a course with given name, description and credits.
     * @param courseName Name of the course.
     * @param courseDescription Description of the course.
     * @param courseCredits Course credits.
     */
    public Course(String courseName, String courseDescription,
            int courseCredits){
        name = courseName;
        description = courseDescription;
        credits = courseCredits;
    }
    
    /**
     * Returns course name.
     * @return course name.
     */
    public String getName(){
        return name;
    }
    
    /**
     * Returns course description.
     * @return course description.
     */
    public String getDescription(){
        return description;
    }
    
    /**
     * Returns course credits.
     * @return course credits.
     */
    public int getCredits(){
        return credits;
    }
    
    /**
     * Converts html string to a normal String.
     * @param html String to convert
     * @return converted String.
     */
    public String html2text(String html) {
        return Jsoup.parse(html).wholeText();
    }
}
