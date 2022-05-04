import fi.tuni.prog3.sisu.App;
import java.util.concurrent.TimeoutException;
import javafx.scene.Node;
import static org.testfx.api.FxAssert.verifyThat;
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

/**
 *
 * @author j
 */
public class Tests extends TestFX {
    final String SCENE1_ID = "#scene";
    final String SCENE2_ID = "#scene2";
    final String SCENE3_ID = "#scene3";
    
    final String OPISKELIJA_NAME = "Opiskelija";
    final String SULJE_NAME = "Sulje";
    
    
    @Test
    public void login(){
        String LOGIN_NAME = "Sisään SISUun";
        clickOn(LOGIN_NAME);
        //verifyThat("#scene2", "scene2");
        Scene scene = stage.getScene();
        assertEquals("#scene2", scene);
    }
}
