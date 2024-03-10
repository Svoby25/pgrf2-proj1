package solid;

import shader.Shader;
import transforms.*;

import java.util.ArrayList;

public abstract class Solid {

    protected final ArrayList<Vertex> vertexBuffer = new ArrayList<>();
    protected final ArrayList<Integer> indexBuffer = new ArrayList<>();

    protected final ArrayList<Part> partBuffer = new ArrayList<Part>();

    protected double translateX, translateY, translateZ, rotateX, rotateY, rotateZ = 1;



    protected Mat4 model = new Mat4Identity();

    private Shader shader;


    protected double scale = 0.5;

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public Mat4 getScaleMatrix() {
        return new Mat4Scale(this.scale, this.scale, this.scale);
    }


    public ArrayList<Vertex> getVertexBuffer() {
        return vertexBuffer;
    }

    public Vertex getVertex(int index) {
        return vertexBuffer.get(index);
    }

    public ArrayList<Integer> getIndexBuffer() {
        return indexBuffer;
    }

    public int getIndex(int index) {
        return indexBuffer.get(index);
    }

    public ArrayList<Part> getPartBuffer() {
        return partBuffer;
    }

    public Mat4 getModel() {
        return model;
    }

    public void setModel(Mat4 model) {
        this.model = model;
    }

    public Mat4 getTranslateMatrix() {
        return new Mat4Transl(this.getTranslateX(), this.getTranslateY(), this.getTranslateZ());
    }

    public double getTranslateX() {
        return translateX;
    }

    public Solid setTranslateX(double translateX) {
        this.translateX = translateX;
        return this;
    }

    public double getTranslateY() {
        return translateY;
    }

    public double getTranslateZ() {
        return translateZ;
    }

    public Solid setTranslateZ(double translateZ) {
        this.translateZ = translateZ;
        return this;
    }
    public double getRotateX() {
        return rotateX;
    }

    public void setRotateX(double rotateX) {
        this.rotateX = rotateX;
    }

    public double getRotateY() {
        return rotateY;
    }

    public void setRotateY(double rotateY) {
        this.rotateY = rotateY;
    }

    public double getRotateZ() {
        return rotateZ;
    }

    public void setRotateZ(double rotateZ) {
        this.rotateZ = rotateZ;
    }

    public Mat4 getRotateMatrixX() {
        return new Mat4RotX(this.rotateX);
    }

    public Mat4 getRotateMatrixY() {
        return new Mat4RotY(this.rotateY);
    }

    public Mat4 getRotateMatrixZ() {
        return new Mat4RotZ(this.rotateZ);
    }

    public Shader getShader() {
        return shader;
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }
}
