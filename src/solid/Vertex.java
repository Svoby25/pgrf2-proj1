package solid;

import transforms.*;

public class Vertex implements Vectorizable<Vertex> {
    private Col color;
    private final Point3D position;

    private Vec2D uv;

    private double one = 1;

    public Vertex(Point3D position, Col color, Vec2D uv) {
        this.color = color;
        this.position = position;
        this.uv = uv;
    }

    public Vertex(Point3D position, Col color, Vec2D uv, double one) {
        this.color = color;
        this.position = position;
        this.uv = uv;
        this.one = one;
    }

    public Point3D getPosition() {
        return position;
    }

    public Col getColor() {
        return color;
    }

    public void setColor(Col color) {
        this.color = color;
    }

    @Override
    public Vertex mul(double k) {
        return new Vertex(this.position.mul(k), this.color.mul(k), uv.mul(k), one * k);
    }

    @Override
    public Vertex add(Vertex v) {
        return new Vertex(this.position.add(v.getPosition()), this.color.add(v.color), uv.add(v.getUv()), one + v.getOne());
    }

    public Vertex transform(Mat4 view, Mat4 proj, Mat4 model)
    {
        return new Vertex(this.position.mul(model).mul(view).mul(proj), color, uv, one);
    }

    public Vertex dehomog()
    {
        return this.mul(1 / this.position.getW());
    }

    public Vec2D getUv() {
        return uv;
    }

    public double getOne() {
        return one;
    }

}
