package com.novik.myblog.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewPostDto {

    @NonNull
    @Size(min = 5, max = 100)
    public String title;

    @NonNull
    @Size(min = 10, max = 5000)
    public String content;

    public String imageUrl;

    public String tags;
}
