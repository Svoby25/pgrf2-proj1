package raster;

public interface Raster<T> {

    void clear();

    void setDefaultValue(T value);

    int getWidth();

    int getHeight();

    T getValue(int x, int y);

    void setValue(int x, int y, T value);

    default boolean isInRaster(int x, int y) {
      return  (x >= 0 && x < getWidth()) && (y >= 0 && y < getHeight());
    }

}
