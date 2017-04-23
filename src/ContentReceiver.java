import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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

	public String getContent()
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
