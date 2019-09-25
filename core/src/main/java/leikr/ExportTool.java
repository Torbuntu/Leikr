/*
 * Copyright 2019 See AUTHORS file.
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.mini2Dx.core.Mdx;

/**
 *
 * @author tor
 */
public class ExportTool {

    static List<String> totalFiles = new ArrayList<String>();

    public static String exportAll() {
        try {
            Arrays.asList(Mdx.files.local("Programs/").list()).forEach(file -> {
                zip(file.name());
            });
            return "Projects exported.";
        } catch (IOException ex) {
            Logger.getLogger(ExportTool.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Failed to export all projects.";
    }

    public static String export(String project) {
        try {
            zip(project);
            return "Package [" + project + "] exported successfully. Check Packages directory.";
        } catch (Exception ex) {
            Logger.getLogger(ExportTool.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Failure to export Package. Please check logs.";
    }

    public static String importProject(String project) {
        try {
            unzip(project);
            return "Package [" + project + "] installed successfully. Check Programs directory.";
        } catch (Exception ex) {
            Logger.getLogger(ExportTool.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Failure to install Package. Please check logs.";
    }

    // With help from: https://www.thejavaprogrammer.com/java-zip-unzip-files/
    public static void zip(String name) {

        try {
            File dir = new File(Mdx.files.local("Programs/" + name).path());
            String dirPath = dir.getAbsolutePath();

            listFiles(dir);
            File exportDir = new File(Mdx.files.local("Packages/").path());
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }
            File zipFile = new File(Mdx.files.local("Packages/" + name).path() + ".lkr");

            try (FileOutputStream fos = new FileOutputStream(zipFile);
                    ZipOutputStream zos = new ZipOutputStream(fos)) {
                byte[] buffer = new byte[1024];
                int len;
                for (String path : totalFiles) {
                    ZipEntry zen = new ZipEntry(path.substring(dirPath.length() + 1, path.length()));
                    zos.putNextEntry(zen);
                    try (FileInputStream fis = new FileInputStream(new File(path))) {
                        while ((len = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, len);
                        }
                        zos.closeEntry();
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ExportTool.class.getName()).log(Level.SEVERE, null, ex);
        }
        totalFiles.clear();
    }

    static void listFiles(File dir) throws IOException {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                listFiles(file);
            } else {
                totalFiles.add(file.getAbsolutePath());
            }
        }
    }

    public static void unzip(String zipName) {
        File outputDir = new File(Mdx.files.local("Programs/" + zipName).path());

        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        byte[] buffer = new byte[1024];
        int len;
        File lkrPackage = new File(Mdx.files.local("Packages/" + zipName).path() + ".lkr");

        try (FileInputStream fis = new FileInputStream(lkrPackage);
                ZipInputStream zis = new ZipInputStream(fis)) {
            ZipEntry zen = zis.getNextEntry();
            while (zen != null) {
                String fileName = zen.getName();

                File newFile = new File(outputDir + File.separator + fileName);

                new File(newFile.getParent()).mkdirs();

                try (FileOutputStream fos = new FileOutputStream(newFile)) {
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                }
                zis.closeEntry();
                zen = zis.getNextEntry();
            }
            zis.closeEntry();
            fis.close();

        } catch (IOException ex) {
            Logger.getLogger(ExportTool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
