import java.awt.Color;

public class SceneObject{
	
	protected Vector3 position;
	protected Color surfaceColor;

	public SceneObject(Vector3 position, Color surfaceColor){
		this.position = position;
		this.surfaceColor = surfaceColor;
	}

	public double DE(Vector3 position){
		return 0;	
	}

	public Color surfaceColor(){
		return surfaceColor;
	}
}