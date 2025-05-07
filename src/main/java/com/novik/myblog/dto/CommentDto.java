package com.novik.myblog.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Data
@Builder
@Setter
public class CommentDto {

    public Long id;

    public String content;
}
