package am.foursteps.pexel.data.remote.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageSrc {
    @JsonProperty("original")
    private String original;
    @JsonProperty("large")
    private String large;
    @JsonProperty("large2x")
    private String large2x;
    @JsonProperty("medium")
    private String medium;
    @JsonProperty("small")
    private String small;
    @JsonProperty("portrait")
    private String portrait;
    @JsonProperty("landscape")
    private String landscape;
    @JsonProperty("tiny")
    private String tiny;

    @JsonProperty("original")
    public String getOriginal() {
        return original;
    }

    @JsonProperty("original")
    public void setOriginal(String original) {
        this.original = original;
    }

    @JsonProperty("large")
    public String getLarge() {
        return large;
    }

    @JsonProperty("large")
    public void setLarge(String large) {
        this.large = large;
    }

    @JsonProperty("large2x")
    public String getLarge2x() {
        return large2x;
    }

    @JsonProperty("large2x")
    public void setLarge2x(String large2x) {
        this.large2x = large2x;
    }

    @JsonProperty("medium")
    public String getMedium() {
        return medium;
    }

    @JsonProperty("medium")
    public void setMedium(String medium) {
        this.medium = medium;
    }

    @JsonProperty("small")
    public String getSmall() {
        return small;
    }

    @JsonProperty("small")
    public void setSmall(String small) {
        this.small = small;
    }

    @JsonProperty("portrait")
    public String getPortrait() {
        return portrait;
    }

    @JsonProperty("portrait")
    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    @JsonProperty("landscape")
    public String getLandscape() {
        return landscape;
    }

    @JsonProperty("landscape")
    public void setLandscape(String landscape) {
        this.landscape = landscape;
    }

    @JsonProperty("tiny")
    public String getTiny() {
        return tiny;
    }

    @JsonProperty("tiny")
    public void setTiny(String tiny) {
        this.tiny = tiny;
    }
}