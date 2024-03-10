package solid;

import transforms.Col;
import transforms.Point3D;
import transforms.Vec2D;

public class Pyramid extends Solid {
    public Pyramid() {
        partBuffer.add(new Part(TopologyType.TRIANGLE, 0, 4));

        vertexBuffer.add(new Vertex(new Point3D(0, 1, 0), new Col(0xffffff), new Vec2D(0.5, 0)));
        vertexBuffer.add(new Vertex(new Point3D(-1, -1, 1), new Col(0xff0000), new Vec2D(0, 1)));
        vertexBuffer.add(new Vertex(new Point3D(1, -1, 1), new Col(0xff0000), new Vec2D(1, 1)));
        vertexBuffer.add(new Vertex(new Point3D(0, -1, -1), new Col(0xff0000), new Vec2D(0.5, 0)));

        indexBuffer.add(0); indexBuffer.add(1); indexBuffer.add(2);
        indexBuffer.add(0); indexBuffer.add(2); indexBuffer.add(3);
        indexBuffer.add(0); indexBuffer.add(3); indexBuffer.add(1);
        indexBuffer.add(3); indexBuffer.add(2); indexBuffer.add(1);
    }
}
