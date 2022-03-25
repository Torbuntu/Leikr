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
package leikr.desktop

import com.badlogic.gdx.Files
import com.badlogic.gdx.backends.lwjgl3.DesktopMini2DxGame
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Mini2DxConfig
import java.security.Security
import java.util.logging.Level
import java.util.logging.Logger
import leikr.GameRuntime

/**
 *
 * @author tor
 */
class DesktopLauncher {

	static void main(String[] args) {
		GameRuntime runtime = new GameRuntime(args, !args.contains("insecure"))

		if (args.contains("insecure")) {
			Logger.getLogger(Security.class.getName()).log(Level.WARNING, "Leikr is running without security.")
		}

		def config = new Lwjgl3Mini2DxConfig(runtime.getGAME_IDENTIFIER())

		config.with {
			setTitle(runtime.checkDirectLaunch() ? runtime.getGameName() : "Leikr")
			setWindowedMode(720, 480)
			useVsync(true)
			setWindowIcon(Files.FileType.Internal, "Data/Logo/logo-16x16.png", "Data/Logo/logo-32x32.png")
		}
		// http://www.groovy-lang.org/operators.html - use of .@ forces usage of the field instead of the getter
		config.@targetFPS = 60
		config.@foregroundFPS = 60

		// Custom window listener to detect file drag and drop operations
		config.@windowListener = new LeikrWindowListener(runtime)

		new DesktopMini2DxGame(runtime, config)
	}
}