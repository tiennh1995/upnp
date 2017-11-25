package is_team.device;

import java.util.HashMap;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.message.header.STAllHeader;
import org.fourthline.cling.model.meta.Action;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.types.UDAServiceId;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.registry.RegistryListener;
import org.fourthline.cling.controlpoint.ActionCallback;

public class TemperatureSensorClient implements Runnable {
  public HashMap<String, RemoteDevice> remoteDevices = new HashMap<String, RemoteDevice>();
  UpnpService upnpService = new UpnpServiceImpl();

  public static void main(String[] args) throws Exception {
    // Start a user thread that runs the UPnP stack
    Thread clientThread = new Thread(new TemperatureSensorClient());
    clientThread.setDaemon(false);
    clientThread.start();
  }

  public void run() {
    try {
      TemperatureSensorClient temperatureSensorClient = new TemperatureSensorClient();

      // Add a listener for device registration events
      upnpService.getRegistry()
        .addListener(temperatureSensorClient.createRegistryListener(upnpService));
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

    if (remoteDevice.getType().getDisplayString().equals("TemperatureSensor")) {
      callService("TemperatureSensor", "ChangeTemperature", "SetTemperature", "setCallback", 20);
      callService("TemperatureSensor", "ChangeTemperature", "GetTemperature", "getCallback", 0);
    }
  }

  void removeRemoteDevice(RemoteDevice remoteDevice) {
    System.out.println(remoteDevice.getType().getDisplayString());
    remoteDevices.remove(remoteDevice.getType().getDisplayString());
  }

  public void callService(String serviceType, String UDAServiceId, String action, String function,
    int value) {
    try {
      RemoteDevice remoteDevice = remoteDevices.get(serviceType);
      if (remoteDevice != null) {
        RemoteService remoteService = remoteDevice.findService(new UDAServiceId(UDAServiceId));
        if (remoteService != null) {
          Action<RemoteService> actionService = remoteService.getAction(action);
          ActionInvocation<RemoteService> actionInvocation = new ActionInvocation<RemoteService>(
            actionService);
          ActionCallback actionCallback = null;
          if (function.equals("setCallback")) {
            actionInvocation.setInput("NewTemperature", value);
            actionCallback = setCallback(actionInvocation);
          } else {
            actionCallback = getCallback(actionInvocation);
          }

          upnpService.getControlPoint().execute(actionCallback);
        }
      }
    } catch (Exception e) {
      System.out.println("System err: " + e);
    }
  }

  public ActionCallback setCallback(ActionInvocation<RemoteService> setInvocation) {
    return new ActionCallback(setInvocation) {
      @Override
      public void success(@SuppressWarnings("rawtypes") ActionInvocation invocation) {
      }

      @Override
      public void failure(@SuppressWarnings("rawtypes") ActionInvocation invocation,
        UpnpResponse operation, String defaultMsg) {
        System.err.println(defaultMsg);
      }
    };
  }

  public ActionCallback getCallback(ActionInvocation<RemoteService> invocation) {
    return new ActionCallback(invocation) {
      @Override
      public void success(@SuppressWarnings("rawtypes") ActionInvocation invocation) {
        System.out
          .println("Get value of temperature: " + invocation.getOutput("Temperature").getValue());
      }

      @Override
      public void failure(@SuppressWarnings("rawtypes") ActionInvocation invocation,
        UpnpResponse operation, String defaultMsg) {
        System.err.println(defaultMsg);
      }
    };
  }
}
