package com.me.beem.beep_me.Database;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.me.beem.beep_me.R;

import java.sql.Blob;

@Entity(tableName = "TeachersDatabase")
public class EntityInformation {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "email")
    private String eadd;

    @ColumnInfo(name = "last_name")
    private String lastName;

    @ColumnInfo(name = "first_name")
    private String firstName;

    @ColumnInfo(name = "middle_name")
    private String middleName;

    @ColumnInfo(name = "gender")
    private String gender;

    @ColumnInfo(name = "subjects")
    private String subjects;

    @ColumnInfo(name = "consultation")
    private String consultationHours;

    @ColumnInfo(name = "advisory")
    private String advisory;

//    @ColumnInfo (name = "picture")
//    private int picture;

    public EntityInformation(String eadd, String lastName, String firstName,
                             String middleName, String gender, String subjects, String consultationHours,
                             String advisory /* int picture*/) {
        this.eadd = eadd;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.gender = gender;
        this.subjects = subjects;
        this.consultationHours = consultationHours;
        this.advisory = advisory;
       // this.picture = picture;
    }

    public String getEadd() {
        return eadd;
    }

    public void setEadd(String eadd) {
        this.eadd = eadd;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public String getConsultationHours() {
        return consultationHours;
    }

    public void setConsultationHours(String consultationHours) {
        this.consultationHours = consultationHours;
    }

    public String getAdvisory() {
        return advisory;
    }

    public void setAdvisory(String advisory) {
        this.advisory = advisory;
    }

//    public int getPicture() {
//        return picture;
//    }
//
//    public void setPicture(int picture) {
//        this.picture = picture;
//    }




    @NonNull
    @org.jetbrains.annotations.Contract(" -> new")
    public static EntityInformation[] populateData() {
        return new EntityInformation[]{
                new EntityInformation(
                        "abraham.cla0168f@calamba.sti.ph",
                        "Abraham",
                        "Karol Ann",
                        "Diego",
                        "Female",
                        "Earth and Life Science\n" + "General Chemistry 2",
                        "MONDAY\n" + "TUESDAY\n" + "THURSDAY\n" + "FRIDAY\n" + "\n" + "4:00 PM - 6:00 PM onwards",
                        "STEM 13\n" + "CULARTS 11"),
                new EntityInformation(
                        "kate.asiamae@gmail.com",
                        "Asia", "Kate Nicoleen",
                        "Buenafe", "Female",
                        "Physical Education 2\n" + "Physical Education 4",
                        "MONDAY 1:00 - 2:30 PM, 3:30 - 8:00 PM",
                        "NONE"),
                new EntityInformation("lorelabarundia@yahoo.com", "Barundia", "Lorela", "Alconaba", "Female",
                        "Oblications and Contracts\n" + "Contemporary Arts\n" + "Politics and Government\n" + "Business Ethics\n" + "Communication Engagement",
                        "TUESDAY 8:30 - 11:30 AM",
                        "NONE"),
                new EntityInformation(
                        "bete.cla0141f@calamba.sti.ph",
                        "Bete",
                        "John Astley",
                        "Ansay",
                        "Male",
                        "Mobile App Programming 1 (Android Studio)\n" + "Computer Programming 3\n" + "Computer Programming 6\n" + "Computer Hardware Fundamentals\n" + "Empowerment Technology",
                        "EVERY WEDNESDAY",
                        "NONE"),
                new EntityInformation(
                        "belarmino.cla0062f@calamba.sti.ph",
                        "Belarmino",
                        "Bea May",
                        "Mas",
                        "Female",
                        "Mobile App Programming 1 (Android Studio)\n" + "Mobile App Programming 2 (Xamarin)\n" + "Empowerment Technology\n" + "Advanced Programming (Java Enterprise Edition)\n" + "IT Special Project\n" + "Thesis 2",
                        "MONDAY 1:00 - 2:30 PM, 3:30 - 8:00 PM",
                        "ITMAW 11\n" + "ITMAW 12"),
                new EntityInformation(
                        "bitancor.cla0177f@calamba.sti.ph",
                        "Bitancor",
                        "Abraham",
                        "Aquino",
                        "Male",
                        "Entrepreneurship\n" +
                                "Applied Economics\n" +
                                "Fundamentals of ABM 1\n" +
                                "Work Immersion\n" +
                                "Inquiries, Investigations and Immersion",
                        "MONDAY AND THURSDAY 1:00 - 2:30 PM\n" +
                                "TUESDAY AND FRIDAY 8:30 AM - 10:00 AM, 2:30 PM - 4:00 PM",
                        "ABM 23"),
                new EntityInformation(
                        "calinagan.cla0150f@calamba.sti.ph",
                        "Calinagan",
                        "Florissa",
                        "Padullo",
                        "Female",
                        "Probability and Statistics (College and SHS)\n" +
                                "Basic Calculus\n" +
                                "Practical Research 1\n" +
                                "Inquiries, Investigations and Immersion",
                        "WEDNESDAY 1:00 - 4:30 PM",
                        "STEM 21\n" +
                                "CULARTS 21"),
                new EntityInformation(
                        "elag.cla0176f@calamba.sti.ph",
                        "Elag",
                        "Jesus Jr.",
                        "Capital",
                        "Male",
                        "Komunikasyon at Pananaliksik",
                        "WEDNESDAY 12:00 - 2:30\n" +
                                "MONDAY AND THURSDAY 2:30 - 5:30\n" +
                                "TUESDAY AND FRIDAY 2:30 - 5:00",
                        "ABM 22\n" +
                                "STEM 22"),
                new EntityInformation(
                        "PaulusArambuloEsguerra1995@yooh.ph",
                        "Esguerra",
                        "John Paulus",
                        "Arambulo",
                        "Male",
                        "Reading and Writing\n" +
                                "Inquiries, Investigations and Immersion\n" +
                                "Contemporary Philippine Arts from the Region\n" +
                                "Komunikasyon at Pananaliksik",
                        "MONDAY AND THURSDAY 8:00 - 10:00 AM",
                        "HOP 11\n" +
                                "ITMAW 13"),
                new EntityInformation(
                        "marvin.evangelista@calamba.sti.edu",
                        "Evangelista",
                        "Marvin",
                        "Capitan",
                        "Male",
                        "Inquiries, Investigations and Immersion\n" +
                                "Reading and Writing\n" +
                                "Practical Research 1",
                        "MONDAY AND THURSDAY 10:00-12:00, 2:30-4:00\n" +
                                "WEDNESDAY 7:00-10:00, 2:00-4:00\n" +
                                "TUESDAY AND FRIDAY 8:30-12:00",
                        "NONE"),
                new EntityInformation(
                        "katrina.geluz@calamba.sti.edu.ph",
                        "Geluz",
                        "Katrina",
                        "Abdul",
                        "Female",
                        "Principles of Marketing\n" +
                                "Entrepreneurship\n" +
                                "Applied Economics\n" +
                                "Work Immersion",
                        "FRIDAY 8:00 - 4:00 PM",
                        "STEM 11\n" +
                                "GAS 11"),
                new EntityInformation(
                        "logo.cla0188f@calamba.sti.ph",
                        "Logo",
                        "Maylene",
                        "Ercia",
                        "Female",
                        "Understanding Culture, Society and Politics\n" +
                                "Contemporary Philippine Arts from the Region",
                        "TUESDAY AND FRIDAY 3:00 - 5:00 PM",
                        "ITMAW 23\n" +
                                "TOP 11"),
                new EntityInformation(
                        "marcelo.cla0187f@calamba.sti.ph",
                        "Marcelo",
                        "Ramona",
                        "Fajardo",
                        "Female",
                        "Reading and Writing\n" +
                                "Inquiries, Investigations and Immersion\n" +
                                "Contemporary Philippine Arts from the Region",
                        "MONDAY AND THURSDAY 10:00 - 11:30\n" +
                                "TUESDAY 11:30 - 1:00, 3:30 - 4:00 PM\n" +
                                "WEDNESDAY 11:00 - 2:30\n" +
                                "FRIDAY 11:30 - 1:00, 2:30 - 4:00",
                        "HOP 21\n" +
                                "ABM 21"),
                new EntityInformation(
                        "mayuga.cla0319f@calamba.sti.edu.ph",
                        "Mayuga",
                        "Lovely Joy",
                        "Eguia",
                        "Female",
                        "General Biology 2\n" +
                                "Earth Science\n" +
                                "Earth and Life Science\n" +
                                "General Chemistry 2\n" +
                                "General Physics 2\n" +
                                "Work Immersion",
                        "MONDAY AND THURSDAY 1:00 - 2:30\n" +
                                "TUESDAY AND FRIDAY 4:00 - 5:00\n" +
                                "WEDNESDAY 3:00 - 5:00",
                        "STEM 24\n" +
                                "ITMAW 22"),
                new EntityInformation(
                        "seanceymarcus@gmail.com",
                        "Mercado",
                        "Ma. Isabel",
                        "Espinosa",
                        "Female",
                        "Practical Research 1\n" +
                                "Inquiries, Investigations and Immersion\n" +
                                "Reading and Writing\n" +
                                "Komunikasyon at Pananaliksik\n" +
                                "Work Immersion\n" +
                                "Contemporary Philippine Arts from the Region",
                        "MONDAY AND THURSDAY 2:30 - 5:00 PM\n" +
                                "TUESDAY AND FRIDAY 10:00 - 1:00 PM\n" +
                                "WEDNESDAY 10:00 - 2:00 PM",
                        "TOP 21\n" +
                                "STEM 23"),
                new EntityInformation(
                        "narsolis.cla0190A@calamba.sti.edu",
                        "Narsolis",
                        "Maricar",
                        "Layuso",
                        "Female",
                        "Understanding Culture, Society and Politics\n" +
                                "Contemporary Philippine Arts from the Region\n" +
                                "Business Ethics\n" +
                                "Art Appreciation",
                        "MONDAY AND THURSDAY 8:30 - 10:00 AM, 4:00 - 6:00 PM\n" +
                                "TUESDAY AND FRIDAY 8:30 - 11:30 , 4:00 - 6:00 PM",
                        "NONE"),
                new EntityInformation(
                        "karlangelo.ortega@calamba.sti.edu.ph",
                        "Ortega",
                        "Karl Angelo II",
                        "Malupig",
                        "Male",
                        "Basic Calculus\n" +
                                "Probability and Statistics\n" +
                                "General Chemistry 2\n" +
                                "General Physics 2\n" +
                                "Physics for Engineers",
                        "MONDAY 11:00 - 11:30 , 1:00 - 4:00\n" +
                                "TUESDAY 12:30 - 1:00\n" +
                                "WEDNESDAY 2:00 - 2:30\n" +
                                "THURSDAY 10:00 - 10:30, 1:00 - 4:00\n" +
                                "FRIDAY 12:30 - 1:00\n" +
                                "SATURDAY 9:00 - 10:00, 2:00 - 2:30",
                        "ITMAW 21\n" +
                                "GAS 21"),
                new EntityInformation(
                        "puyod.cla0179f@calamba.sti.ph",
                        "Puyod",
                        "Salve",
                        "Galang",
                        "Female",
                        "Work Immersion\n" +
                                "Reading and Writing\n" +
                                "Inquiries, Investigations and Immersion\n" +
                                "Contemporary Philippine Arts from the Region\n" +
                                "Communication Arts\n" +
                                "Practical Research 1",
                        "MONDAY AND THURSDAY 7:00 - 10:00\n" +
                                "MONDAY ONLY 2:30 - 4:00\n" +
                                "TUESDAY AND FRIDAY 1:00 - 2:30 ",
                        "ABM 11\n" +
                                "STEM 12")
        };

    }
}
