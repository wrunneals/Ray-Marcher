package renderer;

import maths.Vector3;

public class Hit{

	private Vector3 point;
	private Vector3 normal;
	private Vector3 surfaceColor;

	public Hit(Vector3 point, Vector3 normal, Vector3 surfaceColor){
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

	public Vector3 getColor(){
		return surfaceColor;
	}
}