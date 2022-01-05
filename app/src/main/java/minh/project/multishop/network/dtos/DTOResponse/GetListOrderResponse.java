package minh.project.multishop.network.dtos.DTOResponse;

import minh.project.multishop.network.dtos.DTOmodels.DTOLink;

import java.util.List;

public class GetListOrderResponse {
    public DTOLink links;
    public int total;
    public int page;
    public int page_size;
    public List<OrderDetailResponse> results;
}
