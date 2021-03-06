package org.labmonkeys.home_library.catalog.colaborators.open_library.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

/**
 * BookInfoDetailOL
 */
@Data
@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookInfoDetailOL {

    List<PublisherOL> publishers = null;
    IdentifiersOL identifiers;
    String title;
    String url;
    String notes;
    @JsonProperty("number_of_pages")
    Long numberOfPages;
    CoverOL cover;
    List<Subjects> subjects = null;
    @JsonProperty("publish_date")
    String publishDate;
    String key;
    List<AuthorOL> authors = null;
}