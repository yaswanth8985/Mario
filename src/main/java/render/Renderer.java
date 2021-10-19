package render;

import Components.SpritRenderer;
import jade.GameObject;

import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private final int MAX_BATCH_SIZE=1000;
    private List<RenderBatch> batches;
    public Renderer(){
        this.batches=new ArrayList<>();

    }
    public void add(GameObject go){
        SpritRenderer spr=go.getComponent(SpritRenderer.class);
        if(spr!=null){
            add(spr);
        }
    }
    private void add(SpritRenderer sprite){
        boolean added=false;
        for(RenderBatch batch:batches){
            if(batch.hasRoom()){
                batch.addSprites(sprite);
                added=true;
                break;
            }
        }
        if(!added){
            RenderBatch newBatch =new RenderBatch(MAX_BATCH_SIZE);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprites(sprite);
        }
    }
    public void render(){
        for(RenderBatch batch:batches){
            batch.render();
        }
    }
}
