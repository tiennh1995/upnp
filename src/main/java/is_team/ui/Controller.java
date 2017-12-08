package is_team.ui;

import java.awt.event.ActionEvent;
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

import is_team.service.ChangeDirection;
import is_team.service.ChangeSpeed;
import is_team.service.ChangeTemperature;

public class Controller extends javax.swing.JFrame {
  private static final long serialVersionUID = 1L;

  private HashMap<String, RemoteDevice> remoteDevices = new HashMap<String, RemoteDevice>();
  private UpnpService upnpService = new UpnpServiceImpl();
  private AirConditional airConditional;

  private boolean isOn = false;

  public Controller(AirConditional airConditional) {
    initComponents();
    this.airConditional = airConditional;
    setResizable(false);
    setSize(650, 372);
    setTitle("Controller");
    setLocation(200, 360);
    ClassLoader classLoader = getClass().getClassLoader();
    setIconImage(new ImageIcon(classLoader.getResource("icon/controller.png")).getImage());

    controllerStateLabel.setText(Unit.stateLabel + ":");
    controllerStateCheckbox.setText("ON");
    controllerStateCheckbox.setSelected(isOn);
    controllerSpeedLabel.setText(Unit.speedLabel + ":");
    controllerDirectionLabel.setText(Unit.directionLabel + ":");
    controllerDirectionLeftLabel.setText("Left");
    controllerDirectionMiddleLabel.setText("Middle");
    controllerDirectionRightLabel.setText("Right");
    controllerTempLabel.setText(Unit.temperatureLabel + ":");
    controllerSpeedLeftLabel.setText(ChangeSpeed.MIN_SPEED + Unit.speedUnit);
    controllerSpeedMiddleLabel
      .setText((ChangeSpeed.MIN_SPEED + ChangeSpeed.MAX_SPEED) / 2 + Unit.speedUnit);
    controllerSpeedRightLabel.setText(ChangeSpeed.MAX_SPEED + Unit.speedUnit);
    controllerTempLeftLabel.setText(ChangeTemperature.MIN_TEMP + Unit.tempUnit);
    controllerTempMiddleLabel
      .setText((ChangeTemperature.MIN_TEMP + ChangeTemperature.MAX_TEMP) / 2 + Unit.tempUnit);
    controllerTempRightLabel.setText(ChangeTemperature.MAX_TEMP + Unit.tempUnit);

    setLimitControllerTempSlider();
    setLimitControllerSpeedSlider();
    setLimitControllerWindSlider();

    initEvent();

    upnpService.getRegistry().addListener(createRegistryListener());
    upnpService.getControlPoint().search(new STAllHeader());
  }

  // Event change from sensor
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
      controllerTempSlider.setValue(currentTemp);
      controllerTempLabel.setText(
        Unit.temperatureLabel + " (" + String.valueOf(currentTemp) + Unit.tempUnit + "): ");
    }
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
          String argument = args[i].getArgument().getName();
          Object value = args[i].getValue();
          if (argument.equals("NewTemperature")) {
            airConditional.setAirTempIndexLabel((Integer) value);
          } else if (argument.equals("NewSpeed")) {
            System.out.println("CO VAO DAY");
            airConditional.setAirSpeedIndexLabel((Integer) value);
          } else if (argument.equals("NewDirection")) {
            airConditional.setAirDirectionIndexLabel((Integer) value);
          } else if (argument.equals("NewStatus")) {
            isOn = (Boolean) value;
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

  public ActionCallback getCallback(ActionInvocation<RemoteService> invocation) {
    return new ActionCallback(invocation) {
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

  public void addRemoteDevice(RemoteDevice remoteDevice) {
    System.out.println("ADD DEVICE: " + remoteDevice.getType().getDisplayString());
    if (!remoteDevices.containsKey(remoteDevice.getType().getDisplayString()))
      remoteDevices.put(remoteDevice.getType().getDisplayString(), remoteDevice);
  }

  public void removeRemoteDevice(RemoteDevice remoteDevice) {
    System.out.println("REMOVE DEVICE: " + remoteDevice.getType().getDisplayString());
    remoteDevices.remove(remoteDevice.getType().getDisplayString());
  }

  // Interface
  void setControllerStateCheckbox(boolean isOn) {
    controllerStateCheckbox.setSelected(isOn);
  }

  void setControllerTempSlider(int value) {
    controllerTempSlider.setValue(value);
    controllerTempLabel.setText(Unit.temperatureLabel + " (" + value + Unit.tempUnit + "): ");
  }

  void setAirSpeedIndexLabel(int value) {
    controllerSpeedSlider.setValue(value);
    controllerSpeedLabel.setText(Unit.speedLabel + " (" + value + Unit.speedUnit + "): ");
  }

  void setAirDirectionIndexLabel(int value) {
    controllerDirectionSlider.setValue(value);
    String direction = null;
    if (value > 10)
      direction = "Right";
    else if (value < -10)
      direction = "Left";
    else
      direction = "Middle";
    controllerDirectionLabel.setText(Unit.directionLabel + " (" + direction + "): ");
  }

  // Event of UI
  private void setLimitControllerTempSlider() {
    controllerTempSlider.setMinimum(ChangeTemperature.MIN_TEMP);
    controllerTempSlider.setMaximum(ChangeTemperature.MAX_TEMP);
    controllerTempSlider.setValue(ChangeTemperature.MIN_TEMP);
  }

  private void setLimitControllerSpeedSlider() {
    controllerSpeedSlider.setMinimum(ChangeSpeed.MIN_SPEED);
    controllerSpeedSlider.setMaximum(ChangeSpeed.MAX_SPEED);
    controllerSpeedSlider.setValue(ChangeSpeed.MIN_SPEED);
  }

  private void setLimitControllerWindSlider() {
    controllerDirectionSlider.setMinimum(ChangeDirection.MIN_DIRECTION);
    controllerDirectionSlider.setMaximum(ChangeDirection.MAX_DIRECTION);
    controllerDirectionSlider
      .setValue((ChangeDirection.MIN_DIRECTION + ChangeDirection.MAX_DIRECTION) / 2);
  }

  private void controllerTempSliderStateChanged(javax.swing.event.ChangeEvent evt) {
    int currentTemp = ((JSlider) evt.getSource()).getValue();
    controllerTempLabel
      .setText(Unit.temperatureLabel + " (" + String.valueOf(currentTemp) + Unit.tempUnit + "): ");
    if (isOn)
      this.callService("AirConditional", "ChangeTemperature", "Set", "Temperature", currentTemp);
  }

  private void controllerSpeedSliderStateChanged(javax.swing.event.ChangeEvent evt) {
    int currentSpeed = ((JSlider) evt.getSource()).getValue();
    controllerSpeedLabel
      .setText(Unit.speedLabel + " (" + String.valueOf(currentSpeed) + Unit.speedUnit + "): ");
    if (isOn)
      this.callService("AirConditional", "ChangeSpeed", "Set", "Speed", currentSpeed);
  }

  private void controllerDirectionSliderStateChanged(javax.swing.event.ChangeEvent evt) {
    int currentDirection = ((JSlider) evt.getSource()).getValue();
    String direction = null;
    if (currentDirection > 10)
      direction = "Right";
    else if (currentDirection < -10)
      direction = "Left";
    else
      direction = "Middle";
    controllerDirectionLabel.setText(Unit.directionLabel + " (" + direction + "): ");
    if (isOn)
      this.callService("AirConditional", "ChangeDirection", "Set", "Direction", currentDirection);
  }

  private void controllerStateCheckboxStateChanged(ActionEvent evt) {
    this.callService("AirConditional", "SwitchPower", "Set", "Status",
      controllerStateCheckbox.isSelected());
  }

  private void initComponents() {
    controllerPanel = new javax.swing.JPanel();
    controllerStateLabel = new javax.swing.JLabel();
    controllerStateCheckbox = new javax.swing.JCheckBox();
    controllerSpeedLabel = new javax.swing.JLabel();
    controllerSpeedSlider = new javax.swing.JSlider();
    controllerDirectionLabel = new javax.swing.JLabel();
    controllerDirectionSlider = new javax.swing.JSlider();
    controllerDirectionLeftLabel = new javax.swing.JLabel();
    controllerDirectionMiddleLabel = new javax.swing.JLabel();
    controllerDirectionRightLabel = new javax.swing.JLabel();
    controllerTempLabel = new javax.swing.JLabel();
    controllerTempSlider = new javax.swing.JSlider();
    controllerSpeedLeftLabel = new javax.swing.JLabel();
    controllerSpeedMiddleLabel = new javax.swing.JLabel();
    controllerSpeedRightLabel = new javax.swing.JLabel();
    controllerTempLeftLabel = new javax.swing.JLabel();
    controllerTempMiddleLabel = new javax.swing.JLabel();
    controllerTempRightLabel = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    controllerSpeedSlider.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        controllerSpeedSliderStateChanged(evt);
      }
    });

    controllerDirectionSlider.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        controllerDirectionSliderStateChanged(evt);
      }
    });

    controllerTempSlider.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        controllerTempSliderStateChanged(evt);
      }
    });

    controllerStateCheckbox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        controllerStateCheckboxStateChanged(evt);
      }
    });

    javax.swing.GroupLayout controllerPanelLayout = new javax.swing.GroupLayout(controllerPanel);
    controllerPanel.setLayout(controllerPanelLayout);
    controllerPanelLayout.setHorizontalGroup(controllerPanelLayout
      .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(controllerPanelLayout.createSequentialGroup().addGap(26, 26, 26)
        .addGroup(controllerPanelLayout
          .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(controllerTempLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 148,
            Short.MAX_VALUE)
          .addComponent(controllerSpeedLabel, javax.swing.GroupLayout.DEFAULT_SIZE,
            javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addGroup(controllerPanelLayout.createSequentialGroup().addGroup(
            controllerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(controllerDirectionLabel)
              .addGroup(controllerPanelLayout.createSequentialGroup().addGap(2, 2, 2).addComponent(
                controllerStateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 61,
                javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(0, 0, Short.MAX_VALUE)))
        .addGap(18, 18, 18)
        .addGroup(controllerPanelLayout
          .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
            controllerPanelLayout.createSequentialGroup().addComponent(controllerTempLeftLabel)
              .addGap(159, 159, 159).addComponent(controllerTempMiddleLabel).addGap(158, 158, 158)
              .addComponent(controllerTempRightLabel))
          .addComponent(controllerTempSlider, javax.swing.GroupLayout.DEFAULT_SIZE,
            javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(controllerStateCheckbox)
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
            controllerPanelLayout.createSequentialGroup().addComponent(controllerDirectionLeftLabel)
              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(controllerDirectionMiddleLabel).addGap(147, 147, 147)
              .addComponent(controllerDirectionRightLabel))
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
            controllerPanelLayout.createSequentialGroup().addComponent(controllerSpeedLeftLabel)
              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(controllerSpeedMiddleLabel).addGap(126, 126, 126)
              .addComponent(controllerSpeedRightLabel))
          .addComponent(controllerSpeedSlider, javax.swing.GroupLayout.DEFAULT_SIZE,
            javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(controllerDirectionSlider, javax.swing.GroupLayout.DEFAULT_SIZE,
            javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap(42, Short.MAX_VALUE)));
    controllerPanelLayout
      .setVerticalGroup(
        controllerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(
            controllerPanelLayout.createSequentialGroup().addGap(28, 28, 28)
              .addGroup(controllerPanelLayout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(controllerStateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 34,
                  javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(controllerStateCheckbox))
              .addGroup(
                controllerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addGroup(controllerPanelLayout.createSequentialGroup()
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                      javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(
                      controllerTempLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 33,
                      javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(16, 16, 16))
                  .addGroup(controllerPanelLayout.createSequentialGroup().addGap(38, 38, 38)
                    .addGroup(controllerPanelLayout
                      .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                      .addComponent(controllerTempLeftLabel).addComponent(controllerTempMiddleLabel)
                      .addComponent(controllerTempRightLabel,
                        javax.swing.GroupLayout.PREFERRED_SIZE, 17,
                        javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(controllerTempSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 37,
                      javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13,
                      Short.MAX_VALUE)))
              .addGap(19, 19, 19)
              .addGroup(controllerPanelLayout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(controllerSpeedLeftLabel).addComponent(controllerSpeedMiddleLabel)
                .addComponent(controllerSpeedRightLabel))
              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
              .addGroup(
                controllerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(controllerSpeedSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 35,
                    javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addComponent(controllerSpeedLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 35,
                    javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGap(37, 37, 37)
              .addGroup(controllerPanelLayout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(controllerPanelLayout.createSequentialGroup()
                  .addGroup(controllerPanelLayout
                    .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(controllerDirectionLeftLabel)
                    .addComponent(controllerDirectionMiddleLabel)
                    .addComponent(controllerDirectionRightLabel))
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(controllerDirectionSlider, javax.swing.GroupLayout.PREFERRED_SIZE,
                    34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(controllerDirectionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                  javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGap(33, 33, 33)));

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(controllerPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
    layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(controllerPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

    pack();
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JLabel controllerDirectionLabel;
  private javax.swing.JLabel controllerDirectionLeftLabel;
  private javax.swing.JLabel controllerDirectionMiddleLabel;
  private javax.swing.JLabel controllerDirectionRightLabel;
  private javax.swing.JSlider controllerDirectionSlider;
  private javax.swing.JPanel controllerPanel;
  private javax.swing.JLabel controllerSpeedLabel;
  private javax.swing.JLabel controllerSpeedLeftLabel;
  private javax.swing.JLabel controllerSpeedMiddleLabel;
  private javax.swing.JLabel controllerSpeedRightLabel;
  private javax.swing.JSlider controllerSpeedSlider;
  private javax.swing.JCheckBox controllerStateCheckbox;
  private javax.swing.JLabel controllerStateLabel;
  private javax.swing.JLabel controllerTempLabel;
  private javax.swing.JLabel controllerTempLeftLabel;
  private javax.swing.JLabel controllerTempMiddleLabel;
  private javax.swing.JLabel controllerTempRightLabel;
  private javax.swing.JSlider controllerTempSlider;
  // End of variables declaration//GEN-END:variables
}
