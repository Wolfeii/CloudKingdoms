package se.deepcloud.cloudkingdoms.utilities.formatting.impl;

import se.deepcloud.cloudkingdoms.utilities.formatting.IFormatter;

public class BooleanFormatter implements IFormatter<Boolean> {

    private static final BooleanFormatter INSTANCE = new BooleanFormatter();

    public static BooleanFormatter getInstance() {
        return INSTANCE;
    }

    private BooleanFormatter() {

    }

    @Override
    public String format(Boolean value) {
        return (value ? "Ja" : "Nej");
    }

}