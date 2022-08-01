package Components;

import Editor.PropertiesWindow;
import Hex.GameObject;
import Hex.Prefabs;
import Hex.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class TranslateGizmo extends Component {
    private Vector4f xAxisColor = new Vector4f(1, 0, 0, 1);
    private Vector4f xAxisColorHover = new Vector4f();
    private Vector4f yAxisColor = new Vector4f(0, 1, 0, 1);
    private Vector4f yAxisColorHover = new Vector4f();

    private GameObject xAxisObject;
    private GameObject yAxisObject;
    private SpriteRenderer xAxisSprite;
    private SpriteRenderer yAxisSprite;
    private GameObject activeGameObject = null;

    private Vector2f xAxisOffset = new Vector2f(64, -5);
    private Vector2f yAxisOffset = new Vector2f(16, 61);

    private PropertiesWindow propertiesWindow;

    public TranslateGizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow) {
        this.xAxisObject = Prefabs.generateSpriteObject(arrowSprite, 16, 48);
        this.yAxisObject = Prefabs.generateSpriteObject(arrowSprite, 16, 48);
        this.xAxisSprite = this.xAxisObject.getComponent(SpriteRenderer.class);
        this.yAxisSprite = this.yAxisObject.getComponent(SpriteRenderer.class);
        this.propertiesWindow = propertiesWindow;

        Window.getScene().addGameObjectToScene(this.xAxisObject);
        Window.getScene().addGameObjectToScene(this.yAxisObject);
    }

    @Override
    public void start() {
        xAxisObject.transform.rotation = 90;
        yAxisObject.transform.rotation = 180;
        xAxisObject.setNoSerialize();
        yAxisObject.setNoSerialize();
        xAxisSprite.setZIndex(Integer.MAX_VALUE);
        yAxisSprite.setZIndex(Integer.MAX_VALUE);
    }

    @Override
    public void update(float dt) {
        if (activeGameObject != null) {
            xAxisObject.transform.position.set(activeGameObject.transform.position);
            yAxisObject.transform.position.set(activeGameObject.transform.position);
            xAxisObject.transform.position.add(xAxisOffset);
            yAxisObject.transform.position.add(yAxisOffset);
        }

        this.activeGameObject = this.propertiesWindow.getActiveGameObject();
        if (activeGameObject != null) {
            setActive();
        } else {
            setInactive();
        }
    }

    private void setActive() {
        xAxisSprite.setColor(xAxisColor);
        yAxisSprite.setColor(yAxisColor);
    }

    private void setInactive() {
        activeGameObject = null;
        xAxisSprite.setColor(new Vector4f(0, 0, 0, 0));
        yAxisSprite.setColor(new Vector4f(0, 0, 0, 0));
    }
}
