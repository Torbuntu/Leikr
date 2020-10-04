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

    private static String launchTitle;

    private int x;
    private int A;
    private int B;
    private int Y;
    private int leftBumper;
    private int rightBumper;
    private int select;
    private int start;
    private int up;
    private int down;
    private int right;
    private int left;

    private int horizontalAxis;
    private int verticalAxis;

    private boolean DEBUG;

    private boolean devMode;

    public CustomSystemProperties() {
        Properties prop = new Properties();
        try ( InputStream stream = new FileInputStream(new File("Data/system.properties"))) {
            prop.load(stream);
            launchTitle = (prop.getProperty("launch_title") != null) ? prop.getProperty("launch_title") : "";
            DEBUG = (prop.getProperty("debug_mode") != null) ? Boolean.valueOf(prop.getProperty("debug_mode")) : false;
            devMode = (prop.getProperty("dev_mode") != null) ? Boolean.valueOf(prop.getProperty("dev_mode")) : false;

            x = (prop.getProperty("btn_x") != null) ? Integer.parseInt(prop.getProperty("btn_x")) : 3;
            A = (prop.getProperty("btn_a") != null) ? Integer.parseInt(prop.getProperty("btn_a")) : 1;
            B = (prop.getProperty("btn_b") != null) ? Integer.parseInt(prop.getProperty("btn_b")) : 0;
            Y = (prop.getProperty("btn_y") != null) ? Integer.parseInt(prop.getProperty("btn_y")) : 2;

            leftBumper = (prop.getProperty("btn_lbumper") != null) ? Integer.parseInt(prop.getProperty("btn_lbumper")) : 9;
            rightBumper = (prop.getProperty("btn_rbumper") != null) ? Integer.parseInt(prop.getProperty("btn_rbumper")) : 10;

            select = (prop.getProperty("btn_select") != null) ? Integer.parseInt(prop.getProperty("btn_select")) : 4;
            start = (prop.getProperty("btn_start") != null) ? Integer.parseInt(prop.getProperty("btn_start")) : 6;

            up = (prop.getProperty("btn_up") != null) ? Integer.parseInt(prop.getProperty("btn_up")) : -1;
            down = (prop.getProperty("btn_down") != null) ? Integer.parseInt(prop.getProperty("btn_down")) : 1;
            left = (prop.getProperty("btn_left") != null) ? Integer.parseInt(prop.getProperty("btn_left")) : -1;
            right = (prop.getProperty("btn_right") != null) ? Integer.parseInt(prop.getProperty("btn_right")) : 1;

            horizontalAxis = (prop.getProperty("axis_horizontal") != null) ? Integer.parseInt(prop.getProperty("axis_horizontal")) : 0;
            verticalAxis = (prop.getProperty("axis_vertical") != null) ? Integer.parseInt(prop.getProperty("axis_vertical")) : 1;

        } catch (IOException | NumberFormatException ex) {
            Logger.getLogger(CustomSystemProperties.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getLaunchTitle() {
        return launchTitle;
    }

    public int getX() {
        return x;
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

    public int getLeftBumper() {
        return leftBumper;
    }

    public int getRightBumper() {
        return rightBumper;
    }

    public int getSelect() {
        return select;
    }

    public int getStart() {
        return start;
    }

    public int getUp() {
        return up;
    }

    public int getDown() {
        return down;
    }

    public int getRight() {
        return right;
    }

    public int getLeft() {
        return left;
    }

    public int getHorizontalAxis() {
        return horizontalAxis;
    }

    public int getVerticalAxis() {
        return verticalAxis;
    }

    public boolean isDEBUG() {
        return DEBUG;
    }

    public boolean isDevMode() {
        return devMode;
    }

}
