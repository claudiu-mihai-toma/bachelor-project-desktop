import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;

public class ContentReceiver
{
	private static final int	CONTENT_RECEIVER_PORT	= 9753;
	private static final int	SOCKET_TIMEOUT			= 500;

	private ServerSocket		mServerSocket			= null;

	public ContentReceiver() throws IOException
	{
		mServerSocket = new ServerSocket(CONTENT_RECEIVER_PORT);
		mServerSocket.setSoTimeout(SOCKET_TIMEOUT);
	}

	public String getStringContent()
	{
		try
		{
			Socket socket = mServerSocket.accept();
			DataInputStream is = new DataInputStream(socket.getInputStream());

			String receivedData = is.readUTF();

			return receivedData;

		}
		catch (IOException e)
		{
			// e.printStackTrace();
		}
		return null;
	}

	public BufferedImage getImageContent()
	{
		try
		{
			Socket socket = mServerSocket.accept();
			DataInputStream is = new DataInputStream(socket.getInputStream());

			int imageSize = is.readInt();
			System.out.println("Image size = " + imageSize);
			byte[] imageBytes = new byte[imageSize];
			is.readFully(imageBytes);
			System.out.println("Image fully received.");

			/*FileOutputStream fos = new FileOutputStream("android.bmp");
			fos.write(imageBytes);
			fos.close();*/
			
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
			mServerSocket.close();
		}
		catch (IOException e)
		{
			// e.printStackTrace();
		}
	}
}
