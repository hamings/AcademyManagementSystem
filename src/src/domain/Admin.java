package src.domain;

import lombok.Getter;

@Getter
public class Admin {
    private static Admin admin;
    private String id;
    private String password;

    private Admin() {
        this.id = "admin";
        this.password = "admin123";
    }

    public static Admin getInstance() {
        if (admin == null) {
            admin = new Admin();
        }
        return admin;
    }
}