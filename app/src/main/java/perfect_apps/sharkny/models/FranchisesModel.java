package perfect_apps.sharkny.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mostafa on 08/06/16.
 */
public class FranchisesModel implements Parcelable {

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

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getFranchise_type() {
        return franchise_type;
    }

    public void setFranchise_type(String franchise_type) {
        this.franchise_type = franchise_type;
    }

    public String getFranchise_field() {
        return franchise_field;
    }

    public void setFranchise_field(String franchise_field) {
        this.franchise_field = franchise_field;
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

    private String id;
    private String title;
    private String description ;
    private String terms;
    private String franchise_type;
    private String franchise_field;
    private String country;
    private String image;
    private String general_type;
    private String likes_count;
    private String comments_count;
    private String is_verified;
    private String created_by;

    public FranchisesModel(String id, String title, String description, String terms, String franchise_type, String franchise_field, String country, String image, String general_type, String likes_count, String comments_count, String is_verified, String created_by) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.terms = terms;
        this.franchise_type = franchise_type;
        this.franchise_field = franchise_field;
        this.country = country;
        this.image = image;
        this.general_type = general_type;
        this.likes_count = likes_count;
        this.comments_count = comments_count;
        this.is_verified = is_verified;
        this.created_by = created_by;
    }

    protected FranchisesModel(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        terms = in.readString();
        franchise_type = in.readString();
        franchise_field = in.readString();
        country = in.readString();
        image = in.readString();
        general_type = in.readString();
        likes_count = in.readString();
        comments_count = in.readString();
        is_verified = in.readString();
        created_by = in.readString();
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
        dest.writeString(terms);
        dest.writeString(franchise_type);
        dest.writeString(franchise_field);
        dest.writeString(country);
        dest.writeString(image);
        dest.writeString(general_type);
        dest.writeString(likes_count);
        dest.writeString(comments_count);
        dest.writeString(is_verified);
        dest.writeString(created_by);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FranchisesModel> CREATOR = new Parcelable.Creator<FranchisesModel>() {
        @Override
        public FranchisesModel createFromParcel(Parcel in) {
            return new FranchisesModel(in);
        }

        @Override
        public FranchisesModel[] newArray(int size) {
            return new FranchisesModel[size];
        }
    };
}
