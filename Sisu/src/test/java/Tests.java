import fi.tuni.prog3.sisu.App;
import java.util.concurrent.TimeoutException;
import javafx.scene.Node;
import static org.testfx.api.FxAssert.*;
import static org.testfx.matcher.control.LabeledMatchers.hasText;


import org.testfx.framework.junit.ApplicationTest;
import org.testfx.robot.Motion;

import javafx.scene.control.Button;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.testfx.api.FxToolkit;
import org.testfx.matcher.base.NodeMatchers;

/**
 *
 * @author j
 */
public class Tests extends TestFX {
    
    final String OPISKELIJA_NAME = "Opiskelija";
    final String SULJE_NAME = "Sulje";
    
    
    @Test //Tarkista, että sisään kirjautuminen onnistuu
    public void login(){
        String LOGIN_NAME = "Sisään SISUun";
        clickOn(LOGIN_NAME);
        verifyThat("Valitse tutkinto-ohjelma", NodeMatchers.isVisible());
    }
    @Test
    public void chooseDegree() {
        String LOGIN_NAME = "Sisään SISUun";
        String CHOOSE_DEGREE = "Valitse tutkinto-ohjelma";
        String DEGREE = "Arkkitehdin tutkinto-ohjelma";
        String COURSES = "Arkkitehdin tutkinto-ohjelman yhteiset opinnot";
        clickOn(LOGIN_NAME);
        clickOn(CHOOSE_DEGREE);
        clickOn(DEGREE);
        doubleClickOn(DEGREE);
        verifyThat(COURSES, NodeMatchers.isVisible());
    }
}
