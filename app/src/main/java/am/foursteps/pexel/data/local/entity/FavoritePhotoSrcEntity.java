package am.foursteps.pexel.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonProperty;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = FavoritePhotoEntity.class, parentColumns = "id", childColumns = "image_id", onDelete = CASCADE))
public class FavoritePhotoSrcEntity {


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;
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
    @ColumnInfo(name = "image_id")
    private long imageId;

    public FavoritePhotoSrcEntity() {
    }

    @NonNull
    public long getId() {
        return id;
    }

    @NonNull
    public void setId(long id) {
        this.id = id;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getLarge2x() {
        return large2x;
    }

    public void setLarge2x(String large2x) {
        this.large2x = large2x;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getLandscape() {
        return landscape;
    }

    public void setLandscape(String landscape) {
        this.landscape = landscape;
    }

    public String getTiny() {
        return tiny;
    }

    public void setTiny(String tiny) {
        this.tiny = tiny;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }
}
