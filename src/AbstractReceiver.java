import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public abstract class AbstractReceiver<T>
{
	protected Socket mSocket;
	
	public AbstractReceiver(String address, int port) throws UnknownHostException, IOException
	{
		mSocket = new Socket(address, port);
	}

	public abstract T receive();
	
	public void close()
	{
		try
		{
			mSocket.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
