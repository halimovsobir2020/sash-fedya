package uz.pdp.clients.dtos;

import java.util.HashMap;
import java.util.Map;

public class TypeMapper {

    public static Map<String, Class<?>> typeMapper = new HashMap<>(Map.of(
            "OrderFullDTO", OrderFullDTO.class,
            "Long", Long.class
    ));
}
