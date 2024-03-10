package shader;

import solid.Vertex;
import transforms.Col;

public class ShaderInterpolated implements Shader{
    @Override
    public Col getColor(Vertex v) {
        return v.getColor().mul(1 / v.getOne());
    }
}
