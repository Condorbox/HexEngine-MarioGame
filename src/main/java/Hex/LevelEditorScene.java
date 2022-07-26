package Hex;

import Components.FontRenderer;
import Components.SpriteRenderer;
import Renderer.Shader;
import Renderer.Texture;
import Util.Time;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class LevelEditorScene extends Scene{
    private String vertexShaderSrc = "    #version 330 core\n" +
            "    layout (location=0) in vec3 aPos;\n" +
            "    layout (location=1) in vec4 aColor;\n" +
            "\n" +
            "    out vec4 fColor;\n" +
            "\n" +
            "    void main(){\n" +
            "        fColor = aColor;\n" +
            "        gl_Position = vec4(aPos, 1.0);\n" +
            "    }";
    private String fragmentShaderSrc = "    #version 330 core\n" +
            "\n" +
            "    in vec4 fColor;\n" +
            "\n" +
            "    out vec4 color;\n" +
            "\n" +
            "    void main(){\n" +
            "        color = fColor;\n" +
            "    }";

    private int vertexID, fragmentID, shaderProgram;

    private float[] vertexArray = {
            //Position               //Color                    //UV Coordinates
             100f, 0f,    0.0f,      1.0f, 0.0f, 0.0f, 1.0f,    1, 1, //Bottom right
             0f,   100f,  0.0f,      0.0f, 1.0f, 0.0f, 1.0f,    0, 0, //Top Left
             100f, 100f,  0.0f,      0.0f, 0.0f, 1.0f, 1.0f,    1, 0, //Top right
             0f,   0f,    0.0f,      1.0f, 1.0f, 0.0f, 1.0f,    0, 1, //Bottom left
    };

    //Must be in counter-clockwise order
    private int[] elementArray = {
            2, 1, 0, //Top right triangle
            0, 1, 3, //Bottom left triangle
    };

    private int vaoID, vboID, eboID;
    private Shader defaultShader;
    private Texture testTexture;

    private GameObject testGameObject;
    private boolean firstTime = false;

    public LevelEditorScene(){

    }

    @Override
    public void init(){
        System.out.println("Creating GameObject ...");
        testGameObject = new GameObject("test Game Object");
        testGameObject.addComponent(new SpriteRenderer());
        addGameObjectToScene(testGameObject);

        camera = new Camera(new Vector2f());
        defaultShader = new Shader("Assets/Shaders/default.glsl");
        defaultShader.compile();
        testTexture = new Texture("Assets/Sprites/testImage.png");

        //Generate VAO, VBO, EBO Buffer object and send to GPU
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        //Create VBO upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        //Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        //Add the vertex attribute pointers: 0 Position 1 Color
        int positionSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeBytes = (positionSize + colorSize + uvSize) * Float.BYTES;

        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void update(float deltaTime) {
        camera.position.x -= deltaTime * 50.0f;
        camera.position.y -= deltaTime * 20.0f;

        defaultShader.use();

        //Upload texture to shader
        defaultShader.uploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        testTexture.bind();

        defaultShader.uploadMath4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMath4f("uView", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());
        //Bind current VAO
        glBindVertexArray(vaoID);

        //Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        //Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        defaultShader.detach();

        if(!firstTime){
            System.out.println("Creating GameObject 2 !");
            GameObject testGameObject2 = new GameObject("Test game object 2");
            testGameObject2.addComponent(new SpriteRenderer());
            testGameObject2.addComponent(new FontRenderer());
            addGameObjectToScene(testGameObject2);
            firstTime = true;
        }

        for (GameObject gameObject : gameObjects){
            gameObject.update(deltaTime);
        }
    }
}
