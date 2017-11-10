package assignment3.webserver.registry;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import com.google.common.reflect.ClassPath;

public class WebServerRegistry<T> {

    private static final Logger logger = Logger.getLogger(WebServerRegistry.class.toString());

    private final Map<String, Class<?>> parserRegistry;

    public WebServerRegistry(String packageName) {
        parserRegistry = new HashMap<>();
        getRequestProcessors(packageName);

        logger.info("Request registry started for package: " + packageName);
    }

    public Optional<T> getHandler (String key) {
        Class<?> handler = parserRegistry.get(key);

        if (handler == null) {
            return Optional.empty();
        }

        try {
            T parser;

            parser = (T) handler.newInstance();

            return Optional.of(parser);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private void getRequestProcessors(String packageName) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        ClassPath classPath;

        try {
            classPath = ClassPath.from(loader);
        } catch (IOException e) {
            logger.warning("class loader not found.");
            return;
        }

        Set<ClassPath.ClassInfo> classes = classPath.getTopLevelClassesRecursive(packageName);

        for (ClassPath.ClassInfo classInfo : classes) {
            Class<?> thisClass = classInfo.load();

            if (thisClass.isAnnotationPresent(RegisterProcessor.class)) {
                registerProcessor(thisClass);
            }
        }
    }

    private void registerProcessor(Class<?> clazz) {

        Annotation[] annotations = clazz.getAnnotations();

        String key = "";

        for (Annotation thisAnnotation : annotations) {
            if (thisAnnotation instanceof RegisterProcessor) {
                key = ((RegisterProcessor) thisAnnotation).requestType();
                break;
            }
        }

        parserRegistry.put(key, clazz);

        logger.info("Request processor for request type " + key + " is registered");
    }
}
