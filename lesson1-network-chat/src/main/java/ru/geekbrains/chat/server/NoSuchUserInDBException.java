package ru.geekbrains.chat.server;

public class NoSuchUserInDBException extends Exception {

    public NoSuchUserInDBException(String msg) {
        super(msg);
    }
}
