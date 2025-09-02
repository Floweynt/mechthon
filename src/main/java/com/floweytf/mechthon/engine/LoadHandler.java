package com.floweytf.mechthon.engine;

import java.io.IOException;
import java.nio.file.Path;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.format.NamedTextColor;
import org.graalvm.polyglot.PolyglotException;
import org.slf4j.Logger;

import static com.floweytf.mechthon.util.Util.sendMessage;

public interface LoadHandler {
    void perfBootstrap(long ms);

    void perfBindings(long ms);

    void perfLoad(int scripts, long ms);

    void warnIllegalExtension(Path path);

    void warnBadName(String scriptName, Path path);

    void warnIOException(String scriptName, Path path, IOException e);

    void warnPolyglotException(String scriptName, Path path, PolyglotException e);

    void warnException(IOException e);

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
            public void perfLoad(int scripts, long ms) {
                logger.info("[ScriptEngine] loaded {} scripts in {}ms", scripts, ms);
            }

            @Override
            public void warnIllegalExtension(Path path) {
                logger.warn("[ScriptEngine] illegal file extension (not .py), ignoring: {}", path);
            }

            @Override
            public void warnBadName(String scriptName, Path path) {
                logger.warn("[ScriptEngine] bad script name: {}", scriptName);
            }

            @Override
            public void warnIOException(String scriptName, Path path, IOException e) {
                logger.warn("[ScriptEngine] while reading script: {}", path, e);
            }

            @Override
            public void warnPolyglotException(String scriptName, Path path, PolyglotException e) {
                logger.warn("[ScriptEngine] while reading script: {}", path, e);
            }

            @Override
            public void warnException(IOException e) {
                logger.warn("[ScriptEngine] while loading scripts", e);
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
            public void perfLoad(int scripts, long ms) {
                sendMessage(audience, NamedTextColor.GRAY, "loaded %s scripts in %sms", scripts, ms);
            }

            @Override
            public void warnIllegalExtension(Path path) {
                sendMessage(audience, NamedTextColor.GOLD, "illegal file extension (not .py), ignoring: %s", path);
            }

            @Override
            public void warnBadName(String scriptName, Path path) {
                sendMessage(audience, NamedTextColor.GOLD, "bad script name: %s", scriptName);
            }

            @Override
            public void warnIOException(String scriptName, Path path, IOException e) {
                sendMessage(audience, NamedTextColor.GOLD, "while reading script %s: %s", scriptName, e);
            }

            @Override
            public void warnPolyglotException(String scriptName, Path path, PolyglotException e) {
                sendMessage(audience, NamedTextColor.GOLD, "while reading script %s: %s", scriptName, e);
            }

            @Override
            public void warnException(IOException e) {
                sendMessage(audience, NamedTextColor.GOLD, "while reading scripts %s: %s", e);
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
            public void perfLoad(int scripts, long ms) {
                for (LoadHandler arg : args) {
                    arg.perfLoad(scripts, ms);
                }
            }

            @Override
            public void warnIllegalExtension(Path path) {
                for (LoadHandler arg : args) {
                    arg.warnIllegalExtension(path);
                }
            }

            @Override
            public void warnBadName(String scriptName, Path path) {
                for (LoadHandler arg : args) {
                    arg.warnBadName(scriptName, path);
                }
            }

            @Override
            public void warnIOException(String scriptName, Path path, IOException e) {
                for (LoadHandler arg : args) {
                    arg.warnIOException(scriptName, path, e);
                }
            }

            @Override
            public void warnPolyglotException(String scriptName, Path path, PolyglotException e) {
                for (LoadHandler arg : args) {
                    arg.warnPolyglotException(scriptName, path, e);
                }
            }

            @Override
            public void warnException(IOException e) {
                for (LoadHandler arg : args) {
                    arg.warnException(e);
                }
            }
        };
    }
}
