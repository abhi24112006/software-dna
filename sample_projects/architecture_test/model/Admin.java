package model;

public class Admin extends User {

    private String role;

    public Admin(int id, String name, String role) {
        super(id, name);
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}