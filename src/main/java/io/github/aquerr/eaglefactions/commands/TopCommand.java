package io.github.aquerr.eaglefactions.commands;

import io.github.aquerr.eaglefactions.entities.Faction;
import io.github.aquerr.eaglefactions.logic.FactionLogic;
import io.github.aquerr.eaglefactions.services.PowerService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.*;

public class TopCommand implements CommandExecutor
{
    @Override
    public CommandResult execute(CommandSource source, CommandContext context) throws CommandException
    {
        List<Faction> factionsList = new ArrayList<>(FactionLogic.getFactions());
        List<Text> helpList = new ArrayList<>();
        int index = 0;

        factionsList.sort((o1, o2) -> o2.Power.compareTo(o1.Power));

        //This should show only top 10 factions on the server.

        for(Faction faction: factionsList)
        {
            if(faction.Name.equalsIgnoreCase("safezone") || faction.Name.equalsIgnoreCase("warzone")) continue;
            if(index == 11) break;

            index++;
            String tag = "";
            if(faction.Tag != null && !faction.Tag.equals("")) tag = "[" + faction.Tag + "] ";

            Text factionHelp = Text.builder()
                    .append(Text.builder()
                            .append(Text.of(TextColors.AQUA, index + ". " + tag + faction.Name + " (" + faction.Power + "/" + PowerService.getFactionMaxPower(faction) + ")"))
                            .build())
                    .build();

            helpList.add(factionHelp);
        }

        PaginationService paginationService = Sponge.getServiceManager().provide(PaginationService.class).get();
        PaginationList.Builder paginationBuilder = paginationService.builder().title(Text.of(TextColors.GREEN, "Factions List")).padding(Text.of("-")).contents(helpList);
        paginationBuilder.sendTo(source);

        return CommandResult.success();
    }
}