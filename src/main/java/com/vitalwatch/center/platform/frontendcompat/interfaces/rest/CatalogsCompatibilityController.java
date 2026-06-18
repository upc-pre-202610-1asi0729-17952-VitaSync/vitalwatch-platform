package com.vitalwatch.center.platform.frontendcompat.interfaces.rest;

import com.vitalwatch.center.platform.frontendcompat.interfaces.rest.resources.FrontendCatalogOptionResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Frontend compatibility controller for catalog endpoints expected by Angular.
 */
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Frontend API - Catalogs", description = "Catalog endpoints compatible with the Angular frontend")
public class CatalogsCompatibilityController {

    @GetMapping("/workAreas")
    @Operation(summary = "Get frontend-compatible work areas")
    public ResponseEntity<List<FrontendCatalogOptionResource>> getWorkAreas() {
        return ResponseEntity.ok(List.of(
                option(1L, "emergency", "Emergency", "Emergency", "emergency"),
                option(2L, "icu", "Intensive Care Unit", "Intensive Care Unit", "icu"),
                option(3L, "surgery", "Surgery", "Surgery", "surgery"),
                option(4L, "pediatrics", "Pediatrics", "Pediatrics", "pediatrics"),
                option(5L, "internal-medicine", "Internal Medicine", "Internal Medicine", "internal-medicine"),
                option(6L, "general-care", "General Care", "General Care", "general-care")
        ));
    }

    @GetMapping("/work-areas")
    @Operation(summary = "Get frontend-compatible work areas using kebab-case alias")
    public ResponseEntity<List<FrontendCatalogOptionResource>> getWorkAreasAlias() {
        return getWorkAreas();
    }

    @GetMapping("/specialties")
    @Operation(summary = "Get frontend-compatible medical specialties")
    public ResponseEntity<List<FrontendCatalogOptionResource>> getSpecialties() {
        return ResponseEntity.ok(List.of(
                option(1L, "general-medicine", "General Medicine", "General Medicine", "general-medicine"),
                option(2L, "emergency-medicine", "Emergency Medicine", "Emergency Medicine", "emergency-medicine"),
                option(3L, "critical-care", "Critical Care", "Critical Care", "critical-care"),
                option(4L, "pediatrics", "Pediatrics", "Pediatrics", "pediatrics"),
                option(5L, "surgery", "Surgery", "Surgery", "surgery"),
                option(6L, "nursing", "Nursing", "Nursing", "nursing"),
                option(7L, "technical-care", "Technical Care", "Technical Care", "technical-care")
        ));
    }

    private FrontendCatalogOptionResource option(
            Long id,
            String code,
            String name,
            String label,
            String value
    ) {
        return new FrontendCatalogOptionResource(
                id,
                code,
                name,
                label,
                value,
                true
        );
    }
}