package perfect_apps.sharkny.models;

/**
 * Created by mostafa on 06/06/16.
 */
public class Countries {
    public Countries(){

    }

    public Countries(String id, String title){
        this.id = id;
        this.title = title;
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

    private String id, title;
}
