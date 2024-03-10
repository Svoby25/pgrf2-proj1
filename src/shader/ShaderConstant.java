package shader;

import solid.Vertex;
import transforms.Col;

public class ShaderConstant implements Shader{
    private Col color;

    public ShaderConstant(Col col)
    {
     this.color = col;
    }
    @Override
    public Col getColor(Vertex v) {
        return this.color;
    }

    public void setColor(Col color) {
        this.color = color;
    }
}
