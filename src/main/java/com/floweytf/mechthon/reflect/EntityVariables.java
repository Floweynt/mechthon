package com.floweytf.mechthon.reflect;

import com.floweytf.mechthon.bindings.impl.BindingMember;
import com.floweytf.mechthon.util.MethodHandlesLookup;
import it.unimi.dsi.fastutil.Pair;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import org.bukkit.entity.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityVariables {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityVariables.class);
    private static final MethodHandlesLookup LOOKUP = new MethodHandlesLookup(MethodHandles.lookup());
    private static final List<String> GETTER_PREFIX = Arrays.asList(
        "getCan", "get", "is", "has", "can", "should", "will"
    );
    private static final List<String> SETTER_PREFIX = Arrays.asList(
        "setIs", "setHas", "setCan", "setShould", "setWill", "set"
    );

    private static Map<String, Method> doFilter(Class<?> cl, List<String> pre, BiPredicate<Class<?>, Class<?>[]> p) {
        return Arrays.stream(cl.getDeclaredMethods())
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

    private static void process(Class<?> clazz, BiConsumer<String, BindingMember> consumer) {
        // Process getters
        final var getters = doFilter(clazz, GETTER_PREFIX, (ret, args) -> ret != void.class && args.length == 0);
        final var setters = doFilter(clazz, SETTER_PREFIX, (ret, args) -> ret == void.class && args.length == 1);

        setters.keySet().stream()
            .filter(x -> !getters.containsKey(x))
            .forEach(it -> LOGGER.warn("disassociated setter in class {}: {}", clazz.getCanonicalName(), it));

        // Compare getters and setters
        getters.forEach((name, getter) -> {
            Method setter = setters.get(name);
            if (setter != null) {
                Class<?>[] setterParamTypes = setter.getParameterTypes();
                Class<?> getterReturnType = getter.getReturnType();

                if (!getterReturnType.equals(setterParamTypes[0])) {
                    LOGGER.warn(
                        "mismatched getter/setter in class {}: {}/{}",
                        clazz.getName(), getter.getName(), setter.getName()
                    );
                } else {
                    consumer.accept(name, BindingMember.property(
                        LOOKUP.unreflect(getter),
                        LOOKUP.unreflect(setter)
                    ));
                }
            } else {
                consumer.accept(name, BindingMember.property(LOOKUP.unreflect(getter)));
            }
        });
    }

    private static Set<Class<?>> hierarchyClosure(Class<?> clazz) {
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

    public static void scan() {
        Arrays.stream(EntityType.values())
            .map(EntityType::getEntityClass)
            .filter(Objects::nonNull)
            .flatMap(c -> hierarchyClosure(c).stream())
            .distinct()
            .filter(x -> x.getName().startsWith("org.bukkit.entity") && x.isInterface())
            .forEach(aClass -> {
                process(aClass, (s, member) -> {
                    // LOGGER.info("{} {}", s, member);
                });
            });
    }
}
