package shader;

import solid.Vertex;
import transforms.Col;
import transforms.Vec2D;

import java.awt.image.BufferedImage;

public class ShaderTexture implements Shader{

    private final BufferedImage texture;

    public ShaderTexture(BufferedImage texture) {
        this.texture = texture;
    }
    @Override
    public Col getColor(Vertex v) {
        Vec2D uv = v.getUv().mul(1 / v.getOne());

        int x = (int) (uv.getX() * texture.getWidth());
        int y = (int) (uv.getY() * texture.getHeight());

        return new Col(texture.getRGB(x, y));
    }
}
