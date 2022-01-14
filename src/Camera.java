/*
	TODO
	- pixel to point projection maths ... (need paper and pen)
	- rotation (Quaterion?)
	- aspect ratio
*/

public class Camera{
	
	private int resx;
	private int resy;
	
	private Vector3 forward;
	private Vector3 up;
	private Vector3 left;
	
	private double screenWidth = 1.0;
	private double screenHeight = 1.0;
	
	private Vector3 position;
	
	public Camera(int resx, int resy){
		this.resx = resx;
		this.resy = resy;
		
		forward = Vector3.I;
		left = Vector3.J;
		up = Vector3.K;
		
		position = new Vector3(-10, 0, 0);
	}
	
	//This function does nothing
	private Vector3 pixelToScreenCoord(int x, int y){
		return new Vector3();
	}
	
	private void setPosition(Vector3 newPos){
		position = newPos;
	}
}