package com.wisea.cyq;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarTest {
    public static void unzipJar(String destinationDir, String jarPath) throws IOException {
        File file = new File(jarPath);
        JarFile jar = new JarFile(file);
        for (Enumeration<JarEntry> enums = jar.entries(); enums.hasMoreElements(); ) {
            JarEntry entry = (JarEntry) enums.nextElement();
            String fileName = destinationDir + File.separator + entry.getName();
            File f = new File(fileName);
            if (fileName.endsWith("/")) {
                f.mkdirs();
            }
        }

        for (Enumeration<JarEntry> enums = jar.entries(); enums.hasMoreElements(); ) {
            JarEntry entry = (JarEntry) enums.nextElement();
            String fileName = destinationDir + File.separator + entry.getName();
            File f = new File(fileName);
            if (!fileName.endsWith("/")) {
                InputStream is = jar.getInputStream(entry);
                FileOutputStream fos = new FileOutputStream(f);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = is.read(buf1)) > 0) {
                    fos.write(buf1, 0, len);
                }
//                while (is.available() > 0) {
//                    fos.write(is.read());
//                }
                fos.close();
                is.close();
            }
        }
    }

    public static void main(String[] args) throws IOException {
//        long time1 = System.currentTimeMillis();
//        unzipJar("c:/users/chinoukin/desktop/222", "c:/users/chinoukin/desktop/222/cultivar-common-0.0.1-SNAPSHOT.jar");
//        long time2 = System.currentTimeMillis();
//        System.out.println("当前程序耗时：" + (time2 - time1) + "ms");
//
//        long nowMills = System.currentTimeMillis();
//        try (DirectoryStream<Path> stream = Files.newDirectoryStream(new File("c:/users/chinoukin/desktop/222").toPath(), "*")) {
//            for (Path entry : stream) {
//                if (Files.isDirectory(entry)) {
//                    BasicFileAttributeView basicFileAttributeView = Files.getFileAttributeView(entry, BasicFileAttributeView.class);
//                    BasicFileAttributes basicFileAttributes = basicFileAttributeView.readAttributes();
//                    long createTimeMills = basicFileAttributes.creationTime().toMillis();
//                    if (nowMills - createTimeMills <= 30 * 1000) {
//                        //Files.delete(entry);
//                        deleteDir(entry.toFile());
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        StringBuffer sb = new StringBuffer();
        sb.append("aa").append(",").append("bb").append(",");
        System.out.println(sb.substring(0,sb.length()-1));
    }

    public static boolean deleteDir(File objDir) {
        if (objDir.exists()) {
            File[] dirs = objDir.listFiles();
            for (int i = 0; i < dirs.length; i++) {
                if (dirs[i].isDirectory()) {
                    deleteDir(dirs[i]);
                } else {
                    dirs[i].delete();
                }
            }
        }
        return objDir.exists() ? objDir.delete() : true;
    }

}
