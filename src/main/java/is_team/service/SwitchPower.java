package is_team.service;

import org.fourthline.cling.binding.annotations.*;

//Services
@UpnpService(serviceId = @UpnpServiceId("SwitchPower"),
  serviceType = @UpnpServiceType(value = "SwitchPower", version = 1))
public class SwitchPower {
  @UpnpStateVariable(defaultValue = "0", sendEvents = false)
  private boolean status = false;

  @UpnpAction
  public void setStatus(@UpnpInputArgument(name = "NewStatus") boolean status) {
    this.status = status;
  }

  @UpnpAction(out = @UpnpOutputArgument(name = "Status"))
  public boolean getStatus() {
    return status;
  }
}
