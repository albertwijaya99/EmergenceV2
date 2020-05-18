package id.ac.umn.emergence;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{
    private String name;
    private String phone;
    private String address;
    private String gender;
    private String blood;
    private String email;

    public User(){}

    public User( String name, String phone, String address, String gender, String blood, String email) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.blood = blood;
        this.email = email;
    }


    protected User(Parcel in) {
        name = in.readString();
        phone = in.readString();
        address = in.readString();
        gender = in.readString();
        blood = in.readString();
        email = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getName(){return name;}
    public void setName(String Name){this.name = name;}

    public String getPhone(){return phone;}
    public void setPhone(String phone){this.phone = phone;}

    public String getAddress(){return address;}
    public void setAddress(String address){this.address = address;}

    public String getGender(){return gender;}
    public void setGender(String gender){this.gender = gender;}

    public String getBlood(){return blood;}
    public void setBlood(String blood){this.blood = blood;}

    public String getEmail(){return email;}
    public void setEmail(String email){this.email = email;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(address);
        dest.writeString(gender);
        dest.writeString(blood);
        dest.writeString(email);
    }
}
