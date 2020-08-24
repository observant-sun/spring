package ru.geekbrains.chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class ClientHandler {
    private Server server;
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private String nick;
    String login = null;

    List<String> blackList;

    public String getNick() {
        return nick;
    }

    public ClientHandler(Server server, Socket socket, DatabaseHandler dbHandler) {
        try {
            this.socket = socket;
            this.server = server;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            new Thread(() -> {
                try {
                    while (true) {
                        String str = in.readUTF();
                        if (str.startsWith("/auth")) { // /auth login72 pass72
                            String[] tokens = str.split(" ");
                            String newNick = dbHandler.getNickByLoginAndPass(tokens[1], tokens[2]);
                            if (newNick != null) {
                                if (!server.isNickBusy(newNick)) {
                                    sendMsg("/authok");
                                    nick = newNick;
                                    try {
                                        this.blackList = dbHandler.fetchBlacklistForNick(nick);
                                    } catch (NoSuchUserInDBException e) {
                                        e.printStackTrace();
                                    }
                                    sendBlacklist();
                                    server.subscribe(this);
                                    dbHandler.writeToLog(LoggedEvent.AUTH_OK, tokens[1]);
                                    login = tokens[1];
                                    break;
                                } else {
                                    dbHandler.writeToLog(LoggedEvent.AUTH_FAIL, tokens[1]);
                                    sendMsg("Учетная запись уже используется");
                                }
                            } else {
                                dbHandler.writeToLog(LoggedEvent.AUTH_FAIL, tokens[1]);
                                sendMsg("Неверный логин/пароль");
                            }
                        }
                    }
                    while (true) {
                        String str = in.readUTF();
                        if (str.startsWith("/")) {
                            if (str.equals("/end")) {
                                out.writeUTF("/serverclosed");
                                break;
                            }
                            if (str.startsWith("/w ")) { // /w nick3 lsdfhldf sdkfjhsdf wkerhwr
                                String[] tokens = str.split(" ", 3);
                                String m = str.substring(tokens[1].length() + 4);
                                server.sendPersonalMsg(this, tokens[1], tokens[2]);
                            }
                            if (str.startsWith("/blacklist ")) { // /blacklist nick3
                                String[] tokens = str.split(" ");
                                if (tokens.length > 1 && !tokens[1].isEmpty()) {
                                    String nickToBL = tokens[1];
                                    try {
                                        if (nickToBL.equals(this.nick)) {
                                            sendMsg("Нельзя добавить самого себя в черный список");
                                        } else {
                                            if (dbHandler.toggleNickInClientsBlacklistInDatabase(this, nickToBL)) {
                                                blackList.add(nickToBL);
                                                sendMsg("Вы добавили пользователя \'" + tokens[1] + "\' в черный список");
                                                sendBlacklist();
                                            } else {
                                                blackList.remove(nickToBL);
                                                sendMsg("Вы удалили пользователя \'" + tokens[1] + "\' из черного списка");
                                                sendBlacklist();
                                            }
                                        }
                                    } catch (NoSuchUserInDBException e) {
                                        sendMsg("Такого пользователя не существует");
                                    }
                                }
                            }
                            if (str.startsWith("/register")) {
                                String[] tokens = str.split(" ");
                                if (tokens.length > 3) {
                                    try {
                                        dbHandler.addUser(tokens[1], tokens[2], tokens[3]);
                                        sendMsg("Создан пользователь с ником \'" + tokens[3]  + "\', логином \'" + tokens[1] + "\' и паролем \'" + tokens[2] + '\'');
                                    } catch (BadCredentialException e) {
                                        sendMsg(e.getMessage());
                                    }
                                }
                            }
                        } else {
                            server.broadcastMsg(this, nick + ": " + str);
                        }
                        System.out.println("Client: " + str);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    server.unsubscribe(this);
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (login != null)
                        dbHandler.writeToLog(LoggedEvent.AUTH_EXIT, login);
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkBlackList(String nick) {
        return blackList.contains(nick);
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendBlacklist() {
        StringBuilder sb = new StringBuilder();
        sb.append("/blacklisted ");
        for (String o : blackList) {
            sb.append(o).append(" ");
        }
        sendMsg(sb.toString());
    }
}
