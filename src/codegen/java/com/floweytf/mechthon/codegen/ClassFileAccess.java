package com.floweytf.mechthon.codegen;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.TypeReference;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

public class ClassFileAccess {
    private static final Map<Class<?>, ClassNode> cache = new HashMap<>();

    public static ClassNode getNode(Class<?> clazz) {
        return cache.computeIfAbsent(clazz, ignored -> {
            try (final var stream =
                     ClassFileAccess.class.getResourceAsStream("/" + clazz.getName().replace(".", "/") + ".class")) {
                Preconditions.checkNotNull(stream);
                final var cr = new ClassReader(stream);
                final var node = new ClassNode();
                cr.accept(node, ClassReader.SKIP_CODE);
                return node;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static AnnotationNode getReturnInvisTyAnn(Class<?> annotation, Method method) {
        final var node = getNode(method.getDeclaringClass());
        for (final var m : node.methods) {
            if (!(m.name.equals(method.getName()) && m.desc.equals(Type.getMethodDescriptor(method)))) {
                continue;
            }

            final var invisibleTypeAnnotations = m.invisibleTypeAnnotations;
            if (invisibleTypeAnnotations == null) {
                return null;
            }

            return invisibleTypeAnnotations.stream()
                .filter(x -> new TypeReference(x.typeRef).getSort() == TypeReference.METHOD_RETURN)
                .filter(x -> x.desc.equals(Type.getDescriptor(annotation)))
                .findFirst()
                .orElse(null);
        }

        throw new AssertionError("unreachable");
    }

    public static AnnotationNode getParamInvisTyAnn(Class<?> annotation, Method method, int argIdx) {
        final var node = getNode(method.getDeclaringClass());
        for (final var m : node.methods) {
            if (!(m.name.equals(method.getName()) && m.desc.equals(Type.getMethodDescriptor(method)))) {
                continue;
            }

            final var invisibleTypeAnnotations = m.invisibleTypeAnnotations;
            if (invisibleTypeAnnotations == null) {
                return null;
            }

            return invisibleTypeAnnotations.stream()
                .filter(x -> {
                    final var tr = new TypeReference(x.typeRef);
                    return tr.getSort() == TypeReference.METHOD_FORMAL_PARAMETER &&
                        tr.getFormalParameterIndex() == argIdx;
                })
                .filter(x -> x.desc.equals(Type.getDescriptor(annotation)))
                .findFirst()
                .orElse(null);
        }

        throw new AssertionError("unreachable");
    }
}
