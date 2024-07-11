package shogi.model.utils;

import javafx.scene.image.Image;
public interface ImageStorage<T> {
    Image get(T key);

}
