package Hex;

import Renderer.Shader;
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
            //Position              //Color
             50.5f, -50.5f,  0.0f,      1.0f, 0.0f, 0.0f, 1.0f, //Bottom right
            -50.5f,  50.5f,  0.0f,      0.0f, 1.0f, 0.0f, 1.0f, //Top Left
             50.5f,  50.5f,  0.0f,      0.0f, 0.0f, 1.0f, 1.0f, //Top right
            -50.5f, -50.5f,  0.0f,      1.0f, 1.0f, 0.0f, 1.0f, //Bottom left
    };

    //Must be in counter-clockwise order
    private int[] elementArray = {
            2, 1, 0, //Top right triangle
            0, 1, 3, //Bottom left triangle
    };

    private int vaoID, vboID, eboID;
    private Shader defaultShader;

    public LevelEditorScene(){

    }

    @Override
    public void init(){
        camera = new Camera(new Vector2f());
        defaultShader = new Shader("Assets/Shaders/default.glsl");
        defaultShader.compile();

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
        int floatSizeByte = 4;
        int vertexSizeBytes = (positionSize + colorSize) * floatSizeByte;

        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * floatSizeByte);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float deltaTime) {
        camera.position.x -= deltaTime * 50.0f;

        defaultShader.use();
        defaultShader.uploadMath4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMath4f("uView", camera.getViewMatrix());
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
    }
}
