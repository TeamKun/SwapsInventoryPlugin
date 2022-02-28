package paruteto_plugin_02.swapsinventoryplugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class SwapsInventoryPlugin extends JavaPlugin {

    @Override
    public void onEnable() {

        // Plugin startup logic
        getLogger().info("Start SwapsInventoryPlugin");

        PluginMain.instance.SetPluginInstance(this);

        new BukkitRunnable() {
            @Override
            public void run() {
                PluginMain.instance.update();
            }
        }.runTaskTimer(this, 0, 1);

        CommandMain commandMain = new CommandMain(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Finished SwapsInventoryPlugin");
    }
}
