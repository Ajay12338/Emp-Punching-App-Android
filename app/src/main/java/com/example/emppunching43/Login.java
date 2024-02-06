package com.example.emppunching43;

public class Login {
    private int id;
    private String userName;
    private String  password;

    public Login(int id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }
    //toString Method
    @Override
    public String toString() {
        return
                "id=" + id +
                        ", UserName='" + userName + '\'' +
                        ", password='" + password + '\''
                ;
    }

    //Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
