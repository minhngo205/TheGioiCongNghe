package pfiev.lgsplus1.thegioicongnghe.models;

import com.google.gson.annotations.SerializedName;

public class ProductSpecs {
    @SerializedName("name")
    private String name;
    @SerializedName("value")
    private String value;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
