package org.game.bot.models;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.User;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class Room {

    @Getter
    private final ArrayList<User> users;

    @Getter
    private ConcurrentHashMap<User, Association> associations;

    @Getter
    private User leader;

    @Getter
    private boolean inGame;

    @Getter
    private String keyword;

    @Getter
    private int currentLetterIndex;

    public void setKeyword(String keyword) {
        this.keyword = keyword;
        currentLetterIndex = 0;
    }

    public Room() {
        this.users = new ArrayList<>();
        this.inGame = false;
    }

    public void startGame() {
        inGame = true;
        leader = users.get(new Random().nextInt(users.size()));
        associations = new ConcurrentHashMap<>();
    }

    public void endGame() {
        inGame = false;
        leader = null;
        associations = null;
        keyword = null;
    }

    public boolean addUser(User user) {
        return users.add(user);
    }

    public boolean removeUser(User user) {
        return users.remove(user);
    }


    public static final ConcurrentHashMap<String, Room> rooms = new ConcurrentHashMap<>();

    public static String createRoom() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        String id = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        rooms.put(id, new Room());
        return id;
    }

    public static Optional<Map.Entry<String, Room>> findUser(User user) {
        return rooms.entrySet().stream().filter(item -> item.getValue().getUsers().contains(user)).findFirst();
    }

    public String getCurrentPrefix() {
        return keyword.substring(0, currentLetterIndex + 1);
    }

    public String openNextLetter() {
        currentLetterIndex++;
        return getCurrentPrefix();
    }
}
