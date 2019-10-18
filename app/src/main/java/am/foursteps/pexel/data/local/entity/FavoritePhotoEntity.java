package am.foursteps.pexel.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import am.foursteps.pexel.data.remote.model.ImageSrc;

@Entity
public class FavoritePhotoEntity {

    @PrimaryKey
    @NonNull
    private String primaryKey;

    @JsonProperty("width")
    private Integer width;
    @JsonProperty("height")
    private Integer height;

    @JsonProperty("url")
    private String url;

    private ImageSrc ImageSrc;

    @JsonIgnore
    private float downloadProgress = -1;

    public FavoritePhotoEntity() {
    }

    @NonNull
    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(@NonNull String primaryKey) {
        this.primaryKey = primaryKey;
    }

    @NonNull
    public Integer getWidth() {
        return width;
    }

    public void setWidth(@NonNull Integer width) {
        this.width = width;
    }

    @NonNull
    public Integer getHeight() {
        return height;
    }

    public void setHeight(@NonNull Integer height) {
        this.height = height;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    public void setUrl(@NonNull String url) {
        this.url = url;
    }

    public ImageSrc getImageSrc() {
        return ImageSrc;
    }

    public void setImageSrc(ImageSrc imageSrc) {
        ImageSrc = imageSrc;
    }

    public float getDownloadProgress() {
        return downloadProgress;
    }

    public void setDownloadProgress(float downloadProgress) {
        this.downloadProgress = downloadProgress;
    }
}
