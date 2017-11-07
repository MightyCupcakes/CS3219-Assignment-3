package assignment3.webserver.requestprocessor;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import com.google.common.reflect.ClassPath;

public class RequestProcessorRegistry {

    private static final Logger logger = Logger.getLogger(RequestProcessorRegistry.class.toString());
    private static final RequestProcessorRegistry INSTANCE = new RequestProcessorRegistry();

    private final Map<String, RequestProcessorInfo> parserRegistry;

    private RequestProcessorRegistry() {
        this(RequestProcessorRegistry.class.getPackage().getName());
    }

    protected RequestProcessorRegistry(String packageName) {
        parserRegistry = new HashMap<>();
        getRequestProcessors(packageName);

        logger.info("Request registry started for package: " + packageName);
    }

    public static RequestProcessorRegistry getInstance() {
        return INSTANCE;
    }

    public Optional<RequestProcessor> getRequestProcessor(String commandWord) {
        RequestProcessorInfo requestProcessorInfo = parserRegistry.get(commandWord);

        if (requestProcessorInfo == null) {
            return Optional.empty();
        }

        try {
            RequestProcessor parser;

            parser = requestProcessorInfo.processorClass.newInstance();

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

            if (thisClass.isAnnotationPresent(RegisterProcessor.class)
                    && RequestProcessor.class.isAssignableFrom(thisClass)) {
                registerProcessor(thisClass);
            }
        }
    }

    private void registerProcessor(Class<?> clazz) {

        RequestProcessorInfo requestInfo = getProcessorInfo(clazz);

        parserRegistry.put(requestInfo.requestType, requestInfo);

        logger.info("Request processor for request type " + requestInfo.requestType + " is registered");
    }

    private RequestProcessorInfo getProcessorInfo(Class<?> thisClass) {
        Annotation[] annotations = thisClass.getAnnotations();

        RegisterProcessor registerAnnotation = null;

        for (Annotation thisAnnotation : annotations) {
            if (thisAnnotation instanceof RegisterProcessor) {
                registerAnnotation = (RegisterProcessor) thisAnnotation;
                break;
            }
        }

        assert registerAnnotation != null;
        return new RequestProcessorInfo(registerAnnotation.requestType(), thisClass);
    }

    private static class RequestProcessorInfo {
        public String requestType;
        public Class<? extends RequestProcessor> processorClass;

        @SuppressWarnings("unchecked")
        public RequestProcessorInfo(String requestType, Class<?> processorClass) {
            this.requestType = requestType;
            this.processorClass = (Class<? extends RequestProcessor>) processorClass;
        }
    }
}
