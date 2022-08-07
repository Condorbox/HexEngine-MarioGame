package Components;

import Util.AssetPool;

public class BreakableBrick extends Block {
    @Override
    void playerHit(PlayerController playerController) {
        if (!playerController.isSmall()) {
            AssetPool.getSound("Assets/Sounds/break_block.ogg").play();
            gameObject.destroy();
        }
    }
}
