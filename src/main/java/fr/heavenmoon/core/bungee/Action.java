package fr.heavenmoon.core.bungee;

import fr.heavenmoon.core.bungee.commands.StaffCommand;
import fr.heavenmoon.core.bungee.listeners.CacheListener;
import fr.heavenmoon.core.bungee.listeners.LoginListener;
import fr.heavenmoon.core.bungee.listeners.PlayerHandshakeListener;
import fr.heavenmoon.core.bungee.listeners.ProxyPingEventListener;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;

public class Action {

    private MoonBungeeCore plugin;

    public Action(MoonBungeeCore plugin) {
        this.plugin = plugin;

        registerEvents();
        loadCommands();
    }

    private void register(Listener listener) {
        plugin.getProxy().getPluginManager().registerListener(plugin, listener);
    }

    public void registerEvents() {
        register(new ProxyPingEventListener());
        register(new LoginListener(plugin));
        register(new PlayerHandshakeListener());
        register(new CacheListener(plugin));
    }


    private void load(Command command) {
        plugin.getProxy().getPluginManager().registerCommand(plugin, command);
    }

    public void loadCommands() {
        load(new StaffCommand(plugin,"staff", "", ""));
    }

}
