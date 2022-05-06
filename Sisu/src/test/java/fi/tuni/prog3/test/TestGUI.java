package fi.tuni.prog3.test;

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
public class TestGUI extends TestFX {
    
    final String STUDENT_LOGIN = "Opiskelija";
    final String TEACHER_LOGIN = "Opettaja";
    final String STUDENT_ID_FIELD = "Opiskelijanumero";
    final String STUDENT_NAME_FIELD = "Nimi";
    final String STUDENT_ID = "H123456";
    final String STUDENT_NAME = "Lissu";
    final String BAD_STUDENT_NAME = "Pelikaani-Sakke";
    final String LOGIN = "Sisään";
    final String VERIFICATION_TEXT = "Rajaa tutkinto-ohjelmia";
    final String UPDATE = "Päivitä";
    final String CHOOSE_DEGREE = "Valitse tutkinto-ohjelma";
    final String FILTER_BACH = "Kandidaatin tutkinto";
    final String FILTER_MAST = "Maisterin tutkinto";
    final String FILTER_DOCT = "Tohtorin/Lisensiaatin tutkinto";
    final String FILTER_MISC = "Erikoislääkäri koulutus";
    
    
    @Test //Tarkista, että sisään kirjautuminen onnistuu
    public void studentLogin(){
        clickOn(STUDENT_LOGIN);
        clickOn(STUDENT_NAME_FIELD).write(STUDENT_NAME);
        clickOn(STUDENT_ID_FIELD).write(STUDENT_ID);
        clickOn(LOGIN);
        verifyThat(VERIFICATION_TEXT, NodeMatchers.isVisible());
    }
    @Test
    public void teacherLogin(){
        clickOn(TEACHER_LOGIN);
        clickOn(STUDENT_NAME_FIELD).write(STUDENT_NAME);
        clickOn(STUDENT_ID_FIELD).write(STUDENT_ID);
        clickOn(LOGIN);
        verifyThat(VERIFICATION_TEXT, NodeMatchers.isVisible());
    }
    @Test
    public void chooseDegree() {
        String CHOOSE_DEGREE = "Valitse tutkinto-ohjelma";
        String DEGREE = "Arkkitehtuurin kandidaattiohjelma";
        String VERIFY = "Arkkitehtuurin kandidaattiohjelman yhteiset opinnot";
        
        clickOn(STUDENT_LOGIN);
        clickOn(STUDENT_NAME_FIELD).write(STUDENT_NAME);
        clickOn(STUDENT_ID_FIELD).write(STUDENT_ID);
        clickOn(LOGIN);
        clickOn(CHOOSE_DEGREE);
        clickOn(DEGREE);
        doubleClickOn(DEGREE);
        verifyThat(VERIFY, NodeMatchers.isVisible());
    }
    @Test
    public void filterBach() {
        String ANECDOTAL_BACH = "Kasvatustieteiden kandidaattiohjelma";
        clickOn(STUDENT_LOGIN);
        clickOn(STUDENT_NAME_FIELD).write(STUDENT_NAME);
        clickOn(STUDENT_ID_FIELD).write(STUDENT_ID);
        clickOn(LOGIN);
        clickOn(FILTER_BACH);
        clickOn(UPDATE);
        clickOn(CHOOSE_DEGREE);
        verifyThat(ANECDOTAL_BACH, NodeMatchers.isVisible());
    }
    @Test
    public void filterBachAndMast() {
        String ANECDOTAL_BACH = "Filosofian kandidaattiohjelma";
        String ANECDOTAL_MAST = "Filosofian maisteriohjelma";
        clickOn(STUDENT_LOGIN);
        clickOn(STUDENT_NAME_FIELD).write(STUDENT_NAME);
        clickOn(STUDENT_ID_FIELD).write(STUDENT_ID);
        clickOn(LOGIN);
        clickOn(FILTER_BACH);
        clickOn(FILTER_MAST);
        clickOn(UPDATE);
        clickOn(CHOOSE_DEGREE);
        verifyThat(ANECDOTAL_BACH, NodeMatchers.isVisible());
        verifyThat(ANECDOTAL_MAST, NodeMatchers.isVisible());
    }
    @Test
    public void filterDoct() {
        String ANECDOTAL_DOCT = "Historian tohtoriohjelma";
        clickOn(STUDENT_LOGIN);
        clickOn(STUDENT_NAME_FIELD).write(STUDENT_NAME);
        clickOn(STUDENT_ID_FIELD).write(STUDENT_ID);
        clickOn(LOGIN);
        clickOn(FILTER_DOCT);
        clickOn(UPDATE);
        clickOn(CHOOSE_DEGREE);
        verifyThat(ANECDOTAL_DOCT, NodeMatchers.isVisible());
    }
    @Test
    public void filterMisc() {
        String ANECDOTAL_MISC = "Foniatrian erikoislääkärikoulutus (56/2015)";
        clickOn(STUDENT_LOGIN);
        clickOn(STUDENT_NAME_FIELD).write(STUDENT_NAME);
        clickOn(STUDENT_ID_FIELD).write(STUDENT_ID);
        clickOn(LOGIN);
        clickOn(FILTER_MISC);
        clickOn(UPDATE);
        clickOn(CHOOSE_DEGREE);
        verifyThat(ANECDOTAL_MISC, NodeMatchers.isVisible());
    }

}
