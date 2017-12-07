package is_team.device;

import java.io.IOException;

import org.fourthline.cling.binding.LocalServiceBindingException;
import org.fourthline.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.fourthline.cling.model.DefaultServiceManager;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.DeviceDetails;
import org.fourthline.cling.model.meta.DeviceIdentity;
import org.fourthline.cling.model.meta.Icon;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.LocalService;
import org.fourthline.cling.model.meta.ManufacturerDetails;
import org.fourthline.cling.model.meta.ModelDetails;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.model.types.UDN;

public class Device {
  private String strIdentity;
  private String strType;
  private String strDetails;
  private String strIcon;
  private String strService;
  private String[] strServices;

  public Device(String strIdentity, String strType, String strDetails, String strIcon,
    String strService) {
    this.strIdentity = strIdentity;
    this.strType = strType;
    this.strDetails = strDetails;
    this.strIcon = strIcon;
    this.strService = strService;
  }

  public Device(String strIdentity, String strType, String strDetails, String strIcon,
    String[] strServices) {
    this.strIdentity = strIdentity;
    this.strType = strType;
    this.strDetails = strDetails;
    this.strIcon = strIcon;
    this.strServices = strServices;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public LocalDevice getDevice()
    throws ValidationException, LocalServiceBindingException, IOException, ClassNotFoundException {

    DeviceIdentity identity = new DeviceIdentity(UDN.uniqueSystemIdentifier(strIdentity));

    DeviceType type = new UDADeviceType(strType, 1);

    DeviceDetails details = new DeviceDetails(strDetails, new ManufacturerDetails("ACME"),
      new ModelDetails("IS Team Device", "A demo with is team", "v1"));

    ClassLoader classLoader = getClass().getClassLoader();
    Icon icon = new Icon("image/png", 48, 48, 8, classLoader.getResource(strIcon));
    if (this.strServices == null) {
      LocalService deviceService = new AnnotationLocalServiceBinder()
        .read(Class.forName(strService));
      deviceService.setManager(new DefaultServiceManager(deviceService, Class.forName(strService)));
      return new LocalDevice(identity, type, details, icon, deviceService);
    } else {
      LocalService[] deviceServices = new LocalService[this.strServices.length];
      for (int i = 0; i < this.strServices.length; i++) {
        deviceServices[i] = new AnnotationLocalServiceBinder().read(Class.forName(strServices[i]));
        deviceServices[i]
          .setManager(new DefaultServiceManager(deviceServices[i], Class.forName(strServices[i])));
      }

      return new LocalDevice(identity, type, details, icon, deviceServices);
    }
  }

  public String getStrIdentity() {
    return strIdentity;
  }

  public void setStrIdentity(String strIdentity) {
    this.strIdentity = strIdentity;
  }

  public String getStrType() {
    return strType;
  }

  public void setStrType(String strType) {
    this.strType = strType;
  }

  public String getStrDetails() {
    return strDetails;
  }

  public void setStrDetails(String strDetails) {
    this.strDetails = strDetails;
  }

  public String getStrIcon() {
    return strIcon;
  }

  public void setStrIcon(String strIcon) {
    this.strIcon = strIcon;
  }

  public String getStrService() {
    return strService;
  }

  public void setStrService(String strService) {
    this.strService = strService;
  }

  public String[] getStrServices() {
    return strServices;
  }

  public void setStrServices(String[] strServices) {
    this.strServices = strServices;
  }
}
