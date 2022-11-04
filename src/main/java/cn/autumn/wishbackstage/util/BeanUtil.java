package cn.autumn.wishbackstage.util;

import org.reflections.Reflections;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import static cn.autumn.wishbackstage.config.ConfigureContainer.RELATIVE_PATH;

/**
 * @author cf
 * Created in 2022/11/3
 */
public final class BeanUtil {

    private static Reflections reflections;

    @SuppressWarnings("all")
    public static void getTargetClass(Set<Class<?>> classes, Class<? extends Annotation> annotation, String packagePath) {

        if (reflections == null) reflections = new Reflections(packagePath);

        classes.addAll(reflections.getTypesAnnotatedWith(annotation));

    }

    public static void getTargetClasses(Set<Class<?>> classes, Class<? extends Annotation> annotation, String packaging) {
        getTargetClass(classes, annotation, packaging);
        File[] files = new File(RELATIVE_PATH + packaging.replace(".", "/")).listFiles();
        assert files != null;
        for (File file : files) {
            if (file.getName().equals("entity")) {
                System.out.println("sda");
            }
            if (file.isDirectory()) {
                getTargetClasses(classes, annotation, packaging + "." + file.getName());
            }
        }
    }


    public static Set<Class<?>> getTargetClasses(Class<? extends Annotation> annotation, String packagePath) {
        Set<Class<?>> classes = new HashSet<>();
        getTargetClasses(classes, annotation, packagePath);
        return classes;
    }
}
