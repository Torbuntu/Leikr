/*
 * Copyright 2019 torbuntu.
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

/**
 *
 * @author tor
 */
public class CustomSystemProperties {

    public boolean SINGLE_LAUNCH;
    public String LAUNCH_TITLE;

    public static int X;
    public static int A;
    public static int B;
    public static int Y;
    public static int LEFT_BUMPER;
    public static int RIGHT_BUMPER;
    public static int SELECT;
    public static int START;
    public static int UP;
    public static int DOWN;
    public static int RIGHT;
    public static int LEFT;

    public static int HORIZONTAL_AXIS;
    public static int VERTICAL_AXIS;
    
    public static boolean DEBUG;

    public CustomSystemProperties() {
        Properties prop = new Properties();
        try (InputStream stream = new FileInputStream(new File("Data/system.properties"))) {
            prop.load(stream);
            SINGLE_LAUNCH = (prop.getProperty("single_launch") != null) ? Boolean.valueOf(prop.getProperty("single_launch")) : false;
            LAUNCH_TITLE = (prop.getProperty("launch_title") != null) ? prop.getProperty("launch_title") : "";
            DEBUG = (prop.getProperty("debug_mode") != null) ? Boolean.valueOf(prop.getProperty("debug_mode")) : false;

            X = (prop.getProperty("btn_x") != null) ? Integer.parseInt(prop.getProperty("btn_x")) : 0;
            A = (prop.getProperty("btn_a") != null) ? Integer.parseInt(prop.getProperty("btn_a")) : 1;
            B = (prop.getProperty("btn_b") != null) ? Integer.parseInt(prop.getProperty("btn_b")) : 2;
            Y = (prop.getProperty("btn_y") != null) ? Integer.parseInt(prop.getProperty("btn_y")) : 3;

            LEFT_BUMPER = (prop.getProperty("btn_lbumper") != null) ? Integer.parseInt(prop.getProperty("btn_lbumper")) : 4;
            RIGHT_BUMPER = (prop.getProperty("btn_rbumper") != null) ? Integer.parseInt(prop.getProperty("btn_rbumper")) : 5;

            SELECT = (prop.getProperty("btn_select") != null) ? Integer.parseInt(prop.getProperty("btn_select")) : 8;
            START = (prop.getProperty("btn_start") != null) ? Integer.parseInt(prop.getProperty("btn_start")) : 9;

            UP = (prop.getProperty("btn_up") != null) ? Integer.parseInt(prop.getProperty("btn_up")) : -1;
            DOWN = (prop.getProperty("btn_down") != null) ? Integer.parseInt(prop.getProperty("btn_down")) : 1;
            LEFT = (prop.getProperty("btn_left") != null) ? Integer.parseInt(prop.getProperty("btn_left")) : -1;
            RIGHT = (prop.getProperty("btn_right") != null) ? Integer.parseInt(prop.getProperty("btn_right")) : 1;
            
            HORIZONTAL_AXIS = (prop.getProperty("axis_horizontal") != null) ? Integer.parseInt(prop.getProperty("axis_horizontal")) : 0;
            VERTICAL_AXIS = (prop.getProperty("axis_vertical") != null) ? Integer.parseInt(prop.getProperty("axis_vertical")) : 1;


            
        } catch (IOException | NumberFormatException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
