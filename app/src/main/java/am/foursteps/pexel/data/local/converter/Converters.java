package am.foursteps.pexel.data.local.converter;


import androidx.room.TypeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import am.foursteps.pexel.data.remote.model.ImageSrc;

public class Converters {

    @TypeConverter
    public ImageSrc stringToImageSrc(String value) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(value, ImageSrc.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ImageSrc();
        }
    }


    @TypeConverter
    public String imageSrcToString(ImageSrc src) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(src);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }
}