package org.hbrs.project.wram.util;

import com.vaadin.flow.component.Component;

public class Constant {
    public static String CURRENT_USER = "current_User";

    public static class Pages {
        public static final String SHOW_CARS = "show";
        public static final String ENTER_CAR = "enter";

        public static final String LOGIN_VIEW = "login";
        public static final String MAIN_VIEW = "";
    }

    public static class Roles {
        public static final String ADMIN = "admin";
        public static final String CURRENT_USER = "user";

    }

    public static class Errors {
        public static final String NOUSERFOUND = "nouser";
        public static final String SQLERROR = "sql";
        public static final String DATABASE = "database";
    }

}
