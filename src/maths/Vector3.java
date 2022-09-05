package maths;

import java.awt.Color;

public class Vector3{
	
	//Directional definitions
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

	public Vector3 multiply(Vector3 v){
		return new Vector3(x * v.x, y * v.y, z * v.z);
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
	
	public Vector3 pow(double power){
		return new Vector3(Math.pow(x, power), Math.pow(y, power), Math.pow(z, power));
	}

	public static Vector3 reflect(Vector3 p, Vector3 n){
		double scalarR = p.dot(n) * 2.0;
		Vector3 r = p.subtract(n.scale(scalarR));
		return r;
	}

	public Vector3 scale(double c){
		return new Vector3(x * c, y * c, z * c);
	}
	
	public Vector3 subtract(Vector3 v){
		return new Vector3(x - v.x, y - v.y, z - v.z);
	}
	
	public Color toColor(){
		double red = Math.max(0.0, Math.min(x, 1.0));
		double green = Math.max(0.0, Math.min(y, 1.0));
		double blue = Math.max(0.0, Math.min(z, 1.0));
		return new Color((float)red, (float)green, (float)blue);
	}

	@Override
	public String toString(){
		return "<" + x + ", " + y + ", " + z + ">";
	}
	
	public static void main(String[] args){
		Vector3 point = new Vector3(1, 1, 0);
		Vector3 normal = new Vector3(1, 0, 0).normalize();
		System.out.println(Vector3.reflect(point, normal).toString());
	}
}