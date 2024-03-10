package render;

import raster.LineRasterizer;
import raster.TriangleRasterizer;
import shader.Shader;
import solid.Part;
import solid.Solid;
import solid.Vertex;
import transforms.Mat4;
import transforms.Point3D;
import transforms.Vec3D;
import utils.Lerp;

public class Renderer {
    private TriangleRasterizer triangleRasterizer;

    private LineRasterizer lineRasterizer;

    private Mat4 view;

    private Mat4 proj;

    private Lerp<Vertex> lerp = new Lerp<>();

    public Renderer(TriangleRasterizer triangleRasterizer, LineRasterizer lineRasterizer, Mat4 view, Mat4 proj) {
        this.triangleRasterizer = triangleRasterizer;
        this.lineRasterizer = lineRasterizer;
        this.setView(view);
        this.setProj(proj);

    }

    public void render(Solid solid) {
        for (Part part : solid.getPartBuffer()) {
            int start = part.getStart();
            switch (part.getType()) {
                case TRIANGLE -> {
                    for (int i = 0; i < part.getCount(); i++) {
                        Vertex a = solid.getVertex(solid.getIndex(start)).transform(this.view, this.proj, solid.getModel());
                        Vertex b = solid.getVertex(solid.getIndex(start + 1)).transform(this.view, this.proj, solid.getModel());
                        Vertex c = solid.getVertex(solid.getIndex(start + 2)).transform(this.view, this.proj, solid.getModel());

                        this.clipTriangle(a, b, c, solid.getShader());
                        start += 3;
                    }

                }
                case LINES -> {
                    for (int i = 0; i < part.getCount(); i++) {
                        Vertex a = solid.getVertex(solid.getIndex(start)).transform(this.view, this.proj, solid.getModel());

                        Vertex b = solid.getVertex(solid.getIndex(start + 1)).transform(this.view, this.proj, solid.getModel());

                        a = a.dehomog();
                        b = b.dehomog();

                        Vec3D va = new Vec3D(a.getPosition());
                        Vec3D vb = new Vec3D(b.getPosition());

                        va = this.transformToWindow(va);
                        vb = this.transformToWindow(vb);

                        lineRasterizer.rasterize(
                                new Vertex(new Point3D(va.getX(), va.getY(), a.getPosition().getZ()), a.getColor(), a.getUv()),
                                new Vertex(new Point3D(vb.getX(), vb.getY(), b.getPosition().getZ()), b.getColor(), b.getUv())
                        );

                        start += 2;
                    }
                }
            }
        }
    }

    public Vec3D transformToWindow(Vec3D p) {
        return p.mul(new Vec3D(1, -1, 1))
                .add(new Vec3D(1, 1, 0))
                .mul(new Vec3D((800 - 1) / 2., (600 - 1) / 2., 1));
    }

    public boolean fastClip(Vertex a, Vertex b, Vertex c) {
        Point3D aPoint = a.getPosition();
        Point3D bPoint = b.getPosition();
        Point3D cPoint = c.getPosition();

        return !(
                (aPoint.getX() < -aPoint.getW() && bPoint.getX() < -bPoint.getW() && cPoint.getX() < -cPoint.getW()) ||
                        (aPoint.getX() > aPoint.getW() && bPoint.getX() > bPoint.getW() && cPoint.getX() > cPoint.getW()) ||
                        (aPoint.getY() < -aPoint.getW() && bPoint.getY() < -bPoint.getW() && cPoint.getY() < -cPoint.getW()) ||
                        (aPoint.getY() > aPoint.getW() && bPoint.getY() > bPoint.getW() && cPoint.getY() > cPoint.getW()) ||
                        (aPoint.getZ() < 0 && bPoint.getZ() < 0 && cPoint.getZ() < 0) ||
                        (aPoint.getZ() > aPoint.getW() && bPoint.getZ() > bPoint.getW() && cPoint.getZ() > cPoint.getW()));
    }

    public void setView(Mat4 view) {
        this.view = view;
    }
    public void setProj(Mat4 proj) {
        this.proj = proj;
    }

    private void clipTriangle(Vertex a, Vertex b, Vertex c, Shader shader) {
        if (this.fastClip(a, b, c)) {

            if (a.getPosition().getZ() < c.getPosition().getZ()) {
                Vertex temp = new Vertex(a.getPosition(), a.getColor(), a.getUv(), a.getOne());

                a = new Vertex(c.getPosition(), c.getColor(), c.getUv(), c.getOne());
                c = temp;
            }

            if (b.getPosition().getZ() < c.getPosition().getZ()) {
                Vertex temp2 = new Vertex(b.getPosition(), b.getColor(), b.getUv(), b.getOne());

                b = new Vertex(c.getPosition(), c.getColor(), c.getUv(), c.getOne());
                c = temp2;
            }


            if (a.getPosition().getZ() < b.getPosition().getZ()) {
                Vertex temp3 = new Vertex(a.getPosition(), a.getColor(), a.getUv(), a.getOne());

                a = new Vertex(b.getPosition(), b.getColor(), b.getUv(), b.getOne());
                b = temp3;
            }

            double zMin = 0;

            if (a.getPosition().getZ() < zMin) {
                return;
            }

            if (b.getPosition().getZ() < zMin) {
                double tAB = lerp.t(a.getPosition().getZ(), b.getPosition().getZ(), zMin);

                Vertex v1 = lerp.lerp(a, b, tAB);

                double tAC = lerp.t (a.getPosition().getZ(), c.getPosition().getZ(), zMin);

                Vertex v2 = lerp.lerp(a, c, tAC);

                Vec3D va = new Vec3D(a.getPosition());
                Vec3D vAB = new Vec3D(v1.getPosition());
                Vec3D vAC = new Vec3D(v2.getPosition());

                a = a.dehomog();
                v1 = v1.dehomog();
                v2 = v2.dehomog();

                va = this.transformToWindow(va);
                vAB = this.transformToWindow(vAB);
                vAC = this.transformToWindow(vAC);

                triangleRasterizer.rasterize(
                        new Vertex(new Point3D(va.getX(), va.getY(), a.getPosition().getZ()), a.getColor(), a.getUv(), a.getOne()),
                        new Vertex(new Point3D(vAB.getX(), vAB.getY(), v1.getPosition().getZ()), v1.getColor(), v1.getUv(), v1.getOne()),
                        new Vertex(new Point3D(vAC.getX(), vAC.getY(), v2.getPosition().getZ()), v2.getColor(), v2.getUv(), v2.getOne()),
                        shader
                );

                return;
            }

            if (c.getPosition().getZ() < zMin) {
                double tBC = lerp.t(b.getPosition().getZ(), c.getPosition().getZ(), zMin);

                Vertex v1 = lerp.lerp(b, c, tBC);

                double tAC = lerp.t(a.getPosition().getZ(), c.getPosition().getZ(), zMin);

                Vertex v2 = lerp.lerp(a, c, tAC);

                a = a.dehomog();
                b = b.dehomog();
                v1 = v1.dehomog();
                v2 = v2.dehomog();

                Vec3D va = new Vec3D(a.getPosition());
                Vec3D vb = new Vec3D(b.getPosition());
                Vec3D vBC = new Vec3D(v1.getPosition());
                Vec3D vAC = new Vec3D(v2.getPosition());

                va = this.transformToWindow(va);
                vb = this.transformToWindow(vb);
                vAC = this.transformToWindow(vAC);

                triangleRasterizer.rasterize(
                        new Vertex(new Point3D(va.getX(), va.getY(), a.getPosition().getZ()), a.getColor(), a.getUv(), a.getOne()),
                        new Vertex(new Point3D(vb.getX(), vb.getY(), b.getPosition().getZ()), b.getColor(), b.getUv(), b.getOne()),
                        new Vertex(new Point3D(vBC.getX(), vBC.getY(), v1.getPosition().getZ()), v1.getColor(), v1.getUv(), v1.getOne()),
                        shader
                );
                triangleRasterizer.rasterize(
                        new Vertex(new Point3D(va.getX(), va.getY(), a.getPosition().getZ()), a.getColor(), a.getUv(), a.getOne()),
                        new Vertex(new Point3D(vBC.getX(), vBC.getY(), v1.getPosition().getZ()), v1.getColor(), v1.getUv(), v1.getOne()),
                        new Vertex(new Point3D(vAC.getX(), vAC.getY(), v2.getPosition().getZ()), v2.getColor(), v2.getUv(), v2.getOne()),
                        shader
                );
                return;
            }

            a = a.dehomog();
            b = b.dehomog();
            c = c.dehomog();

            Vec3D va = new Vec3D(a.getPosition());
            Vec3D vb = new Vec3D(b.getPosition());
            Vec3D vc = new Vec3D(c.getPosition());

            va = this.transformToWindow(va);
            vb = this.transformToWindow(vb);
            vc = this.transformToWindow(vc);

            triangleRasterizer.rasterize(
                    new Vertex(new Point3D(va.getX(), va.getY(), a.getPosition().getZ()), a.getColor(), a.getUv(), a.getOne()),
                    new Vertex(new Point3D(vb.getX(), vb.getY(), b.getPosition().getZ()), b.getColor(), b.getUv(), b.getOne()),
                    new Vertex(new Point3D(vc.getX(), vc.getY(), c.getPosition().getZ()), c.getColor(), c.getUv(), c.getOne()),
                    shader
            );
        }
    }
}
