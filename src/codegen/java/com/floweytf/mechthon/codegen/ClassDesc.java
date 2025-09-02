package com.floweytf.mechthon.codegen;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import com.google.common.base.Preconditions;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public record ClassDesc(Class<?> clazz, List<Property> properties, List<Method> dissociatedMethods,
                        List<Class<?>> supers) {
    private static final Converter<String, String> CONVERTER =
        CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE);

    private static final Converter<String, String> CONVERTER_LC =
        CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.LOWER_UNDERSCORE);

    private static final Map<Class<?>, String> PRIM_TO_PY = Map.of(
        boolean.class, "bool",
        byte.class, "int",
        short.class, "int",
        int.class, "int",
        long.class, "int",
        float.class, "float",
        double.class, "float"
    );

    private static void genField(
        PrintWriter builder, String name,
        Property property,
        String typeName,
        String fromNative, String toNative) {

        if (property.nullable()) {
            typeName = "Optional[%s]".formatted(typeName);
            fromNative = "lambda x: None if x is None else %s(x)".formatted(fromNative);
            toNative = "lambda x: None if x is None else %s(x)".formatted(toNative);
        }

        if (property.setterName() != null) {
            builder.printf("    %s = TransformedRWProp[%s](\"%s\", \"%s\", %s, %s)\n",
                name,
                typeName,
                property.getterName(),
                property.setterName(),
                fromNative,
                toNative
            );
        } else {
            builder.printf("    %s = TransformedROProp[%s](\"%s\", %s)\n",
                name,
                typeName,
                property.getterName(),
                fromNative
            );
        }
    }

    private static void genWrapperField(
        PrintWriter builder, String name,
        Property property,
        String typeName,
        String fromNative) {
        Preconditions.checkState(!property.nullable());

        if (property.setterName() != null) {
            builder.printf("    %s = BukkitWrapperRWProp[%s](\"%s\", \"%s\", %s)\n",
                name,
                typeName,
                property.getterName(),
                property.setterName(),
                fromNative
            );
        } else {
            builder.printf("    %s = TransformedROProp[%s](\"%s\", %s)\n",
                name,
                typeName,
                property.getterName(),
                fromNative
            );
        }
    }

    public String codegenPython() {
        final var outputStream = new ByteArrayOutputStream();

        final var builder = new PrintWriter(outputStream);

        final var superList = supers.stream().map(Class::getSimpleName).collect(Collectors.joining(", "));

        builder.printf("class %s(%s):\n",
            clazz.getSimpleName(),
            superList.isEmpty() ? "BukkitWrapper" : superList
        );

        builder.printf("""
                @binding_constructor("%s")
                def __init__(self, delegate: BukkitType):
                    self._delegate = delegate
            
            """, clazz.getName());

        final var genSet = new HashSet<Class<?>>();

        for (final var property : properties) {
            final var name = CONVERTER.convert(property.name());

            // if it's primitive-like, handle it as such
            if (PRIM_TO_PY.containsKey(property.type())) {
                if (property.setterName() != null) {
                    builder.printf("    %s = RWProp[%s](\"%s\", \"%s\")\n",
                        name,
                        PRIM_TO_PY.get(property.type()),
                        property.getterName(),
                        property.setterName()
                    );
                } else {
                    builder.printf("    %s = ROProp[%s](\"%s\")\n",
                        name,
                        PRIM_TO_PY.get(property.type()),
                        property.getterName()
                    );
                }
            } else if (property.type().isEnum() && property.type().getDeclaringClass() == clazz) {
                final var enumMirrorName =
                    "_%s_enum_mirror".formatted(CONVERTER.convert(property.type().getSimpleName()));

                if (genSet.add(property.type())) {
                    builder.printf("    class %s(Enum):\n", property.type().getSimpleName());
                    for (final var constant : property.type().getEnumConstants()) {
                        final var enumName = ((Enum<?>) constant).name();
                        builder.printf("        %s = auto()\n", enumName);
                    }

                    builder.println();
                    builder.printf("    %s = EnumMirror(%s, \"%s\")\n",
                        enumMirrorName,
                        property.type().getSimpleName(),
                        property.type().getName()
                    );
                }

                genField(
                    builder, name, property, property.type().getSimpleName(),
                    enumMirrorName + ".from_native", enumMirrorName + ".to_native"
                );
            } else if (property.type() == UUID.class) {
                genField(
                    builder, name, property, property.type().getSimpleName(),
                    "java_uuid_to_python", "python_uuid_to_java"
                );
            } else if (property.type() == Entity.class && !property.nullable()) {
                genWrapperField(
                    builder, name, property, property.type().getSimpleName(),
                    "wrap_entity"
                );
            } else if (property.type() == Vector.class) {
                genField(
                    builder, name, property, property.type().getSimpleName(),
                    "wrap_vector", "unwrap_vector"
                );
            } else {
                builder.printf("    # TODO: property %s %s %s\n", name, property.getterName(), property.setterName());
            }
        }

        for (final var method : dissociatedMethods) {
            if (method.getName().equals("spigot")) {
                continue;
            }

            if (method.getReturnType() == void.class && method.getParameterCount() == 0) {
                builder.printf("    def %s(self): self._delegate.%s()\n", CONVERTER_LC.convert(method.getName()),
                    method.getName());
            } else {
                builder.printf("    # TODO: method %s\n", method.toGenericString());
            }
        }

        builder.close();
        return outputStream.toString();
    }
}
