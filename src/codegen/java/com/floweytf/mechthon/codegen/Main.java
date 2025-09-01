package com.floweytf.mechthon.codegen;

import it.unimi.dsi.fastutil.Pair;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class Main {
    private static List<ClassDesc> topoSort(List<ClassDesc> descs) {
        final var inDegree = new HashMap<ClassDesc, Integer>();
        final var graph = new HashMap<ClassDesc, List<ClassDesc>>();

        for (final var desc : descs) {
            inDegree.put(desc, 0);
            graph.put(desc, new ArrayList<>());
        }

        for (int i = 0; i < descs.size(); i++) {
            for (int j = 0; j < descs.size(); j++) {
                if (i != j) {
                    final var parent = descs.get(i);
                    final var child = descs.get(j);

                    if (parent.clazz().isAssignableFrom(child.clazz())) {
                        graph.get(parent).add(child);
                        inDegree.put(child, inDegree.get(child) + 1);
                    }
                }
            }
        }

        final var queue = new ArrayDeque<ClassDesc>();
        for (final var entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.offer(entry.getKey());
            }
        }

        final var sortedList = new ArrayList<ClassDesc>();
        while (!queue.isEmpty()) {
            final var current = queue.poll();
            sortedList.add(current);

            for (final var child : graph.get(current)) {
                inDegree.put(child, inDegree.get(child) - 1);
                if (inDegree.get(child) == 0) {
                    queue.offer(child);
                }
            }
        }

        if (sortedList.size() != descs.size()) {
            throw new IllegalStateException("Cycle detected in the graph.");
        }

        return sortedList;
    }

    public static void main(String[] args) throws IOException {
        final var outputDir = Path.of("build/codegen/bindings/");

        try (final var paths = Files.walk(outputDir)) {
            final Iterable<Path> iter = () -> paths.sorted(Comparator.reverseOrder()).iterator();
            for (final var path : iter) {
                Files.delete(path);
            }
        } catch (IOException ignored) {
        }

        final var tasks = Stream.of(
                Arrays.stream(EntityType.values())
                    .map(EntityType::getEntityClass),
                Stream.of(
                    World.class
                )
            ).flatMap(x -> x)
            .filter(Objects::nonNull)
            .flatMap(c -> ReflectionUtil.findParents(c).stream())
            .distinct()
            .filter(Class::isInterface)
            .map(ReflectionUtil::process)
            .collect(Collectors.groupingBy(x -> Packaging.match(x.clazz())))
            .entrySet()
            .stream()
            .map(x -> Pair.of(
                x.getKey(),
                topoSort(x.getValue()).stream().map(ClassDesc::codegenPython).collect(Collectors.joining("\n"))
            ))
            .toList();


        for (final var task : tasks) {
            final var path = outputDir.resolve(task.first().path);
            Files.createDirectories(path.getParent());
            Files.writeString(path, task.right());
        }

        try (final var pw = new PrintWriter(Files.newOutputStream(outputDir.resolve("entity/_entity_type.py")))) {
            pw.printf("""
                from enum import Enum, auto
                
                class EntityType(Enum):
                """);

            for (final var value : EntityType.values()) {
                pw.printf("    %s = auto()\n", value.name());
            }

            pw.printf("_ENTITY_TYPE_ENUM_MIRROR = EnumMirror(EntityType, \"%s\")\n", EntityType.class.getName());
        }

        try (final var pw = new PrintWriter(Files.newOutputStream(outputDir.resolve("entity/_mappings.py")))) {
            pw.printf("""
                from mechs._internal import BukkitType
                from ._entity_type import EntityType, _ENTITY_TYPE_ENUM_MIRROR
                
                _entity_type_to_constructor = {
                """);

            for (final var value : EntityType.values()) {
                final var ec = value.getEntityClass();
                pw.printf("    _ENTITY_TYPE_ENUM_MIRROR.to_native(EntityType.%s): %s,\n",
                    value.name(),
                    (ec == null ? Entity.class : ec).getSimpleName()
                );
            }

            pw.printf("""
                }
                
                def wrap_entity(ty: BukkitType) -> Entity:
                    return _entity_type_to_constructor[ty.getType()](ty)
                """);
        }
    }
}
