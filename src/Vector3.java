public class Vector3{
	
	public static final Vector3 I = new Vector3(1, 0, 0);
	public static final Vector3 J = new Vector3(0, 1, 0);
	public static final Vector3 K = new Vector3(0, 0, 1);
	
	public double x;
	public double y;
	public double z;
	
	public Vector3(){
		x = 0;
		y = 0;
		z = 0;
	}
	
	public Vector3(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3 add(Vector3 v){
		return new Vector3(x + v.x, y + v.y, z + v.z);
	}
	
	public double dot(Vector3 v){
		return x * v.x + y * v.y + z * v.z;
	}

	@Override
	public boolean equals(Object o){
		if (! (o instanceof Vector3)){
			return false;
		}
		
		Vector3 v = (Vector3) o;
		return x == v.x && y == v.y && z == v.z;
	}
	
	public double magnitude(){
		return Math.sqrt(x * x + y * y + z * z);
	}
	
	public Vector3 normalize(){
		return scale(1.0 / magnitude());
	}
	
	public Vector3 scale(double c){
		return new Vector3(x * c, y * c, z * c);
	}
	
	public Vector3 subtract(Vector3 v){
		return new Vector3(x - v.x, y - v.y, z - v.z);
	}
	
	@Override
	public String toString(){
		return "<" + x + ", " + y + ", " + z + ">";
	}
	
	public static void main(String[] args){
		Vector3 v1 = new Vector3(0, 0, 1);
		Vector3 v2 = new Vector3(1, 0, 0);
		Vector3 v3 = new Vector3(3.2, 3.2, 0);
		
		System.out.println(v1.add(v1).toString());
		System.out.println(v2.add(v2).toString());
		System.out.println(v3.toString());
		System.out.println(v1.equals(v2));
	}
}