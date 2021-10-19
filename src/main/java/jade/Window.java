package jade;

import jade.Utill.Time;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width,height;
    private String title;
    private long glfwWindow;
    public float r,g,b,a;
    private boolean fadeToBlack=false;
    private static Window window= null;
    private static Scene currentScene;



    private Window (){
        this.width = 1920;
        this.height = 1080;
        this.title = "Mario";

        r = 0;
        b = 0;
        g = 0;
        a = 1;

    }
    public static void changeScene(int newScene){
        switch (newScene){
            case 0:
                currentScene=new LevelEditorScene();
                currentScene.init();
                currentScene.start();
                break;
            case 1:
                currentScene=new LevelScene();
                currentScene.init();
                currentScene.start();
                break;
            default:
                assert false:"Unknown scene '" +newScene+"'";
                break;
        }
    }
    //Singleton
    public static Window get(){
        if(Window.window==null){
            Window.window=new Window();
        }
        return Window.window;
    }
    public static  Scene getScene(){
        return get().currentScene;
    }
    public void run(){
        System.out.println("Hello LWJGL "+ Version.getVersion()+"!");
        init();
        loop();
        //Free memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //Terminate GLFW window and free the errorcallback
        glfwTerminate();
        glfwSetErrorCallback(null).free();

    }

    public void init(){
        //Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();//Print where error are
        //Initialize GLFW
        if(!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        //Configure GLFW
        glfwDefaultWindowHints();//resets windows

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE,GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED,GLFW_TRUE);

        //Create the Window

        glfwWindow=glfwCreateWindow(this.width,this.height,this.title,NULL,NULL);
        if(glfwWindow==NULL){
            throw new IllegalStateException("Failed to create the GLFW Window");
        }
        glfwSetCursorPosCallback(glfwWindow,MouseListener::mousePosCallBack);
        glfwSetMouseButtonCallback(glfwWindow,MouseListener::mouseBottonCallBack);
        glfwSetScrollCallback(glfwWindow,MouseListener::mouseScrollCallBack);
        glfwSetKeyCallback(glfwWindow,KeyListener::keyCallBack);

        //make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        //Enable v-sync
        glfwSwapInterval(1);

        //Make the window visible
        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
        //calling scene
        Window.changeScene(0);
    }

    public void loop(){
        float beginTime= Time.getTime();
        float endTime;
        float dt=-1.0f;

        while (!glfwWindowShouldClose(glfwWindow)){
            //Poll Events
            glfwPollEvents();
            glClearColor(r,g,b,a);
            glClear(GL_COLOR_BUFFER_BIT);
            if(dt>=0) {
                currentScene.update(dt);
            }

            endTime=Time.getTime();
            dt=endTime-beginTime;
            beginTime=endTime;
            glfwSwapBuffers(glfwWindow);

        }

    }
}
