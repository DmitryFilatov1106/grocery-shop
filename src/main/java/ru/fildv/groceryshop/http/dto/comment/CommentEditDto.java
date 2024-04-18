package ru.fildv.groceryshop.http.dto.comment;

import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class CommentEditDto {
    Integer productId;

    @Size(min = 1, max = 255, message = "{comment.Size}")
    String comment;
}
