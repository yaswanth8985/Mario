package jade;

import render.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    protected Renderer renderer=new Renderer();
    protected Camera camera;
    private  boolean ifRunning=false;
    protected List<GameObject> gameObjects=new ArrayList<>();
    public Scene(){

    }
    public void init(){

    }
    public void addGameObjectToScene(GameObject go){
        if(!ifRunning){
            gameObjects.add(go);
        }else {
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
        }
    }
    public void start(){
        for(GameObject go:gameObjects){
            go.start();
            this.renderer.add(go);
        }
        ifRunning=true;
    }
    public abstract void update(float dt);
    public Camera camera(){
        return this.camera;
    }
}
