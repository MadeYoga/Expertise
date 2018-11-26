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

import experts.Database.AnswerStore;
import experts.Database.Storage;
import experts.Engine.BCDatabase;
import experts.Engine.Manage.BCManager;
import experts.Entities.Answer;
import experts.Entities.Expert;
import experts.Entities.Premise;
import experts.Entities.Rule;
import experts.Modified.swing.RadioButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author owner
 */
public class BCConsult extends javax.swing.JFrame {

    /**
     * Creates new form main
     */
    public Menu parent;
    public BCManager manager;
    public Premise active_premise               = new Premise();
    public ArrayList<RadioButton> radio_buttons = new ArrayList<RadioButton>();
    public ButtonGroup button_group             = new ButtonGroup();
    public DefaultListModel list_model          = new DefaultListModel();
    public DefaultListModel list_model2         = new DefaultListModel();
    public DefaultListModel list_temp           = new DefaultListModel();
    public Answer selected_answer;
    
    public BCConsult() {
        initComponents();

        setTitle("Expertise: Backward Chaining");
        howButton.setVisible(false);
        // MANAGER LOAD EXPERT WITH ID 1
        manager = new BCManager(1);

        active_premise = manager.getNextPremise();
        QuestionLabel.setText("Question: " + active_premise.getQuestion());

        setQueueTableReady();

        memory_item_list.setModel(list_model);
        active_rule_list.setModel(list_model2);

        setButtonsReady();
        
    }
    
    public BCConsult(int expert_id, Menu parent) {
        initComponents();
        
        setTitle("Expertise: Backward Chaining");
        howButton.setVisible(false);
        
        // MANAGER LOAD EXPERT WITH ID 1
        manager = new BCManager(expert_id);        
        manager.answerStore = new AnswerStore(expert_id);
        
        active_premise = manager.getNextPremise();
        manager.printPath_();
        QuestionLabel.setText("Question: " + active_premise.getQuestion());

        setQueueTableReady();

        memory_item_list.setModel(list_model);
        active_rule_list.setModel(list_model2);

        setButtonsReady();
        
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                parent.setVisible(true);
            }
        });
        
        this.parent = parent;
        
    }
    
    public BCConsult(Expert expert, Menu parent) {
        initComponents();
        
        setTitle("Expertise: Backward Chaining");
        howButton.setVisible(false);
        
        titleLabel.setText(expert.getName() + " Experts");
        
        // MANAGER LOAD EXPERT WITH ID 1
        manager = new BCManager(expert.getId());        
        manager.answerStore = new AnswerStore(expert.getId());
        
        active_premise = manager.getNextPremise();
        manager.printPath_();
        QuestionLabel.setText("Question: " + active_premise.getQuestion());

        setQueueTableReady();

        memory_item_list.setModel(list_model);
        active_rule_list.setModel(list_model2);

        setButtonsReady();
        
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                parent.setVisible(true);
            }
        });
        
        this.parent = parent;
        
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
            radio_buttons.get(i).getButton().setBounds(315, 175 + i * 25, 100, 20);
            
            if (i == 0) {
                radio_buttons.get(i).getButton().setSelected(true);
            }

            panel1.add(radio_buttons.get(i).getButton());

            radio_buttons.get(i).getButton().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Answer ans = (Answer) button.getValue();
                    System.out.println(ans.getId() + ans.getAnswer());
                    selected_answer = ans;
                }
            });

            button_group.add(radio_buttons.get(i).getButton());

        }
    }

    public void setMemoryListReady() {
        list_model.removeAllElements();
        for (Object key : manager.getMemory().cache.keySet()) {
            Rule current_rule = manager.getGoalTable().current_rule;
            for (int i = 0; i < current_rule.premises.size(); i++) {
                Premise target = current_rule.premises.get(i);
                if ((int) key == target.getId()) {
                    Answer answer = manager.answerStore.get_answer_by_id (
                        (int) manager.getMemory().cache.get(key)
                    );
                    list_model.addElement(
                            target.getQuestion()
                            + " : "
                            + answer.getAnswer()
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
        active_rule_label.setText(
                "Active Rule: " 
                + manager.getGoalTable().current_rule.getConclusion()
        );
        for (int i = 0; i < manager.getGoalTable().current_rule.premises.size(); i++) {
            Premise premise_target = manager.getGoalTable().current_rule.premises.get(i);
            list_model2.addElement(
                    "<html>" + premise_target.getId() + ". " 
                    + premise_target.getQuestion()
                    + "<br>Actual Value: " 
                    + manager.answerStore.get_answer_by_id(
                            premise_target.getRulesPremiseValue())
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
        whyButton = new javax.swing.JButton();
        howButton = new javax.swing.JButton();
        title = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        Bottom = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        memory_item_list = new javax.swing.JList<>();
        workingMemoryLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        active_rule_list = new javax.swing.JList<>();
        active_rule_label = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        panel1.setBackground(new java.awt.Color(255, 255, 255));

        QuestionLabel.setFont(new java.awt.Font("Palatino Linotype", 1, 24)); // NOI18N
        QuestionLabel.setText("Premise");

        submitButton.setText("submit");
        submitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitButtonActionPerformed(evt);
            }
        });

        conclusionLabel.setFont(new java.awt.Font("Palatino Linotype", 1, 24)); // NOI18N
        conclusionLabel.setText("Conclusion");

        whyButton.setText("why ?");
        whyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                whyButtonActionPerformed(evt);
            }
        });

        howButton.setText("How ?");
        howButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                howButtonActionPerformed(evt);
            }
        });

        title.setBackground(new java.awt.Color(51, 51, 51));

        titleLabel.setFont(new java.awt.Font("Typoster Outline", 0, 36)); // NOI18N
        titleLabel.setForeground(new java.awt.Color(255, 255, 255));
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("Expert's Name");

        javax.swing.GroupLayout titleLayout = new javax.swing.GroupLayout(title);
        title.setLayout(titleLayout);
        titleLayout.setHorizontalGroup(
            titleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(titleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(titleLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 803, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        titleLayout.setVerticalGroup(
            titleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, titleLayout.createSequentialGroup()
                .addContainerGap(37, Short.MAX_VALUE)
                .addComponent(titleLabel)
                .addGap(32, 32, 32))
            .addGroup(titleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(titleLayout.createSequentialGroup()
                    .addContainerGap(89, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        Bottom.setBackground(new java.awt.Color(102, 102, 102));

        memory_item_list.setFont(new java.awt.Font("Palatino Linotype", 0, 13)); // NOI18N
        memory_item_list.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(memory_item_list);

        workingMemoryLabel.setFont(new java.awt.Font("Palatino Linotype", 1, 18)); // NOI18N
        workingMemoryLabel.setForeground(new java.awt.Color(255, 255, 255));
        workingMemoryLabel.setText("Working Memory");

        active_rule_list.setFont(new java.awt.Font("Palatino Linotype", 0, 13)); // NOI18N
        active_rule_list.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(active_rule_list);

        active_rule_label.setFont(new java.awt.Font("Palatino Linotype", 1, 18)); // NOI18N
        active_rule_label.setForeground(new java.awt.Color(255, 255, 255));
        active_rule_label.setText("Active Rule");

        javax.swing.GroupLayout BottomLayout = new javax.swing.GroupLayout(Bottom);
        Bottom.setLayout(BottomLayout);
        BottomLayout.setHorizontalGroup(
            BottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BottomLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(workingMemoryLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 254, Short.MAX_VALUE)
                .addComponent(active_rule_label)
                .addGap(311, 311, 311))
            .addGroup(BottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(BottomLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        BottomLayout.setVerticalGroup(
            BottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BottomLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(active_rule_label)
                    .addComponent(workingMemoryLabel))
                .addContainerGap(227, Short.MAX_VALUE))
            .addGroup(BottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(BottomLayout.createSequentialGroup()
                    .addGap(50, 50, 50)
                    .addGroup(BottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(39, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Bottom, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(conclusionLabel)
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addComponent(submitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(whyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(howButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(QuestionLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(QuestionLabel)
                .addGap(6, 6, 6)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(submitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(whyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(howButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(72, 72, 72)
                .addComponent(conclusionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Bottom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        // int user_answer = getSelectedAnswerId();
        manager.setAnswer(active_premise, selected_answer.getId());
        
        // OUTPUT WORKING MEMORY LIST
        list_temp.addElement(
            "<html>"
            + active_premise.getQuestion() + "<br>User Answer: "
            + manager.answerStore.get_answer_by_id(selected_answer.getId()).getAnswer()
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
            howButton.setVisible(true);
            whyButton.setVisible(false);
            Rule rule = manager.getGoalTable().current_rule;
            QuestionLabel.setText("Question: -");
            conclusionLabel.setText(
                "Conclusion: "
                + "RULE " + rule.getId()
                + ", " + rule.getConclusion()
                + " = " + manager.answerStore.get_answer_by_id(
                        rule.getConclusionValue() ).getAnswer()
            );
            return;
        }

        // GET NEXT PREMISE
        active_premise = manager.getNextPremise();
        if (active_premise == null) {
            return;
        }
        manager.printPath_();

        // OUTPUT LABEL
        QuestionLabel.setText("Question: " + active_premise.getQuestion());
        // SET ACTIVE PREMISE ANSWER OPTION ON RADIO BUTTONS
        setButtonsReady();
    }//GEN-LAST:event_submitButtonActionPerformed

    private void whyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_whyButtonActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(
                this,
                manager.get_path_(),
                "Why ask this question ?",
                JOptionPane.INFORMATION_MESSAGE
        );
    }//GEN-LAST:event_whyButtonActionPerformed

    private void howButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_howButtonActionPerformed
        // TODO add your handling code here:
        // System.out.println(manager.how());
        JOptionPane.showMessageDialog(
                this,
                manager.how(),
                "How can Expertise get this conclusion ?",
                JOptionPane.INFORMATION_MESSAGE
        );
    }//GEN-LAST:event_howButtonActionPerformed

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
            java.util.logging.Logger.getLogger(BCConsult.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BCConsult.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BCConsult.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BCConsult.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BCConsult().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Bottom;
    private javax.swing.JLabel QuestionLabel;
    private javax.swing.JLabel active_rule_label;
    private javax.swing.JList<String> active_rule_list;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JLabel conclusionLabel;
    private javax.swing.JButton howButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JList<String> memory_item_list;
    private javax.swing.JPanel panel1;
    private javax.swing.JButton submitButton;
    private javax.swing.JPanel title;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JButton whyButton;
    private javax.swing.JLabel workingMemoryLabel;
    // End of variables declaration//GEN-END:variables
}
