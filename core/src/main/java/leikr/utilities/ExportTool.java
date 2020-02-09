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
package leikr.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
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

    public static String importProject(String project, String location) {
        try {
            unzip(project, location);
            return "Package [" + project + "] installed successfully. Check [" + location + "].";
        } catch (Exception ex) {
            Logger.getLogger(ExportTool.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Failure to install Package. Please check logs.";
    }

    public static void zip(String name) {
        File exportDir = new File(Mdx.files.local("Packages/").path());
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        zipFolder(new File(Mdx.files.local("Programs/" + name).path()).toPath(), new File(Mdx.files.local("Packages/" + name).path() + ".lkr").toPath());
    }
    
    public static void deployPackage(String name){
        zipFolder(new File(Mdx.files.local("Deploy/"+name).path()).toPath(), new File(Mdx.files.local("Deploy/"+name).path()+".zip").toPath());
    }

    //https://www.quickprogrammingtips.com/java/how-to-zip-a-folder-in-java.html
    private static void zipFolder(Path sourceFolderPath, Path zipPath) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath.toFile()))) {
            Files.walkFileTree(sourceFolderPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String path = sourceFolderPath.relativize(file).toString().replace('\\', '/');
                    zos.putNextEntry(new ZipEntry(path));
                    Files.copy(file, zos);
                    zos.closeEntry();
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (Exception ex) {
            Logger.getLogger(ExportTool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public static void unzip(String zipName, String location) {
        File outputDir = new File(Mdx.files.local(location + "/" + zipName).path());

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
