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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 *
 * @author tor
 * 
 * This utility is used to manage .leikr files. 
 * 
 * Used to both compress and archive, as well as extract and install to Programs
 */
public class LeikrPackageManager {
    
    public static void compress(String name, File... files) throws IOException {
        try (JarArchiveOutputStream out = new JarArchiveOutputStream(new FileOutputStream(name))){
            for (File file : files){
                addToArchiveCompression(out, file, "");
            }
        }
    }

    public static void decompress(String in, File destination) throws IOException {
        try (JarArchiveInputStream jin = new JarArchiveInputStream(new FileInputStream(in))){
            JarArchiveEntry entry;
            while ((entry = jin.getNextJarEntry()) != null) {
                if (entry.isDirectory()){
                    continue;
                }
                File curfile = new File(destination, entry.getName());
                File parent = curfile.getParentFile();
                if (!parent.exists()) {
                    if (!parent.mkdirs()){
                        throw new RuntimeException("could not create directory: " + parent.getPath());
                    }
                }
                IOUtils.copy(jin, new FileOutputStream(curfile));
            }
        }
    }

    private static void addToArchiveCompression(JarArchiveOutputStream out, File file, String dir) throws IOException {
        String name = dir + File.separator + file.getName();
        if (file.isFile()){
            JarArchiveEntry entry = new JarArchiveEntry(name);
            out.putArchiveEntry(entry);
            entry.setSize(file.length());
            IOUtils.copy(new FileInputStream(file), out);
            out.closeArchiveEntry();
        } else if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null){
                for (File child : children){
                    addToArchiveCompression(out, child, name);
                }
            }
        } else {
            System.out.println(file.getName() + " is not supported");
        }
    }
}
