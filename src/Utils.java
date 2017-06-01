import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Utils
{
	public static String getFileName(String path)
	{
		return path.substring(path.lastIndexOf("/") + 1);
	}

	public static byte[] toByteArray(BufferedImage bufferedImage)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try
		{
			ImageIO.write(bufferedImage, "png", baos);
		}
		catch (IOException e)
		{
			// e.printStackTrace();
		}

		try
		{
			baos.close();
		}
		catch (IOException e)
		{
			// e.printStackTrace();
		}
		return baos.toByteArray();
	}
}
