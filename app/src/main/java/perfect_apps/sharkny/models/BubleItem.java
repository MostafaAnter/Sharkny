package perfect_apps.sharkny.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mostafa on 07/06/16.
 */
public class BubleItem implements Parcelable {
    private String id;
    private String title;
    private String description;
    private String start_date;
    private String end_date;
    private String investment_value;
    private String investment_percentage;
    private String guarantees;
    private String send_to_mobile;
    private String project_type;
    private String project_field;
    private String country;
    private String image;
    private String likes_count;
    private String comments_count;
    private String general_type;
    private String is_verified;
    private String created_by;

    private OwnerUser ownerUser;

    public OwnerUser getOwnerUser() {
        return ownerUser;
    }

    public void setOwnerUser(OwnerUser ownerUser) {
        this.ownerUser = ownerUser;
    }

    public static Creator<BubleItem> getCREATOR() {
        return CREATOR;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getInvestment_value() {
        return investment_value;
    }

    public void setInvestment_value(String investment_value) {
        this.investment_value = investment_value;
    }

    public String getInvestment_percentage() {
        return investment_percentage;
    }

    public void setInvestment_percentage(String investment_percentage) {
        this.investment_percentage = investment_percentage;
    }

    public String getGuarantees() {
        return guarantees;
    }

    public void setGuarantees(String guarantees) {
        this.guarantees = guarantees;
    }

    public String getSend_to_mobile() {
        return send_to_mobile;
    }

    public void setSend_to_mobile(String send_to_mobile) {
        this.send_to_mobile = send_to_mobile;
    }

    public String getProject_type() {
        return project_type;
    }

    public void setProject_type(String project_type) {
        this.project_type = project_type;
    }

    public String getProject_field() {
        return project_field;
    }

    public void setProject_field(String project_field) {
        this.project_field = project_field;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(String likes_count) {
        this.likes_count = likes_count;
    }

    public String getComments_count() {
        return comments_count;
    }

    public void setComments_count(String comments_count) {
        this.comments_count = comments_count;
    }

    public String getGeneral_type() {
        return general_type;
    }

    public void setGeneral_type(String general_type) {
        this.general_type = general_type;
    }

    public String getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(String is_verified) {
        this.is_verified = is_verified;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public BubleItem(String id, String title, String description, String start_date, String end_date, String investment_value, String investment_percentage,
                     String guarantees, String send_to_mobile, String project_type, String project_field, String country, String image, String likes_count,
                     String comments_count, String general_type,
                     String is_verified, String created_by, OwnerUser ownerUser){
        this.id = id;
        this.title = title;
        this.description = description;
        this.start_date = start_date;
        this.end_date = end_date;
        this.investment_value = investment_value;
        this.investment_percentage = investment_percentage;
        this.guarantees = guarantees;
        this.send_to_mobile = send_to_mobile;
        this.project_type = project_type;
        this.project_field = project_field;
        this.country = country;
        this.image = image;
        this.likes_count = likes_count;
        this.comments_count = comments_count;
        this.general_type = general_type;
        this.is_verified = is_verified;
        this.created_by = created_by;

        this.ownerUser = ownerUser;


    }

    protected BubleItem(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        start_date = in.readString();
        end_date = in.readString();
        investment_value = in.readString();
        investment_percentage = in.readString();
        guarantees = in.readString();
        send_to_mobile = in.readString();
        project_type = in.readString();
        project_field = in.readString();
        country = in.readString();
        image = in.readString();
        likes_count = in.readString();
        comments_count = in.readString();
        general_type = in.readString();
        is_verified = in.readString();
        created_by = in.readString();
        ownerUser = (OwnerUser) in.readValue(OwnerUser.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(start_date);
        dest.writeString(end_date);
        dest.writeString(investment_value);
        dest.writeString(investment_percentage);
        dest.writeString(guarantees);
        dest.writeString(send_to_mobile);
        dest.writeString(project_type);
        dest.writeString(project_field);
        dest.writeString(country);
        dest.writeString(image);
        dest.writeString(likes_count);
        dest.writeString(comments_count);
        dest.writeString(general_type);
        dest.writeString(is_verified);
        dest.writeString(created_by);
        dest.writeValue(ownerUser);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BubleItem> CREATOR = new Parcelable.Creator<BubleItem>() {
        @Override
        public BubleItem createFromParcel(Parcel in) {
            return new BubleItem(in);
        }

        @Override
        public BubleItem[] newArray(int size) {
            return new BubleItem[size];
        }
    };
}