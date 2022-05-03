
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
    
    public Course(String courseName, String courseDescription,
            int courseCredits){
        name = courseName;
        description = courseDescription;
        credits = courseCredits;
    }
    
    public String html2text(String html) {
        return Jsoup.parse(html).wholeText();
    }
}
