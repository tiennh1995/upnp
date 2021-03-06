package is_team.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JSlider;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.model.action.ActionArgumentValue;
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

import is_team.service.ChangeTemperature;

public class Sensor extends javax.swing.JFrame {
  private static final long serialVersionUID = 1L;

  private HashMap<String, RemoteDevice> remoteDevices = new HashMap<String, RemoteDevice>();
  private UpnpService upnpService = new UpnpServiceImpl();
  private AirConditional airConditional;
  private boolean hasUser = false;
  private boolean isOn = false;

  public Sensor(AirConditional airConditional) {
    this.airConditional = airConditional;
    initComponents();
    setResizable(false);
    setTitle("Sensor");
    ClassLoader classLoader = getClass().getClassLoader();
    setIconImage(new ImageIcon(classLoader.getResource("icon/temperature_sensor.png")).getImage());
    setLocation(500, 0);
    sensorTempLabel.setText(Unit.temperatureLabel + ":");
    sensorUserLabel.setText(Unit.userLabel + ": ");
    sensorUserCheckbox.setText("Yes");
    sensorUserCheckbox.setSelected(hasUser);
    sensorTempLeftLabel.setText(ChangeTemperature.MIN_TEMP + Unit.tempUnit);
    sensorTempMiddleLabel
      .setText((ChangeTemperature.MIN_TEMP + ChangeTemperature.MAX_TEMP) / 2 + Unit.tempUnit);
    sensorTempRightLabel.setText(ChangeTemperature.MAX_TEMP + Unit.tempUnit);
    setLimitSensorTempSlider();

    upnpService.getRegistry().addListener(createRegistryListener());
    upnpService.getControlPoint().search(new STAllHeader());

    initEvent();
  }

  // Event change from controller
  private void initEvent() {
    airConditional.getAirTempIndexLabel().addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        airTempStateChanged(evt);
      }
    });
  }

  private void airTempStateChanged(PropertyChangeEvent evt) {
    this.callService("AirConditional", "SwitchPower", "Get", "Status", 0);
    if (isOn) {
      String temperature = ((JLabel) evt.getSource()).getText().replaceAll(Unit.tempUnit, "");
      int currentTemp = Integer.valueOf(temperature);
      sensorTempSlider.setValue(currentTemp);
      sensorTempLabel.setText(
        Unit.temperatureLabel + " (" + String.valueOf(currentTemp) + Unit.tempUnit + "): ");
    }
  }

  // Event and GUI
  private void sensorTempSliderStateChanged(javax.swing.event.ChangeEvent evt) {
    int currentTemp = ((JSlider) evt.getSource()).getValue();
    sensorTempLabel.setText("Temperature (" + String.valueOf(currentTemp) + Unit.tempUnit + "): ");
    this.callService("AirConditional", "SwitchPower", "Get", "Status", 0);
    if (hasUser && isOn) {
      this.callService("AirConditional", "ChangeTemperature", "Set", "Temperature", currentTemp);
    }
  }

  private void sensorUserCheckboxActionPerformed(java.awt.event.ActionEvent evt) {
    this.hasUser = sensorUserCheckbox.isSelected();
  }

  private void setLimitSensorTempSlider() {
    sensorTempSlider.setMinimum(ChangeTemperature.MIN_TEMP);
    sensorTempSlider.setMaximum(ChangeTemperature.MAX_TEMP);
    sensorTempSlider.setValue(ChangeTemperature.MIN_TEMP);
  }

  // Services
  public RegistryListener createRegistryListener() {
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

  public void callService(String deviceName, String UDAServiceId, String actionType,
    String attribute, Object value) {
    String actionName = actionType + attribute;
    ActionInvocation<RemoteService> actionInvocation = getActionInvocation(deviceName, UDAServiceId,
      actionName);
    ActionCallback actionCallback = null;
    if (actionInvocation != null) {
      if (actionType.equals("Set")) {
        actionInvocation.setInput("New" + attribute, value);
        actionCallback = setCallback(actionInvocation);
      } else {
        actionCallback = getCallback(actionInvocation);
      }
      upnpService.getControlPoint().execute(actionCallback);
    }
  }

  public ActionInvocation<RemoteService> getActionInvocation(String deviceName, String UDAServiceId,
    String actionName) {
    try {
      RemoteDevice remoteDevice = remoteDevices.get(deviceName);
      if (remoteDevice != null) {
        RemoteService remoteService = remoteDevice.findService(new UDAServiceId(UDAServiceId));
        if (remoteService != null) {
          Action<RemoteService> actionService = remoteService.getAction(actionName);
          return new ActionInvocation<RemoteService>(actionService);
        }
      } else {
        System.out.println("Remote device khong tim thay!");
      }
    } catch (Exception e) {
      System.out.println("System err: " + e);
    }
    return null;
  }

  public ActionCallback setCallback(ActionInvocation<RemoteService> setInvocation) {
    return new ActionCallback(setInvocation) {
      @Override
      public void success(@SuppressWarnings("rawtypes") ActionInvocation invocation) {
        @SuppressWarnings("rawtypes")
        ActionArgumentValue[] args = invocation.getInput();
        for (int i = 0; i < args.length; i++) {
          if (args[i].getArgument().getName().equals("NewTemperature"))
            airConditional.setAirTempIndexLabel((Integer) args[i].getValue());
        }
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
        @SuppressWarnings("rawtypes")
        ActionArgumentValue[] args = invocation.getOutput();
        for (int i = 0; i < args.length; i++) {
          if (args[i].getArgument().getName().equals("Status")) {
            isOn = (Boolean) args[i].getValue();
          }
        }
      }

      @Override
      public void failure(@SuppressWarnings("rawtypes") ActionInvocation invocation,
        UpnpResponse operation, String defaultMsg) {
        System.err.println(defaultMsg);
      }
    };
  }

  public void addRemoteDevice(RemoteDevice remoteDevice) {
    System.out.println("ADD DEVICE: " + remoteDevice.getType().getDisplayString());
    if (!remoteDevices.containsKey(remoteDevice.getType().getDisplayString()))
      remoteDevices.put(remoteDevice.getType().getDisplayString(), remoteDevice);
  }

  public void removeRemoteDevice(RemoteDevice remoteDevice) {
    System.out.println("REMOVE DEVICE: " + remoteDevice.getType().getDisplayString());
    remoteDevices.remove(remoteDevice.getType().getDisplayString());
  }

  // UI
  private void initComponents() {
    sensorPanel = new javax.swing.JPanel();
    sensorTempSlider = new javax.swing.JSlider();
    sensorTempLabel = new javax.swing.JLabel();
    sensorUserLabel = new javax.swing.JLabel();
    sensorUserCheckbox = new javax.swing.JCheckBox();
    sensorTempLeftLabel = new javax.swing.JLabel();
    sensorTempMiddleLabel = new javax.swing.JLabel();
    sensorTempRightLabel = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    sensorTempSlider.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        sensorTempSliderStateChanged(evt);
      }
    });

    sensorUserCheckbox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        sensorUserCheckboxActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout sensorPanelLayout = new javax.swing.GroupLayout(sensorPanel);
    sensorPanel.setLayout(sensorPanelLayout);
    sensorPanelLayout.setHorizontalGroup(
      sensorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
          sensorPanelLayout.createSequentialGroup().addGap(26, 26, 26)
            .addGroup(
              sensorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(sensorTempLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 149,
                  javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(sensorUserLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 95,
                  javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(
              javax.swing.LayoutStyle.ComponentPlacement.RELATED,
              javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(
              sensorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                  sensorPanelLayout.createSequentialGroup().addComponent(sensorUserCheckbox)
                    .addGap(214, 214, 214))
                .addGroup(sensorPanelLayout.createSequentialGroup()
                  .addGroup(sensorPanelLayout
                    .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(sensorTempSlider, javax.swing.GroupLayout.Alignment.TRAILING,
                      javax.swing.GroupLayout.PREFERRED_SIZE, 253,
                      javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(
                      sensorPanelLayout.createSequentialGroup().addComponent(sensorTempLeftLabel)
                        .addGap(78, 78, 78).addComponent(sensorTempMiddleLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                          javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(sensorTempRightLabel)))
                  .addContainerGap()))));
    sensorPanelLayout.setVerticalGroup(
      sensorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(sensorPanelLayout.createSequentialGroup()
          .addGroup(sensorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sensorPanelLayout.createSequentialGroup().addContainerGap(65, Short.MAX_VALUE)
              .addComponent(sensorTempLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 54,
                javax.swing.GroupLayout.PREFERRED_SIZE)
              .addGap(32, 32, 32))
            .addGroup(sensorPanelLayout.createSequentialGroup().addGap(62, 62, 62)
              .addGroup(
                sensorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                  .addComponent(sensorTempLeftLabel).addComponent(sensorTempMiddleLabel)
                  .addComponent(sensorTempRightLabel))
              .addGap(1, 1, 1)
              .addComponent(sensorTempSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 27,
                javax.swing.GroupLayout.PREFERRED_SIZE)
              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
          .addGroup(
            sensorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(sensorUserLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 36,
                javax.swing.GroupLayout.PREFERRED_SIZE)
              .addComponent(sensorUserCheckbox))
          .addGap(41, 41, 41)));

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(sensorPanel, javax.swing.GroupLayout.PREFERRED_SIZE,
          javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(0, 40, Short.MAX_VALUE)));
    layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(sensorPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

    pack();
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel sensorPanel;
  private javax.swing.JLabel sensorTempLabel;
  private javax.swing.JLabel sensorTempLeftLabel;
  private javax.swing.JLabel sensorTempMiddleLabel;
  private javax.swing.JLabel sensorTempRightLabel;
  private javax.swing.JSlider sensorTempSlider;
  private javax.swing.JCheckBox sensorUserCheckbox;
  private javax.swing.JLabel sensorUserLabel;
  // End of variables declaration//GEN-END:variables
}
