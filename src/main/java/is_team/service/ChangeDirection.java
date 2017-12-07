package is_team.service;

import org.fourthline.cling.binding.annotations.UpnpAction;
import org.fourthline.cling.binding.annotations.UpnpInputArgument;
import org.fourthline.cling.binding.annotations.UpnpOutputArgument;
import org.fourthline.cling.binding.annotations.UpnpService;
import org.fourthline.cling.binding.annotations.UpnpServiceId;
import org.fourthline.cling.binding.annotations.UpnpServiceType;
import org.fourthline.cling.binding.annotations.UpnpStateVariable;

@UpnpService(serviceId = @UpnpServiceId("ChangeDirection"),
  serviceType = @UpnpServiceType(value = "ChangeDirection", version = 1))
public class ChangeDirection {
  public final static int MIN_DIRECTION = -60;
  public final static int MAX_DIRECTION = 60;

  @UpnpStateVariable(defaultValue = "0", sendEvents = false)
  private int direction = 0;

  @UpnpAction
  public void setDirection(@UpnpInputArgument(name = "NewDirection") int direction) {
    if (direction < MIN_DIRECTION)
      this.direction = MIN_DIRECTION;
    else if (direction > MAX_DIRECTION)
      this.direction = MAX_DIRECTION;
    else
      this.direction = direction;
    System.out.println("Set direction is: " + this.direction);
  }

  @UpnpAction(out = @UpnpOutputArgument(name = "Direction"))
  public int getDirection() {
    System.out.println("Get direction is: " + this.direction);
    return direction;
  }
}
