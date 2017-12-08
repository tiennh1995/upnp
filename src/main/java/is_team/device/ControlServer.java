package is_team.device;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.model.meta.LocalDevice;

import is_team.service.ChangeDirection;
import is_team.service.ChangeSpeed;
import is_team.service.ChangeTemperature;
import is_team.service.SwitchPower;

public class ControlServer implements Runnable {
  public static void main(String[] args) {
    // Start a user thread that runs the UPnP stack
    Thread serverThread = new Thread(new ControlServer());
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

      Device deviceTemp = new Device("Temperature Sensor", "TemperatureSensor",
        "Temperature Sensor", "icon/temperature_sensor.png", SwitchPower.class.getName());
      LocalDevice localDeviceTemp = deviceTemp.getDevice();
      upnpService.getRegistry().addDevice(localDeviceTemp);

      Device deviceUser = new Device("User Sensor", "UserSensor", "User Sensor",
        "icon/user_sensor.png", SwitchPower.class.getName());
      LocalDevice localDeviceUser = deviceUser.getDevice();
      upnpService.getRegistry().addDevice(localDeviceUser);

      Device deviceAir = new Device("Air Conditional", "AirConditional", "Air Conditional",
        "icon/air_conditional.png",
        new String[] { ChangeDirection.class.getName(), ChangeSpeed.class.getName(),
          ChangeTemperature.class.getName(), SwitchPower.class.getName() });
      LocalDevice localDeviceAir = deviceAir.getDevice();
      upnpService.getRegistry().addDevice(localDeviceAir);

      Device deviceController = new Device("Controller", "Controller", "Controller",
        "icon/controller.png", SwitchPower.class.getName());
      LocalDevice localDeviceController = deviceController.getDevice();
      upnpService.getRegistry().addDevice(localDeviceController);
    } catch (Exception ex) {
      System.err.println("Exception occured: " + ex);
      ex.printStackTrace(System.err);
      System.exit(1);
    }
  }
}
