package is_team.ui;

import java.util.HashMap;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.model.meta.RemoteDevice;

import is_team.service.ChangeSpeed;
import is_team.service.ChangeTemperature;

public class AirConditional extends javax.swing.JFrame {
  private static final long serialVersionUID = 1L;

  public HashMap<String, RemoteDevice> remoteDevices = new HashMap<String, RemoteDevice>();
  public UpnpService upnpService = new UpnpServiceImpl();

  public AirConditional() {
    initComponents();
    setTitle("AirConditional");

    airTempLabel.setText("Temperature:");
    airTempIndexLabel.setText(ChangeTemperature.MIN_TEMP + Unit.tempUnit);
    airSpeedLabel.setText("Speed:");
    airSpeedIndexLabel.setText(ChangeSpeed.MIN_SPEED + Unit.speedUnit);
    airDirectionLabel.setText("Wind Conditional:");
    airDirectionIndexLabel.setText("Left");
  }

  // Interface change UI
  void setAirTempIndexLabel(String txt) {
    airTempIndexLabel.setText(txt);
  }

  // UI
  private void initComponents() {
    airPanel = new javax.swing.JPanel();
    airTempLabel = new javax.swing.JLabel();
    airTempIndexLabel = new javax.swing.JLabel();
    airSpeedLabel = new javax.swing.JLabel();
    airSpeedIndexLabel = new javax.swing.JLabel();
    airSpeedIndexLabel.setEnabled(true);
    airDirectionLabel = new javax.swing.JLabel();
    airDirectionIndexLabel = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    javax.swing.GroupLayout airPanelLayout = new javax.swing.GroupLayout(airPanel);
    airPanel.setLayout(airPanelLayout);
    airPanelLayout
      .setHorizontalGroup(
        airPanelLayout
          .createParallelGroup(
            javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(airPanelLayout.createSequentialGroup().addGap(36, 36, 36)
            .addGroup(
              airPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(airDirectionLabel, javax.swing.GroupLayout.DEFAULT_SIZE,
                  javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(airSpeedLabel, javax.swing.GroupLayout.DEFAULT_SIZE,
                  javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(airTempLabel, javax.swing.GroupLayout.DEFAULT_SIZE,
                  javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGap(26, 26, 26)
            .addGroup(
              airPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(airTempIndexLabel, javax.swing.GroupLayout.DEFAULT_SIZE,
                  javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(airSpeedIndexLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 151,
                  Short.MAX_VALUE)
                .addComponent(airDirectionIndexLabel, javax.swing.GroupLayout.DEFAULT_SIZE,
                  javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap(67, Short.MAX_VALUE)));
    airPanelLayout.setVerticalGroup(
      airPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(airPanelLayout.createSequentialGroup().addGap(39, 39, 39)
          .addGroup(airPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(airTempLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32,
              javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(airTempIndexLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32,
              javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGap(43, 43, 43)
          .addGroup(airPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(airSpeedLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
              javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(airSpeedIndexLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
              javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGap(63, 63, 63)
          .addGroup(airPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(airDirectionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29,
              javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(airDirectionIndexLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29,
              javax.swing.GroupLayout.PREFERRED_SIZE))
          .addContainerGap(66, Short.MAX_VALUE)));

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(airPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
    layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(airPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

    pack();
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  public javax.swing.JPanel airPanel;
  public javax.swing.JLabel airSpeedIndexLabel;
  public javax.swing.JLabel airSpeedLabel;
  public javax.swing.JLabel airTempIndexLabel;
  public javax.swing.JLabel airTempLabel;
  public javax.swing.JLabel airDirectionIndexLabel;
  public javax.swing.JLabel airDirectionLabel;
  // End of variables declaration//GEN-END:variables
}
