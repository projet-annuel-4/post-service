package com.example.postservice.data.request;


import java.util.Date;

public class PostRequest {

    private String content;
    private Integer nbLike;
    private Date creationDate;
    private Date updateDate;
    private Long userId;
    private String tagName;
    private String attachmentUrl;
    private String attachmentDescription;


    public String getContent() {
        return content;
    }

    public Integer getNbLike() {
        return nbLike;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public Long getUserId() {
        return userId;
    }

    public String getTagName() {
        return tagName;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public String getAttachmentDescription() {
        return attachmentDescription;
    }
}
