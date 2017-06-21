import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
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

	public static BufferedImage getBufferedImage(String path)
	{
		File file = new File(path);
		BufferedImage bufferedImage = null;
		try
		{
			bufferedImage = ImageIO.read(file);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bufferedImage;
	}
}
