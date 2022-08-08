package Components;

import Hex.GameObject;
import Physics2D.Components.Rigidbody2D;
import Util.AssetPool;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;

public class Flower extends Component {
    private transient Rigidbody2D rb;

    @Override
    public void start() {
        this.rb = gameObject.getComponent(Rigidbody2D.class);
        AssetPool.getSound("Assets/Sounds/powerup_appears.ogg").play();
        this.rb.setIsSensor();
    }

    @Override
    public void beginCollision(GameObject obj, Contact contact, Vector2f contactNormal) {
        PlayerController playerController = obj.getComponent(PlayerController.class);
        if (playerController != null) {
            playerController.powerup();
            this.gameObject.destroy();
        }
    }
}
