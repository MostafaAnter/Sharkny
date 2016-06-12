package perfect_apps.sharkny.models;

/**
 * Created by mostafa on 09/06/16.
 */
public class FavoritItem {
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    private String item_id;
    private String title ;
    private String description;
    private String image;


    public FavoritItem(String item_id, String type, String title, String description, String image) {
        this.item_id = item_id;
        this.type = type;
        this.title = title;
        this.description = description;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
