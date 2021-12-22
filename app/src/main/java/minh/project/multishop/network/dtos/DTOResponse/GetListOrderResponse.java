package minh.project.multishop.network.dtos.DTOResponse;

import java.util.List;

import minh.project.multishop.network.dtos.DTOmodels.DTOLink;


public class GetListOrderResponse {
    public DTOLink links;
    public int total;
    public int page;
    public int page_size;
    public List<OrderDetailResponse> results;
}
