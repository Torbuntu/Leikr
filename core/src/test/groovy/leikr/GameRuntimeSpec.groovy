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

package leikr

import spock.lang.Specification
import spock.lang.Unroll

/**
 *
 * @author tor
 */
class GameRuntimeSpec extends Specification {
    
    @Unroll
    def "gameRuntime constructor sets correct args when #desc" () {
        when:
        GameRuntime runtime = new GameRuntime(title)

        then:
        runtime.checkDirectLaunch()
        runtime.getGameName() == title
        runtime.getGamePath() == "Programs/" + title
        runtime.getToolPath() == "Data/Tools/" + title
        
        where:
        title       || desc
        "game"      || "title is game"
        "project"   || "title is project"
        
    }
	
}

