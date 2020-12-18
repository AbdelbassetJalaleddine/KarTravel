package jalaleddine.abdelbasset.kartravel;

public class ContactInformation {
    private String Name,Gender,LastSeen;
    private boolean Corona;
    public ContactInformation(String name, String gender,String lastSeen) {
        Name = name;
        Gender = gender;
        LastSeen = lastSeen;

    }

    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ContactInformation(String name, String gender, long timestamp) {
        Name = name;
        Gender = gender;
        this.timestamp = timestamp;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
    public String getLastSeen() {
        return LastSeen;
    }

    public void setLastSeen(String lastSeen) {
        LastSeen = lastSeen;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

public String toString(){
    return "Contact Info: "+ " Name: " + getName() + " Gender: " + getGender()  + " Time Stamp: " + getTimestamp();
    }

}
