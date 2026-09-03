package model;

public class Admin extends User {

    public Admin(
            int id,
            String name) {

        super(
                id,
                name
        );

    }


    public void manageUsers() {

        System.out.println(
                "Managing users"
        );

    }

}