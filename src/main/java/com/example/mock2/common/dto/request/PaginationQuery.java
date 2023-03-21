package com.example.mock2.common.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.domain.PageRequest;

@Getter
@Setter
@Accessors(chain = true)
public class PaginationQuery {
    private PageRequest pageRequest;

    public PaginationQuery() {
        this.pageRequest = PageRequest.of(0, 10);
    }

}
