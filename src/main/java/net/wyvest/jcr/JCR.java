package net.wyvest.jcr;

import cc.polyfrost.oneconfig.events.EventManager;
import cc.polyfrost.oneconfig.events.event.ChatReceiveEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.commands.CommandManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.wyvest.jcr.command.JCRCommand;
import org.apache.commons.lang3.StringUtils;

import java.util.Deque;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mod(modid = JCR.ID, name = JCR.NAME, version = JCR.VER)
public class JCR {
    public static final String NAME = "@NAME@", VER = "@VER@", ID = "@ID@";
    @Mod.Instance
    public static JCR instance;
    private static final Pattern pattern = Pattern.compile("^(?:From |To )?(?:\\[(?:MVP|VIP|PIG|YOUTUBE|MOD|HELPER|ADMIN|OWNER|@|MOJANG|SLOTH|EVENTS|MCP)\\+*] |)([a-zA-Z0-9_]+): (.*)$");
    public JCRConfig config;
    public Deque<ReportRequest> reportRequests = new ConcurrentLinkedDeque<>();

    @Mod.EventHandler
    protected void onInitialization(FMLInitializationEvent event) {
        CommandManager.register(new JCRCommand());
        config = new JCRConfig();
        EventManager.INSTANCE.register(this);
    }

    @Subscribe
    private void onChat(ChatReceiveEvent event) {
        if (config.enabled) {
            Multithreading.runAsync(() -> {
                String message = event.getFullyUnformattedMessage();
                Matcher matcher = pattern.matcher(message);
                if (matcher.matches()) {
                    String contents = matcher.group(2);
                    for (String trigger : config.equals) {
                        if (contents.equalsIgnoreCase(trigger)) {
                            reportRequests.add(new ReportRequest(matcher.group(1), contents));
                            return;
                        }
                    }
                    for (String trigger : config.contains) {
                        if (contents.toLowerCase(Locale.ENGLISH).contains(trigger)) {
                            reportRequests.add(new ReportRequest(matcher.group(1), contents));
                            return;
                        }
                    }
                    for (String trigger : config.startsWith) {
                        if (contents.toLowerCase(Locale.ENGLISH).startsWith(trigger)) {
                            reportRequests.add(new ReportRequest(matcher.group(1), contents));
                            return;
                        }
                    }
                    for (String trigger : config.endsWith) {
                        if (contents.toLowerCase(Locale.ENGLISH).endsWith(trigger)) {
                            reportRequests.add(new ReportRequest(matcher.group(1), contents));
                            return;
                        }
                    }
                    String[] words = StringUtils.split(contents);
                    for (String word : words) {
                        for (String trigger : config.equalsWords) {
                            if (word.equalsIgnoreCase(trigger)) {
                                reportRequests.add(new ReportRequest(matcher.group(1), contents));
                                return;
                            }
                        }
                        for (String trigger : config.containsWords) {
                            if (word.toLowerCase(Locale.ENGLISH).contains(trigger)) {
                                reportRequests.add(new ReportRequest(matcher.group(1), contents));
                                return;
                            }
                        }
                        for (String trigger : config.startsWithWords) {
                            if (word.toLowerCase(Locale.ENGLISH).startsWith(trigger)) {
                                reportRequests.add(new ReportRequest(matcher.group(1), contents));
                                return;
                            }
                        }
                        for (String trigger : config.endsWithWords) {
                            if (word.toLowerCase(Locale.ENGLISH).endsWith(trigger)) {
                                reportRequests.add(new ReportRequest(matcher.group(1), contents));
                                return;
                            }
                        }
                    }
                }
            });
        }
    }
}
