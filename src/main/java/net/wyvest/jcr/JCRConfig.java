package net.wyvest.jcr;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Info;
import cc.polyfrost.oneconfig.config.annotations.KeyBind;
import cc.polyfrost.oneconfig.config.annotations.Number;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import cc.polyfrost.oneconfig.config.data.InfoType;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.libs.universal.UKeyboard;
import cc.polyfrost.oneconfig.utils.Notifications;

import java.util.Iterator;
import java.util.LinkedHashSet;

public class JCRConfig extends Config {

    @Info(
            text = "Use the /jcr command to add or remove words from the list", type = InfoType.INFO)
    private boolean ignored;

    @Number(
            name = "Seconds to confirm",
            min = 0,
            max = 60,
            description = "The amount of seconds it takes to automatically chat report."
    )
    public int confirmSeconds = 5;

    @KeyBind(
            name = "Confirm Keybind",
            description = "The keybind to confirm a chat report."
    )
    public OneKeyBind confirmKeybind = new OneKeyBind(UKeyboard.KEY_LMENU, UKeyboard.KEY_Y);

    @KeyBind(
            name = "Deny Keybind",
            description = "The keybind to deny a chat report."
    )
    public OneKeyBind denyKeybind = new OneKeyBind(UKeyboard.KEY_LMENU, UKeyboard.KEY_N);

    public LinkedHashSet<String> startsWith = new LinkedHashSet<>();
    public LinkedHashSet<String> endsWith = new LinkedHashSet<>();
    public LinkedHashSet<String> contains = new LinkedHashSet<>();
    public LinkedHashSet<String> equals = new LinkedHashSet<>();
    public LinkedHashSet<String> startsWithWords = new LinkedHashSet<>();
    public LinkedHashSet<String> endsWithWords = new LinkedHashSet<>();
    public LinkedHashSet<String> containsWords = new LinkedHashSet<>();
    public LinkedHashSet<String> equalsWords = new LinkedHashSet<>();

    public JCRConfig() {
        super(new Mod(JCR.NAME, ModType.HYPIXEL), "jcr.json");
        initialize();
        registerKeyBind(confirmKeybind, () -> {
            for (Iterator<ReportRequest> it = JCR.instance.reportRequests.descendingIterator(); it.hasNext(); ) {
                ReportRequest reportRequest = it.next();
                if (reportRequest.shouldReport) {
                    reportRequest.shouldReport = false;
                    reportRequest.reportUser(reportRequest.username);
                    reportRequest.remove();
                    break;
                }
            }
        });
        registerKeyBind(denyKeybind, () -> {
            for (Iterator<ReportRequest> it = JCR.instance.reportRequests.descendingIterator(); it.hasNext(); ) {
                ReportRequest reportRequest = it.next();
                if (reportRequest.shouldReport) {
                    reportRequest.shouldReport = false;
                    reportRequest.remove();
                    Notifications.INSTANCE.send("JCR", "Denied report request for " + reportRequest.username);
                    break;
                }
            }
        });
    }
}
