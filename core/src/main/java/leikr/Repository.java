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
package leikr;

import java.io.File;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;

/**
 *
 * @author tor
 */
public class Repository {

    public String lpmInstall(String repoType, String userName, String project) {
        repoType = (repoType.length() > 0) ? repoType : "github";

        try {
            Git.cloneRepository().setURI("https://" + repoType + ".com/" + userName + "/" + project + ".git").setDirectory(new File("./Programs/" + project)).call();
            return "Install success: `" + project + "`";
        } catch (Exception ex) {
            return "Install failure:  " + ex.getMessage();
        }
    }

    public String lpmUpdate(String project) {
        try (Git git = Git.open(new F‌ile("./Programs/" + project + "/.git"))) {
            PullCommand pc = git.pull();
            pc.call();
            return "Update success: `" + project + "`";
        } catch (Exception ex) {
            return "Update failure:  " + ex.getMessage();
        }
    }
}
