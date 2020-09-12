/*
 * Copyright 2019 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package leikr.customProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tor
 */
public class CustomSystemProperties {

    private static String LAUNCH_TITLE;

    private int X;
    private int A;
    private int B;
    private int Y;
    private int LEFT_BUMPER;
    private int RIGHT_BUMPER;
    private int SELECT;
    private int START;
    private int UP;
    private int DOWN;
    private int RIGHT;
    private int LEFT;

    private int HORIZONTAL_AXIS;
    private int VERTICAL_AXIS;

    private boolean DEBUG;

    public CustomSystemProperties() {
        Properties prop = new Properties();
        try ( InputStream stream = new FileInputStream(new File("Data/system.properties"))) {
            prop.load(stream);
            LAUNCH_TITLE = (prop.getProperty("launch_title") != null) ? prop.getProperty("launch_title") : "";
            DEBUG = (prop.getProperty("debug_mode") != null) ? Boolean.valueOf(prop.getProperty("debug_mode")) : false;

            X = (prop.getProperty("btn_x") != null) ? Integer.parseInt(prop.getProperty("btn_x")) : 3;
            A = (prop.getProperty("btn_a") != null) ? Integer.parseInt(prop.getProperty("btn_a")) : 1;
            B = (prop.getProperty("btn_b") != null) ? Integer.parseInt(prop.getProperty("btn_b")) : 0;
            Y = (prop.getProperty("btn_y") != null) ? Integer.parseInt(prop.getProperty("btn_y")) : 2;

            LEFT_BUMPER = (prop.getProperty("btn_lbumper") != null) ? Integer.parseInt(prop.getProperty("btn_lbumper")) : 9;
            RIGHT_BUMPER = (prop.getProperty("btn_rbumper") != null) ? Integer.parseInt(prop.getProperty("btn_rbumper")) : 10;

            SELECT = (prop.getProperty("btn_select") != null) ? Integer.parseInt(prop.getProperty("btn_select")) : 4;
            START = (prop.getProperty("btn_start") != null) ? Integer.parseInt(prop.getProperty("btn_start")) : 6;

            UP = (prop.getProperty("btn_up") != null) ? Integer.parseInt(prop.getProperty("btn_up")) : -1;
            DOWN = (prop.getProperty("btn_down") != null) ? Integer.parseInt(prop.getProperty("btn_down")) : 1;
            LEFT = (prop.getProperty("btn_left") != null) ? Integer.parseInt(prop.getProperty("btn_left")) : -1;
            RIGHT = (prop.getProperty("btn_right") != null) ? Integer.parseInt(prop.getProperty("btn_right")) : 1;

            HORIZONTAL_AXIS = (prop.getProperty("axis_horizontal") != null) ? Integer.parseInt(prop.getProperty("axis_horizontal")) : 0;
            VERTICAL_AXIS = (prop.getProperty("axis_vertical") != null) ? Integer.parseInt(prop.getProperty("axis_vertical")) : 1;

        } catch (IOException | NumberFormatException ex) {
            Logger.getLogger(CustomSystemProperties.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getLAUNCH_TITLE() {
        return LAUNCH_TITLE;
    }

    public int getX() {
        return X;
    }

    public int getA() {
        return A;
    }

    public int getB() {
        return B;
    }

    public int getY() {
        return Y;
    }

    public int getLEFT_BUMPER() {
        return LEFT_BUMPER;
    }

    public int getRIGHT_BUMPER() {
        return RIGHT_BUMPER;
    }

    public int getSELECT() {
        return SELECT;
    }

    public int getSTART() {
        return START;
    }

    public int getUP() {
        return UP;
    }

    public int getDOWN() {
        return DOWN;
    }

    public int getRIGHT() {
        return RIGHT;
    }

    public int getLEFT() {
        return LEFT;
    }

    public int getHORIZONTAL_AXIS() {
        return HORIZONTAL_AXIS;
    }

    public int getVERTICAL_AXIS() {
        return VERTICAL_AXIS;
    }

    public boolean isDEBUG() {
        return DEBUG;
    }

}
