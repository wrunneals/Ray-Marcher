package sceneobjects;

import maths.Vector3;

public class Tetrabox extends SceneObject{

	public Tetrabox(Vector3 color){
		super(new Vector3(0, 0, 0), color);
	}

	@Override
	public double DE(Vector3 p){
		double delta = 3; // 3 for tetra, 0 for octo
		
		double ctx = p.x + p.y + p.z - delta;
		double cty = -p.x - p.y + p.z - delta;
		double ctz = -p.x + p.y - p.z - delta;
		double ctw = p.x - p.y - p.z - delta;

		double vx = 0;
		double vy = 0;
		double vz = 0;
		double vw = 0;
		double dr = 2.0;
		double radius2 = 0.0;
		for(int i = 0; i < 32; i ++){
			vx = Math.max(-1.0, Math.min(vx, 1.0)) * 2.0 - vx;
			vy = Math.max(-1.0, Math.min(vy, 1.0)) * 2.0 - vy;
			vz = Math.max(-1.0, Math.min(vz, 1.0)) * 2.0 - vz;
			vw = Math.max(-1.0, Math.min(vw, 1.0)) * 2.0 - vw;
			radius2 = vx * vx + vy * vy + vz * vz + vw * vw;
			double c = Math.max(0.25/radius2, 0.25);
			c = Math.max(0.0, Math.min(c, 1.0)) / 0.25;

			vx *= c;
			vy *= c;
			vz *= c;
			vw *= c;
			dr /= c;

			vx = vx * 2.0 + ctx;
			vy = vy * 2.0 + cty;
			vz = vz * 2.0 + ctz;
			vw = vw * 2.0 + ctw;
			dr /= 2.0;
			if(radius2 > 3600){
				break;
			}
		}
		return dr * Math.sqrt(radius2);
	}
}