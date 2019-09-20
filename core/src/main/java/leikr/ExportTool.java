/*
 * Copyright 2019 tor.
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
package leikr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.tools.Compiler;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.files.FileHandle;

/**
 *
 * @author tor
 */
public class ExportTool {

    public static void export(String project) {
        try {
            compileCode(project);
            createJar(project);
        } catch (IOException ex) {
            Logger.getLogger(ExportTool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void compileCode(String project) throws IOException {
        CompilerConfiguration cc = new CompilerConfiguration();
        cc.setClasspath(Mdx.files.local("Programs/" + project).path());

        cc.setTargetDirectory(Mdx.files.local("Programs/" + project).path());
        Compiler compiler = new Compiler(cc);

        FileHandle[] list = Mdx.files.local("Programs/" + project + "/Code/").list(".groovy");
        ArrayList<String> files = new ArrayList<>();
        for (FileHandle f : list) {
            files.add(f.path());
        }
        String[] out = new String[files.size()];
        out = files.toArray(out);
        compiler.compile(out);
    }

    private static void createJar(String program) {
        try {
            String s;
            Process p = Runtime.getRuntime().exec("jar cvf " + program + ".lkr -C Programs/" + program + "/ .");
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
        } catch (IOException ex) {
            Logger.getLogger(ExportTool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
