package kurosio.kurosioats.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ATSTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(
            CommandSender sender,
            Command command,
            String alias,
            String[] args
    ) {

        List<String> list = new ArrayList<>();


        // /ats <subcommand>
        if (args.length == 1) {

            list.add("help");
            list.add("main");
            list.add("rule");
            list.add("check");
            list.add("reload");
            list.add("warp");


            if (sender.hasPermission("ats.admin")) {

                list.add("sync");
                list.add("lottery");
                list.add("status");
                list.add("delete");

            }

        }


        // /ats delete <type>
        if (args.length == 2
                && args[0].equalsIgnoreCase("delete")
                && sender.hasPermission("ats.admin")) {

            list.add("result");
            list.add("reception");

        }



        // /ats warp set <設定名>
        if (args.length == 2
                && args[0].equalsIgnoreCase("warp")
                && sender.hasPermission("ats.admin")) {

            list.add("create");
            list.add("set");

        }


        Collections.sort(list);


        List<String> result = new ArrayList<>();

        for (String sub : list) {

            if (sub.toLowerCase()
                    .startsWith(
                            args[args.length - 1]
                                    .toLowerCase()
                    )) {

                result.add(sub);

            }

        }


        return result;

    }

}