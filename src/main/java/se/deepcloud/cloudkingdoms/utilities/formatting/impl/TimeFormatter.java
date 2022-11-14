package se.deepcloud.cloudkingdoms.utilities.formatting.impl;

import se.deepcloud.cloudkingdoms.utilities.formatting.IFormatter;

import java.time.Duration;

public class TimeFormatter implements IFormatter<Duration> {

    private static final TimeFormatter INSTANCE = new TimeFormatter();

    public static TimeFormatter getInstance() {
        return INSTANCE;
    }

    private TimeFormatter() {

    }

    @Override
    public String format(Duration value) {
        StringBuilder timeBuilder = new StringBuilder();

        {
            long days = value.toDays();
            if (days > 0) {
                formatTimeSection(timeBuilder, days, days == 1 ? "dag" : "dagar");
                value = value.minusDays(days);
            }
        }

        {
            long hours = value.toHours();
            if (hours > 0) {
                formatTimeSection(timeBuilder, hours, hours == 1 ? "timme" : "timmar");
                value = value.minusHours(hours);
            }
        }

        {
            long minutes = value.toMinutes();
            if (minutes > 0) {
                formatTimeSection(timeBuilder, minutes, minutes == 1 ? "minut" : "minuter");
                value = value.minusMinutes(minutes);
            }
        }

        {
            long seconds = value.getSeconds();
            if (seconds > 0)
                formatTimeSection(timeBuilder, seconds, seconds == 1 ? "sekund" : "sekunder");
        }

        if (timeBuilder.length() == 0) {
            timeBuilder.append("1 ").append("sekund").append(", ");
        }

        return timeBuilder.substring(0, timeBuilder.length() - 2);
    }

    private static void formatTimeSection(StringBuilder stringBuilder, long value, String timeFormatMessage) {
        stringBuilder.append(value)
                .append(" ")
                .append(timeFormatMessage)
                .append(", ");
    }

}