package is_team.service;

import org.fourthline.cling.binding.annotations.*;

@UpnpService(serviceId = @UpnpServiceId("ChangeTemperature"),
  serviceType = @UpnpServiceType(value = "ChangeTemperature", version = 1))
public class ChangeTemperature {
  final int MAX_TEMP = 30;
  final int MIN_TEMP = 15;

  @UpnpStateVariable(defaultValue = "15", sendEvents = false)
  private int temperature = 15;

  @UpnpAction
  public void setTemperature(@UpnpInputArgument(name = "NewTemperature") int temperature) {
    this.temperature = temperature;
    System.out.println("Set temperature is: " + this.temperature);
  }

  @UpnpAction(out = @UpnpOutputArgument(name = "Temperature"))
  public int getTemperature() {
    System.out.println("Get temperature is: " + this.temperature);
    return temperature;
  }
}
