package se.deepcloud.cloudkingdoms.utilities.formatting;

import org.bukkit.Location;
import se.deepcloud.cloudkingdoms.utilities.formatting.impl.*;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Formatters {

    public static final IFormatter<Boolean> BOOLEAN_FORMATTER = BooleanFormatter.getInstance();
    public static final IFormatter<String> CAPITALIZED_FORMATTER = CapitalizedFormatter.getInstance();
    public static final IFormatter<String> COLOR_FORMATTER = ColorFormatter.getInstance();
    public static final IFormatter<Stream<String>> COMMA_FORMATTER = CommaFormatter.getInstance();
    public static final IFormatter<Date> DATE_FORMATTER = DateFormatter.getInstance();
    public static final IFormatter<Number> FANCY_NUMBER_FORMATTER = FancyNumberFormatter.getInstance();
    public static final IFormatter<Location> LOCATION_FORMATTER = LocationFormatter.getInstance();
    public static final IFormatter<Number> NUMBER_FORMATTER = NumberFormatter.getInstance();
    public static final IFormatter<String> STRIP_COLOR_FORMATTER = StripColorFormatter.getInstance();
    public static final IFormatter<Duration> TIME_FORMATTER = TimeFormatter.getInstance();

    private Formatters() {

    }

    public static <T> List<String> formatList(List<T> list, IFormatter<T> formatter) {
        return list.stream().map(formatter::format).collect(Collectors.toList());
    }
}
