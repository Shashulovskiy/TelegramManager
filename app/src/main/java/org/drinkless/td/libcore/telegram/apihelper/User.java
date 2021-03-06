package org.drinkless.td.libcore.telegram.apihelper;

import org.drinkless.td.libcore.telegram.TdApi;

public class User {
    TdApi.User user;
    User(TdApi.User user) {
        this.user = user;
    }

    public String getFirstName() {
        return user.firstName;
    }

    public String getLastName() {
        return user.lastName;
    }

    public String getUserName() {
        return user.username;
    }

    public String getShortName() {
        if (getUserName().isEmpty()) return getFirstName();
        return getUserName();
    }
}
