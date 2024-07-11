package shogi.model.utils;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class EnumImageStorage<T extends Enum<?>> implements ImageStorage<T> {

    private final Map<T, Image> map = new HashMap<>();

    public EnumImageStorage(Class<T> enumClass) {
        for (var constant : enumClass.getEnumConstants()) {
            var url = String.format("/assets/%s.png", constant.name().toLowerCase());
            try {
                map.put(constant, new Image(url));
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public Image get(T constant) {
        return map.get(constant);
    }

}