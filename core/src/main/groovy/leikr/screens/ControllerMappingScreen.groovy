package leikr.screens

import leikr.GameRuntime
import leikr.managers.ManagerDTO
import leikr.properties.ControllerMapping
import org.mini2Dx.core.Graphics
import org.mini2Dx.core.Mdx
import org.mini2Dx.core.game.GameContainer
import org.mini2Dx.core.graphics.FrameBuffer
import org.mini2Dx.core.graphics.viewport.FitViewport
import org.mini2Dx.core.graphics.viewport.StretchViewport
import org.mini2Dx.core.input.GamePad
import org.mini2Dx.core.input.GamePadListener
import org.mini2Dx.core.screen.BasicGameScreen
import org.mini2Dx.core.screen.GameScreen
import org.mini2Dx.core.screen.ScreenManager
import org.mini2Dx.core.screen.Transition
import org.mini2Dx.core.util.Align
import org.mini2Dx.gdx.math.Vector3

// TODO: Make this page not ugly.
// TODO: Make controller mapping persist.
// TODO: Only initialize this page if a mapping for the connected device does not exist, or if user navigates here.
class ControllerMappingScreen extends BasicGameScreen implements GamePadListener {
    public static int ID = 8
    def buttonMapping = [:]

    // while not complete, we add new button codes and proceed
    def processing = true
    // We start with 0 and work towards a complete mapping of 8 buttons and then coordinate data.
    // directions are awkward because they can either be a directional pad or an analog stick.
    def progress = 0

    ManagerDTO managerDTO
    private final FitViewport viewport
    private final StretchViewport stretchViewport
    private FrameBuffer framebuffer
    GameRuntime runtime
    ControllerMapping controllerMapping

    ControllerMappingScreen(ManagerDTO managerDTO, FitViewport viewport, GameRuntime runtime) {
        this.managerDTO = managerDTO
        this.runtime = runtime
        this.viewport = viewport
        stretchViewport = new StretchViewport(runtime.WIDTH, runtime.HEIGHT)
        controllerMapping = new ControllerMapping()
    }

    @Override
    void postTransitionIn(Transition transitionIn) {
        framebuffer = Mdx.graphics.newFrameBuffer(runtime.WIDTH, runtime.HEIGHT)
        Mdx.input.getGamePads().get(0).addListener(this)
    }

    @Override
    void preTransitionOut(Transition transition) {
        framebuffer.dispose()
        Mdx.input.getGamePads().get(0).removeListener(this)
    }

    @Override
    void initialise(GameContainer gc) {}

    def saveToProps() {
        Properties prop = new Properties()
        if (!Mdx.files.external("Data/Controllers/${Mdx.input.getGamePads().get(0).getModelInfo()}.properties").exists()) {
            println "File does not exist, creating new: Data/Controllers/${Mdx.input.getGamePads().get(0).getModelInfo()}.properties"
            if(!Mdx.files.external("Data/Controllers/").exists()){
                if (new File("Data/Controllers/").mkdirs()) {
                    println "Successfully created Controllers directory"
                }
            }
            new File("Data/Controllers/${Mdx.input.getGamePads().get(0).getModelInfo()}.properties").createNewFile()
        }
        try (InputStream instream = new FileInputStream(new File("Data/Controllers/${Mdx.input.getGamePads().get(0).getModelInfo()}.properties"))) {
            prop.load(instream)
        } catch (Exception ex) {
            ex.printStackTrace()
            return false
        }
        try (FileOutputStream stream = new FileOutputStream(new File("Data/Controllers/${Mdx.input.getGamePads().get(0).getModelInfo()}.properties"))) {
            buttonMapping.each {
                println "${it.value}, ${it.key}"
            }
            prop.setProperty("btn_x", controllerMapping.getX() as String)
            prop.setProperty("btn_a", controllerMapping.getA() as String)
            prop.setProperty("btn_b", controllerMapping.getB() as String)
            prop.setProperty("btn_y", controllerMapping.getY() as String)

            prop.setProperty("btn_lbumper", controllerMapping.getLeftBumper() as String)
            prop.setProperty("btn_rbumper", controllerMapping.getRightBumper() as String)
            prop.setProperty("btn_select", controllerMapping.getSelect() as String)
            prop.setProperty("btn_start", controllerMapping.getStart() as String)

            prop.setProperty("btn_up", controllerMapping.getUp() as String)
            prop.setProperty("btn_down", controllerMapping.getDown() as String)
            prop.setProperty("btn_left", controllerMapping.getLeft() as String)
            prop.setProperty("btn_right", controllerMapping.getRight() as String)
            prop.setProperty("axis_horizontal", controllerMapping.getHorizontalAxis() as String ?: "0")
            prop.setProperty("axis_vertical", controllerMapping.getVerticalAxis() as String ?: "1")
            prop.store(stream, "Saved from Leikr Controller Mapping Utility")
        } catch (IOException | NumberFormatException ex) {
            println(ex.getMessage())
            return false
        }
        println "Save was successful"
        return true
    }

    @Override
    void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager, float delta) {
        if (progress == 12) {
            saveToProps()
            managerDTO.inputManager.createControllers()
            screenManager.enterGameScreen(MenuScreen.ID, null, null)
        }
    }

    @Override
    void render(GameContainer gc, Graphics g) {
        stretchViewport.apply(g)
        framebuffer.begin()
        g.clearContext()
        if (processing) {
            switch (progress) {
                case 0: g.drawString("Press: A", 0, 0, 240, Align.CENTER); break
                case 1: g.drawString("Press: B", 0, 0, 240, Align.CENTER); break
                case 2: g.drawString("Press: X", 0, 0, 240, Align.CENTER); break
                case 3: g.drawString("Press: Y", 0, 0, 240, Align.CENTER); break

                case 4: g.drawString("Press: SELECT", 0, 0, 240, Align.CENTER); break
                case 5: g.drawString("Press: START", 0, 0, 240, Align.CENTER); break
                case 6: g.drawString("Press: LEFT_BUMPER", 0, 0, 240, Align.CENTER); break
                case 7: g.drawString("Press: RIGHT_BUMPER", 0, 0, 240, Align.CENTER); break

                case 8: g.drawString("Press: UP", 0, 0, 240, Align.CENTER); break
                case 9: g.drawString("Press: DOWN", 0, 0, 240, Align.CENTER); break
                case 10: g.drawString("Press: LEFT", 0, 0, 240, Align.CENTER); break
                case 11: g.drawString("Press: RIGHT", 0, 0, 240, Align.CENTER); break
            }
        }
        g.flush()
        framebuffer.end()

        viewport.apply(g)
        g.drawTexture(framebuffer.getTexture(), 0, 0, false)
    }

    @Override
    int getId() {
        ID
    }

    @Override
    void onConnect(GamePad gamePad) {

    }

    @Override
    void onDisconnect(GamePad gamePad) {

    }

    @Override
    void onButtonDown(GamePad gamePad, int buttonCode) {
        switch (progress) {
            case 0: controllerMapping.setA(buttonCode); progress++; break
            case 1: controllerMapping.setB(buttonCode); progress++; break
            case 2: controllerMapping.setX(buttonCode); progress++; break
            case 3: controllerMapping.setY(buttonCode); progress++; break

            case 4: controllerMapping.setSelect(buttonCode); progress++; break
            case 5: controllerMapping.setStart(buttonCode); progress++; break
            case 6: controllerMapping.setLeftBumper(buttonCode); progress++; break
            case 7: controllerMapping.setRightBumper(buttonCode); progress++; break

            case 8: controllerMapping.setUp(buttonCode); progress++; break
            case 9: controllerMapping.setDown(buttonCode); progress++; break
            case 10: controllerMapping.setLeft(buttonCode); progress++; break
            case 11: controllerMapping.setRight(buttonCode); progress++; break
        }
    }

    @Override
    void onButtonUp(GamePad gamePad, int buttonCode) {

    }

    @Override
    void onAxisChanged(GamePad gamePad, int axisCode, float axisValue) {
        println "code: ${axisCode} , value: ${axisValue}"
        if (axisValue as int == 0) return

        switch (progress) {
            case 8: controllerMapping.setUp(axisValue as int); controllerMapping.setVerticalAxis(axisCode as int); progress++; break
            case 9: controllerMapping.setDown(axisValue as int); progress++; break
            case 10: controllerMapping.setLeft(axisValue as int); controllerMapping.setHorizontalAxis(axisCode as int); progress++; break
            case 11: controllerMapping.setRight(axisValue as int); progress++; break
        }
    }

    @Override
    void onAccelerometerChanged(GamePad gamePad, int accelerometerCode, Vector3 value) {

    }
}
