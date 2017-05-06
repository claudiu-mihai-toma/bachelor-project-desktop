
public class QRBeaconAction implements BeaconActionInterface
{

	private QRFrame mQRFrame;

	public QRBeaconAction(QRFrame qrFrame)
	{
		mQRFrame = qrFrame;
	}

	@Override
	public void actionSuccess(String address)
	{
		mQRFrame.setTitle("phone address: " + address);
		mQRFrame.displayInCorner();
	}

	@Override
	public void actionFailure()
	{
		mQRFrame.hideFrame();
	}

}
