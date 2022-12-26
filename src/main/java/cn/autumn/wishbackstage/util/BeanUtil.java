package cn.autumn.wishbackstage.util;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

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
//        File[] files = new File(RELATIVE_PATH + packaging.replace(".", "/")).listFiles();
//        assert files != null;
//        for (File file : files) {
//            if (file.isDirectory()) {
//                getTargetClasses(classes, annotation, packaging + "." + file.getName());
//            }
//            if (!file.isFile()) continue;
//            reflections = new Reflections(file.getPath());
//
//            System.out.println("sad");
//        }
    }


    public static Set<Class<?>> getTargetClasses(Class<? extends Annotation> annotation, String packagePath) {
        Set<Class<?>> classes = new HashSet<>();
        getTargetClasses(classes, annotation, packagePath);
        return classes;
    }
}
