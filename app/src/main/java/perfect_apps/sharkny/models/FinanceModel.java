package perfect_apps.sharkny.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mostafa on 08/06/16.
 */
public class FinanceModel implements Parcelable {
    private String id;
    private String title;
    private String description;
    private String investment_value;
    private String investment_percentage;
    private String is_verified;
    private String inance_type;
    private String investment_Field;
    private String country;
    private String image;
    private String general_type;
    private String likes_count;
    private String comments_count;
    private String created_by;

    private OwnerUser ownerUser;

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

    public String getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(String is_verified) {
        this.is_verified = is_verified;
    }

    public String getInance_type() {
        return inance_type;
    }

    public void setInance_type(String inance_type) {
        this.inance_type = inance_type;
    }

    public String getInvestment_Field() {
        return investment_Field;
    }

    public void setInvestment_Field(String investment_Field) {
        this.investment_Field = investment_Field;
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

    public String getGeneral_type() {
        return general_type;
    }

    public void setGeneral_type(String general_type) {
        this.general_type = general_type;
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

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public OwnerUser getOwnerUser() {
        return ownerUser;
    }

    public void setOwnerUser(OwnerUser ownerUser) {
        this.ownerUser = ownerUser;
    }

    public FinanceModel(String id, String title, String description, String investment_value, String investment_percentage, String is_verified, String inance_type, String investment_Field, String country, String image, String general_type, String likes_count, String comments_count, String created_by, OwnerUser ownerUser) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.investment_value = investment_value;
        this.investment_percentage = investment_percentage;
        this.is_verified = is_verified;
        this.inance_type = inance_type;
        this.investment_Field = investment_Field;
        this.country = country;
        this.image = image;
        this.general_type = general_type;
        this.likes_count = likes_count;
        this.comments_count = comments_count;
        this.created_by = created_by;
        this.ownerUser = ownerUser;
    }

    protected FinanceModel(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        investment_value = in.readString();
        investment_percentage = in.readString();
        is_verified = in.readString();
        inance_type = in.readString();
        investment_Field = in.readString();
        country = in.readString();
        image = in.readString();
        general_type = in.readString();
        likes_count = in.readString();
        comments_count = in.readString();
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
        dest.writeString(investment_value);
        dest.writeString(investment_percentage);
        dest.writeString(is_verified);
        dest.writeString(inance_type);
        dest.writeString(investment_Field);
        dest.writeString(country);
        dest.writeString(image);
        dest.writeString(general_type);
        dest.writeString(likes_count);
        dest.writeString(comments_count);
        dest.writeString(created_by);
        dest.writeValue(ownerUser);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FinanceModel> CREATOR = new Parcelable.Creator<FinanceModel>() {
        @Override
        public FinanceModel createFromParcel(Parcel in) {
            return new FinanceModel(in);
        }

        @Override
        public FinanceModel[] newArray(int size) {
            return new FinanceModel[size];
        }
    };
}