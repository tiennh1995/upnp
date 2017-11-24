package is_team.upnp;

import java.util.HashMap;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.model.message.header.STAllHeader;
import org.fourthline.cling.model.meta.*;
import org.fourthline.cling.registry.*;

// Control point
public class UserSensorClient implements Runnable {
	public HashMap<String, RemoteDevice> remoteDevices = new HashMap<String, RemoteDevice>();

	public static void main(String[] args) throws Exception {
		// Start a user thread that runs the UPnP stack
		Thread clientThread = new Thread(new UserSensorClient());
		clientThread.setDaemon(false);
		clientThread.start();
	}

	public void run() {
		try {
			UserSensorClient userSensorClient = new UserSensorClient();
			UpnpService upnpService = new UpnpServiceImpl();

			// Add a listener for device registration events
			upnpService.getRegistry().addListener(userSensorClient.createRegistryListener(upnpService));
			upnpService.getControlPoint().search(new STAllHeader());
		} catch (Exception ex) {
			System.err.println("Exception occured: " + ex);
			System.exit(1);
		}
	}

	RegistryListener createRegistryListener(final UpnpService upnpService) {
		return new DefaultRegistryListener() {
			@Override
			public void remoteDeviceAdded(Registry registry, RemoteDevice remoteDevice) {
				addRemoteDevice(remoteDevice);
			}

			@Override
			public void remoteDeviceRemoved(Registry registry, RemoteDevice remoteDevice) {
				removeRemoteDevice(remoteDevice);
			}
		};
	}

	void addRemoteDevice(RemoteDevice remoteDevice) {
		System.out.println(remoteDevice.getType().getDisplayString());
		remoteDevices.put(remoteDevice.getType().getDisplayString(), remoteDevice);
	}

	void removeRemoteDevice(RemoteDevice remoteDevice) {
		System.out.println(remoteDevice.getType().getDisplayString());
		remoteDevices.remove(remoteDevice.getType().getDisplayString());
	}

	// Action setTargetAction = service.getAction("SetTarget");
	// ActionInvocation setTargetInvocation = new ActionInvocation(setTargetAction);
	// setTargetInvocation.setInput("NewTargetValue", true);
	// ActionCallback setTargetCallback = new ActionCallback(setTargetInvocation) {
	// @Override
	// public void success(ActionInvocation invocation) {
	// }
	//
	// @Override
	// public void failure(ActionInvocation invocation, UpnpResponse operation,
	// String defaultMsg) {
	// System.err.println(defaultMsg);
	// }
	// };
	//
	// upnpService.getControlPoint().execute(setTargetCallback);

	// Action getStatusAction = service.getAction("GetStatus");
	// ActionInvocation getStatusInvocation = new ActionInvocation(getStatusAction);
	// ActionCallback getStatusCallback = new ActionCallback(getStatusInvocation) {
	// @Override
	// public void success(ActionInvocation invocation) {
	// ActionArgumentValue status = invocation.getOutput("ResultStatus");
	// System.out.println(status.getValue().toString());
	// }
	//
	// @Override
	// public void failure(ActionInvocation invocation,
	// UpnpResponse operation,
	// String defaultMsg) {
	// System.err.println(defaultMsg);
	// }
	// };
	// upnpService.getControlPoint().execute(getStatusCallback);

}