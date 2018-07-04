package net.mrporky.anisoc.members;

public class Member {
    private String firstName, lastName, uniqueID, email;
    public Member(String firstName, String lastName, String uniqueID, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.uniqueID = uniqueID;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUniqueID() {
        return uniqueID;
    }
}
