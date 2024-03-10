package control;

import raster.*;
import render.Renderer;
import shader.Shader;
import shader.ShaderConstant;
import shader.ShaderInterpolated;
import shader.ShaderTexture;
import solid.*;
import transforms.*;
import view.Panel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Controller3D implements Controller {
    private final Panel panel;

    private TriangleRasterizer triangleRasterizer;

    private LineRasterizer lineRasterizer;

    private Renderer renderer;

    private Camera camera;

    private Mat4 projection;

    private ArrayList<Solid> objects = new ArrayList<>();

    private Solid pyramid, arrow, cube, axisX, axisY, axisZ;

    private int currentObject, mClicked, selectedView = 0;
    private int startingX, startingY;

    private boolean objectSelected;

    private BufferedImage image;

    public Controller3D(Panel panel) {
        this.panel = panel;
        initObjects(panel.getRaster());

        initListeners();
        redraw();
    }

    public void initObjects(Raster<Col> raster) {
        raster.setDefaultValue(new Col(0x000000));
        ZBuffer buffer = new ZBuffer(panel.getRaster());

        try {
            image = ImageIO.read(new File("textures/wall.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.triangleRasterizer = new TriangleRasterizer(buffer);
        this.lineRasterizer = new FilledLineRasterizer(buffer);

        this.projection = new Mat4PerspRH(Math.PI / 4, 600 / 800., 0, 1);

        this.camera = new Camera(
                new Vec3D(0, -3, 0),
                Math.toRadians(90),
                Math.toRadians(0),
                1,
                true
        );

        this.renderer = new Renderer(this.triangleRasterizer,  this.lineRasterizer, this.camera.getViewMatrix(), this.projection);

        this.pyramid = new Pyramid();

        this.pyramid.setTranslateX(pyramid.getTranslateX() + 1);
        this.pyramid.setModel(
                        this.pyramid.getModel().mul(this.pyramid.getScaleMatrix())
                        .mul(this.pyramid.getRotateMatrixX())
                        .mul(this.pyramid.getRotateMatrixY())
                        .mul(this.pyramid.getRotateMatrixZ())
        );

        this.pyramid.setShader(new ShaderConstant(new Col(0xff0000)));

        this.arrow = new Arrow();

        this.arrow.setModel(
                this.arrow.getTranslateMatrix()
                        .mul(this.pyramid.getScaleMatrix())
         .mul(this.pyramid.getRotateMatrixX())
                 .mul(this.pyramid.getRotateMatrixY())
                  .mul(this.pyramid.getRotateMatrixZ())
        );

        this.arrow.setShader(new ShaderTexture(image));

        this.cube = new Cube();

       this.cube.setModel(
             this.cube.getTranslateMatrix()
                       .mul(this.pyramid.getScaleMatrix())
                        .mul(this.pyramid.getRotateMatrixX())
      .mul(this.pyramid.getRotateMatrixY())
                   .mul(this.pyramid.getRotateMatrixZ())
  );
        this.cube.setShader(new ShaderInterpolated());

       this.objects.add(this.pyramid);

       this.axisX = new Axis('x', new Vertex(new Point3D(1,0, 0.1), new Col(0x0000ff), new Vec2D(0, 0)));

        this.axisX.setModel(
                this.axisX.getTranslateMatrix()
                        .mul(new Mat4Scale(0.5, 0.5, 0.5))
                     .mul(this.axisX.getRotateMatrixX())
                     .mul(this.axisX.getRotateMatrixY())
                   .mul(this.axisX.getRotateMatrixZ())
        );

        this.axisY = new Axis('y', new Vertex(new Point3D(1,0, 0.1), new Col(0xff0000), new Vec2D(0, 0)));

        this.axisX.setModel(
                this.axisY.getTranslateMatrix()
                        .mul(new Mat4Scale(0.5, 0.5, 0.5))
                        .mul(this.axisY.getRotateMatrixX())
                        .mul(this.axisY.getRotateMatrixY())
                        .mul(this.axisY.getRotateMatrixZ())
        );

        this.axisZ = new Axis('z', new Vertex(new Point3D(1,0, 0.1), new Col(0x00ff00), new Vec2D(0, 0)));

        this.axisZ.setModel(
                this.axisZ.getTranslateMatrix()
                        .mul(new Mat4Scale(0.5, 0.5, 0.5))
                      .mul(this.axisZ.getRotateMatrixX())
                       .mul(this.axisZ.getRotateMatrixY())
                       .mul(this.axisZ.getRotateMatrixZ())
        );

    }

    @Override
    public void initListeners() {
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel.resize();
                initObjects(panel.getRaster());
            }
        });

        panel.requestFocus();
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_A -> {
                        camera = camera.right(0.1);
                    }
                    case KeyEvent.VK_D -> {
                        camera = camera.left(0.1);
                    }
                    case KeyEvent.VK_W -> {
                        camera = camera.forward(0.1);
                    }
                    case KeyEvent.VK_S -> {
                        camera = camera.backward(0.1);
                    }
                    case KeyEvent.VK_M -> {
                        objectSelected = true;
                        if (currentObject >= objects.size() - 1) {
                            currentObject = 0;
                            mClicked = 0;
                        }

                        if (mClicked != 0) {
                            currentObject++;
                        }

                        mClicked++;
                    }

                    case KeyEvent.VK_N -> {
                        objectSelected = true;
                        if (currentObject >= objects.size()) {
                            currentObject = 0;
                            mClicked = 0;
                        }
                    }

                    case KeyEvent.VK_Z -> {
                        if (objectSelected) {
                            Solid object = objects.get(currentObject);
                            object.setScale(object.getScale() + 0.1);
                            setModelForObject(object);
                        }
                    }

                    case KeyEvent.VK_U -> {
                        if (objectSelected) {
                            Solid object = objects.get(currentObject);
                            object.setScale(object.getScale() - 0.1);
                            setModelForObject(object);
                        }
                    }

                    case KeyEvent.VK_LEFT -> {
                        if (objectSelected) {
                            Solid solid = objects.get(currentObject);
                            solid.setTranslateX(solid.getTranslateX() - 0.1);
                            setModelForObject(solid);
                        }
                    }
                    case KeyEvent.VK_RIGHT -> {
                        if (objectSelected) {
                            Solid solid = objects.get(currentObject);
                            solid.setTranslateX(solid.getTranslateX() + 0.1);
                            setModelForObject(solid);
                        }
                    }

                    case KeyEvent.VK_UP -> {
                        if (objectSelected) {
                            Solid solid = objects.get(currentObject);
                            solid.setTranslateZ(solid.getTranslateZ() + 0.1);
                            setModelForObject(solid);
                        }
                    }


                    case KeyEvent.VK_DOWN -> {
                        if (objectSelected) {
                            Solid solid = objects.get(currentObject);
                            solid.setTranslateZ(solid.getTranslateZ() - 0.1);
                            setModelForObject(solid);

                        }
                    }

                    case KeyEvent.VK_B -> {
                        if (selectedView == 0) {
                            selectedView = 1;
                            projection = new Mat4OrthoRH(Math.PI / 4, 600 / 800., 0, 1);
                        } else {
                            selectedView = 0;
                            projection = new Mat4PerspRH(Math.PI / 4, 600 / 800., 0, 1);
                        }
                    }

                    case KeyEvent.VK_O -> {
                        if (objectSelected) {
                            Solid solid = objects.get(currentObject);
                            solid.setRotateX(solid.getRotateX() + 0.1);
                            setModelForObject(solid);
                        }
                    }
                    case KeyEvent.VK_P -> {
                        if (objectSelected) {
                            Solid solid = objects.get(currentObject);
                            solid.setRotateY(solid.getRotateY() + 0.1);
                            setModelForObject(solid);
                        }
                    }

                    case KeyEvent.VK_L -> {
                        if (objectSelected) {
                            Solid solid = objects.get(currentObject);
                            solid.setRotateZ(solid.getRotateZ() + 0.1);
                            setModelForObject(solid);
                        }
                    }

                }

                redraw();
            }
        });

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startingX = e.getX();
                startingY = e.getY();
            }
        });
        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                int dx = startingX - x;
                double azimuth = dx / 1000.;

                int dy = startingY - y;
                double zenith = dy / 1000.;

                camera = camera.addAzimuth(azimuth);

                startingX = x;

                if (zenith <= 90 && zenith >= -90) {
                    camera = camera.addZenith(zenith);
                    startingY = y;
                }

                redraw();
            }
        });

    }

    private void redraw() {
        panel.clear();

        this.renderer.setView(this.camera.getViewMatrix());
        this.renderer.setProj(this.projection);

        this.renderer.render(this.axisX);

        this.renderer.render(this.axisY);

        this.renderer.render(this.axisZ);

        for (Solid object: this.objects) {
            this.renderer.render(object);
        }

        panel.repaint();
    }

    private void setModelForObject(Solid object) {
        object.setModel(
                object.getTranslateMatrix()
                        .mul(object.getScaleMatrix())
                        .mul(object.getRotateMatrixX())
                        .mul(object.getRotateMatrixY())
                        .mul(object.getRotateMatrixZ())
        );

    }
}
