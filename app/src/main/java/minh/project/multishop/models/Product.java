package minh.project.multishop.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Product {
    @SerializedName("id")
    public int productID;

    @SerializedName("name")
    public String productName;

    @SerializedName("thumbnail")
    public String imgThumbnail;

    @SerializedName("brand")
    public Brand brand;

    @SerializedName("created_at")
    public Date createdAt;

    @SerializedName("updated_at")
    public Date updatedAt;

    @SerializedName("category")
    public Category category;

    @SerializedName("specifications")
    public List<ProductSpecs> specifications;

    @SerializedName("short_description")
    public String descriptionShort;

    @SerializedName("description")
    public String description;

    @SerializedName("price")
    public int productPrice;

    @SerializedName("sale_price")
    public int salePrice;

    @SerializedName("discount")
    public int isDiscount;

    @SerializedName("avg_rating")
    public double avgRating;

    @SerializedName("ratings")
    public List<Rating> listRatings;

    @SerializedName("images")
    public Image[] listImages;

    public int getID() {
        return productID;
    }

    public void setID(int ID) {
        this.productID = ID;
    }

    public double getAvgRate() {
        return avgRating;
    }

    public void setAvgRate(double avgRate) {
        this.avgRating = avgRate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageThumbnail() {
        return imgThumbnail;
    }

    public void setImageThumbnail(String imageThumbnail) {
        this.imgThumbnail = imageThumbnail;
    }

    public Category getCategory() {
        return category;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public Brand getBrand() {
        return brand;
    }

    public List<ProductSpecs> getSpecs() {
        return specifications;
    }

    public void setSpecs(List<ProductSpecs> specs) {
        this.specifications = specs;
    }

    public String getShortDes() {
        return descriptionShort;
    }

    public void setShortDes(String shortDes) {
        this.descriptionShort = shortDes;
    }

    public int getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(int salePrice) {
        this.salePrice = salePrice;
    }

    public int getIsDiscount() {
        return isDiscount;
    }

    public void setIsDiscount(int isDiscount) {
        this.isDiscount = isDiscount;
    }

    public List<Rating> getRatingList() {
        return listRatings;
    }

    public void setRatingList(List<Rating> ratingList) {
        this.listRatings = ratingList;
    }

    public Image[] getImageList() {
        return listImages;
    }
}
