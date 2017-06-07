package com.tistory.tobyspring.domain;

/**
 * 사용자 정보를 저장하는 자바빈 규약을 따르는 오브젝트
 */
public class User {

    private String id;
    private String name;
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}