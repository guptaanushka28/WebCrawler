package com.webcrawler.endpoint;
import com.webcrawler.dto.CrawlRequestDTO;
import com.webcrawler.dto.CrawlResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface CrawlControllerEndpoint {

    @Operation(summary = "Start a new crawl", description = "Initiates a new web crawl for the given seed URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Crawl started successfully",
                    content = @Content(schema = @Schema(implementation = CrawlResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    ResponseEntity<CrawlResponseDTO> startCrawl(@RequestBody CrawlRequestDTO crawlRequestDTO);

    @Operation(summary = "Get crawl result by ID", description = "Fetches crawl details using the crawl ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Crawl result retrieved successfully",
                    content = @Content(schema = @Schema(implementation = CrawlResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Crawl result not found")
    })
    @GetMapping("/{id}")
    ResponseEntity<CrawlResponseDTO> getCrawlResult(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    );
}
