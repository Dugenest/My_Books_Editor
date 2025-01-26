package com.afci.data;
import java.util.Date;

public class UserProfile {
    private Long id_profile;
    private String lastname;
    private String firstname;
    private Date birthday;
    private String phone;
    private String email;
    private String address;

    // Constructeur
    public UserProfile() {
    }
    
    public UserProfile(Long id_profile, String lastname, String firstname, Date birthday, 
                  String phone, String email, String address) {
        this.id_profile = id_profile;
        this.lastname = lastname;
        this.firstname = firstname;
        this.birthday = birthday;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    // Getters et Setters
    public Long getId_profile() {
        return id_profile;
    }

    public void setId_profile(Long id_profile) {
        this.id_profile = id_profile;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Méthodes
    public String getProfileDetails(Long profileId) {
        // Retourne les détails du profil
        return null;
    }

    public void updateProfile(String profileData) {
        // Mise à jour du profil
    }

    public void deleteProfile(Long profileId) {
        // Suppression du profil
    }

    //methode toString
    @Override   
    public String toString() {
            return "UserProfile{" +
                "id_profile=" + id_profile +
                ", lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", birthday=" + birthday +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
