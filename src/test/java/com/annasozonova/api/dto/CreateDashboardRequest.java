package com.annasozonova.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object (DTO) for creating a dashboard via API.
 * Contains fields required by the POST /dashboard request.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDashboardRequest {
    private String name;
    private String description;
    private boolean share;
}
