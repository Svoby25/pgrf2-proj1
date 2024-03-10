package solid;

import transforms.Col;
import transforms.Point3D;
import transforms.Vec2D;

public class Arrow extends Solid{
    public Arrow() {
        getVertexBuffer().add(new Vertex(new Point3D(1., 3., 0.5), new Col(0x00ff00), new Vec2D(0.1, 0.3)));
        getVertexBuffer().add(new Vertex(new Point3D(2., 3., 0.5), new Col(0x00ff00), new Vec2D(0.3, 0.5)));

        getVertexBuffer().add(new Vertex(new Point3D(2., 1., 0.5), new Col(0x00ff00), new Vec2D(0., 1.)));
        getVertexBuffer().add(new Vertex(new Point3D(2., 5., 0.5), new Col(0x00ff00), new Vec2D(0.5,0.5)));
        getVertexBuffer().add(new Vertex(new Point3D(4., 3., 0.5), new Col(0x00ff00), new Vec2D(0.,1.)));

        getIndexBuffer().add(0);
        getIndexBuffer().add(1);

        getIndexBuffer().add(4);
        getIndexBuffer().add(2);
        getIndexBuffer().add(3);


        getPartBuffer().add(new Part(TopologyType.LINES, 0, 1));
        getPartBuffer().add(new Part(TopologyType.TRIANGLE, 2, 1));
    }
}
