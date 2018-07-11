package com.example.q.NameCardMaker.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.InputStream;
import java.io.Serializable;

public class ModelContacts implements Serializable {

    private String name, mobile_num, home_num, email;

    public ModelContacts(String name, String mobile_number, String home_num, String email) {

        this.name = name;
        this.mobile_num = mobile_number;
        this.home_num = home_num;
        this.email = email;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getMobile_num(){ return mobile_num; }
    public void setMobile_num(String mobile_num){ this.mobile_num = mobile_num; }

    public String getHome_num(){ return home_num; }
    public void setHome_num(String mobile_num){ this.home_num = home_num; }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
