import java.awt.Color;

public class Hit{

	private Vector3 point;
	private Vector3 normal;
	private Color surfaceColor;

	public Hit(Vector3 point, Vector3 normal, Color surfaceColor){
		this.point = point;
		this.normal = normal;
		this.surfaceColor = surfaceColor;
	}

	public Vector3 getPoint(){
		return point;
	}

	public Vector3 getNormal(){
		return normal;
	}

	public Color getColor(){
		return surfaceColor;
	}
}