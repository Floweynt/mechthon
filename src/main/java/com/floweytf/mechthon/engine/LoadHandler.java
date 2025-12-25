package com.floweytf.mechthon.engine;

import java.io.IOException;
import java.nio.file.Path;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.format.NamedTextColor;
import org.graalvm.polyglot.PolyglotException;
import org.slf4j.Logger;

import static com.floweytf.mechthon.util.Util.sendMessage;

public interface LoadHandler {
    enum LoadType {
        SCRIPT("script", "py"),
        GLOBAL_PERSISTENT_KEY("global persistent key metadata", "json");

        private final String displayName;
        private final String extension;

        LoadType(String displayName, String extension) {
            this.displayName = displayName;
            this.extension = extension;
        }

        public String displayName() {
            return displayName;
        }

        public String extension() {
            return extension;
        }
    }

    void perfBootstrap(long ms);

    void perfBindings(long ms);

    void perfLoad(LoadType type, int count, long ms);

    void warnIllegalExtension(LoadType type, Path path);

    void warnBadName(LoadType type, String scriptName, Path path);

    void warnIOException(LoadType type, String scriptName, Path path, IOException e);

    void warnPolyglotException(LoadType type, String scriptName, Path path, PolyglotException e);

    void warnException(LoadType type, IOException e);

    static LoadHandler logging(Logger logger) {
        return new LoadHandler() {
            @Override
            public void perfBootstrap(long ms) {
                logger.info("[ScriptEngine] bootstrap took {}ms", ms);
            }

            @Override
            public void perfBindings(long ms) {
                logger.info("[ScriptEngine] binding initialization took {}ms", ms);
            }

            @Override
            public void perfLoad(LoadType type, int scripts, long ms) {
                logger.info("[ScriptEngine] loaded {} `{}` in {}ms", scripts, type.displayName(), ms);
            }

            @Override
            public void warnIllegalExtension(LoadType type, Path path) {
                logger.warn(
                    "[ScriptEngine] illegal file extension (expected .{}), ignoring: {}",
                    type.extension(), path
                );
            }

            @Override
            public void warnBadName(LoadType type, String scriptName, Path path) {
                logger.warn("[ScriptEngine] bad file name: {}", scriptName);
            }

            @Override
            public void warnIOException(LoadType type, String scriptName, Path path, IOException e) {
                logger.warn("[ScriptEngine] while reading {}: {}", type.displayName(), path, e);
            }

            @Override
            public void warnPolyglotException(LoadType type, String scriptName, Path path, PolyglotException e) {
                logger.warn("[ScriptEngine] while reading {}: {}", type.displayName(), path, e);
            }

            @Override
            public void warnException(LoadType type, IOException e) {
                logger.warn("[ScriptEngine] while loading {}", type.displayName(), e);
            }
        };
    }

    static LoadHandler broadcasting(Audience audience) {
        return new LoadHandler() {
            @Override
            public void perfBootstrap(long ms) {
                sendMessage(audience, NamedTextColor.GRAY, "bootstrap took %sms", ms);
            }

            @Override
            public void perfBindings(long ms) {
                sendMessage(audience, NamedTextColor.GRAY, "binding initialization took %sms", ms);
            }

            @Override
            public void perfLoad(LoadType type, int count, long ms) {
                sendMessage(audience, NamedTextColor.GRAY, "loaded %s '%s' in %sms", count, type.displayName(), ms);
            }

            @Override
            public void warnIllegalExtension(LoadType type, Path path) {
                sendMessage(audience, NamedTextColor.GOLD, "illegal file extension (expected .%s), ignoring: %s",
                    type.extension(), path);
            }

            @Override
            public void warnBadName(LoadType type, String scriptName, Path path) {
                sendMessage(audience, NamedTextColor.GOLD, "bad script name: %s", scriptName);
            }

            @Override
            public void warnIOException(LoadType type, String scriptName, Path path, IOException e) {
                sendMessage(audience, NamedTextColor.GOLD, "while reading %s: %s", type.displayName(), path, e);
            }

            @Override
            public void warnPolyglotException(LoadType type, String scriptName, Path path, PolyglotException e) {
                sendMessage(audience, NamedTextColor.GOLD, "while reading %s: %s", type.displayName(), path, e);
            }

            @Override
            public void warnException(LoadType type, IOException e) {
                sendMessage(audience, NamedTextColor.GOLD, "while loading %s: %s", type.displayName(), e);
            }
        };
    }

    static LoadHandler of(LoadHandler... args) {
        return new LoadHandler() {
            @Override
            public void perfBootstrap(long ms) {
                for (LoadHandler arg : args) {
                    arg.perfBootstrap(ms);
                }
            }

            @Override
            public void perfBindings(long ms) {
                for (LoadHandler arg : args) {
                    arg.perfBindings(ms);
                }
            }

            @Override
            public void perfLoad(LoadType type, int count, long ms) {
                for (LoadHandler arg : args) {
                    arg.perfLoad(type, count, ms);
                }
            }

            @Override
            public void warnIllegalExtension(LoadType type, Path path) {
                for (LoadHandler arg : args) {
                    arg.warnIllegalExtension(type, path);
                }
            }

            @Override
            public void warnBadName(LoadType type, String scriptName, Path path) {
                for (LoadHandler arg : args) {
                    arg.warnBadName(type, scriptName, path);
                }
            }

            @Override
            public void warnIOException(LoadType type, String scriptName, Path path, IOException e) {
                for (LoadHandler arg : args) {
                    arg.warnIOException(type, scriptName, path, e);
                }
            }

            @Override
            public void warnPolyglotException(LoadType type, String scriptName, Path path, PolyglotException e) {
                for (LoadHandler arg : args) {
                    arg.warnPolyglotException(type, scriptName, path, e);
                }
            }

            @Override
            public void warnException(LoadType type, IOException e) {
                for (LoadHandler arg : args) {
                    arg.warnException(type, e);
                }
            }
        };
    }
}
