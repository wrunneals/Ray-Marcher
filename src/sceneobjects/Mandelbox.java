package sceneobjects;

import maths.Vector3;

public class Mandelbox extends SceneObject{
	
	public Mandelbox(Vector3 color){
		super(new Vector3(0, 0, 0), color);
	}

	@Override
	public double DE(Vector3 p){

		double x = p.x;
		double y = p.y;
		double z = p.z;

		double scale = 2.0;
		double fixedRadius = 1.0;
		double minRadius = 0.5;
		double fr2 = fixedRadius * fixedRadius;
		double mr2 = minRadius * minRadius;

		int i = 0;
		double dr = 1.0;
		while(i < 32){

			//boxfold
			if(x > 1.0){
				x = 2.0 - x;
			}
			else if(x < -1.0){
				x = -2.0 - x;
			}

			if(y > 1.0){
				y = 2.0 - y;
			}
			else if(y < -1.0){
				y = -2.0 - y;
			}

			if(z > 1.0){
				z = 2.0 - z;
			}
			else if(z < -1.0){
				z = -2.0 - z;
			}

			//spherefold
			double r2 = x * x + y * y + z * z;
			double r = Math.sqrt(r2);

			if(r < mr2){
				x = x * fr2 / mr2;
				y = y * fr2 / mr2;
				z = z * fr2 / mr2;
				dr = dr * fr2 / mr2;
			}
			else if(r2 < fr2){
				x = x * fr2 / r2;
				y = y * fr2 / r2;
				z = z * fr2 / r2;
				dr = dr * fr2 / r2;
			}	

			//translate and scale
			x = x * scale + p.x;
			y = y * scale + p.y;
			z = z * scale + p.z;
			dr = dr * Math.abs(scale) + 1.0;

			i ++;
		}
		return Math.sqrt(x * x + y * y + z * z) / Math.abs(dr);
	}

}