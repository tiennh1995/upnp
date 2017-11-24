package is_team.upnp;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.model.meta.*;
import is_team.upnp.Device;

public class UserSensorServer implements Runnable {

	public static void main(String[] args) throws Exception {
		// Start a user thread that runs the UPnP stack
		Thread serverThread = new Thread(new UserSensorServer());
		serverThread.setDaemon(false);
		serverThread.start();
	}

	public void run() {
		try {
			final UpnpService upnpService = new UpnpServiceImpl();

			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					upnpService.shutdown();
				}
			});

			Device device = new Device("User Sensor", "UserSensor", "User Sensor", "user_sensor_icon.png", SwitchPower.class.getName());
			LocalDevice localDevice = device.getDevice();
			upnpService.getRegistry().addDevice(localDevice);
		} catch (Exception ex) {
			System.err.println("Exception occured: " + ex);
			ex.printStackTrace(System.err);
			System.exit(1);
		}
	}
}
