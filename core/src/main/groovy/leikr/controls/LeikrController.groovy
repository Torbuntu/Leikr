package leikr.controls

import org.mini2Dx.gdx.controllers.Controller
import org.mini2Dx.gdx.controllers.ControllerListener
import org.mini2Dx.gdx.controllers.PovDirection
import org.mini2Dx.gdx.math.Vector3

class LeikrController implements ControllerListener {

    def buttons = [
            "A": false,
            "B": false,
            "X": false,
            "Y": false,
            "SELECT": false,
            "START": false,
            "UP": false,
            "DOWN": false,
            "LEFT": false,
            "RIGHT": false
    ]

    @Override
    void connected(Controller controller) {
    }

    @Override
    void disconnected(Controller controller) {

    }

    @Override
    boolean buttonDown(Controller controller, int i) {
        return false
    }

    @Override
    boolean buttonUp(Controller controller, int i) {
        return false
    }

    @Override
    boolean axisMoved(Controller controller, int i, float v) {
        return false
    }

    @Override
    boolean povMoved(Controller controller, int i, PovDirection povDirection) {
        return false
    }

    @Override
    boolean xSliderMoved(Controller controller, int i, boolean b) {
        return false
    }

    @Override
    boolean ySliderMoved(Controller controller, int i, boolean b) {
        return false
    }

    @Override
    boolean accelerometerMoved(Controller controller, int i, Vector3 vector3) {
        return false
    }
}
