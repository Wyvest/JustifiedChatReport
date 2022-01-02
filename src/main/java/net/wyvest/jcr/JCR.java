package net.wyvest.jcr;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gg.essential.api.EssentialAPI;
import gg.essential.api.utils.Multithreading;
import kotlin.Unit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.wyvest.jcr.command.JCRCommand;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mod(modid = JCR.ID, name = JCR.NAME, version = JCR.VER)
public class JCR {
    public static final String NAME = "@NAME@", VER = "@VER@", ID = "@ID@";
    @Mod.Instance
    public static JCR instance;
    public File modDir = new File(new File(Minecraft.getMinecraft().mcDataDir, "config"), NAME);
    public Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public File jsonFile = new File(modDir, "config.json");
    public Config config;
    private final Pattern pattern = Pattern.compile("^(?:From |To )?(?:\\[(?:MVP|VIP|PIG|YOUTUBE|MOD|HELPER|ADMIN|OWNER|@|MOJANG|SLOTH|EVENTS|MCP)\\+*] |)([a-zA-Z0-9_]+): (.*)$");
    public boolean shouldReport = false;
    public boolean awaitingConfirm = false;

    @Mod.EventHandler
    protected void onFMLPreInitialization(FMLPreInitializationEvent event) {
        if (!modDir.exists()) modDir.mkdirs();
    }

    @Mod.EventHandler
    protected void onInitialization(FMLInitializationEvent event) {
        new JCRCommand().register();
        if (jsonFile.exists()) {
            try {
                config = gson.fromJson(FileUtils.readFileToString(jsonFile, StandardCharsets.UTF_8), Config.class);
            } catch (IOException e) {
                e.printStackTrace();
                config = new Config();
                try {
                    FileUtils.write(jsonFile, gson.toJson(config), StandardCharsets.UTF_8);
                } catch (Exception ignored) {

                }
            }
        } else {
            config = new Config();
            try {
                jsonFile.createNewFile();
            } catch (Exception ignored) {

            }
        }
        try {
            FileUtils.write(jsonFile, gson.toJson(config), StandardCharsets.UTF_8);
        } catch (Exception ignored) {

        }
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (event.type != 2) {
            if (config.enabled) {
                Multithreading.runAsync(() -> {
                    String message = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());
                    Matcher matcher = pattern.matcher(message);
                    if (matcher.matches()) {
                        String contents = matcher.group(2);
                        for (String trigger : config.equals) {
                            if (contents.equalsIgnoreCase(trigger)) {
                                handleTriggered(matcher.group(1), contents);
                                return;
                            }
                        }
                        for (String trigger : config.contains) {
                            if (contents.toLowerCase(Locale.ENGLISH).contains(trigger)) {
                                handleTriggered(matcher.group(1), contents);
                                return;
                            }
                        }
                        for (String trigger : config.startsWith) {
                            if (contents.toLowerCase(Locale.ENGLISH).startsWith(trigger)) {
                                handleTriggered(matcher.group(1), contents);
                                return;
                            }
                        }
                        for (String trigger : config.endsWith) {
                            if (contents.toLowerCase(Locale.ENGLISH).endsWith(trigger)) {
                                handleTriggered(matcher.group(1), contents);
                                return;
                            }
                        }
                        String[] words = StringUtils.split(contents);
                        for (String word : words) {
                            for (String trigger : config.equalsWords) {
                                if (word.equalsIgnoreCase(trigger)) {
                                    handleTriggered(matcher.group(1), contents);
                                    return;
                                }
                            }
                            for (String trigger : config.containsWords) {
                                if (word.toLowerCase(Locale.ENGLISH).contains(trigger)) {
                                    handleTriggered(matcher.group(1), contents);
                                    return;
                                }
                            }
                            for (String trigger : config.startsWithWords) {
                                if (word.toLowerCase(Locale.ENGLISH).startsWith(trigger)) {
                                    handleTriggered(matcher.group(1), contents);
                                    return;
                                }
                            }
                            for (String trigger : config.endsWithWords) {
                                if (word.toLowerCase(Locale.ENGLISH).endsWith(trigger)) {
                                    handleTriggered(matcher.group(1), contents);
                                    return;
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    private void handleTriggered(String username, String message) {
        shouldReport = true;
        EssentialAPI.getNotifications().push("JCR", "Blacklisted word detected, sent by " + username + ".\nMessage is: \"" + message + "\". They will automatically be chat reported in " + config.confirmSeconds + " seconds. Click Alt + Y to confirm now or Alt + N to deny.", config.confirmSeconds, () -> {
            shouldReport = false;
            reportUser(username);
            return Unit.INSTANCE;
        });
        Multithreading.schedule(() -> {
            if (shouldReport) {
                shouldReport = false;
                reportUser(username);
            }
        }, config.confirmSeconds, TimeUnit.SECONDS);
        Multithreading.runAsync(() -> {
            while (shouldReport) {
                if (Keyboard.getEventKeyState() && GuiScreen.isAltKeyDown()) {
                    if (Keyboard.isKeyDown(Keyboard.KEY_Y)) {
                        shouldReport = false;
                        reportUser(username);
                    } else if (Keyboard.isKeyDown(Keyboard.KEY_N)) {
                        shouldReport = false;
                    }
                }
            }
        });
    }

    private void reportUser(String username) {
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/cr " + username);
        awaitingConfirm = true;
        Multithreading.schedule(() -> awaitingConfirm = false, 7, TimeUnit.SECONDS);
    }

    @SubscribeEvent
    public void onMessageReceived(ClientChatReceivedEvent event) {
        if (awaitingConfirm && EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText()).equals("Please type /report confirm to log your report for staff review.")) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/report confirm");
            event.setCanceled(true);
        }
    }

    public void writeData() {
        Multithreading.runAsync(() -> {
            try {
                FileUtils.write(jsonFile, gson.toJson(config), StandardCharsets.UTF_8);
            } catch (Exception ignored) {

            }
        });
    }
}
