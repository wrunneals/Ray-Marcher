/* TODO:
	- rayMarch and shadowMarch share the same code, break into function
	- phong shading model (mostly to add ambient light)
*/

package renderer;

import java.awt.Color;
import java.awt.image.BufferedImage;

import sceneobjects.*;
import maths.*;

public class Renderer{
	
	public static final double EPSILON = 0.001;
	public static final double screenGamma = 2.2;

	private boolean warnedMaxDistance = false;

	private int resx;
	private int resy;

	// Scene objects
	private Vector3 lightPos;
	private Sphere s1;
	private Sphere s2;
	Mandelbox boxy;
	SierpinskiTetrahedron st;
	
	public Renderer(int resx, int resy){
		this.resx = resx;
		this.resy = resy;
		lightPos = new Vector3(-10, 0, 10);
		s1 = new Sphere(new Vector3(0, 0, 0), new Color(200, 10, 10), 5.0);
		s2 = new Sphere(new Vector3(-4.5, 5, 5), new Color(10, 10, 200), 1.0);
		boxy = new Mandelbox();
		st = new SierpinskiTetrahedron();
	}

	// Rough structure for a scene based DE that finds the closest object (might make this it's own class later...)
	private double sceneDE(Vector3 point){
		// return s1.DE(point);
		//return Math.min(s1.DE(point), s2.DE(point));

		
		return boxy.DE(point);
	}

	// Helps get the specific object that was hit, may be move with the above function later...
	private Hit getHitObject(Vector3 point){
		/*
		Sphere s = s1;
		if(s2.DE(point) < s1.DE(point)){
			s = s2;
		}
		return new Hit(point, s.getNormal(point), s.getColor());
		*/
		return new Hit(point, boxy.getNormal(point), boxy.getColor());
	}

	private Color addColor(Color c1, Color c2){
		double r1 = (double) c1.getRed() / 255.0;
		double g1 = (double) c1.getGreen() / 255.0;
		double b1 = (double) c1.getBlue() / 255.0;
		double r2 = (double) c2.getRed() / 255.0;
		double g2 = (double) c2.getGreen() / 255.0;
		double b2 = (double) c2.getBlue() / 255.0;
		double red = r1 + r2;
		double green = g1 + g2;
		double blue = b1 + b2;
		if(red > 1.0){
			red = 1.0;
		}
		if(green > 1.0){
			green = 1.0;
		}
		if(blue > 1.0){
			blue = 1.0;
		}
		return new Color((float)red, (float)green, (float)blue);
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
		if(red > 255){
			red = 255;
		}
		if(green > 255){
			green = 255;
		}
		if(blue > 255){
			blue = 255;
		}
		return new Color(red, green, blue);
	}

	private Color multiplyColor(Color c1, Color c2){
		double r1 = (double) c1.getRed() / 255.0;
		double g1 = (double) c1.getGreen() / 255.0;
		double b1 = (double) c1.getBlue() / 255.0;
		double r2 = (double) c2.getRed() / 255.0;
		double g2 = (double) c2.getGreen() / 255.0;
		double b2 = (double) c2.getBlue() / 255.0;
		double red = r1 * r2;
		double green = g1 * g2;
		double blue = b1 * b2;
		return new Color((float)red, (float)green, (float)blue);
	}

	private Color gammaCorrection(Color c){
		double red = (double) c.getRed() / 255.0;
		double green = (double) c.getGreen() / 255.0;
		double blue = (double) c.getBlue() / 255.0;
		red = Math.pow(red, 1.0 / screenGamma);
		green = Math.pow(green, 1.0 / screenGamma);
		blue = Math.pow(blue, 1.0 / screenGamma);
		return new Color((float) red, (float) green, (float) blue);
	}

	/** Calculates the color for the point. Currently this is done by just taking the dot product of the surface normal and the light direction and scaling the surface color.
	 * Though this will later handle shadows and phong shading.
	 * @param p The surface point to calculate the lighting for.
	 * @param norm The surface normal at the point.
	 * @param c The surface color at the point.
	 * @return The color of the surface point with lighting.
	 */
	private Color calculateColor(Hit h, double shadowVal, double ambientOcclusion){
		/*
		Vector3 p = h.getPoint();
		Vector3 norm = h.getNormal();
		Color c = h.getColor();
		Vector3 lightDir = h.getPoint().subtract(lightPos).normalize().scale(-1);
		double val = norm.dot(lightDir) * shadowVal;
		if(val < 0){
			val = 0;
		}
		return scaleColor(c, val);
		*/
		//Bing-Phong Shading Model
		Color lightColor = new Color(1f, 1f, 1f);
		Color surfaceSpecColor = new Color(1f, 1f, 1f); // this should be apart of a surface material class later...
		double lightPower = 40.0;
		Vector3 norm = h.getNormal();
		Vector3 lightDir = lightPos.subtract(h.getPoint());
		double distance = h.getPoint().subtract(lightPos).magnitude();
		distance *= distance;
		lightDir = lightDir.normalize();
		double lambertian = Math.max(lightDir.dot(norm), 0.0);
		double shininess = 16.0;
		double specular = 0.0;
		if(lambertian > 0.0){
			Vector3 veiwDir = h.getPoint().normalize();
			Vector3 halfDir = lightDir.add(veiwDir).normalize();
			double theta = Math.max(halfDir.dot(norm), 0.0);
			specular = Math.pow(theta, shininess);
		}
		Color ambientColor = scaleColor(h.getColor(), 0.1);
		Color diffuseColor = multiplyColor(scaleColor(h.getColor(), lambertian * 0.8), scaleColor(lightColor, lightPower / distance));
		Color specularColor = multiplyColor(scaleColor(surfaceSpecColor, specular), scaleColor(lightColor, lightPower / distance));
		Color linearColor = scaleColor(addColor(diffuseColor, specularColor), shadowVal);
		linearColor = addColor(scaleColor(ambientColor, shadowVal), linearColor); 
		//try{
		//	linearColor = addColor(scaleColor(ambientColor, Math.pow(ambientOcclusion, 10)), linearColor);
		//}
		//catch(IllegalArgumentException e){
		//	System.out.println(ambientOcclusion);
		//}
		return gammaCorrection(linearColor);
	}

	/** Marches a ray based on a distance function. If a colision occurs it will return the color of the surface that was hit, otherwise it will return the background color.
	 * @param r The ray to march, will be advanced by the min distance from the distance field each iteration.
	 * @return Will return the color of the surface if a surface if hit, otherwise the background color will be returned.
	 */
	private Color rayMarch(Ray r){
		double ambientOcclusion = 1.0;
		int maxSteps = 255;
		double maxDistance = 10;
		int steps = 0;
		double t = 0;
		while(steps < maxSteps){
			double distance = sceneDE(r.getPoint(t));
			if(distance < EPSILON){
				// Hit
				Hit h = getHitObject(r.getPoint(t));
				Vector3 norm = h.getNormal();
				double shadowVal = shadowMarch(r.getPoint(t), norm);
				double occlusion = 1.0 - (double)steps/(double)maxSteps;
				return calculateColor(h, shadowVal, occlusion);
			}
			if(distance > maxDistance){
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
	

	/** Marches a ray towards a light source to see if it's obscured. Uses the number of steps to get a idea of how obscured it was to produce soft shadows (penumbra).
	 * @param p The point of the ray intersection.
	 * @param norm The surface normal at the point p.
	 * @return Function returns 0.0 if the point is obscured by some object in the distance field. Otherwise returns 1.0 scaled by the penumbra factor.
	 */
	private double shadowMarch(Vector3 p, Vector3 norm){
		Vector3 lightDir = p.subtract(lightPos).normalize().scale(-1);
		Vector3 orig = p.add(norm.scale(EPSILON * 2.0));
		Ray r = new Ray(orig, lightDir);
		int maxSteps = 255;
		double maxDistance = 10;
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