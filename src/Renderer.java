import java.awt.Color;
import java.awt.image.BufferedImage;

/*

for every pixel in the image space
	- covert from pixel space to 3D space using a pinhole projection (camera object, pixelToPoint function)
	- march ray forward along direction towards DE object (Ray Object, SceneObject with DE function, camera integration)
	- when surface is hit calculate surface normal used for color calculation (either with a known normal calculation (shape) or using a surface gradient calculation(fractal))
	- a whole bunch of lighting stuff I can't remember...
	- return pixel color
*/

public class Renderer{
	
	private int resx;
	private int resy;
	
	public Renderer(int resx, int resy){
		this.resx = resx;
		this.resy = resy;
	}
	
	public BufferedImage renderScene(){
		BufferedImage ouputImage = new BufferedImage(resx, resy, BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < resx; x ++){
			for(int y = 0; y < resy; y ++){
				ouputImage.setRGB(x, y, new Color(255, 0, 0).getRGB());
			}
		}
		return ouputImage;
	}
	
}