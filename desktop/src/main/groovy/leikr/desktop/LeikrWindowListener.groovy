/*
 * Copyright 2020 tor.
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

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Mini2DxWindow
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Mini2DxWindowListener
import java.util.logging.Level
import java.util.logging.Logger
import leikr.GameRuntime

/**
 *
 * @author tor
 */
class LeikrWindowListener implements Lwjgl3Mini2DxWindowListener {

	private final GameRuntime runtime

	LeikrWindowListener(GameRuntime runtime) {
		this.runtime = runtime
	}

	@Override
	void filesDropped(String[] files) {
		runtime.setFileDroppedTitle(files[0].substring(files[0].lastIndexOf('/') + 1, files[0].length()))
		log("New runtime title: " + runtime.getFileDroppedTitle())
		files.each(message -> log("Found file: $message"))
	}

	private static void log(String message) {
		Logger.getLogger(LeikrWindowListener.class.getName()).log(Level.INFO, message)
	}

	@Override
	boolean closeRequested() {
		return true
	}

	@Override
	void created(Lwjgl3Mini2DxWindow lmdw) {
	}

	@Override
	void resized(Lwjgl3Mini2DxWindow lmdw) {
	}

	@Override
	void iconified(boolean bln) {
	}

	@Override
	void maximized(boolean bln) {
	}

	@Override
	void focusLost() {
		log("[I] Focus lost")
	}

	@Override
	void focusGained() {
		log("[I] Window focused")
	}

	@Override
	void refreshRequested() {
	}

}
