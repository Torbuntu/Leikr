package leikr.controls;

import org.mini2Dx.core.Mdx;
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
        realMouse.x = Mdx.input.getX();
        realMouse.y = Mdx.input.getY();
        viewport.toWorldCoordinates(leikrMouse, realMouse.x, realMouse.y);
    }

    static void setViewport(FitViewport view) {
        viewport = view;
    }

    /**
     * Detects a mouse click event.
     * @return
     */
    public boolean mouseClick() {
        return Mdx.input.justTouched();
    }

    public float mouseX() {
        return leikrMouse.x;
    }

    public float mouseY() {
        return leikrMouse.y;
    }

}
