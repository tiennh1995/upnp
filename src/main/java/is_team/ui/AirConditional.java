package is_team.ui;

import javax.swing.ImageIcon;

import is_team.service.ChangeSpeed;
import is_team.service.ChangeTemperature;

public class AirConditional extends javax.swing.JFrame {
  private static final long serialVersionUID = 1L;

  public AirConditional() {
    initComponents();
    ClassLoader classLoader = getClass().getClassLoader();
    setIconImage(new ImageIcon(classLoader.getResource("icon/air_conditional.png")).getImage());
    setTitle("AirConditional");
    setResizable(false);
    setLocation(0, 0);
    airTempLabel.setText(Unit.temperatureLabel + ":");
    airTempIndexLabel.setText(ChangeTemperature.MIN_TEMP + Unit.tempUnit);
    airSpeedLabel.setText(Unit.speedLabel + ":");
    airSpeedIndexLabel.setText(ChangeSpeed.MIN_SPEED + Unit.speedUnit);
    airDirectionLabel.setText(Unit.directionLabel + ":");
    airDirectionIndexLabel.setText("Middle");
  }

  // Interface change UI
  void setAirTempIndexLabel(int value) {
    airTempIndexLabel.setText(value + Unit.tempUnit);
  }

  void setAirSpeedIndexLabel(int value) {
    airSpeedIndexLabel.setText(value + Unit.speedUnit);
  }

  void setAirDirectionIndexLabel(int value) {
    String direction = null;
    if (value > 10)
      direction = "Right";
    else if (value < -10)
      direction = "Left";
    else
      direction = "Middle";
    airDirectionIndexLabel.setText(direction);
  }

  public javax.swing.JLabel getAirTempIndexLabel() {
    return airTempIndexLabel;
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
  private javax.swing.JPanel airPanel;
  private javax.swing.JLabel airSpeedIndexLabel;
  private javax.swing.JLabel airSpeedLabel;
  private javax.swing.JLabel airTempIndexLabel;
  private javax.swing.JLabel airTempLabel;
  private javax.swing.JLabel airDirectionIndexLabel;
  private javax.swing.JLabel airDirectionLabel;
  // End of variables declaration//GEN-END:variables
}
