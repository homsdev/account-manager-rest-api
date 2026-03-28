package com.homs.account_rest_api.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
@Schema(
        name = "Account List Response",
        description = "DTO for account list response"
)
public class AccountListResponseDTO extends RepresentationModel<AccountListResponseDTO> {

    @Schema(
            description = "Accounts data"
    )
    private List<AccountDTO> data;

    @Schema(
            description = "The timestamp of the response",
            example = "2023-01-01T12:00:00Z"
    )
    private Instant timestamp;

    @Schema(hidden = true)
    @Override
    public Links getLinks() {
        return super.getLinks();
    }
}
