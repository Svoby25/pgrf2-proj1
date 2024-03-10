package shader;

import solid.Vertex;
import transforms.Col;

public interface Shader {
    public Col getColor(Vertex v);
}
