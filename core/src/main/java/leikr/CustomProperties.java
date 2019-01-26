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

    public int MAX_SPRITES;
    public int MAX_SPRITE_SHEETS;

    public CustomProperties(String gameName) {

        Properties prop = new Properties();
        try {
            InputStream stream = new FileInputStream(new File("./Games/"+gameName + "/game.properties"));
            prop.load(stream);

            //String example for later
            //customPalette = (prop.getProperty("customPalette") != null) ? prop.getProperty("customPalette") : "";

            //2048 is maximum, 120 is default
            MAX_SPRITES  = (prop.getProperty("max_sprites") != null) ? Integer.parseInt(prop.getProperty("max_sprites")) : 120;
            if(MAX_SPRITES > 2048){
                MAX_SPRITES = 2048;
            }
            
            //16 is maximum, 4 is default
            MAX_SPRITE_SHEETS  = (prop.getProperty("max_sprite_sheets") != null) ? Integer.parseInt(prop.getProperty("max_sprite_sheets")) : 4;
            if(MAX_SPRITE_SHEETS > 16){
                MAX_SPRITE_SHEETS = 16;
            }
        } catch (IOException | NumberFormatException ex) {
            System.out.println(ex.getMessage());
        }

    }
}
