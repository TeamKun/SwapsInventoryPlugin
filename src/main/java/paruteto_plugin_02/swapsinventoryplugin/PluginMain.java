package paruteto_plugin_02.swapsinventoryplugin;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

public class PluginMain {

    public static final PluginMain instance = new PluginMain();

    SwapsInventoryPlugin plugin = null;

    boolean bEnable = false;

    long lLastRunTime = 0;

    int interval = 60;

    int countdown = 1000*10;

    ArrayList<Player> listPlayers = new ArrayList<Player>();

    public void SetPluginInstance(SwapsInventoryPlugin obj){
        plugin = obj;
    }

    public PluginMain(){
        Lottery();
        lLastRunTime = System.currentTimeMillis();
    }

    public void update() {
        if (plugin == null) return;

        if (!bEnable) return;

        long lElapsedTime = System.currentTimeMillis() - lLastRunTime;
        long lTimer = (interval*1000)-lElapsedTime;

        for(int i = 0; i < listPlayers.size(); i++){

            Player player = listPlayers.get(i);

            Player player_p = listPlayers.get((i+1+listPlayers.size())%listPlayers.size());
            Player player_f = listPlayers.get((i-1+listPlayers.size())%listPlayers.size());

            if (!player.isOnline()) continue;

            if (lTimer < countdown) {
                player.sendTitle("",
                        "残り" +
                                DecolationConst.GOLD + Long.valueOf((lTimer/1000)+1).toString() + DecolationConst.RESET + "秒で" +
                                DecolationConst.GREEN + player_p.getName() + DecolationConst.RESET + "の" +
                                "インベントリになります", 0, 40, 20);
            }
            else{
                player.sendTitle("", "", 0, 40, 20);
            }
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                    "次は"+
                    DecolationConst.GREEN+player_f.getName()+DecolationConst.RESET+"が"+
                    "あなたのインベントリになります"));
        }

        if (lTimer < 0){
            Swap();
            Lottery();
            lLastRunTime = System.currentTimeMillis();
        }
    }

    public void Lottery(){
        listPlayers.clear();
        for (Player player : Bukkit.getOnlinePlayers()) {
            listPlayers.add(player);
        }
        Collections.shuffle(listPlayers);
    }

    public void Swap(){
        if (listPlayers.size() < 1) return;

        ItemStack[] inv = listPlayers.get(0).getInventory().getContents();

        for(int i = 0; i < listPlayers.size()-1; i++){
            listPlayers.get(i).getInventory().setContents(listPlayers.get((i+1)%listPlayers.size()).getInventory().getContents());
            listPlayers.get(i).updateInventory();
        }

        listPlayers.get(listPlayers.size() - 1).getInventory().setContents(inv);
        listPlayers.get(listPlayers.size() - 1).updateInventory();
    }

    public boolean Enable(CommandSender sender, String[] args){
        if (args.length != 1){
            sender.sendMessage("[/swpinv enable]以降の引数は不要です。");
            return false;
        }
        bEnable = true;
        Lottery();
        lLastRunTime = System.currentTimeMillis();
        sender.sendMessage("SwapsInventoryPluginを有効にしました。");
        return true;
    }

    public boolean Disable(CommandSender sender, String[] args){
        if (args.length != 1){
            sender.sendMessage("[/swpinv disable]以降の引数は不要です。");
            return false;
        }
        bEnable = false;
        sender.sendMessage("SwapsInventoryPluginを無効にしました。");
        return true;
    }

    public boolean Interval(CommandSender sender, String[] args){
        if (args.length == 2) {
            sender.sendMessage("現在のインベントリ入れ替え間隔は" + Long.valueOf(interval).toString() + "秒です。");
            return true;
        }
        if (args.length == 3) {
            if (isNumber2(args[2])) {
                interval = Integer.parseInt(args[2]);
                Lottery();
                lLastRunTime = System.currentTimeMillis();
                sender.sendMessage("インベントリ入れ替え間隔を" + Long.valueOf(interval).toString() + "秒に変更しました。");
                return true;
            }
        }
        sender.sendMessage(DecolationConst.RED + "[/swpinv conf interval]の使用法をを確認してください");
        return false;
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
