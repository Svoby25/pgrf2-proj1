package raster;

public class DepthBuffer implements Raster<Double>
{
    private final double[][] buffer;

    private double defaultValue;
    private int width, height;

    public DepthBuffer(int height, int width) {
        this.width = width;
        this.height = height;
        this.buffer = new double[width][height];
        this.defaultValue = 1;
        clear();
    }
    @Override
    public void clear() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                this.buffer[x][y] = this.defaultValue;
            }
        }
    }

    @Override
    public void setDefaultValue(Double value) {
        this.defaultValue = value;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public Double getValue(int x, int y) {
        if(isInRaster(x, y)) {
            return buffer[x][y];
        }
        return this.defaultValue;
    }

    @Override
    public void setValue(int x, int y, Double value) {
        if(isInRaster(x, y)) {
            this.buffer[x][y] = value;
        }
    }
}
