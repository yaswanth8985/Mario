package render;

import Components.SpritRenderer;
import jade.Window;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;


public class RenderBatch {
    //vertex
    //=================
    //Pos              Colour
    //float,float,     float,float,float,float
    private final int POS_SIZE=2;
    private final int COLOUR_SIZE=4;
    private final int POS_OFFSET=0;
    private final  int COLOUR_OFFSET=POS_OFFSET+POS_SIZE *Float.BYTES;
    private final int VERTEX_SIZE=6;
    private final int VERTEX_SIZE_BYTES=VERTEX_SIZE*Float.BYTES;

    private SpritRenderer[] sprites;
    private int numSprites;
    private boolean hasRoom;
    private float[] vertices;

    private int vaoID,vboID;
    private int maxBatchSize;
    private Shader shader;

    public RenderBatch(int maxBatchSize){
        shader=new Shader("assests/Shaders/default.glsl");
        shader.compile();
        this.sprites=new SpritRenderer[maxBatchSize];
        this.maxBatchSize=maxBatchSize;

        //4 vertices quads
        vertices=new float[maxBatchSize*4*VERTEX_SIZE];

        this.numSprites=0;
        this.hasRoom=true;
    }
    public void start(){
        //Generate and bind a Vertex Array Object
        vaoID=glGenVertexArrays();
        glBindVertexArray(vaoID);

        //Allocate space for the vertices
        vboID=glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,vboID);
        glBufferData(GL_ARRAY_BUFFER,vertices.length*Float.BYTES,GL_DYNAMIC_DRAW);

        //Create and upload indices buffer
        int eboID=glGenBuffers();
        int [] indices=generateIndecies();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,eboID);
        glBufferData(GL_ARRAY_BUFFER,indices,GL_STATIC_DRAW);

        //Enable buffer attribute pointers
        glVertexAttribPointer(0,POS_SIZE,GL_FLOAT,false,VERTEX_SIZE_BYTES,POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1,COLOUR_SIZE,GL_FLOAT,false,VERTEX_SIZE_BYTES,COLOUR_OFFSET);
        glEnableVertexAttribArray(1);

    }
    public void addSprites(SpritRenderer spr){
        //Get index and add renderObject
        int index=this.numSprites;
        this.sprites[index]=spr;
        this.numSprites++;

        //Add properties to local vertices array
        loadVertexProperties(index);

        if(numSprites>=this.maxBatchSize){
            this.hasRoom=false;
        }
    }
    public void render(){
        //For now, we will rebuffer all data every frame
        glBindBuffer(GL_ARRAY_BUFFER,vboID);
        glBufferSubData(GL_ARRAY_BUFFER,0,vertices);

        //Use shader
        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().camera().getProjectionMatrix());
        shader.uploadMat4f("uView",Window.getScene().camera().getViewMatrix());

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES,this.numSprites*6,GL_UNSIGNED_INT,0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shader.detach();

    }
    public void loadVertexProperties(int index){
        SpritRenderer sprite=this.sprites[index];

        //Find offset within array (4 vertices per sprites)
        int offset=4*index*VERTEX_SIZE;
        Vector4f colour=sprite.getColour();

        //Add the vertices with appropriate properties
        //*    *
        //*    *
        float xAdd=1.0f;
        float yAdd=1.0f;
        for(int  i=0;i<4;i++){
            if(i==1){
                yAdd=0.0f;
            }else if(i==2){
                xAdd=0.0f;
            }else if (i==3){
                xAdd=1.0f;
            }
            //Load position first
            vertices[offset]=sprite.gameObject.transform.position.x+(xAdd*sprite.gameObject.transform.scale.x);
            vertices[offset+1]=sprite.gameObject.transform.position.y+(yAdd*sprite.gameObject.transform.scale.y);

            //Load colour
            vertices[offset+2]=colour.x;
            vertices[offset+3]=colour.y;
            vertices[offset+4]=colour.z;
            vertices[offset+5]=colour.w;

            offset+=VERTEX_SIZE;
        }
    }
    private int[] generateIndecies(){
        //0 indices per quad (3 per triangle)
        int[] elements=new int[6*maxBatchSize];
        for(int i=0;i<maxBatchSize;i++){
            loadElementsIndicies(elements,i);
        }
        return elements;
    }
    private void loadElementsIndicies(int [] elements,int index){
        int offsetArrayIndex =6* index;
        int offset=4*index;
        //3,2,0,0,2,1          7,6,4,4,6,5
        //Triangle 1
        elements[offsetArrayIndex]=offset+3;
        elements[offsetArrayIndex+1]=offset+2;
        elements[offsetArrayIndex+2]=offset+0;

        //Triangle 2
        elements[offsetArrayIndex+3]=offset+0;
        elements[offsetArrayIndex+4]=offset+2;
        elements[offsetArrayIndex+5]=offset+1;

    }
    public boolean hasRoom(){
        return  this.hasRoom;
    }
}
