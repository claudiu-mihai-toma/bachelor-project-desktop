import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.TimerTask;

public class BroadcastBeaconReceiver extends TimerTask
{
	private static final int	BEACON_RECEIVER_PORT	= 9751;
	private static final String	BEACON_MESSAGE			= "interactive_information_share";
	public static final int		SOCKET_TIMEOUT			= 500;

	private DatagramSocket		mDatagramSocket			= null;
	private byte[]				mReceiveData			= new byte[BEACON_MESSAGE.length()];
	private QRFrame				mQRFrame				= null;

	public BroadcastBeaconReceiver(String localAddress, QRFrame qrframe) throws SocketException, UnknownHostException
	{
		mDatagramSocket = new DatagramSocket(BEACON_RECEIVER_PORT);
		mDatagramSocket.setSoTimeout(SOCKET_TIMEOUT);
		mQRFrame = qrframe;
	}

	public void close()
	{
		mDatagramSocket.close();
	}

	@Override
	public void run()
	{
		try
		{
			DatagramPacket datagramPacket = new DatagramPacket(mReceiveData, mReceiveData.length);
			mDatagramSocket.receive(datagramPacket);

			String message = new String(datagramPacket.getData()).substring(0, datagramPacket.getLength());
			System.out.println("Broadcast received: " + message);

			if (message.equals(BEACON_MESSAGE))
			{
				mQRFrame.displayInCorner();
				clearDataQueue();
			}
			else
			{
				mQRFrame.setVisible(false);
			}
		}
		catch (SocketException e)
		{
			// e.printStackTrace();
			mQRFrame.setVisible(false);
		}
		catch (IOException e)
		{
			// e.printStackTrace();
			mQRFrame.setVisible(false);
		}
	}

	// Reopen the connection in order to free the queued data.
	private void clearDataQueue() throws SocketException
	{
		mDatagramSocket.close();
		mDatagramSocket = new DatagramSocket(BEACON_RECEIVER_PORT);
		mDatagramSocket.setSoTimeout(SOCKET_TIMEOUT);
	}
}
