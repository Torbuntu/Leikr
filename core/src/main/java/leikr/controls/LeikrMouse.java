package leikr.controls;

import com.badlogic.gdx.Gdx;
import org.mini2Dx.core.graphics.viewport.FitViewport;
import org.mini2Dx.gdx.math.Vector2;

/**
 * The class to handle mouse interactions within Leikr programs
 *
 * @author Torbuntu
 */
public class LeikrMouse {

    static FitViewport viewport;

    Vector2 realMouse;
    Vector2 leikrMouse;

    private static LeikrMouse instance;

    public static LeikrMouse getLeikrMouse(FitViewport viewport) {
        if (instance == null) {
            instance = new LeikrMouse();
        }
        setViewport(viewport);
        return instance;
    }
    
    private LeikrMouse(){
        realMouse = new Vector2();
        leikrMouse = new Vector2();
    }
    
    public void updateMouse() {
        realMouse.x = Gdx.input.getX();
        realMouse.y = Gdx.input.getY();
        viewport.toWorldCoordinates(leikrMouse, realMouse.x, realMouse.y);
    }

    static void setViewport(FitViewport view) {
        viewport = view;
    }

    /**
     * Button codes Left = 0 Middle = 2 Right = 1
     *
     * @param btn
     * @return
     */
    public boolean mouseClick(int btn) {
        return Gdx.input.isButtonPressed(btn);
    }

    public float mouseX() {
        return leikrMouse.x;
    }

    public float mouseY() {
        return leikrMouse.y;
    }

}
