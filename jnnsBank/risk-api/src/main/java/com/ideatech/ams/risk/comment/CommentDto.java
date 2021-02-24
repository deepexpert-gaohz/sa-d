package com.ideatech.ams.risk.comment;

import lombok.Data;

import java.util.Date;

@Data
public class CommentDto {

    private String userId;
    private Date time;
    private String taskId;
    private String message;
}
