/* TODO:
	- rayMarch and shadowMarch share the same code, break into function
*/

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Renderer{
	
	public static final double EPSILON = 0.00000001;
	private boolean warnedMaxDistance = false;

	private int resx;
	private int resy;

	// Scene objects
	private Vector3 lightPos;
	private Sphere s1;
	private Sphere s2;
	
	public Renderer(int resx, int resy){
		this.resx = resx;
		this.resy = resy;
		lightPos = new Vector3(-10, 50, 50);
		s1 = new Sphere(new Vector3(0, 0, 0), new Color(200, 10, 10), 5.0);
		s2 = new Sphere(new Vector3(-4.5, 5, 5), new Color(10, 10, 200), 1.0);
	}

	private double sceneDE(Vector3 point){
		// return s1.DE(point);
		return Math.min(s1.DE(point), s2.DE(point));
	}

	private Hit getHitObject(Vector3 point){
		Sphere s = s1;
		if(s2.DE(point) < s1.DE(point)){
			s = s2;
		}
		return new Hit(point, s.getNormal(point), s.getColor());
	}

	/** Scales a color by a scalar value. Scalar value should never be less that 0 or greater that 1.
	 * @param c The base color to scale.
	 * @param val The scalar value.
	 * @return The result color.
	 */
	private Color scaleColor(Color c, double val){
		int red = c.getRed();
		int green = c.getGreen();
		int blue = c.getBlue();
		red = (int) ((double) red * val);
		green = (int) ((double) green  * val);
		blue = (int) ((double) blue * val);
		return new Color(red, green, blue);
	}

	/** Calculates the color for the point. Currently this is done by just taking the dot product of the surface normal and the light direction and scaling the surface color.
	 * Though this will later handle shadows and phong shading.
	 * @param p The surface point to calculate the lighting for.
	 * @param norm The surface normal at the point.
	 * @param c The surface color at the point.
	 * @return The color of the surface point with lighting.
	 */
	private Color calculateColor(Hit h, double shadowVal){
		Vector3 p = h.getPoint();
		Vector3 norm = h.getNormal();
		Color c = h.getColor();
		Vector3 lightDir = h.getPoint().subtract(lightPos).normalize().scale(-1);
		double val = norm.dot(lightDir) * shadowVal;
		if(val < 0){
			val = 0;
		}
		return scaleColor(c, val);
	}

	/** Marches a ray based on a distance function. If a colision occurs it will return the color of the surface that was hit, otherwise it will return the background color.
	 * @param r The ray to march, will be advanced by the min distance from the distance field each iteration.
	 * @return Will return the color of the surface if a surface if hit, otherwise the background color will be returned.
	 */
	private Color rayMarch(Ray r){
		
		int maxSteps = 1000;
		int steps = 0;
		double t = 0;
		while(steps < maxSteps){
			double distance = sceneDE(r.getPoint(t));
			if(distance < EPSILON){
				// Hit
				Hit h = getHitObject(r.getPoint(t));
				Vector3 norm = h.getNormal();
				double shadowVal = shadowMarch(r.getPoint(t), norm);
				return calculateColor(h, shadowVal);
			}
			if(distance > 100){
				// Return background color
				// ¯\_(ツ)_/¯
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
	
	private double shadowMarch(Vector3 p, Vector3 norm){
		Vector3 lightDir = p.subtract(lightPos).normalize().scale(-1);
		Vector3 orig = p.add(norm.scale(EPSILON * 2.0));
		Ray r = new Ray(orig, lightDir);
		int maxSteps = 1000;
		double maxDistance = 1000;
		int steps = 0;
		double t = 0.0;

		// Softshadows
		double res = 1.0;
		while(steps < maxSteps){
			double distance = sceneDE(r.getPoint(t));
			if(distance < EPSILON){
				return 0.0; // obscured
			}
			if(lightPos.subtract(r.getPoint(t)).magnitude() < EPSILON || t > maxDistance){
				break;
			}
			
			if(t != 0){
				res = Math.min(res, 16.0 * distance / t);
			}
			t += distance;
			
			steps ++;
		}
		return res;
	}

	/** Main function to be called from outside the class. Will return a bufferred image of the rendered scene.
	 * @return The Bufferred image of the rendered scene.
	 */
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