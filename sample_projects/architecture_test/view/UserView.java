package view;

import model.User;

public class UserView {


    public void display(
            User user) {

        if (user == null) {

            System.out.println(
                    "User not found"
            );

            return;

        }


        System.out.println(
                "===================="
        );

        System.out.println(
                "User Details"
        );

        System.out.println(
                "===================="
        );

        System.out.println(
                "ID   : "
                        + user.getId()
        );

        System.out.println(
                "Name : "
                        + user.getName()
        );

    }

}