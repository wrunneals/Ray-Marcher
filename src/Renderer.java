import java.awt.Color;
import java.awt.image.BufferedImage;

public class Renderer{
	
	private final double EPSILON = 0.00000001;
	private boolean warnedMaxDistance = false;

	private int resx;
	private int resy;
	
	public Renderer(int resx, int resy){
		this.resx = resx;
		this.resy = resy;
	}

	private Color scaleColor(Color c, double val){
		int red = c.getRed();
		int green = c.getGreen();
		int blue = c.getBlue();
		red = (int) ((double) red * val);
		green = (int) ((double) green  * val);
		blue = (int) ((double) blue * val);
		return new Color(red, green, blue);
	}

	private Color calculateColor(Vector3 p, Vector3 norm, Color c){
		Vector3 lightPos = new Vector3(-10, 50, 50);
		Vector3 lightDir =p.subtract(lightPos).normalize().scale(-1);
		double val = norm.dot(lightDir);
		if(val < 0){
			val = 0;
		}
		return scaleColor(c, val);
	}

	private Color rayMarch(Ray r){
		Sphere s1 = new Sphere(new Vector3(0, 0, 0), new Color(200, 10, 10), 5);
		int maxSteps = 1000;
		int steps = 0;
		double t = 0;
		while(steps < maxSteps){
			double distance = s1.DE(r.getPoint(t));
			if(distance < EPSILON){
				Vector3 norm = s1.getNormal(r.getPoint(t), EPSILON);
				return calculateColor(r.getPoint(t), norm, s1.getColor());
			}
			if(distance > 100){
				return new Color(0, 0, 0);
			}
			t += distance;
			steps ++;
		}
		if(!warnedMaxDistance){
			System.out.println("WARNING: At least one ray march was terminated after reaching max steps...");
			warnedMaxDistance = true;
		}
		return new Color(0, 0, 0);
	}
	
	public BufferedImage renderScene(){
		Camera view = new Camera(resx, resy);
		BufferedImage ouputImage = new BufferedImage(resx, resy, BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < resx; x ++){
			for(int y = 0; y < resy; y ++){
				Vector3 dir = view.pixelToScreenCoord(x, y);
				Ray r = new Ray(view.getPos(), dir);
				Color c = rayMarch(r);
				ouputImage.setRGB(x, y, c.getRGB());
			}
		}
		return ouputImage;
	}
}