package fi.tuni.prog3.test;



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
import org.junit.Before;
import org.testfx.api.FxToolkit;
/**
 *
 * @author j
 */
public abstract class TestFX extends ApplicationTest {
    public Stage stage;
    
    @Before
    public void setUpClass() throws Exception {
        ApplicationTest.launch(App.class);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        stage.show();
    }
    
    @After
    public void afterEachTest() throws TimeoutException {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }
    
    public <T extends Node> T find(final String query) {
        return (T) lookup(query).queryAll().iterator().next();
    }
}
