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
    
    final String OPISKELIJA_NAME = "Opiskelija";
    final String SULJE_NAME = "Sulje";
    
    
    @Test //Tarkista, että sisään kirjautuminen onnistuu
    public void studentLogin(){
        String STUDENT_LOGIN = "Opiskelija";
        String STUDENT_ID_FIELD = "Opiskelijanumero";
        String STUDENT_NAME_FIELD = "Nimi";
        String STUDENT_ID = "H123456";
        String STUDENT_NAME = "Lissu";
        String LOGIN = "Sisään";
        String VERIFICATION_TEXT = "Rajaa tutkinto-ohjelmia";
        clickOn(STUDENT_LOGIN);
        clickOn(STUDENT_NAME_FIELD).write(STUDENT_NAME);
        clickOn(STUDENT_ID_FIELD).write(STUDENT_ID);
        clickOn(LOGIN);
        verifyThat(VERIFICATION_TEXT, NodeMatchers.isVisible());
    }
    @Test
    public void chooseDegree() {
        String STUDENT_LOGIN = "Opiskelija";
        String STUDENT_ID_FIELD = "Opiskelijanumero";
        String STUDENT_NAME_FIELD = "Nimi";
        String STUDENT_ID = "H123456";
        String STUDENT_NAME = "Lissu";
        String LOGIN = "Sisään";
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
    /*
    @Test
    public void backButtonFromScene3() {
        String LOGIN = "Sisään SISUun";
        String CHOOSE_DEGREE = "Valitse tutkinto-ohjelma";
        String DEGREE = "Arkkitehtuurin kandidaattiohjelma";
        String BACK = "Takaisin";
        String UPDATE = "Päivitä";
        clickOn(LOGIN);
        clickOn(CHOOSE_DEGREE);
        clickOn(DEGREE);
        clickOn(BACK);
        verifyThat(UPDATE, NodeMatchers.isVisible());
    }
    @Test
    public void filterBach() {
        String LOGIN = "Sisään SISUun";
        String CHOOSE_DEGREE = "Valitse tutkinto-ohjelma";
        String FILTER_BACH = "Kandidaatin tutkinto";
        String FILTER_MAST = "Maisterin tutkinto";
        String FILTER_DOCT = "Tohtorin/Lisensiaatin tutkinto";
        String FILTER_MISC = "Erikoislääkäri koulutus";
        String ANECDOTAL_BACH = "Kasvatustieteiden kandidaattiohjelma";
        String ANECDOTAL_MAST = "Johtamisen ja tietotekniikan DI-ohjelma";
        String ANECDOTAL_DOCT = "Historian tohtoriohjelma";
        String ANECDOTAL_MISC = "Foniatrian erikoislääkärikoulutus (56/2015)";
        String UPDATE = "Päivitä";
        clickOn(LOGIN);
        clickOn(FILTER_BACH);
        clickOn(UPDATE);
        clickOn(CHOOSE_DEGREE);
        verifyThat(ANECDOTAL_BACH, NodeMatchers.isVisible());
    }
    @Test
    public void filterBachAndMast() {
        String LOGIN = "Sisään SISUun";
        String CHOOSE_DEGREE = "Valitse tutkinto-ohjelma";
        String FILTER_BACH = "Kandidaatin tutkinto";
        String FILTER_MAST = "Maisterin tutkinto";
        String FILTER_DOCT = "Tohtorin/Lisensiaatin tutkinto";
        String FILTER_MISC = "Erikoislääkäri koulutus";
        String ANECDOTAL_BACH = "Filosofian kandidaattiohjelma";
        String ANECDOTAL_MAST = "Filosofian maisteriohjelma";
        String ANECDOTAL_DOCT = "Historian tohtoriohjelma";
        String ANECDOTAL_MISC = "Foniatrian erikoislääkärikoulutus (56/2015)";
        String UPDATE = "Päivitä";
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
        String LOGIN = "Sisään SISUun";
        String CHOOSE_DEGREE = "Valitse tutkinto-ohjelma";
        String FILTER_BACH = "Kandidaatin tutkinto";
        String FILTER_MAST = "Maisterin tutkinto";
        String FILTER_DOCT = "Tohtorin/Lisensiaatin tutkinto";
        String FILTER_MISC = "Erikoislääkäri koulutus";
        String ANECDOTAL_BACH = "Kasvatustieteiden kandidaattiohjelma";
        String ANECDOTAL_MAST = "Johtamisen ja tietotekniikan DI-ohjelma";
        String ANECDOTAL_DOCT = "Historian tohtoriohjelma";
        String ANECDOTAL_MISC = "Foniatrian erikoislääkärikoulutus (56/2015)";
        String UPDATE = "Päivitä";
        clickOn(LOGIN);
        clickOn(FILTER_DOCT);
        clickOn(UPDATE);
        clickOn(CHOOSE_DEGREE);
        verifyThat(ANECDOTAL_DOCT, NodeMatchers.isVisible());
    }
    @Test
    public void filterMisc() {
        String LOGIN = "Sisään SISUun";
        String CHOOSE_DEGREE = "Valitse tutkinto-ohjelma";
        String FILTER_BACH = "Kandidaatin tutkinto";
        String FILTER_MAST = "Maisterin tutkinto";
        String FILTER_DOCT = "Tohtorin/Lisensiaatin tutkinto";
        String FILTER_MISC = "Erikoislääkäri koulutus";
        String ANECDOTAL_BACH = "Kasvatustieteiden kandidaattiohjelma";
        String ANECDOTAL_MAST = "Johtamisen ja tietotekniikan DI-ohjelma";
        String ANECDOTAL_DOCT = "Historian tohtoriohjelma";
        String ANECDOTAL_MISC = "Foniatrian erikoislääkärikoulutus (56/2015)";
        String UPDATE = "Päivitä";
        clickOn(LOGIN);
        clickOn(FILTER_MISC);
        clickOn(UPDATE);
        clickOn(CHOOSE_DEGREE);
        verifyThat(ANECDOTAL_MISC, NodeMatchers.isVisible());
    }*/

}
