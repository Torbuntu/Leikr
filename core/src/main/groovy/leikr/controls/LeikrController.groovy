package leikr.controls

import leikr.properties.ControllerMapping
import org.mini2Dx.core.input.GamePad
import org.mini2Dx.core.input.GamePadListener
import org.mini2Dx.gdx.math.Vector3


class LeikrController implements GamePadListener {
	def playerId, instanceId
	def buttons = [
			"A"           : false,
			"B"           : false,
			"X"           : false,
			"Y"           : false,
			"SELECT"      : false,
			"START"       : false,
			"LEFT_BUMPER" : false,
			"RIGHT_BUMPER": false,
			"UP"          : false,
			"DOWN"        : false,
			"LEFT"        : false,
			"RIGHT"       : false
	]

	def buttonLookup = [:]

	def horizontalAxis, verticalAxis
	ControllerMapping controllerMapping

	LeikrController(int playerId) {
		this.playerId = playerId
		instanceId = 0
		controllerMapping = new ControllerMapping(playerId)
		buttonLookup = [
				(controllerMapping.getA())          : "A",
				(controllerMapping.getB())          : "B",
				(controllerMapping.getX())          : "X",
				(controllerMapping.getY())          : "Y",
				(controllerMapping.getSelect())     : "SELECT",
				(controllerMapping.getStart())      : "START",
				(controllerMapping.getLeftBumper()) : "LEFT_BUMPER",
				(controllerMapping.getRightBumper()): "RIGHT_BUMPER"
		]
		horizontalAxis = controllerMapping.getHorizontalAxis()
		verticalAxis = controllerMapping.getVerticalAxis()
		if (horizontalAxis == 999 || verticalAxis == 999) {
			buttonLookup.putAll(
					(controllerMapping.getUp()): "UP",
					(controllerMapping.getDown()): "DOWN",
					(controllerMapping.getLeft()): "LEFT",
					(controllerMapping.getRight()): "RIGHT")
		}
		println buttonLookup
	}

	LeikrController(int playerId, String model) {
		this.playerId = playerId
		instanceId = 0
		controllerMapping = new ControllerMapping(model)
		buttonLookup = [
				(controllerMapping.getA())          : "A",
				(controllerMapping.getB())          : "B",
				(controllerMapping.getX())          : "X",
				(controllerMapping.getY())          : "Y",
				(controllerMapping.getSelect())     : "SELECT",
				(controllerMapping.getStart())      : "START",
				(controllerMapping.getLeftBumper()) : "LEFT_BUMPER",
				(controllerMapping.getRightBumper()): "RIGHT_BUMPER"
		]
		horizontalAxis = controllerMapping.getHorizontalAxis()
		verticalAxis = controllerMapping.getVerticalAxis()
		if (horizontalAxis == 999 || verticalAxis == 999) {
			buttonLookup.putAll(
					(controllerMapping.getUp()): "UP",
					(controllerMapping.getDown()): "DOWN",
					(controllerMapping.getLeft()): "LEFT",
					(controllerMapping.getRight()): "RIGHT")
		}
	}

	@Override
	void onConnect(GamePad gamePad) {
		println "Connection: ${gamePad.getModelInfo()}, ${gamePad.getInstanceId()}, ${gamePad.getPlayerIndex()}, ${gamePad.getGamePadType()}"
	}

	@Override
	void onDisconnect(GamePad gamePad) {
		println "Where am I going?! Ah!..."
		println "Connection: ${gamePad.getModelInfo()}, ${gamePad.getInstanceId()}, ${gamePad.getPlayerIndex()}, ${gamePad.getGamePadType()}"
	}

	@Override
	void onButtonDown(GamePad gamePad, int buttonCode) {
		println "Code: $buttonCode, Set: ${buttonLookup[buttonCode]}"
		buttons.replace(buttonLookup.get(buttonCode) as String, true)
	}

	@Override
	void onButtonUp(GamePad gamePad, int buttonCode) {
		buttons.replace(buttonLookup.get(buttonCode) as String, false)
	}

	@Override
	void onAxisChanged(GamePad gamePad, int axisCode, float axisValue) {
		println "${axisCode} : ${axisValue}"
		if (axisValue as int == 0) {
			if (axisCode as int == horizontalAxis) {
				buttons.replace("LEFT", false)
				buttons.replace("RIGHT", false)
			}
			if (axisCode as int == verticalAxis) {
				buttons.replace("UP", false)
				buttons.replace("DOWN", false)
			}
		}
		if (axisCode as int == horizontalAxis) {
			if (axisValue as int == controllerMapping.getLeft()) {
				buttons.replace("LEFT", true)
			}
			if (axisValue as int == controllerMapping.getRight()) {
				buttons.replace("RIGHT", true)
			}
		}
		if (axisCode as int == verticalAxis) {
			if (axisValue as int == controllerMapping.getUp()) {
				buttons.replace("UP", true)
			}
			if (axisValue as int == controllerMapping.getDown()) {
				buttons.replace("DOWN", true)
			}
		}
	}

	@Override
	void onAccelerometerChanged(GamePad gamePad, int accelerometerCode, Vector3 value) {
		println accelerometerCode
	}

	boolean button(String key) {
		buttons.get(key)
	}

	boolean left() {
		buttons.get("LEFT")
	}
}
