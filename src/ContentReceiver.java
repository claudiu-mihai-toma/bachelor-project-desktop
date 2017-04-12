import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ContentReceiver
{
	private static final int	CONTENT_RECEIVER_PORT	= 9753;
	private static final int	RECEIVE_DATA_BUFFER_LEN	= 1024;
	private static final int	SOCKET_TIMEOUT			= 500;

	private DatagramSocket		datagramSocket			= null;
	byte[]						receiveData				= new byte[RECEIVE_DATA_BUFFER_LEN];

	public ContentReceiver(String localAddress) throws SocketException, UnknownHostException
	{
		datagramSocket = new DatagramSocket(CONTENT_RECEIVER_PORT, InetAddress.getByName(localAddress));
		datagramSocket.setSoTimeout(SOCKET_TIMEOUT);
	}

	public String getContent()
	{
		try
		{
			DatagramPacket datagramPacket = new DatagramPacket(receiveData, receiveData.length);
			datagramSocket.receive(datagramPacket);

			return new String(datagramPacket.getData()).substring(0, datagramPacket.getLength());

		}
		catch (SocketException e)
		{
			// e.printStackTrace();
		}
		catch (IOException e)
		{
			// e.printStackTrace();
		}
		return null;
	}

	public void close()
	{
		datagramSocket.close();
	}
}
