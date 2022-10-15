package leikr.properties

import groovy.util.logging.Log4j2
import org.mini2Dx.core.Mdx

@Log4j2
class ControllerMapping {
	def a, b, x, y, select, start, leftBumper, rightBumper, up, down, left, right, horizontalAxis, verticalAxis
	def model

	ControllerMapping() {}

	ControllerMapping(int id) {
		if (!(Mdx.input.getGamePads().size > id)) {
			log.debug("No controller available for id: $id")
			return
		}
		try {
			model = Mdx.input.getGamePads().get(id).getModelInfo()
			if (model) {
				if (new File("Data/Controllers/${model}.properties").exists()) {
					log.info("Collecting mapping of known controller: ${model}")
					getKnownControllerMapping(model)
				} else {
					log.warn("Unmapped controller, switching to default mapping. Please create mapping for: $model")
					getDefaults()
				}
			}
		} catch (Exception ex) {
			log.error(ex)
		}
	}

	ControllerMapping(String model) {
		if (new File("Data/Controllers/${model}.properties").exists()) {
			log.info("Collecting mapping of known controller: ${model}")
			getKnownControllerMapping(model)
		} else {
			log.warn("Unmapped controller, switching to default mapping. Please create mapping for: $model")
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
		log.info("Data/Controllers/${modelName}.properties")
		Properties prop = new Properties()
		try (InputStream stream = new FileInputStream(new File("Data/Controllers/${modelName}.properties"))) {
			prop.load(stream)
			x = Integer.parseInt(prop.getProperty("btn_x"))
			a = Integer.parseInt(prop.getProperty("btn_a"))
			b = Integer.parseInt(prop.getProperty("btn_b"))
			y = Integer.parseInt(prop.getProperty("btn_y"))

			leftBumper = Integer.parseInt(prop.getProperty("btn_lbumper"))
			rightBumper = Integer.parseInt(prop.getProperty("btn_rbumper"))

			select = Integer.parseInt(prop.getProperty("btn_select"))
			start = Integer.parseInt(prop.getProperty("btn_start"))

			// default to analog stick directions
			up = Integer.parseInt(prop.getProperty("btn_up"))
			down = Integer.parseInt(prop.getProperty("btn_down"))
			left = Integer.parseInt(prop.getProperty("btn_left"))
			right = Integer.parseInt(prop.getProperty("btn_right"))

			horizontalAxis = Integer.parseInt(prop.getProperty("axis_horizontal"))
			verticalAxis = Integer.parseInt(prop.getProperty("axis_vertical"))
		} catch (Exception ex) {
			log.error(ex)
		}
	}
}
