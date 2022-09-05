import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import renderer.Renderer;

public class RayMarcher{
	
	public static void main(String[] args){
		Renderer r = new Renderer();
		BufferedImage outputImage = r.renderScene();
		File outputFile = new File("output.png");
		
		try{
			ImageIO.write(outputImage, "png", outputFile);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}