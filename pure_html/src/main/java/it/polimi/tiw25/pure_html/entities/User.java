package it.polimi.tiw25.pure_html.entities;

/**
 * Class to represent the user.
 */
public class User {
    private int id;
    private String nickname;
    private String name;
    private String surname;
    private String password;

    public User(int id, String nickname, String name, String surname, String password) {
        this.id = id;
        this.nickname = nickname;
        this.name = name;
        this.surname = surname;
        this.password = password;
    }

    public User(String nickname) {
        this.nickname = nickname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
