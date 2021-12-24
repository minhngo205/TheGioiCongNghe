package minh.project.multishop.network.dtos.DTOResponse;

import com.google.gson.annotations.SerializedName;
import minh.project.multishop.database.entity.ProductName;
import minh.project.multishop.network.dtos.DTOmodels.DTOLink;

import java.util.List;

public class GetProductNameResponse {
    @SerializedName("links")
    private DTOLink links;
    @SerializedName("total")
    private int total;
    @SerializedName("page")
    private int page;
    @SerializedName("page_size")
    private int page_size;
    @SerializedName("results")
    private List<ProductName> productNameList;

    public List<ProductName> getProductNameList() {
        return productNameList;
    }
}
