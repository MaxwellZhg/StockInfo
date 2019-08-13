package com.dycm.modulea.model;

/**
 * Created by Maxwell.
 * E-mail: maxwell_smith@163.com
 * Date: 2019/8/13
 * Desc:
 */
public class PersonalInfo {

    public PersonalInfo(String name, String gender, String hobbies) {
        this.name = name;
        this.gender = gender;
        this.hobbies = hobbies;
    }

    private String name;

    private String gender;

    private String hobbies;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }
}