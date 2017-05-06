import java.io.IOException;
import java.net.ServerSocket;

public abstract class AbstractServerReceiver<T>
{
	private static final int	SOCKET_TIMEOUT			= 500;

	protected ServerSocket		mServerSocket;

	public AbstractServerReceiver(int port) throws IOException
	{
		mServerSocket = new ServerSocket(port);
		mServerSocket.setSoTimeout(SOCKET_TIMEOUT);
	}
	
	public abstract T receive();

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
