package Components;

public class FontRenderer extends Component{
    @Override
    public void start(){
        if (gameObject.getComponent(FontRenderer.class) != null){
            System.out.println("Font Renderer found");
        }
    }
    @Override
    public void update(float deltaTime) {

    }
}
