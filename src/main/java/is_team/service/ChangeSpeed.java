package is_team.service;

import org.fourthline.cling.binding.annotations.UpnpAction;
import org.fourthline.cling.binding.annotations.UpnpInputArgument;
import org.fourthline.cling.binding.annotations.UpnpOutputArgument;
import org.fourthline.cling.binding.annotations.UpnpService;
import org.fourthline.cling.binding.annotations.UpnpServiceId;
import org.fourthline.cling.binding.annotations.UpnpServiceType;
import org.fourthline.cling.binding.annotations.UpnpStateVariable;

@UpnpService(serviceId = @UpnpServiceId("ChangeSpeed"),
  serviceType = @UpnpServiceType(value = "ChangeSpeed", version = 1))
public class ChangeSpeed {
  public final static int  MIN_SPEED = 20;
  public final static int  MAX_SPEED = 40;

  @UpnpStateVariable(defaultValue = "20", sendEvents = false)
  private int speed = 20;

  @UpnpAction
  public void setSpeed(@UpnpInputArgument(name = "NewSpeed") int speed) {
    if (speed < MIN_SPEED)
      this.speed = MIN_SPEED;
    else if (speed > MAX_SPEED)
      this.speed = MAX_SPEED;
    else
      this.speed = speed;
    System.out.println("Set speed is: " + this.speed);
  }

  @UpnpAction(out = @UpnpOutputArgument(name = "Speed"))
  public int getSpeed() {
    System.out.println("Get speed is: " + this.speed);
    return speed;
  }
}
