package fr.heavenmoon.core.common;

import fr.heavenmoon.core.common.data.PlatformData;
import fr.heavenmoon.core.common.logger.LoggerAdapter;
import org.redisson.api.listener.MessageListener;

import java.nio.file.Path;

public interface MoonPlatform {

    MoonCommons getCommons();

    LoggerAdapter getMoonLogger();

    PlatformType getPlatformType();

    String getPlatformName();

    String getPlatformVersion();

    String getPluginVersion();

    Path getDataDirectory();
    
    String getHost();
    
    int getMaxPlayers();

    default void executeSync(Runnable runnable) {
        executeAsync(runnable);
    }

    MessageListener<String> getMessageEvent();

    void executeAsync(Runnable runnable);

    default PlatformData getPlatformData() {
        return new PlatformData(getPlatformType(), getPlatformName(), getPlatformVersion());
    }

    void sendMessage(String name, String message);

    void shutdown();

    void reboot();

}
