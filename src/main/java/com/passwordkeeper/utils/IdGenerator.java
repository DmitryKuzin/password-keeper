package com.passwordkeeper.utils;

import java.util.Date;

public class IdGenerator {

    public static String generateId() {
        return String.valueOf(new Date().getTime());
    }
}
