package minh.project.multishop.network.dtos.DTOResponse;

import minh.project.multishop.network.dtos.DTOmodels.DTOUser;

import java.util.Date;

public class ReplyReviewResponse {
    public int id;
    public String comment;
    public Date created_at;
    public Date updated_at;
    public int rating;
    public DTOUser user;
}
