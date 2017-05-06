import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BufferedImageReceiver extends AbstractReceiver<BufferedImage>
{
	private DataInputStream mInputStream = new DataInputStream(mSocket.getInputStream());
	
	public BufferedImageReceiver(String address, int port) throws IOException
	{
		super(address, port);
		mInputStream = new DataInputStream(mSocket.getInputStream());
	}

	@Override
	public BufferedImage receive()
	{
		try
		{
			int imageSize = mInputStream.readInt();
			System.out.println("Image size = " + imageSize);
			byte[] imageBytes = new byte[imageSize];
			mInputStream.readFully(imageBytes);
			System.out.println("Image fully received.");

			/*
			 * FileOutputStream fos = new FileOutputStream("android.bmp");
			 * fos.write(imageBytes); fos.close();
			 */

			BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));

			if (image != null)
			{
				System.out.println("Image returned.");
			}
			else
			{
				System.out.println("Cannot read image!");
			}

			return image;

		}
		catch (IOException e)
		{
			// e.printStackTrace();
		}
		return null;
	}

	public void close()
	{
		try
		{
			mInputStream.close();
		}
		catch (IOException e)
		{
			//e.printStackTrace();
		}
		super.close();
	}
}
