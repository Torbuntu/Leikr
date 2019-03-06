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

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonWriter;

import de.golfgl.gdx.controllers.ControllerMenuDialog;
import de.golfgl.gdx.controllers.mapping.ControllerMappings;

public class GamepadMappingDialog extends ControllerMenuDialog {

    private static final String PRESS_THE_BUTTON_TO = "Hold the button to ";
    private final Label instructionLabel;
    private final ControllerMappings mappings;
    private final Controller controller;
    private final TextButton skipButton;
    private final Label axisLabel;
    private final Label buttonLabel;
    private int currentStep = 0;
    private float timeSinceLastRecord = 0;
    private int inputToRecord = -1;

    public GamepadMappingDialog(Skin skin, Controller controller, ControllerMappings mappings) {
        super("", skin, Assets.WINDOW_SMALL);

        this.mappings = mappings;
        this.controller = controller;

        instructionLabel = new Label("", skin, Assets.LABEL_SIMPLE25);
        instructionLabel.setWrap(true);

        buttonLabel = new Label("", skin, Assets.LABEL_SIMPLE25);
        axisLabel = new Label("", skin, Assets.LABEL_SIMPLE25);

        String name = controller.getName();
        if (name.length() > 50) {
            name = name.substring(0, 48) + "...";
        }

        getContentTable().add(new Label("Configure " + name, skin, Assets.LABEL_SIMPLE25)).colspan(2);
        getContentTable().row();

        getContentTable().add(instructionLabel).fill().minWidth(MaryoGame.NATIVE_WIDTH * .7f)
                .minHeight(MaryoGame.NATIVE_HEIGHT * .5f).colspan(2);
        instructionLabel.setAlignment(Align.center);

        getButtonTable().defaults().pad(20, 40, 0, 40);

        skipButton = new ColorableTextButton("Skip", getSkin(), Assets.BUTTON_SMALL);
        skipButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (inputToRecord >= 0) {
                    currentStep = currentStep + 2 - (currentStep % 2);
                    switchStep();
                } else {
                    hide();
                }
            }
        });

        getButtonTable().add(skipButton);
        buttonsToAdd.add(skipButton);

        TextButton restartButton = new ColorableTextButton("Restart", getSkin(), Assets.BUTTON_SMALL);
        restartButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                currentStep = 0;
                switchStep();
            }
        });

        getButtonTable().add(restartButton);
        buttonsToAdd.add(restartButton);

        switchStep();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        timeSinceLastRecord += delta;

        if (timeSinceLastRecord > .25f && inputToRecord >= 0) {
            timeSinceLastRecord = 0;
            ControllerMappings.RecordResult recordResult = mappings.recordMapping(controller, inputToRecord);

            switch (recordResult) {
                case need_second_button:
                    currentStep++;
                    switchStep();
                    break;
                case recorded:
                    currentStep = currentStep + 2 - (currentStep % 2);
                    switchStep();
                    break;
                default:
                //nix zu tun, wir warten ab
            }
        }
    }

    private void switchStep() {
        switch (currentStep) {
            case 0:
                mappings.resetMappings(controller);
                instructionLabel.setText(PRESS_THE_BUTTON_TO + "move RIGHT");
                inputToRecord = MyControllerMapping.AXIS_HORIZONTAL;
                break;
            case 1:
                instructionLabel.setText(PRESS_THE_BUTTON_TO + "move LEFT");
                inputToRecord = MyControllerMapping.AXIS_HORIZONTAL;
                break;
            case 2:
                instructionLabel.setText(PRESS_THE_BUTTON_TO + "move DOWN");
                inputToRecord = MyControllerMapping.AXIS_VERTICAL;
                break;
            case 3:
                instructionLabel.setText(PRESS_THE_BUTTON_TO + "move UP");
                inputToRecord = MyControllerMapping.AXIS_VERTICAL;
                break;
            case 4:
            case 5:
                instructionLabel.setText(PRESS_THE_BUTTON_TO + "X");
                inputToRecord = MyControllerMapping.BUTTON_X;
                break;
            case 6:
            case 7:
                instructionLabel.setText(PRESS_THE_BUTTON_TO + "Y");
                inputToRecord = MyControllerMapping.BUTTON_Y;
                break;
            case 8:
            case 9:
                instructionLabel.setText(PRESS_THE_BUTTON_TO + "A");
                inputToRecord = MyControllerMapping.BUTTON_A;
                break;
            case 10:
            case 11:
                instructionLabel.setText(PRESS_THE_BUTTON_TO + "B");
                inputToRecord = MyControllerMapping.BUTTON_B;
                break;
            case 12:
            case 13:
                instructionLabel.setText(PRESS_THE_BUTTON_TO + "ENTER/START");
                inputToRecord = MyControllerMapping.BUTTON_START;
                break;
            case 14:
            case 15:
                instructionLabel.setText(PRESS_THE_BUTTON_TO + "ESCAPE/SELECT");
                inputToRecord = MyControllerMapping.BUTTON_SELECT;
                break;
            case 16:
            case 17:
                instructionLabel.setText(PRESS_THE_BUTTON_TO + "Left Bumper");
                inputToRecord = MyControllerMapping.BUTTON_LBUMPER;
                break;
            case 18:
            case 19:
                instructionLabel.setText(PRESS_THE_BUTTON_TO + "Right Bumper");
                inputToRecord = MyControllerMapping.BUTTON_RBUMPER;
                break;
            default:
                instructionLabel.setText("Finished");
                ControllerMappingManager.saveControllerMappings(mappings.toJson().toJson(JsonWriter.OutputType.json));
                skipButton.setText("OK");

                inputToRecord = -1;
        }
    }
}
