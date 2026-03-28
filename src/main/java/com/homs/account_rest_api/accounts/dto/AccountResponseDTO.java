package com.homs.account_rest_api.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;

@Getter
@Builder
@Schema(
        name = "AccountResponse",
        description = "Response wrapper for account data"
)
public class AccountResponseDTO extends RepresentationModel<AccountResponseDTO> {

    @Schema(
            description = "The account data",
            implementation = AccountDTO.class
    )
    private AccountDTO data;

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
