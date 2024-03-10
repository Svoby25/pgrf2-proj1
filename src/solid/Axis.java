package solid;

import transforms.Col;
import transforms.Point3D;
import transforms.Vec2D;

import java.awt.*;

public class Axis extends Solid {
    public Axis(char axis, Vertex center) {
        this.vertexBuffer.add(center);

        switch (axis) {
            case 'x' -> {
                this.vertexBuffer.get(0).setColor(new Col(0x0000ff));

                this.vertexBuffer.add(new Vertex(new Point3D(2., center.getPosition().getY(), center.getPosition().getZ()), new Col(0x0000ff), new Vec2D(0,0)));
            }
            case 'y' -> {
                this.vertexBuffer.get(0).setColor(new Col(0xff0000));

                this.vertexBuffer.add(new Vertex(new Point3D(center.getPosition().getX(), 2., center.getPosition().getZ()), new Col(0xff0000), new Vec2D(0,0)));
            }
            case 'z' -> {
                this.vertexBuffer.get(0).setColor(new Col(0x00ff00));

                this.vertexBuffer.add(new Vertex(new Point3D(center.getPosition().getX(), center.getPosition().getY(), 2.), new Col(0x00ff00), new Vec2D(0,0)));
            }
        }

        this.indexBuffer.add(0);
        this.indexBuffer.add(1);

      this.partBuffer.add(new Part(TopologyType.LINES, 0, 1));
    }
    }