package co.com.ancas.util;

import co.com.ancas.dto.Restaurant;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class RestaurantUtil {

    public static List<Restaurant> getAllRestaurants() {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream stream = RestaurantUtil.class.getResourceAsStream("restaurants.json");
        try {
            return objectMapper.readValue(stream, new TypeReference<List<Restaurant>>() {
            });
        } catch (IOException e) {
           e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
