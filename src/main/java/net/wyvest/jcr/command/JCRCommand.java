package net.wyvest.jcr.command;

import gg.essential.api.EssentialAPI;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.Greedy;
import gg.essential.api.commands.SubCommand;
import gg.essential.universal.ChatColor;
import net.wyvest.jcr.JCR;

@SuppressWarnings("unused")
public class JCRCommand extends Command {
    public JCRCommand() {
        super(JCR.ID, true);
    }

    @SubCommand(value = "toggle", description = "Toggle the mod.")
    public void toggle() {
        JCR.instance.config.enabled = !JCR.instance.config.enabled;
        JCR.instance.writeData();
        EssentialAPI.getMinecraftUtil().sendMessage("", ChatColor.BLUE + "JCR was toggled " + (JCR.instance.config.enabled ? ChatColor.GREEN + "on" : ChatColor.RED + "off") + ".");
    }

    @SubCommand(value = "seconds", description = "Toggle the amount of seconds it takes to chat report automatically.")
    public void setSeconds(int seconds) {
        JCR.instance.config.confirmSeconds = seconds;
        JCR.instance.writeData();
        EssentialAPI.getMinecraftUtil().sendMessage("", ChatColor.BLUE + "Time was set to " + ChatColor.GOLD + seconds + ChatColor.BLUE + " seconds!");
    }

    @SubCommand(value = "add", description = "Add a word to the list")
    public void addWord(String type, String mode, @Greedy String word) {
        if (!word.trim().isEmpty()) {
            switch (type) {
                case "whole":
                    handleAddWhole(mode, word);
                    break;
                case "words":
                    handleAddWords(mode, word);
                    break;
                default:
                    EssentialAPI.getMinecraftUtil().sendMessage("", ChatColor.RED + "Invalid type! Available types are:" + ChatColor.BLUE + "[whole, words]");
            }
        }
    }

    @SubCommand(value = "remove", description = "Remove a word from the list")
    public void removeWord(String type, String mode, @Greedy String word) {
        if (!word.trim().isEmpty()) {
            switch (type) {
                case "whole":
                    handleRemoveWhole(mode, word);
                    break;
                case "words":
                    handleRemoveWords(mode, word);
                    break;
                default:
                    EssentialAPI.getMinecraftUtil().sendMessage("", ChatColor.RED + "Invalid type! Available types are:" + ChatColor.BLUE + "[whole, words]");
            }
        }
    }

    private void handleRemoveWhole(String mode, String word) {
        boolean succeeded;
        switch (mode) {
            case "starts":
                succeeded = JCR.instance.config.startsWith.remove(word);
                break;
            case "ends":
                succeeded = JCR.instance.config.endsWith.remove(word);
                break;
            case "contains":
                succeeded = JCR.instance.config.contains.remove(word);
                break;
            case "equals":
                succeeded = JCR.instance.config.equals.remove(word);
                break;
            default:
                EssentialAPI.getMinecraftUtil().sendMessage("", ChatColor.RED + "Invalid type! Available types are:" + ChatColor.BLUE + "[starts, ends, contains, equals]");
                return;
        }
        if (succeeded) {
            JCR.instance.writeData();
            EssentialAPI.getMinecraftUtil().sendMessage("", ChatColor.BLUE + "The phrase " + ChatColor.LIGHT_PURPLE + word + ChatColor.BLUE + " was removed!");
        } else {
            EssentialAPI.getMinecraftUtil().sendMessage("", ChatColor.RED + "The phrase " + ChatColor.LIGHT_PURPLE + word + ChatColor.RED + " does not exist!");
        }
    }

    private void handleRemoveWords(String mode, String word) {
        boolean succeeded;
        switch (mode) {
            case "starts":
                succeeded = JCR.instance.config.startsWithWords.remove(word);
                break;
            case "ends":
                succeeded = JCR.instance.config.endsWithWords.remove(word);
                break;
            case "contains":
                succeeded = JCR.instance.config.containsWords.remove(word);
                break;
            case "equals":
                succeeded = JCR.instance.config.equalsWords.remove(word);
                break;
            default:
                EssentialAPI.getMinecraftUtil().sendMessage("", ChatColor.RED + "Invalid type! Available types are:" + ChatColor.BLUE + "[starts, ends, contains, equals]");
                return;
        }
        if (succeeded) {
            JCR.instance.writeData();
            EssentialAPI.getMinecraftUtil().sendMessage("", ChatColor.BLUE + "The phrase " + ChatColor.LIGHT_PURPLE + word + ChatColor.BLUE + " was removed!");
        } else {
            EssentialAPI.getMinecraftUtil().sendMessage("", ChatColor.RED + "The phrase " + ChatColor.LIGHT_PURPLE + word + ChatColor.RED + " does not exist!");
        }
    }

    private void handleAddWhole(String mode, String word) {
        boolean succeeded;
        switch (mode) {
            case "starts":
                succeeded = JCR.instance.config.startsWith.add(word);
                break;
            case "ends":
                succeeded = JCR.instance.config.endsWithWords.add(word);
                break;
            case "contains":
                succeeded = JCR.instance.config.contains.add(word);
                break;
            case "equals":
                succeeded = JCR.instance.config.equals.add(word);
                break;
            default:
                EssentialAPI.getMinecraftUtil().sendMessage("", ChatColor.RED + "Invalid type! Available types are:" + ChatColor.BLUE + "[starts, ends, contains, equals]");
                return;
        }
        if (succeeded) {
            JCR.instance.writeData();
            EssentialAPI.getMinecraftUtil().sendMessage("", ChatColor.BLUE + "The phrase " + ChatColor.LIGHT_PURPLE + word + ChatColor.BLUE + " was added!");
        } else {
            EssentialAPI.getMinecraftUtil().sendMessage("", ChatColor.RED + "The phrase " + ChatColor.LIGHT_PURPLE + word + ChatColor.RED + " already exists!");
        }
    }

    private void handleAddWords(String mode, String word) {
        boolean succeeded;
        switch (mode) {
            case "starts":
                succeeded = JCR.instance.config.startsWithWords.add(word);
                break;
            case "ends":
                succeeded = JCR.instance.config.endsWithWords.add(word);
                break;
            case "contains":
                succeeded = JCR.instance.config.containsWords.add(word);
                break;
            case "equals":
                succeeded = JCR.instance.config.equalsWords.add(word);
                break;
            default:
                EssentialAPI.getMinecraftUtil().sendMessage("", ChatColor.RED + "Invalid type! Available types are:" + ChatColor.BLUE + "[starts, ends, contains, equals]");
                return;
        }
        if (succeeded) {
            JCR.instance.writeData();
            EssentialAPI.getMinecraftUtil().sendMessage("", ChatColor.BLUE + "The phrase " + ChatColor.LIGHT_PURPLE + word + ChatColor.BLUE + " was added!");
        } else {
            EssentialAPI.getMinecraftUtil().sendMessage("", ChatColor.RED + "The phrase " + ChatColor.LIGHT_PURPLE + word + ChatColor.RED + " already exists!");
        }
    }

    @SubCommand(value = "print", description = "Print the values of the config.")
    public void print() {
        EssentialAPI.getMinecraftUtil().sendMessage("", ChatColor.BLUE + "Enabled: " + (JCR.instance.config.enabled ? ChatColor.GREEN + "True" : ChatColor.RED + "False"));
        EssentialAPI.getMinecraftUtil().sendMessage("", ChatColor.BLUE + "Seconds to Confirm: " + ChatColor.GOLD + JCR.instance.config.confirmSeconds);
        EssentialAPI.getMinecraftUtil().sendMessage("", ChatColor.BLUE + "Whole Triggers: ");
        EssentialAPI.getMinecraftUtil().sendMessage("", ChatColor.BLUE + "Starts: " + ChatColor.LIGHT_PURPLE + JCR.instance.config.startsWith);
        EssentialAPI.getMinecraftUtil().sendMessage("", ChatColor.BLUE + "Ends: " + ChatColor.LIGHT_PURPLE + JCR.instance.config.endsWith);
        EssentialAPI.getMinecraftUtil().sendMessage("", ChatColor.BLUE + "Contains: " + ChatColor.LIGHT_PURPLE + JCR.instance.config.contains);
        EssentialAPI.getMinecraftUtil().sendMessage("", ChatColor.BLUE + "Equals: " + ChatColor.LIGHT_PURPLE + JCR.instance.config.equals);
        EssentialAPI.getMinecraftUtil().sendMessage("", ChatColor.BLUE + "Per Word Triggers: ");
        EssentialAPI.getMinecraftUtil().sendMessage("", ChatColor.BLUE + "Starts: " + ChatColor.LIGHT_PURPLE + JCR.instance.config.startsWithWords);
        EssentialAPI.getMinecraftUtil().sendMessage("", ChatColor.BLUE + "Ends: " + ChatColor.LIGHT_PURPLE + JCR.instance.config.endsWithWords);
        EssentialAPI.getMinecraftUtil().sendMessage("", ChatColor.BLUE + "Contains: " + ChatColor.LIGHT_PURPLE + JCR.instance.config.containsWords);
        EssentialAPI.getMinecraftUtil().sendMessage("", ChatColor.BLUE + "Equals: " + ChatColor.LIGHT_PURPLE + JCR.instance.config.equalsWords);
    }
}