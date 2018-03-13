package com.MhMohamed.PillUp.models;

/**
 * Created by Mohamed on 3/13/2018.
 */

public class Medication {

    String name;
    String dosage;
    String dosageTome;

    public void setName(String name) {
        this.name = name;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public void setDosageTime(String dosageTome) {
        this.dosageTome = dosageTome;
    }


    public String getName() {
        return name;
    }

    public String getDosage() {
        return dosage;
    }

    public String getDosageTime() {
        return dosageTome;
    }
}
