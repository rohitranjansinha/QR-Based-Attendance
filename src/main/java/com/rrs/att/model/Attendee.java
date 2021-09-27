package com.rrs.att.model;

public class Attendee {
    private String UID;
    private String timestamp;
    private String name;
    private String gender;

    public Attendee(){}

    public Attendee(String UID, String timestamp, String name, String gender){
        this.UID = UID;
        this.timestamp = timestamp;
        this.name = name;
        this.gender = gender;
    }
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public void setName(String name) { this.name = name; }

    public String getName() { return name; }

    public void setGender(String gender) { this.gender = gender; }

    public String getGender() { return gender; }

    @Override
    public String toString() {
        return "Attendee{" +
                "UID='" + UID + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
