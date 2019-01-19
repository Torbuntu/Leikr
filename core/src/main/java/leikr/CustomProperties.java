/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leikr;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 *
 * @author tor
 */
public class CustomProperties {

    int MAX_SPRITES;

    CustomProperties(String gameName) {

        Properties prop = new Properties();
        try {
            InputStream stream = new FileInputStream(new File("./Games/"+gameName + "/game.properties"));
            prop.load(stream);

            //String example for later
            //customPalette = (prop.getProperty("customPalette") != null) ? prop.getProperty("customPalette") : "";

            //integers.
            MAX_SPRITES  = (prop.getProperty("max_sprites") != null) ? Integer.parseInt(prop.getProperty("max_sprites")) : 120;

        } catch (IOException | NumberFormatException ex) {
            System.out.println(ex.getMessage());
        }

    }
}
