
public class Constants
{
	public class Ports
	{
		public static final int	PICTURE_STREAM_SERVER_PORT	= 55001;
		public static final int	PICTURE_STREAM_BEACON_PORT	= 55002;
		public static final int	CONTENT_RECEIVER_PORT		= 55004;
	}

	public class Timeouts
	{
		public static final int	PHONE_IMAGE_SOCKET_TIMEOUT		= 3000;
		public static final int	DATA_TRANSFER_SOCKET_TIMEOUT	= 5000;
	}
	
	public class Delays
	{

		public static final int	PHONE_IMAGE_TIMER_DELAY			= 1;	
	}

	public class ContentTypeIDs
	{
		public static final int TEXT = 0;
		public static final int IMAGE = 1;
	}
	
	public class Charsets
	{
		public static final String UTF_8 = "UTF-8";
	}
}
