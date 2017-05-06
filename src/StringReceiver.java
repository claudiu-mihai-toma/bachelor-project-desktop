import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class StringReceiver extends AbstractServerReceiver<String>
{
	public StringReceiver(int port) throws IOException
	{
		super(port);
	}

	@Override
	public String receive()
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

}
