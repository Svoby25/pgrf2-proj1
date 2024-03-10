package raster;

import solid.Vertex;
import transforms.Col;
import utils.Lerp;

public class FilledLineRasterizer extends LineRasterizer {

    public FilledLineRasterizer(ZBuffer zBuffer) {
        super(zBuffer);
    }

    @Override
    protected void drawLine(Vertex a, Vertex b) {
        int x1 = (int) Math.round(a.getPosition().getX());
        int x2 = (int) Math.round(b.getPosition().getX());

        int y1 = (int) Math.round(a.getPosition().getY());
        int y2 = (int) Math.round(b.getPosition().getY());

        double z1 = a.getPosition().getZ();
        double z2 = b.getPosition().getZ();

        int dx = Math.abs(x1 - x2);
        int dy = Math.abs(y1 - y2);
        int moveOnX = (x1 < x2) ? 1 : -1;
        int moveOnY = (y1 < y2) ? 1 : -1;

        int prediction = dx - dy;
        int currentX = x1;
        int currentY = y1;

        Col colorA = a.getColor();
        Col colorB = b.getColor();

        int tZ = 0;

        if(y2 - y1 != 0) {
            tZ = (currentY - y1) / (y2 - y1);
        }


        while (currentX != x2 || currentY != y2) {

            if (tZ >= 0 && tZ <= 1) {
                double interpolatedZ = z1 + tZ * (z2 - z1);

                Col finalColor = colorA.mul(1 - interpolatedZ).add(colorB.mul(interpolatedZ));

                zBuffer.setPixelWithZTest(currentX, currentY, interpolatedZ, finalColor);

                int newPrediction = 2 * prediction;

                if (newPrediction > -dy) {
                    prediction -= dy;
                    currentX += moveOnX;
                }

                if (newPrediction < dx) {
                    prediction += dx;
                    currentY += moveOnY;
                }
            }

        }

    }
}
