package org.example.junmin.comm.dto;

import java.io.Serializable;

public class BlogSyncMessage implements Serializable {
    private Long blogId;

    /**
     * CREATE / UPDATE / DELETE
     */
    private String eventType;

    private Integer retryCount = 0;

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
