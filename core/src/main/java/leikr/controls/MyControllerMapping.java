/*
 * Copyright 2019 torbuntu.
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
package leikr.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import de.golfgl.gdx.controllers.mapping.ConfiguredInput;
import de.golfgl.gdx.controllers.mapping.ControllerMappings;
import de.golfgl.gdx.controllers.mapping.ControllerToInputAdapter;

public class MyControllerMapping extends ControllerMappings {

    public static final int BUTTON_X = 0;
    public static final int BUTTON_Y = 1;
    public static final int BUTTON_A = 2;
    public static final int BUTTON_B = 3;
    public static final int BUTTON_START = 4;
    public static final int BUTTON_SELECT = 5;
    public static final int BUTTON_LBUMPER = 6;
    public static final int BUTTON_RBUMPER = 7;
    public static final int AXIS_VERTICAL = 8;
    public static final int AXIS_HORIZONTAL = 9;

    public ControllerToInputAdapter controllerToInputAdapter;
    public boolean loadedSavedSettings;

    public MyControllerMapping() {
        super();

        addConfiguredInput(new ConfiguredInput(ConfiguredInput.Type.button, BUTTON_X));
        addConfiguredInput(new ConfiguredInput(ConfiguredInput.Type.button, BUTTON_Y));
        addConfiguredInput(new ConfiguredInput(ConfiguredInput.Type.button, BUTTON_A));
        addConfiguredInput(new ConfiguredInput(ConfiguredInput.Type.button, BUTTON_B));
        addConfiguredInput(new ConfiguredInput(ConfiguredInput.Type.button, BUTTON_START));
        addConfiguredInput(new ConfiguredInput(ConfiguredInput.Type.button, BUTTON_SELECT));
        addConfiguredInput(new ConfiguredInput(ConfiguredInput.Type.button, BUTTON_LBUMPER));
        addConfiguredInput(new ConfiguredInput(ConfiguredInput.Type.button, BUTTON_RBUMPER));
        addConfiguredInput(new ConfiguredInput(ConfiguredInput.Type.axisDigital, AXIS_VERTICAL));
        addConfiguredInput(new ConfiguredInput(ConfiguredInput.Type.axisDigital, AXIS_HORIZONTAL));

        commitConfig();
        loadedSavedSettings = false;

        try {
            String json = ControllerMappingManager.loadControllerMappings();
            JsonValue jsonValue = new JsonReader().parse(json);
            if (jsonValue != null) {
                loadedSavedSettings = fillFromJson(jsonValue);
            }
        } catch (Throwable t) {
            Gdx.app.error("Prefs", "Error reading saved controller mappings", t);
        }

        controllerToInputAdapter = new ControllerToInputAdapter(this);

        controllerToInputAdapter.addButtonMapping(BUTTON_X, Input.Keys.S);
        controllerToInputAdapter.addButtonMapping(BUTTON_Y, Input.Keys.A);
        controllerToInputAdapter.addButtonMapping(BUTTON_A, Input.Keys.X);
        controllerToInputAdapter.addButtonMapping(BUTTON_B, Input.Keys.Z);
        controllerToInputAdapter.addButtonMapping(BUTTON_START, Input.Keys.ENTER);
        controllerToInputAdapter.addButtonMapping(BUTTON_SELECT, Input.Keys.SPACE);
        controllerToInputAdapter.addButtonMapping(BUTTON_LBUMPER, Input.Keys.Q);
        controllerToInputAdapter.addButtonMapping(BUTTON_RBUMPER, Input.Keys.W);
        controllerToInputAdapter.addAxisMapping(AXIS_HORIZONTAL, Input.Keys.LEFT, Input.Keys.RIGHT);
        controllerToInputAdapter.addAxisMapping(AXIS_VERTICAL, Input.Keys.UP, Input.Keys.DOWN);
    }

    public void setInputProcessor(InputProcessor input) {
        controllerToInputAdapter.setInputProcessor(input);
    }

    @Override
    public boolean getDefaultMapping(MappedInputs defaultMapping) {

        defaultMapping.putMapping(new MappedInput(AXIS_VERTICAL, new ControllerAxis(1)));
        defaultMapping.putMapping(new MappedInput(AXIS_HORIZONTAL, new ControllerAxis(0)));
        defaultMapping.putMapping(new MappedInput(BUTTON_X, new ControllerButton(0)));
        defaultMapping.putMapping(new MappedInput(BUTTON_Y, new ControllerButton(3)));
        defaultMapping.putMapping(new MappedInput(BUTTON_A, new ControllerButton(1)));
        defaultMapping.putMapping(new MappedInput(BUTTON_B, new ControllerButton(2)));
        defaultMapping.putMapping(new MappedInput(BUTTON_START, new ControllerButton(9)));
        defaultMapping.putMapping(new MappedInput(BUTTON_SELECT, new ControllerButton(8)));
        defaultMapping.putMapping(new MappedInput(BUTTON_LBUMPER, new ControllerButton(4)));
        defaultMapping.putMapping(new MappedInput(BUTTON_RBUMPER, new ControllerButton(5)));

        return true;
    }
}
