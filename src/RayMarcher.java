import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class RayMarcher{
	
	private static final int RESX = 1920;
	private static final int RESY = 1080;
	
	public static void main(String[] args){
		Renderer r = new Renderer(RESX, RESY);
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