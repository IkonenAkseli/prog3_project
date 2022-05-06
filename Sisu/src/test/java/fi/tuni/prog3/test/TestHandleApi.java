package fi.tuni.prog3.test;
import fi.tuni.prog3.sisu.HandleApi;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertThrows;
import org.junit.Before;
import org.junit.Test;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author j
 */
public class TestHandleApi {
    

    private HandleApi API = new HandleApi();
    
    @Test
    public void getApiData_BadURLExc() throws MalformedURLException {
        Exception exception = assertThrows(MalformedURLException.class, () -> {
                API.getApiData("kiuruvesi..sdef");
                });
    }
}
