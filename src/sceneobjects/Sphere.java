package sceneobjects;

import java.awt.Color;
import maths.Vector3;

public class Sphere extends SceneObject{
	
	private double radius;

	public Sphere(Vector3 position, Color surfaceColor, double radius){
		super(position, surfaceColor);
		this.radius = radius;
	}

	@Override
	public double DE(Vector3 p){
		double pointDist =  Math.sqrt(Math.pow(p.x - position.x, 2) + Math.pow(p.y - position.y, 2) + Math.pow(p.z - position.z, 2));
		return pointDist - radius;
	}
}