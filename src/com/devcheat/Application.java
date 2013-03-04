package com.devcheat;

import com.devcheat.io.JarFileHandler;

/**
 * Created with IntelliJ IDEA.
 * User: Vincent
 * Date: 25/02/13
 * Time: 16:16
 * To change this template use File | Settings | File Templates.
 */
public class Application {
    public static void main(String[] args){
        JarFileHandler handler = new JarFileHandler("input.jar");
        handler.readJarFile();
    }
}
