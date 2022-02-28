package paruteto_plugin_02.swapsinventoryplugin;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

public abstract class CommandBase implements CommandExecutor, TabCompleter {
    private static SwapsInventoryPlugin PLUGIN = null;// 直接扱わず、メソッド ***Plugin() で介す

    public CommandBase(SwapsInventoryPlugin plugin) {
        if (this.getPlugin() == null)
            this.setPlugin(plugin);
        if (this.getInstance() == null)
            throw new NullPointerException("Instance is null");
        if (this.getCommandName() == null)
            throw new NullPointerException("CommandName is null");

        this.register();
    }

    /**
     * プラグインのインスタンスをゲット
     *
     */
    final SwapsInventoryPlugin getPlugin() {
        return CommandBase.PLUGIN;
    }

    /**
     * プラグインのインスタンスをセットする
     *
     * @param plugin
     */
    final void setPlugin(SwapsInventoryPlugin plugin) {
        if (plugin == null)
            throw new IllegalArgumentException("Argument \"plugin\" is null");
        CommandBase.PLUGIN = plugin;
    }

    /**
     * CommandExecutor と TabCompleter の登録
     *
     */
    public void register() {
        PluginCommand c = this.getPlugin().getCommand(this.getCommandName());
        if (c != null) {
            c.setExecutor(this.getInstance());
            if (this.getInstance() instanceof TabCompleter) {
                c.setTabCompleter((TabCompleter) this.getInstance());
                this.getPlugin().getLogger().info("[" + getCommandName() + "]登録しました");
            }
        }
    }

    /**
     * 自身のインスタンスを返す
     *
     */
    abstract CommandBase getInstance();

    /**
     * コマンド名をゲットする
     *
     * @return コマンド名を返す
     */
    public abstract String getCommandName();
}
