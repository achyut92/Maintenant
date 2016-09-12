package com.self.achyut.maintenant.domain;

/**
 * Created by boonkui on 05-09-2016.
 */
public class Landlord {

    private String name,mobile;
    private int no_of_tenants;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNo_of_tenants() {
        return no_of_tenants;
    }

    public void setNo_of_tenants(int no_of_tenants) {
        this.no_of_tenants = no_of_tenants;
    }
}
