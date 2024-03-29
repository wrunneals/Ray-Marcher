package sceneobjects;

import maths.Vector3;
import renderer.Renderer;

public class SceneObject{
	
	protected Vector3 position;
	protected Vector3 surfaceColor;

	public SceneObject(Vector3 position, Vector3 surfaceColor){
		this.position = position;
		this.surfaceColor = surfaceColor;
	}

	public double DE(Vector3 position){
		return 0; //This was not meant to be used	
	}

	public Vector3 getColor(){
		return surfaceColor;
	}

	public Vector3 getNormal(Vector3 p){
		double dx = DE(p.add(new Vector3(Renderer.EPSILON, 0, 0))) - DE(p.add(new Vector3(-Renderer.EPSILON, 0, 0)));
		double dy = DE(p.add(new Vector3(0, Renderer.EPSILON, 0))) - DE(p.add(new Vector3(0, -Renderer.EPSILON, 0)));
		double dz = DE(p.add(new Vector3(0, 0, Renderer.EPSILON))) - DE(p.add(new Vector3(0, 0, -Renderer.EPSILON)));
		return new Vector3(dx, dy, dz).normalize();
	}
}