package org.oyakushev.hospitalclient.dto;

public enum PersonalRole {
    Viewer(0, "Viewer"),
    Nurse(1, "Nurse"),
    Doctor(2, "Doctor"),
    Admin(3, "Admin");

    private final int index;
    private final String title;

    PersonalRole(int index, String title) {
        this.index = index;
        this.title = title;
    }

    public static PersonalRole fromIndex(int index) {
        return switch (index) {
            case 0 -> Viewer;
            case 1 -> Nurse;
            case 2 -> Doctor;
            case 3 -> Admin;
            default -> throw new IllegalArgumentException("Can't find role for index = " + index);
        };
    }

    public int getIndex() {
        return index;
    }

    public String getTitle() {
        return title;
    }
}
