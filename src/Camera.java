public class Camera{
	
	private int resx;
	private int resy;
	
	private Vector3 forward;
	private Vector3 up;
	private Vector3 left;
	
	private double aspectRatio;
	private double viewHeight = 2.0;
	private double viewWidth;
	
	private Vector3 position;
	
	public Camera(int resx, int resy){
		this.resx = resx;
		this.resy = resy;
		aspectRatio = (double) resx / (double) resy;
		viewWidth = viewHeight * aspectRatio;
		
		forward = Vector3.I;
		left = Vector3.J;
		up = Vector3.K;
		
		position = new Vector3(-13, 0, 0);
	}
	
	public Vector3 pixelToScreenCoord(int x, int y){
		Vector3 horizon = left.scale(viewWidth / 2.0).add(left.scale(-viewWidth * (double) x / (double) resx));
		Vector3 vert = up.scale(viewHeight / 2.0).add(up.scale(-viewHeight * (double) y / (double) resy));
		return forward.add(horizon).add(vert).normalize();
	}
	
	private void setPosition(Vector3 newPos){
		position = newPos;
	}

	public Vector3 getPos(){
		return position;
	}
}