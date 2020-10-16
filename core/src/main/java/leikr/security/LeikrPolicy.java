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
package leikr.security;

import java.io.FilePermission;
import java.security.PermissionCollection;
import java.security.Policy;
import java.security.ProtectionDomain;

/**
 *
 * @author tor
 */
public class LeikrPolicy extends Policy{
    FilePermission filePermission;
    
    public LeikrPolicy(String dir){
        setCustomLeikrRootDirectory(dir);
    }

    @Override
    public void refresh() {
        super.refresh(); //To change body of generated methods, choose Tools | Templates.
    }

   

    @Override
    public PermissionCollection getPermissions(ProtectionDomain domain) {
        return super.getPermissions(domain); //To change body of generated methods, choose Tools | Templates.
    }

    public void setCustomLeikrRootDirectory(String dir){
        filePermission = new FilePermission(dir, "read,write");
        Policy.setPolicy(this);
    }
    
}

//TODO: Set policy root dir on startup, if ENV is available, in GameRuntime