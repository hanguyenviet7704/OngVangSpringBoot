package org.example.shoppefood.dto.responsePage;
import lombok.Data;

@Data
public class ResponsePage<T>  {
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private Long totalElements;
    private T content;
}
