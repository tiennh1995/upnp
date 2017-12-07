package is_team.service;

import org.fourthline.cling.binding.annotations.*;

@UpnpService(serviceId = @UpnpServiceId("ChangeTemperature"),
  serviceType = @UpnpServiceType(value = "ChangeTemperature", version = 1))
public class ChangeTemperature {
  public final static int MIN_TEMP = 15;
  public final static int MAX_TEMP = 30;

  @UpnpStateVariable(defaultValue = "0", sendEvents = true)
  private int temperature = 15;

  @UpnpAction
  public void setTemperature(@UpnpInputArgument(name = "NewTemperature") int temperature) {
    if (temperature < MIN_TEMP)
      this.temperature = MIN_TEMP;
    else if (temperature > MAX_TEMP)
      this.temperature = MAX_TEMP;
    else
      this.temperature = temperature;
    System.out.println("Set temperature is: " + this.temperature);
  }

  @UpnpAction(out = @UpnpOutputArgument(name = "Temperature"))
  public int getTemperature() {
    System.out.println("Get temperature is: " + this.temperature);
    return temperature;
  }
}
