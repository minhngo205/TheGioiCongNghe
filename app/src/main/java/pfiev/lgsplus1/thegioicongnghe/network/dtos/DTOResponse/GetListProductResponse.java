package pfiev.lgsplus1.thegioicongnghe.network.dtos.DTOResponse;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import pfiev.lgsplus1.thegioicongnghe.models.Product;
import pfiev.lgsplus1.thegioicongnghe.network.dtos.DTOmodels.DTOLink;


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
