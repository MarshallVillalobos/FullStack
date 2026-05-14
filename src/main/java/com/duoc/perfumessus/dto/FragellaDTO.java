package com.duoc.perfumessus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class FragellaDTO {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Brand")
    private String brand;

    @JsonProperty("Image URL")
    private String imageUrl;

    @JsonProperty("rating")
    private String rating;

    @JsonProperty("Price")
    private String price;

    @JsonProperty("General Notes")
    private List<String> generalNotes;
    
    @JsonProperty("Main Accords")
    private List<String> mainAccords;
}