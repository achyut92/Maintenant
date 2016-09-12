package com.self.achyut.maintenant.domain;

import java.util.Date;

/**
 * Created by boonkui on 06-09-2016.
 */
public class ElectricityCharge {

    private double previousReading,currentReading,unitsConsumed,total;
    private Date dateNoted;

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Date getDateNoted() {
        return dateNoted;
    }

    public void setDateNoted(Date dateNoted) {
        this.dateNoted = dateNoted;
    }

    public double getCurrentReading() {
        return currentReading;
    }

    public void setCurrentReading(double currentReading) {
        this.currentReading = currentReading;
    }

    public double getPreviousReading() {
        return previousReading;
    }

    public void setPreviousReading(double previousReading) {
        this.previousReading = previousReading;
    }

    public double getUnitsConsumed() {
        return unitsConsumed;
    }

    public void setUnitsConsumed(double unitsConsumed) {
        this.unitsConsumed = unitsConsumed;
    }

}
