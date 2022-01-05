package minh.project.multishop.network.dtos.DTORequest;

import com.google.gson.annotations.SerializedName;

public class ReplyReviewRequest {
    @SerializedName("comment")
    private String replyComment;

    public ReplyReviewRequest(String replyComment) {
        this.replyComment = replyComment;
    }

    public String getReplyComment() {
        return replyComment;
    }

    public void setReplyComment(String replyComment) {
        this.replyComment = replyComment;
    }
}
