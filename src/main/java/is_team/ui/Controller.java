package is_team.ui;

import javax.swing.JSlider;

public class Controller extends javax.swing.JFrame {
  private static final long serialVersionUID = 1L;

  public Controller() {
    initComponents();
    setTitle("Controller");
    setLimitControllerTempSlider();
    setLimitControllerSpeedSlider();
    setLimitControllerWindSlider();
  }

  private void setLimitControllerTempSlider() {
    controllerTempSlider.setMinimum(10);
    controllerTempSlider.setMaximum(30);
    controllerTempSlider.setValue(20);
  }

  private void setLimitControllerSpeedSlider() {
    controllerSpeedSlider.setMinimum(20);
    controllerSpeedSlider.setMaximum(40);
    controllerSpeedSlider.setValue(30);
  }

  private void setLimitControllerWindSlider() {
    controllerDirectionSlider.setMinimum(-60);
    controllerDirectionSlider.setMaximum(60);
    controllerDirectionSlider.setValue(0);
  }

  private void controllerTempSliderStateChanged(javax.swing.event.ChangeEvent evt) {
    int currentTemp = ((JSlider) evt.getSource()).getValue();
    controllerTempLabel
      .setText("Temperature (" + String.valueOf(currentTemp) + Unit.tempUnit + "): ");
  }

  private void controllerSpeedSliderStateChanged(javax.swing.event.ChangeEvent evt) {
    int currentSpeed = ((JSlider) evt.getSource()).getValue();
    controllerSpeedLabel.setText("Speed (" + String.valueOf(currentSpeed) + Unit.speedUnit + "): ");
  }

  private void controllerDirectionSliderStateChanged(javax.swing.event.ChangeEvent evt) {
    int currentDirection = ((JSlider) evt.getSource()).getValue();
    controllerDirectionLabel
      .setText("Direction (" + String.valueOf(currentDirection) + Unit.directionUnit + "): ");
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

    controllerStateLabel.setText("State:");

    controllerStateCheckbox.setText("ON");

    controllerSpeedLabel.setText("Speed:");

    controllerSpeedSlider.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        controllerSpeedSliderStateChanged(evt);
      }
    });

    controllerDirectionLabel.setText("Direction: ");

    controllerDirectionSlider.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        controllerDirectionSliderStateChanged(evt);
      }
    });

    controllerDirectionLeftLabel.setText("Left");

    controllerDirectionMiddleLabel.setText("Middle");

    controllerDirectionRightLabel.setText("Right");

    controllerTempLabel.setText("Temperature:");

    controllerTempSlider.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        controllerTempSliderStateChanged(evt);
      }
    });

    controllerSpeedLeftLabel.setText("20km/h");

    controllerSpeedMiddleLabel.setText("30km/h");

    controllerSpeedRightLabel.setText("40km/h");

    controllerTempLeftLabel.setText("10℃");

    controllerTempMiddleLabel.setText("20℃");

    controllerTempRightLabel.setText("30℃");

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
