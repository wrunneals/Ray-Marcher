import java.awt.Color;
import java.awt.image.BufferedImage;

public class Renderer{
	
	Sphere s1 = new Sphere(new Vector3(0, 0, 0), new Color(200, 10, 10), 5);

	private final double EPSILON = 0.00000001;
	boolean warnedMaxDistance = false;

	private int resx;
	private int resy;
	
	public Renderer(int resx, int resy){
		this.resx = resx;
		this.resy = resy;
	}

	private double DE(Vector3 point){
		return point.magnitude() - 5.0; //sphere with radius 5 at the origin
	}

	private Vector3 getGradient(Vector3 p, SceneObject s){
		double dx = s.DE(p.add(new Vector3(EPSILON, 0, 0))) - s.DE(p.add(new Vector3(-EPSILON, 0, 0)));
		double dy = s.DE(p.add(new Vector3(0, EPSILON, 0))) - s.DE(p.add(new Vector3(0, -EPSILON, 0)));
		double dz = s.DE(p.add(new Vector3(0, 0, EPSILON))) - s.DE(p.add(new Vector3(0, 0, -EPSILON)));
		return new Vector3(dx, dy, dz).normalize();
	}

	private Color rayMarch(Ray r){
		Vector3 lightPos = new Vector3(-10, 50, 50);
		int maxSteps = 1000;
		int steps = 0;
		double t = 0;
		while(steps < maxSteps){
			double distance = s1.DE(r.getPoint(t));
			if(distance < EPSILON){
				Vector3 norm = getGradient(r.getPoint(t), s1);
				Vector3 lightDir = r.getPoint(t).subtract(lightPos).normalize().scale(-1);
				double val = norm.dot(lightDir);
				if(val < 0){
					val = 0;
				}
				int red = s1.surfaceColor().getRed();
				int green = s1.surfaceColor().getGreen();
				int blue = s1.surfaceColor().getBlue();
				red = (int) ((double) red * val);
				green = (int) ((double) green  * val);
				blue = (int) ((double) blue * val);
				return new Color(red, green, blue);
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