package ua.kiev.prog;

public enum UserRole {
    ADMIN, USER, PREACT;

    @Override
    public String toString() {
        return "ROLE_" + name();
    }
}
