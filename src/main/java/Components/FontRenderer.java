package Components;

import Renderer.Font.FontBatch;
import Renderer.Font.HFont;
import Renderer.Shader;
import Util.AssetPool;
import org.joml.Vector2f;

import static org.lwjgl.opengl.GL11C.glBindTexture;
import static org.lwjgl.opengl.GL13C.*;
import static org.lwjgl.opengl.GL31C.GL_TEXTURE_BUFFER;

public class FontRenderer extends Component {
    private transient FontBatch batch;
    private String text = "Hello";

    @Override
    public void start(){
        batch = new FontBatch();
        batch.shader = AssetPool.getShader("Assets/Shaders/fontShader.glsl");
        batch.font = AssetPool.addFont("Assets/Fonts/segoeui.ttf", 64);
        batch.initBatch();
    }

    @Override
    public void editorUpdate(float deltaTime) {
        batch.addText(text, 200, 200, 100f, 0x0000FF);
        batch.flushBatch();
    }
}
