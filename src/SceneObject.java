import java.awt.Color;

public class SceneObject{
	
	protected Vector3 position;
	protected Color surfaceColor;

	public SceneObject(Vector3 position, Color surfaceColor){
		this.position = position;
		this.surfaceColor = surfaceColor;
	}

	public double DE(Vector3 position){
		return 0; //This was not meant to be used	
	}

	public Color getColor(){
		return surfaceColor;
	}

	public Vector3 getNormal(Vector3 p, double EPSILON){
		double dx = DE(p.add(new Vector3(EPSILON, 0, 0))) - DE(p.add(new Vector3(-EPSILON, 0, 0)));
		double dy = DE(p.add(new Vector3(0, EPSILON, 0))) - DE(p.add(new Vector3(0, -EPSILON, 0)));
		double dz = DE(p.add(new Vector3(0, 0, EPSILON))) - DE(p.add(new Vector3(0, 0, -EPSILON)));
		return new Vector3(dx, dy, dz).normalize();
	}
}