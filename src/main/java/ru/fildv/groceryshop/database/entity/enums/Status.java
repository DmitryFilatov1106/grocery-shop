package ru.fildv.groceryshop.database.entity.enums;

public enum Status {
    A_CLASS("Класс А"),
    B_CLASS("Класс B"),
    C_CLASS("Класс C");
    private final String name;

    Status(String name) {
        this.name = name;
    }

    public static String[] getAllNames() {
        var names = new String[Status.values().length];
        int i = 0;
        for (Status s : Status.values()) names[i++] = s.getName();
        return names;
    }

    public static String[] getAllNamesFilter() {
        var names = new String[Status.values().length + 1];
        int i = 1;
        names[0] = "-";
        for (Status s : Status.values()) names[i++] = s.getName();
        return names;
    }

    public static Status getStatus(String status) {
        for (Status s : Status.values())
            if (s.getName().equals(status)) return s;
        return null;
    }

    public String getName() {
        return this.name;
    }

}
