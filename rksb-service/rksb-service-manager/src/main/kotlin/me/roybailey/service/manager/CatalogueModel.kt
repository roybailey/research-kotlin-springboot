package me.roybailey.service.manager

import com.fasterxml.jackson.annotation.JsonProperty


data class CatalogueModel (
    
    @JsonProperty("url") var url: String
    
)
