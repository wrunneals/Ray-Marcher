public class Ray{
	
	private Vector3 origin;
	private Vector3 direction;
	
	public Ray(Vector3 origin, Vector3 direction){
		this.origin = origin;
		this.direction = direction;
	}

	public Vector3 getPoint(double t){
		return origin.add(direction.scale(t));
	}

	@Override
	public String toString(){
		return "Origin: " + origin.toString() + ", Direction: " + direction.toString();
	}
}