package jade;

import static org.lwjgl.glfw.GLFW.*;

public class MouseListener {
    private static MouseListener instance;
    private double scrollX,scrollY;
    private double XPos,YPos,lastX,lastY;
    private boolean mouseButtonPressed[] =new boolean[3];
    private boolean isDragging;

    private MouseListener(){
        this.scrollX=0.0;
        this.scrollY=0.0;
        this.XPos=0.0;
        this.YPos=0.0;
        this.lastX=0.0;
        this.lastY=0.0;

    }
    public static MouseListener get(){
        if(MouseListener.instance == null){
            MouseListener.instance=new MouseListener();
        }
        return instance;
    }
    public static void mousePosCallBack(long window,double Xpos,double Ypos){
        get().lastX= get().XPos;
        get().lastY= get().YPos;
        get().XPos=Xpos;
        get().YPos=Ypos;
        get().isDragging=get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];

    }
    public static void mouseBottonCallBack(long window,int button,int action,int mods){
        if(action== GLFW_PRESS){
            if(button<get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = true;
            }
        }else if(action ==GLFW_RELEASE){
            get().mouseButtonPressed[button]=false;
            get().isDragging =false;
        }
    }
    public static void mouseScrollCallBack(long window,double xOffset,double yOffset){
        get().scrollX=xOffset;
        get().scrollY=yOffset;

    }
    public static void endFrame(){
        get().scrollX=0;
        get().scrollY=0;
        get().lastX= get().XPos;
        get().lastY=get().YPos;

    }
    public static float getX(){
        return (float) get().XPos;
    }
    public static float getY(){
        return (float) get().YPos;
    }
    public static float getDx(){
        return (float) (get().lastX-get().XPos);
    }
    public static float getDy(){
        return (float) (get().lastY-get().YPos);
    }
    public static float getScrollX(){
        return (float) (get().scrollX);
    }
    public static float getScrollY(){
        return (float) (get().scrollY);
    }

    public static boolean isDragging(){
        return get().isDragging;
    }
    public static boolean mouseButtonDown(int button){
        if(button<get().mouseButtonPressed.length){
            return get().mouseButtonPressed[button];
        }
        else{
            return false;
        }
    }



}
