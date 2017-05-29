import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public abstract class AbstractReceiver<T>
{
	protected Socket mSocket;
	
	public AbstractReceiver(String address, int port) throws UnknownHostException, IOException
	{
		System.out.println("New Socket!");
		mSocket = new Socket(address, port);
		mSocket.setSoTimeout(Constants.Timeouts.DATA_TRANSFER_SOCKET_TIMEOUT);
	}

	public abstract T receive();
	
	public void close()
	{
		try
		{
			mSocket.close();
			System.out.println("Socket closed.");
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
