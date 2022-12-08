package net.wyvest.jcr;

import cc.polyfrost.oneconfig.events.EventManager;
import cc.polyfrost.oneconfig.events.event.ChatReceiveEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.Notifications;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ReportRequest {
    public final String username;
    public final String message;
    public boolean shouldReport;
    public boolean awaitingConfirm = false;

    public ReportRequest(String username, String message) {
        this.username = username;
        this.message = message;
        shouldReport = true;
        Notifications.INSTANCE.send("JCR", "Blacklisted word detected, sent by " + username + ".\nMessage is: \"" + message + "\". They will automatically be chat reported in " + JCR.instance.config.confirmSeconds + " seconds. Click " + JCR.instance.config.confirmKeybind.getDisplay() + " to confirm now or " + JCR.instance.config.denyKeybind.getDisplay() +" to deny.", JCR.instance.config.confirmSeconds * 1000, () -> {
            shouldReport = false;
            reportUser(username);
            remove();
        });
        Multithreading.schedule(() -> {
            if (shouldReport) {
                shouldReport = false;
                reportUser(username);
            }
        }, JCR.instance.config.confirmSeconds, TimeUnit.SECONDS);
        EventManager.INSTANCE.register(this);
    }

    public void reportUser(String username) {
        UChat.say("/cr " + username);
        Notifications.INSTANCE.send("JCR", "Chat reported " + username + " for blacklisted message: \"" + message + "\".");
        awaitingConfirm = true;
        Multithreading.schedule(() -> {
            awaitingConfirm = false;
            remove();
        }, 7, TimeUnit.SECONDS);
    }

    @Subscribe
    private void onMessageReceived(ChatReceiveEvent event) {
        if (awaitingConfirm && Objects.equals(event.getFullyUnformattedMessage(), "Please type /report confirm to log your report for staff review.")) {
            UChat.say("/report confirm");
            event.isCancelled = true;
            remove();
        }
    }

    public void remove() {
        EventManager.INSTANCE.unregister(this);
        JCR.instance.reportRequests.remove(this);
    }
}
