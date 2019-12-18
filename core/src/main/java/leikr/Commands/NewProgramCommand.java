/*
 * Copyright 2019 See AUTHORS.
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
package leikr.Commands;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import leikr.managers.TerminalManager;

/**
 *
 * @author tor
 */
public class NewProgramCommand extends Command {
    public NewProgramCommand(){
        super.name = "new";
    }
    
    @Override
    public String execute(String[] command) {
        if (command.length == 2) {
            try {
                return leikr.NewProgramGenerator.setNewProgramFileName(command[1], "Default");
            } catch (IOException ex) {
                Logger.getLogger(NewProgramCommand.class.getName()).log(Level.SEVERE, null, ex);
                return "New program with name [" + command[1] + "] failed to generate.";
            }
        }
        TerminalManager.setState(TerminalManager.TerminalState.NEW_PROGRAM);
        return "Create a new program.";
    }
    
    @Override
    public String help(){
        return ">new [option]\nOpens a new project builder.\nIf run with option, will attempt to generate a project with the given name.";
    }

}
