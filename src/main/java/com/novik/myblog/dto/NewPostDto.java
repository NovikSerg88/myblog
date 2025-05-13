package com.novik.myblog.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewPostDto {

    @NotNull
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    public String title;

    @NotNull
    @Size(min = 10, max = 5000, message = "Content must be between 10 and 5000 characters")
    public String content;

    public String imageUrl;

    public String tags;
}
