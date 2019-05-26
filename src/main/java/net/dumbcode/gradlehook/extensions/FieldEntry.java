package net.dumbcode.gradlehook.extensions;

/**
 * Used to hold information about the field entry that is stored
 */
public class FieldEntry {
    private final String name;
    private final String value;

    public FieldEntry(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
