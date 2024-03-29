package leikr.screens

import groovy.util.logging.Log4j2
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
@Log4j2
class ControllerMappingScreen extends BasicGameScreen implements GamePadListener {
	public static final int ID = 8
	def buttonMapping = [:]

	def stepText = ["Press: A", "Press: B", "Press: X", "Press: Y", "Press: SELECT", "Press: START",
					"Press: LEFT_BUMPER", "Press: RIGHT_BUMPER", "Press: UP", "Press: DOWN", "Press: LEFT", "Press: RIGHT"]

	// while not complete, we add new button codes and proceed
	def processing = true
	// We start with 0 and work towards a complete mapping of 8 buttons and then coordinate data.
	// directions are awkward because they can either be a directional pad or an analog stick.
	def progress = 0
	// The controller ID to map. Default to 0 for first controller
	def playerId = 0

	def modelInfo

	ManagerDTO managerDTO
	final FitViewport viewport
	final StretchViewport stretchViewport
	FrameBuffer framebuffer
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
		setControllerInfo(0)
	}

	def setControllerInfo(int id) {
		playerId = id
		Mdx.input.getGamePads().get(playerId).with {
			addListener(this)
			modelInfo = getModelInfo()
		}
	}

	@Override
	void preTransitionOut(Transition transition) {
		framebuffer.dispose()
		Mdx.input.getGamePads().each {
			it.removeListener(this)
		}
	}

	@Override
	void initialise(GameContainer gc) {}

	def saveToProps() {
		String filePath = "Data/Controllers/${modelInfo}.properties"
		Properties prop = new Properties()
		if (!Mdx.files.external(filePath).exists()) {
			log.warn("File does not exist, creating new: $filePath")
			if (!Mdx.files.external("Data/Controllers/").exists()) {
				if (new File("Data/Controllers/").mkdirs()) {
					log.info("Successfully created Data/Controllers directory")
				}
			}
			new File(filePath).createNewFile()
		}
		try (InputStream inputStream = new FileInputStream(new File(filePath))) {
			prop.load(inputStream)
		} catch (Exception ex) {
			log.error(ex)
			return false
		}
		try (FileOutputStream outputStream = new FileOutputStream(new File(filePath))) {
			buttonMapping.each {
				log.debug("${it.value}, ${it.key}")
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

			prop.setProperty("axis_horizontal", controllerMapping.getHorizontalAxis() as String ?: "999")
			prop.setProperty("axis_vertical", controllerMapping.getVerticalAxis() as String ?: "999")
			prop.store(outputStream, "Saved from Leikr Controller Mapping Utility")
		} catch (IOException | NumberFormatException ex) {
			log.error(ex)
			return false
		}
		log.info("Save was successful")
		return true
	}

	@Override
	void update(GameContainer gc, ScreenManager<? extends GameScreen> screenManager, float delta) {
		if (progress == 12) {
			progress = 0
			saveToProps()
			Mdx.input.getGamePads().each {
				it.removeListener(this)
				it.removeListener(managerDTO.getInputManager().getControllerA())
				it.removeListener(managerDTO.getInputManager().getControllerB())
			}
			managerDTO.inputManager.createControllers()
			if (Mdx.input.getGamePads().size() > 1 && playerId == 0) {
				setControllerInfo(1)
			} else {
				screenManager.enterGameScreen(MenuScreen.ID, null, null)
			}
		}
	}

	@Override
	void render(GameContainer gc, Graphics g) {
		stretchViewport.apply(g)
		framebuffer.begin()
		g.clearContext()
		g.drawString("Mapping: ${modelInfo}", 0, 0, 240, Align.CENTER)

		if (processing) {
			g.drawString(stepText[progress], 0, 40, 240, Align.CENTER)
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
		log.debug("code: ${axisCode} , value: ${axisValue}")
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
		// We don't use Accelerometer
	}
}
