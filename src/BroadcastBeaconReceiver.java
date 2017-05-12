import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class BroadcastBeaconReceiver implements Runnable
{
	private static final String		BEACON_MESSAGE	= "interactive_information_share";

	private DatagramSocket			mDatagramSocket	= null;
	private byte[]					mReceiveData	= new byte[BEACON_MESSAGE.length()];
	private int						mPort;
	private int						mTimeout;
	private BeaconActionInterface	mBeaconAction;

	public BroadcastBeaconReceiver(int port, int timeout, BeaconActionInterface beaconAction)
			throws SocketException, UnknownHostException
	{
		mPort = port;
		mTimeout = timeout;
		mDatagramSocket = new DatagramSocket(mPort);
		mDatagramSocket.setSoTimeout(mTimeout);
		mBeaconAction = beaconAction;
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
			// System.out.println("Broadcast received: " + message);

			if (message.equals(BEACON_MESSAGE))
			{
				String address = datagramPacket.getAddress().getHostAddress();
				mBeaconAction.actionSuccess(address);
				clearDataQueue();
			}
			else
			{
				mBeaconAction.actionFailure();
			}

		}
		catch (IOException e)
		{
			mBeaconAction.actionFailure();
		}
	}

	// Reopen the connection in order to free the queued data.
	private void clearDataQueue() throws SocketException
	{
		mDatagramSocket.close();
		mDatagramSocket = new DatagramSocket(mPort);
		mDatagramSocket.setSoTimeout(mTimeout);
	}
}
