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
package leikr.customProperties

import java.util.logging.Level
import java.util.logging.Logger

/**
 *
 * @author tor
 */
class CustomSystemProperties {

    private static String launchTitle

    private int x
    private int A
    private int B
    private int Y
    private int leftBumper
    private int rightBumper
    private int select
    private int start
    private int up
    private int down
    private int right
    private int left

    private int horizontalAxis
    private int verticalAxis

    private boolean DEBUG

    private boolean devMode

    CustomSystemProperties() {
        Properties prop = new Properties()
        try ( InputStream stream = new FileInputStream(new File("Data/system.properties"))) {
            prop.load(stream)
            launchTitle = prop.getProperty("launch_title") ?: ""
            DEBUG = Boolean.valueOf(prop.getProperty("debug_mode")) ?: false
            devMode = Boolean.valueOf(prop.getProperty("dev_mode")) ?: false

            x = Integer.parseInt(prop.getProperty("btn_x")) ?: 3
            A = Integer.parseInt(prop.getProperty("btn_a")) ?: 1
            B = Integer.parseInt(prop.getProperty("btn_b")) ?: 0
            Y = Integer.parseInt(prop.getProperty("btn_y")) ?: 2

            leftBumper = Integer.parseInt(prop.getProperty("btn_lbumper")) ?: 9
            rightBumper = Integer.parseInt(prop.getProperty("btn_rbumper")) ?: 10

            select = Integer.parseInt(prop.getProperty("btn_select")) ?: 4
            start = Integer.parseInt(prop.getProperty("btn_start")) ?: 6

            up = Integer.parseInt(prop.getProperty("btn_up")) ?: -1
            down = Integer.parseInt(prop.getProperty("btn_down")) ?: 1
            left = Integer.parseInt(prop.getProperty("btn_left")) ?: -1
            right = Integer.parseInt(prop.getProperty("btn_right")) ?: 1

            horizontalAxis = Integer.parseInt(prop.getProperty("axis_horizontal")) ?: 0
            verticalAxis = Integer.parseInt(prop.getProperty("axis_vertical")) ?: 1

        } catch (IOException | NumberFormatException ex) {
            Logger.getLogger(CustomSystemProperties.class.getName()).log(Level.SEVERE, null, ex)
        }
    }

    static String getLaunchTitle() {
        launchTitle
    }

    int getX() {
        x
    }

    int getA() {
        A
    }

    int getB() {
        B
    }

    int getY() {
        Y
    }

    int getLeftBumper() {
        leftBumper
    }

    int getRightBumper() {
        rightBumper
    }

    int getSelect() {
        select
    }

    int getStart() {
        start
    }

    int getUp() {
        up
    }

    int getDown() {
        down
    }

    int getRight() {
        right
    }

    int getLeft() {
        left
    }

    int getHorizontalAxis() {
        horizontalAxis
    }

    int getVerticalAxis() {
        verticalAxis
    }

    boolean isDEBUG() {
        DEBUG
    }

    boolean isDevMode() {
        devMode
    }

}
