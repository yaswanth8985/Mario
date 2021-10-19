package Components;

import jade.Component;
import org.joml.Vector4f;

public class SpritRenderer extends Component {

    private Vector4f colour;
    public SpritRenderer(Vector4f colour){
        this.colour=colour;
    }
    @Override
    public void start(){

    }
    @Override
    public void update(float dt) {

    }
    public Vector4f getColour(){
        return this.colour;
    }
}
