package com.floweytf.mechthon.codegen;

import it.unimi.dsi.fastutil.Pair;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;

public class ReflectionUtil {
    private static final List<String> GETTER_PREFIX = Arrays.asList(
        "getCan", "get", "is", "has", "can", "should", "will"
    );
    private static final List<String> SETTER_PREFIX = Arrays.asList(
        "setIs", "setHas", "setCan", "setShould", "setWill", "set"
    );

    private static Map<String, Method> doFilter(Collection<Method> methods, List<String> pre, BiPredicate<Class<?>,
        Class<?>[]> p) {
        return methods.stream()
            .map(method -> {
                if (!p.test(method.getReturnType(), method.getParameterTypes())) {
                    return null;
                }

                if (method.getAnnotation(Deprecated.class) != null) {
                    return null;
                }

                for (final var s : pre) {
                    if (method.getName().startsWith(s)) {
                        return Pair.of(method.getName().substring(s.length()), method);
                    }
                }

                return null;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.groupingBy(Pair::first))
            .entrySet()
            .stream()
            .filter(e -> e.getValue().size() == 1)
            .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(0).second()));
    }

    private static Property mkProp(String name, String getter, @Nullable String setter, Class<?> type,
                                   boolean nullable) {
        if (getter.startsWith("getCan") || getter.startsWith("can")) {
            name = "Can" + name;
        } else if (getter.startsWith("is")) {
            name = "Is" + name;
        } else if (getter.startsWith("has")) {
            name = "Has" + name;
        } else if (getter.startsWith("should")) {
            name = "Should" + name;
        } else if (getter.startsWith("will")) {
            name = "Will" + name;
        }

        return new Property(name, getter, setter, type, nullable);
    }

    public static boolean isOverride(Method subclassMethod) {
        // Cannot override if method is private or static
        final var modifiers = subclassMethod.getModifiers();
        if (Modifier.isPrivate(modifiers) || Modifier.isStatic(modifiers)) {
            return false;
        }

        final var methodName = subclassMethod.getName();
        final var paramTypes = subclassMethod.getParameterTypes();
        final var allParents = findParents(subclassMethod.getDeclaringClass());

        for (Class<?> parent : allParents) {
            if (parent.equals(subclassMethod.getDeclaringClass())) {
                continue;
            }

            try {
                Method parentMethod = parent.getDeclaredMethod(methodName, paramTypes);
                int parentModifiers = parentMethod.getModifiers();
                if (!Modifier.isPrivate(parentModifiers) && !Modifier.isStatic(parentModifiers)) {
                    if (parentMethod.getReturnType().isAssignableFrom(subclassMethod.getReturnType())) {
                        return true;
                    }
                }
            } catch (NoSuchMethodException ignored) {
            }
        }

        return false;
    }

    public static ClassDesc process(Class<?> clazz) {
        // Process getters

        final var methods = Arrays.stream(clazz.getDeclaredMethods())
            .filter(x -> x.getAnnotation(Deprecated.class) == null)
            .filter(x -> !isOverride(x))
            .collect(Collectors.toCollection(HashSet::new));

        final var getters = doFilter(methods, GETTER_PREFIX, (ret, args) -> ret != void.class && args.length == 0);
        final var setters = doFilter(methods, SETTER_PREFIX, (ret, args) -> ret == void.class && args.length == 1);

        setters.keySet().stream()
            .filter(x -> !getters.containsKey(x))
            .forEach(it -> System.err.printf("disassociated setter in class %s: %s\n", clazz.getCanonicalName(), it));

        final var props = new ArrayList<Property>();

        // Compare getters and setters
        getters.forEach((name, getter) -> {
            Class<?> getterReturnType = getter.getReturnType();

            Method setter = setters.get(name);
            final var getterNullable = ClassFileAccess.getReturnInvisTyAnn(Nullable.class, getter) != null;

            if (setter != null) {
                final var setterNullable = ClassFileAccess.getParamInvisTyAnn(Nullable.class, setter, 0) != null;

                Class<?>[] setterParamTypes = setter.getParameterTypes();

                if (setterNullable && !getterNullable) {
                    System.err.println("warning: you need to write a nullable setter for " + setter);
                }

                // only warn if the getter is nullable but the setter is not
                if (!getterReturnType.equals(setterParamTypes[0]) || (!setterNullable && getterNullable)) {
                    System.err.printf(
                        "mismatched getter/setter in class %s: %s/%s\n",
                        clazz.getName(), getter.getName(), setter.getName()
                    );
                } else {
                    methods.remove(getter);
                    methods.remove(setter);
                    props.add(mkProp(name, getter.getName(), setter.getName(), getterReturnType, getterNullable));
                }
            } else {
                methods.remove(getter);
                props.add(mkProp(name, getter.getName(), null, getterReturnType, getterNullable));
            }
        });

        return new ClassDesc(
            clazz,
            props,
            new ArrayList<>(methods),
            Stream.concat(Stream.ofNullable(clazz.getSuperclass()), Arrays.stream(clazz.getInterfaces()))
                .filter(Objects::nonNull)
                .toList()
        );
    }

    public static Set<Class<?>> findParents(Class<?> clazz) {
        final var visited = new HashSet<Class<?>>();
        final var queue = new ArrayDeque<Class<?>>();
        queue.add(clazz);

        while (!queue.isEmpty()) {
            final var current = queue.poll();

            if (!visited.add(current)) {
                continue;
            }

            final var superclass = current.getSuperclass();
            if (superclass != null) {
                queue.add(superclass);
            }
            queue.addAll(Arrays.asList(current.getInterfaces()));
        }

        return visited;
    }
}
