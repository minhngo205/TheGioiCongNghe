package minh.project.multishop.models;

import com.google.gson.annotations.SerializedName;

public class Image {
    @SerializedName("id")
    private int id;
    @SerializedName("label")
    private String label;
    @SerializedName("url")
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
