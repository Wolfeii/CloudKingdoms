package se.deepcloud.cloudkingdoms.utilities.formatting.impl;

import se.deepcloud.cloudkingdoms.utilities.formatting.Formatters;
import se.deepcloud.cloudkingdoms.utilities.formatting.IFormatter;

public class FancyNumberFormatter implements IFormatter<Number> {

    private static final FancyNumberFormatter INSTANCE = new FancyNumberFormatter();

    private static final double Q = 1000000000000000D;
    private static final double T = 1000000000000D;
    private static final double B = 1000000000D;
    private static final double M = 1000000D;
    private static final double K = 1000D;

    public static FancyNumberFormatter getInstance() {
        return INSTANCE;
    }

    private FancyNumberFormatter() {

    }

    @Override
    public String format(Number value) {
        double doubleValue = value.doubleValue();
        if (doubleValue >= Q)
            return Formatters.NUMBER_FORMATTER.format(doubleValue / Q) + "Biljarder";
        else if (doubleValue >= T)
            return Formatters.NUMBER_FORMATTER.format(doubleValue / T) + "Biljoner";
        else if (doubleValue >= B)
            return Formatters.NUMBER_FORMATTER.format(doubleValue / B) + "Miljarder";
        else if (doubleValue >= M)
            return Formatters.NUMBER_FORMATTER.format(doubleValue / M) + "Miljoner";
        else if (doubleValue >= K)
            return Formatters.NUMBER_FORMATTER.format(doubleValue / K) + "Tusen";
        else
            return Formatters.NUMBER_FORMATTER.format(value);
    }

}