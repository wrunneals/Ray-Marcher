/* TODO:
	- super samples
	- in direct lighting (bounces)
	- clean and comment
*/

package renderer;

import java.awt.Color;
import java.awt.image.BufferedImage;

import sceneobjects.*;
import maths.*;

public class Renderer{
	
	// Config
	public static final double EPSILON = 0.001;
	public static final double screenGamma = 2.2;
	private final int RESX = 1920;
	private final int RESY = 1080;

	// Scene objects
	private Vector3 lightPos = new Vector3(-10, 0, 10);
	Mandelbox boxy = new Mandelbox(new Vector3(1.0, 0, 0));
	
	public Renderer(){}// nothing

	// Rough structure for a scene based DE that finds the closest object (might make this it's own class later...)
	private double sceneDE(Vector3 point){
		return boxy.DE(point);
	}

	// Helps get the specific object that was hit, may be move with the above function later...
	private Hit getHitObject(Vector3 point){
		return new Hit(point, boxy.getNormal(point), boxy.getColor());
	}

	/** Calculates the color for the point. Currently this is done by just taking the dot product of the surface normal and the light direction and scaling the surface color.
	 * Though this will later handle shadows and phong shading.
	 * @param p The surface point to calculate the lighting for.
	 * @param norm The surface normal at the point.
	 * @param c The surface color at the point.
	 * @return The color of the surface point with lighting.
	 */
	private Color calculateColor(Hit h, double shadowVal, double ambientOcclusion){
		//Blinn-Phong Shading Model
		Vector3 lightColor = new Vector3(1, 1, 1);
		Vector3 surfaceSpecColor = new Vector3(1, 1, 1); // this should be apart of a surface material class later...
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
		Vector3 ambientColor = h.getColor().scale(0.1);
		Vector3 diffuseColor = h.getColor().scale(lambertian * 0.5).multiply(lightColor.scale(lightPower / distance));
		Vector3 specularColor = surfaceSpecColor.scale(specular).multiply(lightColor.scale(lightPower / distance));
		Vector3 linearColor = ambientColor.add(diffuseColor).add(specularColor).scale(shadowVal);
		return linearColor.pow(1.0 / screenGamma).toColor();
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
		Camera view = new Camera(RESX, RESY);
		BufferedImage ouputImage = new BufferedImage(RESX, RESY, BufferedImage.TYPE_INT_RGB);
		for(int x = 0; x < RESX; x ++){
			for(int y = 0; y < RESY; y ++){
				Vector3 dir = view.pixelToScreenCoord(x, y);
				Ray r = new Ray(view.getPos(), dir);
				Color c = rayMarch(r);
				ouputImage.setRGB(x, y, c.getRGB());
			}
		}
		return ouputImage;
	}
}