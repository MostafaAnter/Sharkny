package perfect_apps.sharkny.models;

/**
 * Created by mostafa on 09/06/16.
 */
public class FavoritItem {
    private String title ;
    private String description;
    private String image;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public FavoritItem(String title, String description, String image) {
        this.title = title;
        this.description = description;
        this.image = image;
    }
}
