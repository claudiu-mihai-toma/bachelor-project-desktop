import java.io.IOException;
import java.net.ServerSocket;

public abstract class AbstractTransferServer<T>
{
	private static final int	SOCKET_TIMEOUT	= 10;

	protected ServerSocket		mServerSocket;

	public AbstractTransferServer(int port) throws IOException
	{
		mServerSocket = new ServerSocket(port);
		mServerSocket.setSoTimeout(SOCKET_TIMEOUT);
	}

	public abstract boolean send(T data);

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
