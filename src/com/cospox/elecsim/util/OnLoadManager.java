package com.cospox.elecsim.util;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.cospox.elecsim.components.Component;

public final class OnLoadManager {

    public static void doOnLoad() throws Exception {
        for (Class<?> cls : getClasses(Component.class)) {
        	try {
            	Method onLoad = cls.getDeclaredMethod("onLoad");
            	onLoad.setAccessible(true);
            	onLoad.invoke(null);
        	} catch (NoSuchMethodException e) {
        		System.out.println("[WARNING] Class <" + cls.getName() + "> has no <onLoad> method");
        	}
        }
    }

    public static void doOnLoadPriority() throws Exception {
        for (Class<?> cls : getClasses(Component.class)) {
        	try {
            	Method onLoad = cls.getDeclaredMethod("onLoadPriority");
            	onLoad.setAccessible(true);
            	onLoad.invoke(null);
        	} catch (NoSuchMethodException e) {
        		System.out.println("[WARNING] Class <" + cls.getName() + "> has no <onLoadPriority> method");
        	}
        }
    }

    private static List<Class<?>> getClasses(Class<?> caller)
            throws IOException, URISyntaxException {

        return Files.walk(getPackagePath(caller))
                .filter(Files::isRegularFile)
                .filter(file -> file.toString().endsWith(".class"))
                .map(path -> mapPathToClass(path, caller.getPackage().getName()))
                .collect(Collectors.toList());
    }

    private static Class<?> mapPathToClass(Path clsPath, String packageName) {
        String className = clsPath.getFileName().toString();
        className = className.substring(0, className.length() - 6);
        return loadClass(packageName + "." + className);
    }

    private static Path getPackagePath(Class<?> caller)
            throws IOException, URISyntaxException {
        String packageName = createPackageName(caller);
        Enumeration<URL> resources = caller.getClassLoader()
                .getResources(packageName);
        
        URL element = resources.nextElement();
        
        if (element.toURI().toString().startsWith("file:")) {
        	return Paths.get(element.toURI());
        }
        
        final Map<String, String> env = new HashMap<>();
        final String[] array = element.toURI().toString().split("!");
        final FileSystem fs = FileSystems.newFileSystem(URI.create(array[0]), env);
        final Path path = fs.getPath(array[1]);

		return path;
    }

    private static String createPackageName(Class<?> caller) {
        return caller.getPackage().getName().replace(".", "/");
    }

    private static Class<?> loadClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
