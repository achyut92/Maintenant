package com.self.achyut.maintenant.domain;

import java.util.Date;

/**
 * Created by boonkui on 06-09-2016.
 */
public class Tenant {

    private String name,mobile;
    private double rent,maintenance,advance;
    private double perUnitCharge;
    private Date dateOccupied;

    public Date getDateOccupied() {
        return dateOccupied;
    }

    public void setDateOccupied(Date dateOccupied) {
        this.dateOccupied = dateOccupied;
    }

    public double getAdvance() {
        return advance;
    }

    public void setAdvance(double advance) {
        this.advance = advance;
    }

    public double getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(double maintenance) {
        this.maintenance = maintenance;
    }

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

    public double getPerUnitCharge() {
        return perUnitCharge;
    }

    public void setPerUnitCharge(double perUnitCharge) {
        this.perUnitCharge = perUnitCharge;
    }

    public double getRent() {
        return rent;
    }

    public void setRent(double rent) {
        this.rent = rent;
    }

    @Override
    public String toString() {
        return getName();
    }
}
