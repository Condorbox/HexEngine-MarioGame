package Components;

import Renderer.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer extends Component{
    private Vector4f color;
    private Sprite sprite;

    private Transform lastTransform;
    private boolean isDirty = false;

    public SpriteRenderer(Vector4f color){
        this.color = color;
        this.sprite = new Sprite(null);
    }
    public SpriteRenderer(Sprite sprite){
        this.sprite = sprite;
        this.color = new Vector4f(1, 1, 1, 1);
    }
    @Override
    public void start(){
        this.lastTransform = gameObject.transform.copy();
    }
    @Override
    public void update(float deltaTime) {
        if(!lastTransform.equals(gameObject.transform)){
            gameObject.transform.copy(lastTransform);
            isDirty = true;
        }
    }

    public Vector4f getColor() {
        return color;
    }

    public Texture getTexture() {
        return sprite.getTexture();
    }

    public Vector2f[] getTexCoords() {
        return sprite.getTexCoords();
    }

    public void setSprite(Sprite sprite){
        this.sprite = sprite;
        isDirty = true;
    }

    public void setColor(Vector4f color){
        if(!color.equals(this.color)){
            this.color = color;
            isDirty = true;
        }
    }

    public boolean isDirty(){
        return isDirty;
    }

    public void setClean(){
        isDirty = false;
    }
}
