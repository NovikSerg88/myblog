package com.novik.myblog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewPostDto {

    @NonNull
    public String title;

    @NonNull
    public String text;

    public String imageUrl;

    public String tags;
}
