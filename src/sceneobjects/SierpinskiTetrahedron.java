package sceneobjects;

import java.awt.Color;
import maths.Vector3;


public class SierpinskiTetrahedron extends SceneObject{
	

	public SierpinskiTetrahedron(){
		super(new Vector3(0, 0, 0), new Color(250, 10, 10));
	}

	@Override
	public double DE(Vector3 p){
		double scale = 2.0;
		Vector3 a1 = new Vector3(1, 1, 1);
		Vector3 a2 = new Vector3(-1, -1, 1);
		Vector3 a3 = new Vector3(1, -1, -1);
		Vector3 a4 = new Vector3(-1, 1, -1);
		Vector3 c = new Vector3();

		int n = 0;
		double distance = 0;
		double d = 0;

		while(n < 10){
			c = a1;
			distance = p.subtract(a1).magnitude();
			d = p.subtract(a2).magnitude();
			if(d < distance){
				c = a2;
				distance = d;
			}
			d = p.subtract(a3).magnitude();
			if(d < distance){
				c = a3;
				distance = d;
			}
			d = p.subtract(a4).magnitude();
			if(d < distance){
				c = a4;
				distance = d;
			}
			p = p.scale(scale).subtract(c.scale(scale - 1.0));
			n ++;
		}

		return p.magnitude() * Math.pow(scale, (double)-n);
	}
}