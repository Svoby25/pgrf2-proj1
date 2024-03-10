package raster;

import transforms.Col;

public class ZBuffer {
    private final Raster<Col> imageBuffer;
    private final Raster<Double> depthBuffer;

    public ZBuffer(Raster<Col> imageBuffer)
    {
        this.imageBuffer = imageBuffer;
        this.depthBuffer = new DepthBuffer(imageBuffer.getHeight(), imageBuffer.getWidth());
    }

    public void setPixelWithZTest(int x, int y, double z, Col color)
    {
        if(!depthBuffer.isInRaster(x, y)) {
            return;
        }
        double zBufferZ = depthBuffer.getValue(x, y);

        if(z <= 0 && z > zBufferZ) {
            return;
        }

        this.depthBuffer.setValue(x, y, z);
        this.imageBuffer.setValue(x, y, color);
    }
}
