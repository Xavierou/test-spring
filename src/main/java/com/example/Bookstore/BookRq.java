package com.example.Bookstore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(accessMode = Schema.AccessMode.READ_ONLY)
public class BookRq {
    private String title;
    private String author;
}
