package com.devcheat.io;


import com.devcheat.bytecode.ClassInfo;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

/**
 * User: Tageteg
 * Date: 16/08/12
 * Time: 19:51
 */
public class JarFileHandler {
    private JarFile jarFile;
    private Map<String, ClassInfo> class_files = new HashMap<String, ClassInfo>();
    private Map<String, byte[]> non_class_files = new HashMap<String, byte[]>();

    public JarFileHandler(String path) {
        if (path.endsWith(".jar")) {
            if (path.startsWith("http://")) {
                try {
                    URL url = new URL(path);
                    InputStream inStream = url.openStream();
                    BufferedInputStream bufIn = new BufferedInputStream(inStream);
                    path = path.substring(path.lastIndexOf("/") + 1, path.length());
                    File fileWrite = new File(path);
                    OutputStream out = new FileOutputStream(fileWrite);
                    BufferedOutputStream bufOut = new BufferedOutputStream(out);
                    byte buffer[] = new byte[1024];
                    while (true) {
                        int nRead = bufIn.read(buffer, 0, buffer.length);
                        if (nRead <= 0)
                            break;
                        bufOut.write(buffer, 0, nRead);
                    }
                    bufOut.flush();
                    out.close();
                    inStream.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            File file = new File(path);
            if (file.exists()) {
                try {
                    jarFile = new JarFile(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                throw new RuntimeException("File does not exist");
            }

        } else {
            throw new RuntimeException("Invalid path supplied");
        }
    }

    public Map<String,ClassInfo> getClassFiles(){
        return class_files;
    }

    public Map<String,byte[]> getNonClassFiles(){
        return non_class_files;
    }

    public void setClassFiles(Map<String, ClassInfo> class_files) {
        this.class_files = class_files;
    }

    public void setNonClassFiles(Map<String, byte[]> non_class_files) {
        this.non_class_files = non_class_files;
    }

    public void readJarFile() {
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry next = entries.nextElement();
            try {
                InputStream in = jarFile.getInputStream(next);

                if (next.getName().endsWith(".class")){
                    System.out.println(next.getName());
                    String name  = next.getName().substring(0, next.getName().lastIndexOf("."));
                    byte[] buffer = new byte[in.available()];
                    int nBytes = in.read(buffer);
                    if (nBytes <= 0)
                        continue;
                    ClassInfo node = new ClassInfo(buffer);


                    class_files.put(name, node);
                }else {
                    byte[] buffer = new byte[in.available()];
                    int nBytes = in.read(buffer);
                    if (nBytes <= 0)
                        continue;
                    non_class_files.put(next.getName(), buffer);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public void writeJarFile(Map<String, byte[]> files) {
        try {
            if (jarFile != null) {
                JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarFile.getName().replace(".jar", "_patched.jar")));
                for (String key : files.keySet()) {
                    byte[] file = files.get(key);
                    if (key.contains(".")) {
                        key = key.replaceAll("\\.", "/");
                    }
                    JarEntry je = new JarEntry(key);
                    jos.putNextEntry(je);
                    jos.write(file);
                    jos.closeEntry();
                }
                jos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeJarFile() {
        try {
            if (jarFile != null) {
                JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarFile.getName().replace(".jar", "_patched.jar")));
                for (String key : class_files.keySet()) {

                    ClassInfo file = class_files.get(key);
                    if (key.contains(".")) {
                        key = key.replaceAll("\\.", "/");
                    }
                    //  System.out.println(key+"->"+file.name);
                    JarEntry je = new JarEntry(key + ".class");
                    jos.putNextEntry(je);
                    jos.write(file.toByteArray());
                    jos.closeEntry();
                }
                for (String key : non_class_files.keySet()) {
                    byte[] file = non_class_files.get(key);
                    JarEntry je = new JarEntry(key);
                    jos.putNextEntry(je);
                    jos.write(file);
                    jos.closeEntry();
                }
                jos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
