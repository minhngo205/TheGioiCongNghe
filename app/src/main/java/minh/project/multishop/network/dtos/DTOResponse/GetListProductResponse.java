package minh.project.multishop.network.dtos.DTOResponse;

import com.google.gson.annotations.SerializedName;
import minh.project.multishop.models.Product;
import minh.project.multishop.network.dtos.DTOmodels.DTOLink;

import java.util.List;

public class GetListProductResponse {
    @SerializedName("links")
    public DTOLink links;
    @SerializedName("total")
    public int total;
    @SerializedName("page")
    public int page;
    @SerializedName("page_size")
    public int pageSize;
    @SerializedName("results")
    public List<Product> productList;
}
