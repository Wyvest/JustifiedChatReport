package net.wyvest.jcr.command;

import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.commands.annotations.*;
import net.wyvest.jcr.JCR;

@Command(JCR.ID)
public class JCRCommand {

    @Main
    private void main() {
        JCR.instance.config.openGui();
    }

    @SubCommand(description = "Print the values of the config.")
    private static void print() {
        UChat.chat(ChatColor.BLUE + "Whole Triggers: ");
        UChat.chat(ChatColor.BLUE + "Starts: " + ChatColor.LIGHT_PURPLE + JCR.instance.config.startsWith);
        UChat.chat(ChatColor.BLUE + "Ends: " + ChatColor.LIGHT_PURPLE + JCR.instance.config.endsWith);
        UChat.chat(ChatColor.BLUE + "Contains: " + ChatColor.LIGHT_PURPLE + JCR.instance.config.contains);
        UChat.chat(ChatColor.BLUE + "Equals: " + ChatColor.LIGHT_PURPLE + JCR.instance.config.equals);
        UChat.chat(ChatColor.BLUE + "Per Word Triggers: ");
        UChat.chat(ChatColor.BLUE + "Starts: " + ChatColor.LIGHT_PURPLE + JCR.instance.config.startsWithWords);
        UChat.chat(ChatColor.BLUE + "Ends: " + ChatColor.LIGHT_PURPLE + JCR.instance.config.endsWithWords);
        UChat.chat(ChatColor.BLUE + "Contains: " + ChatColor.LIGHT_PURPLE + JCR.instance.config.containsWords);
        UChat.chat(ChatColor.BLUE + "Equals: " + ChatColor.LIGHT_PURPLE + JCR.instance.config.equalsWords);
    }

    @SubCommand(description = "Add a word to the list")
    private static void add(@Description(autoCompletesTo = {"whole", "words"}) String type, @Description(autoCompletesTo = {"starts", "ends", "contains", "equals"}) String mode, @Greedy String word) {
        if (!word.trim().isEmpty()) {
            switch (type) {
                case "whole":
                    handleAddWhole(mode, word);
                    break;
                case "words":
                    handleAddWords(mode, word);
                    break;
                default:
                    UChat.chat(ChatColor.RED + "Invalid type! Available types are:" + ChatColor.BLUE + "[whole, words]");
            }
        }
    }

    @SubCommand(description = "Remove a word from the list")
    private static void remove(@Description(autoCompletesTo = {"whole", "words"}) String type, @Description(autoCompletesTo = {"starts", "ends", "contains", "equals"}) String mode, @Greedy String word) {
        if (!word.trim().isEmpty()) {
            switch (type) {
                case "whole":
                    handleRemoveWhole(mode, word);
                    break;
                case "words":
                    handleRemoveWords(mode, word);
                    break;
                default:
                    UChat.chat(ChatColor.RED + "Invalid type! Available types are:" + ChatColor.BLUE + "[whole, words]");
            }
        }
    }

    @SubCommand(description = "Clear the list")
    private static void clear() {
        UChat.chat(ChatColor.GREEN + "Clearing:");
        print();
        JCR.instance.config.startsWith.clear();
        JCR.instance.config.endsWith.clear();
        JCR.instance.config.contains.clear();
        JCR.instance.config.equals.clear();
        JCR.instance.config.startsWithWords.clear();
        JCR.instance.config.endsWithWords.clear();
        JCR.instance.config.containsWords.clear();
        JCR.instance.config.equalsWords.clear();
        JCR.instance.config.save();
        UChat.chat(ChatColor.GREEN + "Cleared the list!");
    }

    private static void handleRemoveWhole(String mode, String word) {
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
                UChat.chat(ChatColor.RED + "Invalid type! Available types are:" + ChatColor.BLUE + "[starts, ends, contains, equals]");
                return;
        }
        if (succeeded) {
            JCR.instance.config.save();
            UChat.chat(ChatColor.BLUE + "The phrase " + ChatColor.LIGHT_PURPLE + word + ChatColor.BLUE + " was removed!");
        } else {
            UChat.chat(ChatColor.RED + "The phrase " + ChatColor.LIGHT_PURPLE + word + ChatColor.RED + " does not exist!");
        }
    }

    private static void handleRemoveWords(String mode, String word) {
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
                UChat.chat(ChatColor.RED + "Invalid type! Available types are:" + ChatColor.BLUE + "[starts, ends, contains, equals]");
                return;
        }
        if (succeeded) {
            JCR.instance.config.save();
            UChat.chat(ChatColor.BLUE + "The phrase " + ChatColor.LIGHT_PURPLE + word + ChatColor.BLUE + " was removed!");
        } else {
            UChat.chat(ChatColor.RED + "The phrase " + ChatColor.LIGHT_PURPLE + word + ChatColor.RED + " does not exist!");
        }
    }

    private static void handleAddWhole(String mode, String word) {
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
                UChat.chat(ChatColor.RED + "Invalid type! Available types are:" + ChatColor.BLUE + "[starts, ends, contains, equals]");
                return;
        }
        if (succeeded) {
            JCR.instance.config.save();
            UChat.chat(ChatColor.BLUE + "The phrase " + ChatColor.LIGHT_PURPLE + word + ChatColor.BLUE + " was added!");
        } else {
            UChat.chat(ChatColor.RED + "The phrase " + ChatColor.LIGHT_PURPLE + word + ChatColor.RED + " already exists!");
        }
    }

    private static void handleAddWords(String mode, String word) {
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
                UChat.chat(ChatColor.RED + "Invalid type! Available types are:" + ChatColor.BLUE + "[starts, ends, contains, equals]");
                return;
        }
        if (succeeded) {
            JCR.instance.config.save();
            UChat.chat(ChatColor.BLUE + "The phrase " + ChatColor.LIGHT_PURPLE + word + ChatColor.BLUE + " was added!");
        } else {
            UChat.chat(ChatColor.RED + "The phrase " + ChatColor.LIGHT_PURPLE + word + ChatColor.RED + " already exists!");
        }
    }
}