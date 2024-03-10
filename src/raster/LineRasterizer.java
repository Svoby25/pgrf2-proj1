package raster;

import solid.Vertex;
import transforms.Col;

public abstract class LineRasterizer {
    Col color;

    ZBuffer zBuffer;


    public LineRasterizer(ZBuffer zBuffer){
        this.zBuffer = zBuffer;
    }

    public void setColor(Col color) {
        this.color = color;
    }

    public void setColor(int color) {
        this.color = new Col(color);
    }

    public void rasterize(Vertex a, Vertex b) {
        drawLine(a, b);
    }

    protected void drawLine(Vertex a, Vertex b) {

    }
}
