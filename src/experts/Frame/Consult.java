/*
 * The MIT License
 *
 * Copyright 2018 Expertise Team.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package experts.Frame;

import experts.Database.FCDatabase;
import experts.Engine.Manager;
import experts.Entities.Answer;
import experts.Entities.Premise;
import experts.Engine.QueueTable;
import experts.Entities.Rule;
import experts.Engine.WorkingMemory;
import experts.Modified.swing.RadioButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

/**
 *
 * @author owner
 */
public class Consult extends javax.swing.JFrame {

    /**
     * Creates new form main
     */
    public Manager manager;
    public Premise active_premise               = new Premise();
    public ArrayList<RadioButton> radio_buttons = new ArrayList<RadioButton>();
    public ButtonGroup button_group             = new ButtonGroup();
    public DefaultListModel list_model          = new DefaultListModel();
    public DefaultListModel list_model2         = new DefaultListModel();
    public DefaultListModel list_temp           = new DefaultListModel();

    public Consult() {
        initComponents();

        setTitle("Expertise");

        // MANAGER LOAD EXPERT WITH ID 1
        manager = new Manager(17);

        active_premise = manager.getNextPremise();
        QuestionLabel.setText("Question: " + active_premise.getQuestion());

        setQueueTableReady();

        memory_item_list.setModel(list_model);
        active_rule_list.setModel(list_model2);

        setButtonsReady();
        
    }
    
    public Consult(int expert_id) {
        initComponents();

        setTitle("Expertise");

        // MANAGER LOAD EXPERT WITH ID 1
        manager = new Manager(expert_id);

        active_premise = manager.getNextPremise();
        QuestionLabel.setText("Question: " + active_premise.getQuestion());

        setQueueTableReady();

        memory_item_list.setModel(list_model);
        active_rule_list.setModel(list_model2);

        setButtonsReady();
        
    }
    
    public int getSelectedAnswerId() {
        for (int i = 0; i < radio_buttons.size(); i++) {
            RadioButton button = radio_buttons.get(i);
            if (radio_buttons.get(i).getButton().isSelected()) {
                return ((Answer) button.getValue()).getId();
            }
        }
        return -1;
    }

    public void setButtonsReady() {
        for (int i = 0; i < radio_buttons.size(); i++) {
            panel1.remove(radio_buttons.get(i).getButton());
        }

        radio_buttons.clear();

        for (int i = 0; i < active_premise.list_of_answer.size(); i++) {

            RadioButton button = new RadioButton();
            button.setValue(active_premise.list_of_answer.get(i));
            button.setText(active_premise.list_of_answer.get(i).getAnswer());

            radio_buttons.add(button);
            radio_buttons.get(i).getButton().setBounds(250, 175 + i * 25, 50, 20);

            if (i == 0) {
                radio_buttons.get(i).getButton().setSelected(true);
            }

            panel1.add(radio_buttons.get(i).getButton());

            radio_buttons.get(i).getButton().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Answer ans = (Answer) button.getValue();
                    System.out.println(ans.getId() + ans.getAnswer());
                }
            });

            button_group.add(radio_buttons.get(i).getButton());

        }
    }

    public void setMemoryListReady() {
        list_model.removeAllElements();
        for (Object key : manager.getMemory().cache.keySet()) {
            Rule current_rule = manager.getQueueTable().current_rule;
            for (int i = 0; i < current_rule.premises.size(); i++) {
                Premise target = current_rule.premises.get(i);
                if ((int) key == target.getId()) {
                    list_model.addElement(
                            target.getQuestion()
                            + " : "
                            + manager.getMemory().cache.get(key).toString()
                    );
                }
            }
        }
        // list_model.addElement(list_temp);
        for (Object o : list_temp.toArray()) {
            list_model.addElement(o);
        }
    }

    public void setQueueTableReady() {
        list_model2.removeAllElements();
        active_rule_label.setText("Active Rule: " + manager.getQueueTable().current_rule.getConclusion());
        for (int i = 0; i < manager.getQueueTable().current_rule.premises.size(); i++) {
            Premise premise_target = manager.getQueueTable().current_rule.premises.get(i);
            list_model2.addElement(
                    "<html>" + premise_target.getId() + ". " + premise_target.getQuestion()
                    + "<br>value: " + premise_target.getRulesPremiseValue()
                    + "</html>"
            );
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        panel1 = new javax.swing.JPanel();
        QuestionLabel = new javax.swing.JLabel();
        submitButton = new javax.swing.JButton();
        conclusionLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        memory_item_list = new javax.swing.JList<>();
        workingMemoryLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        active_rule_list = new javax.swing.JList<>();
        active_rule_label = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panel1.setBackground(new java.awt.Color(204, 204, 255));

        QuestionLabel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        QuestionLabel.setText("Premise");

        submitButton.setText("submit");
        submitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitButtonActionPerformed(evt);
            }
        });

        conclusionLabel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        conclusionLabel.setText("Conclusion");

        memory_item_list.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(memory_item_list);

        workingMemoryLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        workingMemoryLabel.setText("Working Memory");

        active_rule_list.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(active_rule_list);

        active_rule_label.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        active_rule_label.setText("Active Rule");

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(QuestionLabel)
                    .addComponent(submitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(conclusionLabel))
                .addGap(596, 596, Short.MAX_VALUE))
            .addGroup(panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(workingMemoryLabel)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addComponent(active_rule_label)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addGap(120, 120, 120)
                .addComponent(QuestionLabel)
                .addGap(18, 18, 18)
                .addComponent(submitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(conclusionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 129, Short.MAX_VALUE)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(workingMemoryLabel)
                    .addComponent(active_rule_label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitButtonActionPerformed
        // TODO add your handling code here:
        // CHECK IF FINISHED, 
        if (active_premise == null
                || manager.getUnknownConclusion()
                || manager.conclusionObtained()) {
            return;
        }

        // MANAGER HANDLE USER'S ANSWER
        int user_answer = getSelectedAnswerId();
        manager.setAnswer(active_premise, user_answer);

        // OUTPUT WORKING MEMORY LIST
        list_temp.addElement(
                "<html>"
                + active_premise.getQuestion() + "<br>User Answer: "
                + user_answer
                + "</html>"
        );
        setMemoryListReady();
        memory_item_list.setModel(list_model);

        // SET CURRENT QUEUE_TABLE's RULE IN LIST
        setQueueTableReady();

        // CHECK KONDISI RULE YANG SEDANG ACTIVE (queue_table)
        manager.checkRuleStatus();

        // CHECK MANAGER'S CURRENT CONDITION
        if (manager.getUnknownConclusion()) {
            QuestionLabel.setText("Question: -");
            conclusionLabel.setText("UNKNOWN");
            return;
        } else if (manager.conclusionObtained()) {
            Rule rule = manager.getQueueTable().current_rule;
            QuestionLabel.setText("Question: -");
            conclusionLabel.setText(
                    "Conclusion: "
                    + "RULE " + rule.getId()
                    + ", " + rule.getConclusion()
                    + " = " + rule.getConclusionValue()
            );
            return;
        }

        // GET NEXT PREMISE
        active_premise = manager.getNextPremise();
        if (active_premise == null) {
            return;
        }

        // OUTPUT LABEL
        QuestionLabel.setText("Question: " + active_premise.getQuestion());
        // SET ACTIVE PREMISE ANSWER OPTION ON RADIO BUTTONS
        setButtonsReady();
    }//GEN-LAST:event_submitButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Consult.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Consult.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Consult.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Consult.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Consult().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel QuestionLabel;
    private javax.swing.JLabel active_rule_label;
    private javax.swing.JList<String> active_rule_list;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JLabel conclusionLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<String> memory_item_list;
    private javax.swing.JPanel panel1;
    private javax.swing.JButton submitButton;
    private javax.swing.JLabel workingMemoryLabel;
    // End of variables declaration//GEN-END:variables
}
