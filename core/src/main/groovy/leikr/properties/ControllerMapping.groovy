package leikr.properties

import leikr.screens.ControllerMappingScreen
import org.mini2Dx.core.Mdx

class ControllerMapping {
    def a, b, x, y, select, start, leftBumper, rightBumper, up, down, left, right, horizontalAxis, verticalAxis
    def model

    ControllerMapping() {}

    ControllerMapping(int id) {
        if (!(Mdx.input.getGamePads().size > id)) {
            println "No controller available for id: $id"
            return
        }
        try {
            model = Mdx.input.getGamePads().get(id).getModelInfo()
            if (model) {
                if (new File("Data/Controllers/${model}.properties").exists()) {
                    println "Collecting mapping of known controller: ${model}"
                    getKnownControllerMapping(model)
                } else {
                    println "Unmapped controller, switching to default mapping. Please create mapping for: $model"
                    getDefaults()
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace()
        }
    }

    ControllerMapping(String model) {
        if (new File("Data/Controllers/${model}.properties").exists()) {
            println "Collecting mapping of known controller: ${model}"
            getKnownControllerMapping(model)
        } else {
            println "Unmapped controller, switching to default mapping. Please create mapping for: $model"
            getDefaults()
        }
    }

    void getDefaults() {
        x = 3
        a = 1
        b = 0
        y = 2

        leftBumper = 9
        rightBumper = 10

        select = 4
        start = 6

        up = -1
        down = 1
        left = -1
        right = 1

        horizontalAxis = 0
        verticalAxis = 1
    }

    void getKnownControllerMapping(modelName) {
        println "Data/Controllers/${modelName}.properties"
        Properties prop = new Properties()
        try (InputStream stream = new FileInputStream(new File("Data/Controllers/${modelName}.properties"))) {
            prop.load(stream)
            x = Integer.parseInt(prop.getProperty("btn_x")) ?: 3
            a = Integer.parseInt(prop.getProperty("btn_a")) ?: 1
            b = Integer.parseInt(prop.getProperty("btn_b")) ?: 0
            y = Integer.parseInt(prop.getProperty("btn_y")) ?: 2

            leftBumper = Integer.parseInt(prop.getProperty("btn_lbumper")) ?: 9
            rightBumper = Integer.parseInt(prop.getProperty("btn_rbumper")) ?: 10

            select = Integer.parseInt(prop.getProperty("btn_select")) ?: 4
            start = Integer.parseInt(prop.getProperty("btn_start")) ?: 6

            // default to analog stick directions
            up = Integer.parseInt(prop.getProperty("btn_up")) ?: -1
            down = Integer.parseInt(prop.getProperty("btn_down")) ?: 1
            left = Integer.parseInt(prop.getProperty("btn_left")) ?: -1
            right = Integer.parseInt(prop.getProperty("btn_right")) ?: 1

            horizontalAxis = Integer.parseInt(prop.getProperty("axis_horizontal")) ?: 0
            verticalAxis = Integer.parseInt(prop.getProperty("axis_vertical")) ?: 1
        } catch (Exception ex) {
            ex.printStackTrace()
        }
    }
}
