package minh.project.multishop.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Category {
    @SerializedName("id")
    private int cateID;
    @SerializedName("name")
    private String cateName;
    @SerializedName("thumbnail")
    private String cateThumbnail;
    @SerializedName("created_at")
    private Date createdAt;
    @SerializedName("updated_at")
    private Date updatedAt;
    @SerializedName("brands")
    private List<Brand> brandList;

    public Category(int cateID, String cateName, String cateThumbnail, Date createdAt, Date updatedAt, List<Brand> brandList) {
        this.cateID = cateID;
        this.cateName = cateName;
        this.cateThumbnail = cateThumbnail;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.brandList = brandList;
    }

    public int getID() {
        return cateID;
    }

    public String getName() {
        return cateName;
    }

    public void setName(String name) {
        this.cateName = name;
    }

    public void setCateID(int cateID) {
        this.cateID = cateID;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public String getCateThumbnail() {
        return cateThumbnail;
    }

    public void setCateThumbnail(String cateThumbnail) {
        this.cateThumbnail = cateThumbnail;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Brand> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<Brand> brandList) {
        this.brandList = brandList;
    }
}