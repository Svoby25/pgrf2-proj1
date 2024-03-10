package raster;

import shader.Shader;
import solid.Vertex;
import utils.Lerp;
import view.Panel;

public class TriangleRasterizer {
    private final ZBuffer zBuffer;

    private final Lerp<Vertex> lerp;

    public TriangleRasterizer(ZBuffer zBuffer) {
        this.lerp = new Lerp<>();
        this.zBuffer = zBuffer;
    }

    public void rasterize(Vertex a, Vertex b, Vertex c, Shader shader) {

        if(a.getPosition().getY() > c.getPosition().getY()) {
            Vertex temp1 = new Vertex(a.getPosition(), a.getColor(), a.getUv());

            a = new Vertex(c.getPosition(), c.getColor(), c.getUv());

            c = temp1;
        }

        if(b.getPosition().getY() > c.getPosition().getY()) {
            Vertex temp2 = new Vertex(b.getPosition(), b.getColor(), b.getUv());

            b = new Vertex(c.getPosition(), c.getColor(), c.getUv());
            c = temp2;
        }

        if(a.getPosition().getY() > b.getPosition().getY()) {
            Vertex temp3 = new Vertex(a.getPosition(), a.getColor(), a.getUv());

            a = new Vertex(b.getPosition(), b.getColor(), a.getUv());
            b = temp3;
        }


        for (int y = Math.max((int) a.getPosition().getY() + 1, 0); y <= Math.min((int) b.getPosition().getY(), Panel.HEIGHT - 1); y++) {
            double tAB = lerp.t(a.getPosition().getY(), b.getPosition().getY(), y);

            Vertex v1 = lerp.lerp(a, b, tAB);

            double tAC = lerp.t(a.getPosition().getY(), c.getPosition().getY(), y);

            Vertex v2 = lerp.lerp(a, c, tAC);

            if(v1.getPosition().getX() > v2.getPosition().getX()) {
                Vertex temp4 = new Vertex(v1.getPosition(), v1.getColor(), v1.getUv());
                v1 = new Vertex(v2.getPosition(), v2.getColor(), v2.getUv());
                v2 = temp4;
            }

            for (int x = Math.max((int)v1.getPosition().getX() + 1, 0); x <= Math.min(v2.getPosition().getX(), Panel.WIDTH - 1); x++) {
                double tZ = lerp.t(v1.getPosition().getX(), v2.getPosition().getX(), x);

                Vertex v3 = lerp.lerp(v1, v2, tZ);

                zBuffer.setPixelWithZTest(x, y, v3.getPosition().getZ(), shader.getColor(v3));
            }
        }

        for (int y = Math.max((int) b.getPosition().getY() + 1, 0); y <= Math.min((int )c.getPosition().getY(), Panel.HEIGHT - 1); y++) {
            double tBC = lerp.t(b.getPosition().getY(), c.getPosition().getY(), y);

            Vertex v1 = lerp.lerp(b, c, tBC);

            double tAC = lerp.t(a.getPosition().getY(), c.getPosition().getY(), y);

            Vertex v2 = lerp.lerp(a, c, tAC);

            if(v1.getPosition().getX() > v1.getPosition().getX()) {
                Vertex temp5 = new Vertex(v1.getPosition(), v1.getColor(), v1.getUv());

                v1 = new Vertex(v2.getPosition(), v2.getColor(), v2.getUv());
                v2 = temp5;
            }

            for (int x = Math.max((int) v1.getPosition().getX() + 1, 0); x <= Math.min((int) v2.getPosition().getX(), Panel.WIDTH - 1); x++) {
                double tZ = lerp.t(v1.getPosition().getX(), v2.getPosition().getX(), x);

                Vertex v3 = lerp.lerp(v1, v2, tZ);

                zBuffer.setPixelWithZTest(x, y, v3.getPosition().getZ(), shader.getColor(v3));
            }
        }
    }
}