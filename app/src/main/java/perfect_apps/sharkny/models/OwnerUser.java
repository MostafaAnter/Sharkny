package perfect_apps.sharkny.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mostafa on 07/06/16.
 */
public class OwnerUser implements Parcelable {
    private String id;
    private String username;
    private String fullname;
    private String job;
    private String address;
    private String mobile;
    private String email;
    private String image;
    private String gender;
    private String nationality;
    private String country;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public OwnerUser(String id, String username, String fullname, String job, String address, String mobile, String email, String image,
                     String gender, String nationality, String country){
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.job = job;
        this.address = address;
        this.mobile = mobile;
        this.email = email;
        this.image = image;
        this.gender = gender;
        this.nationality = nationality;
        this.country = country;
    }

    protected OwnerUser(Parcel in) {
        id = in.readString();
        username = in.readString();
        fullname = in.readString();
        job = in.readString();
        address = in.readString();
        mobile = in.readString();
        email = in.readString();
        image = in.readString();
        gender = in.readString();
        nationality = in.readString();
        country = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(username);
        dest.writeString(fullname);
        dest.writeString(job);
        dest.writeString(address);
        dest.writeString(mobile);
        dest.writeString(email);
        dest.writeString(image);
        dest.writeString(gender);
        dest.writeString(nationality);
        dest.writeString(country);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<OwnerUser> CREATOR = new Parcelable.Creator<OwnerUser>() {
        @Override
        public OwnerUser createFromParcel(Parcel in) {
            return new OwnerUser(in);
        }

        @Override
        public OwnerUser[] newArray(int size) {
            return new OwnerUser[size];
        }
    };
}