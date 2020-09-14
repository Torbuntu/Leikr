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

package leikr.commands

import spock.lang.Specification
import spock.lang.Unroll

/**
 *
 * @author tor
 */
class AboutCommandSpec extends Specification {
    
    def "getHelp returns correct help text for about command" () {
        given:
        def expectedResult = ">about [name]\nReads the property file of the given program name."
        AboutCommand command = new AboutCommand()
        
        when:
        def result = command.help()
        
        then:
        println "whoop"
        result == expectedResult
    }
    
    @Unroll
    def "execute returns #expected when command is #progName" () {
        given:
        AboutCommand command = new AboutCommand()
        def args = ["about"]
                
        when:
        def result = command.execute(args as String[])
        
        then:
        result == expected
        
        where:
        progName | expected
        ""       | "Pass a program name to get the program's about info."
    }
}
