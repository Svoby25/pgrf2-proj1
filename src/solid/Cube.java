package solid;

import transforms.Col;
import transforms.Point3D;
import transforms.Vec2D;

public class Cube extends Solid {
    public Cube() {
        this.getVertexBuffer().add(new Vertex(new Point3D( 1,  1,  1), new Col(0xffffff), new Vec2D(1.0f, 1.0f)));
        this.getVertexBuffer().add(new Vertex(new Point3D(-1,  1,  1), new Col(0xffffff), new Vec2D(0.0f, 1.0f)));
        this.getVertexBuffer().add(new Vertex(new Point3D(-1, -1,  1), new Col(0xffffff), new Vec2D(0.0f, 0.0f)));
        this.getVertexBuffer().add(new Vertex(new Point3D( 1, -1,  1), new Col(0xffffff), new Vec2D(1.0f, 0.0f)));
        this.getVertexBuffer().add(new Vertex(new Point3D( 1,  1, -1), new Col(0xff0000), new Vec2D(0.0f, 1.0f)));
        this.getVertexBuffer().add(new Vertex(new Point3D(-1,  1, -1), new Col(0xff0000), new Vec2D(1.0f, 1.0f)));
        this.getVertexBuffer().add(new Vertex(new Point3D(-1, -1, -1), new Col(0xff0000), new Vec2D(1.0f, 0.0f)));
        this.getVertexBuffer().add(new Vertex(new Point3D( 1, -1, -1), new Col(0xff0000), new Vec2D(0.0f, 0.0f)));

        indexBuffer.add(0); indexBuffer.add(1); indexBuffer.add(2);
        indexBuffer.add(0); indexBuffer.add(2); indexBuffer.add(3);


        indexBuffer.add(4); indexBuffer.add(5); indexBuffer.add(6);
        indexBuffer.add(4); indexBuffer.add(6); indexBuffer.add(7);

        indexBuffer.add(0); indexBuffer.add(1); indexBuffer.add(5);
        indexBuffer.add(0); indexBuffer.add(5); indexBuffer.add(4);

        indexBuffer.add(1); indexBuffer.add(2); indexBuffer.add(6);
        indexBuffer.add(1); indexBuffer.add(6); indexBuffer.add(5);

        indexBuffer.add(2); indexBuffer.add(3); indexBuffer.add(7);
        indexBuffer.add(2); indexBuffer.add(7); indexBuffer.add(6);

        indexBuffer.add(3); indexBuffer.add(0); indexBuffer.add(4);
        indexBuffer.add(3); indexBuffer.add(4); indexBuffer.add(7);

        this.partBuffer.add(new Part(TopologyType.TRIANGLE, 0, 12));

    }
}
