package paruteto_plugin_02.swapsinventoryplugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CommandMain extends CommandBase {

    static String commandName = "swpinv";

    final String config = "conf";
    final String config_interval = "interval";

    public CommandMain(SwapsInventoryPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        boolean result = false;
        if (args.length > 0) {
            switch (args[0]) {
                case "enable":
                    result = PluginMain.instance.Enable(sender, args);
                    break;
                case "disable":
                    result = PluginMain.instance.Disable(sender, args);
                    break;
                case config:
                    if (args.length > 1) {
                        switch (args[1]) {
                            case config_interval:
                                result = PluginMain.instance.Interval(sender, args);
                                break;
                            default:
                                sender.sendMessage(DecolationConst.RED + "[/" + commandName + " " + config + "]の使用法をを確認してください");
                                break;
                        }
                    }
                    else {
                        sender.sendMessage(DecolationConst.RED + "[/" + commandName + " " + config + "]の使用法をを確認してください");
                    }
                    break;
                default:
                    sender.sendMessage(DecolationConst.RED + "[/" + commandName + "]の使用法をを確認してください");
                    break;
            }
        }
        else {
            sender.sendMessage(DecolationConst.RED + "[/" + commandName + "]の使用法をを確認してください");
        }
        return result;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<String>();
        if (args.length == 1) {
            completions.add(config);
            completions.add("enable");
            completions.add("disable");

        }
        if (args.length == 2){
            if(args[0].equals(config)){
                completions.add(config_interval);
            }
        }
        if (args.length == 3) {
            if (args[0].equals(config)){
                if (args[1].equals(config_interval)){
                    completions.add("<整数値>");
                }
            }
        }
        return completions;
    }

    @Override
    CommandBase getInstance() {
        return this;
    }

    @Override
    public String getCommandName() {
        return commandName;
    }

    public static boolean isNumber2(String value) {
        boolean result = false;
        if (value != null) {
            Pattern pattern = Pattern.compile("^[0-9]+$");
            result = pattern.matcher(value).matches();
        }
        return result;
    }
}
