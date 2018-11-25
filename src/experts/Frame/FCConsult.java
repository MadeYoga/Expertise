/*
 * The MIT License
 *
 * Copyright 2018 owner.
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
import experts.Engine.Manage.FCManager;
import experts.Entities.Answer;
import experts.Entities.Expert;
import experts.Entities.Premise;
import experts.Modified.swing.RadioButton;
import experts.Modified.swing.TextAreaOutputStream;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author owner
 */
public class FCConsult extends javax.swing.JFrame {
    
    public Menu parent;
    public FCManager manager;
    public Premise current_premise;
    public AnswerStore answerStore;
    private DefaultListModel environment_list;
    private DefaultListModel answer_list;
    private ArrayList<RadioButton> radio_buttons = new ArrayList<RadioButton>();
    private ButtonGroup button_group             = new ButtonGroup();
    private Answer selected_answer;
    
    /**
     * Creates new form FCConsult
     */
    public FCConsult() {
        
        initComponents();
        
        setTitle("Expertise: Forward Chaining ");
        
        manager = new FCManager(1);
        
        current_premise = manager.getNextPremise();
        
        questionLabel.setText(
                "Question: " + 
                current_premise.getQuestion()
        );
        
        titleLabel.setText("Expertise");
        
        PrintStream outStream = new PrintStream( new TextAreaOutputStream(textArea));
        textArea.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));

        System.setOut( outStream );
        
        System.setErr( outStream );
        
        environment_list = new DefaultListModel();
        
        answer_list = new DefaultListModel();
        
        answerStore = new AnswerStore();
        
        selected_answer = null;
        
        setAnswerList();
        
        howButton.setVisible(false);
        
    }
    
    public FCConsult(int _expert_id, Menu parent){
        
        initComponents();
        
        setTitle("Expertise: Forward Chaining ");
        
        manager = new FCManager(_expert_id);
        
        current_premise = manager.getNextPremise();
        
        questionLabel.setText(
                "Question: " + 
                current_premise.getQuestion()
        );
        
        titleLabel.setText("Expertise");
        
        PrintStream outStream = new PrintStream( new TextAreaOutputStream(textArea));
        
        textArea.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
        
        System.setOut( outStream );
        
        System.setErr( outStream );
        
        environment_list = new DefaultListModel();
        
        answer_list = new DefaultListModel();
        
        answerStore = new AnswerStore(_expert_id);
        
        selected_answer = null;
        
        setAnswerList();
        
        howButton.setVisible(false);
        
        this.parent = parent;
        
    }
    
    public FCConsult(Expert expert, Menu parent){
        
        initComponents();
        
        setTitle("Expertise: Forward Chaining ");
        
        manager = new FCManager(expert.getId());
        
        current_premise = manager.getNextPremise();
        
        questionLabel.setText(
                "Question: " + 
                current_premise.getQuestion()
        );
        
        titleLabel.setText(expert.getName() + " Experts");
        
        PrintStream outStream = new PrintStream( new TextAreaOutputStream(textArea));
        
        textArea.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
        
        System.setOut( outStream );
        
        System.setErr( outStream );
        
        environment_list = new DefaultListModel();
        
        answer_list = new DefaultListModel();
        
        answerStore = new AnswerStore(expert.getId());
        
        selected_answer = null;
        
        setAnswerList();
        
        howButton.setVisible(false);
        
        this.parent = parent;
        
    }
    
    public void setEnvironmentList() {
        environment_list.clear();
        for (Object key : manager.working_memory.environment.keySet())
        {
            Object variable = manager.working_memory.environment.get(key);
            environment_list.addElement (
                    "<html>" + 
                    key.toString() + " -> " + 
                    answerStore.get_answer_by_id( (int)variable ) + 
                    "</html>"
            );
        }
        environmentList.setModel(environment_list);
    }
    
    public void setAnswerList() {
        for (int i = 0; i < radio_buttons.size(); i++) {
            this.getContentPane().remove(radio_buttons.get(i).getButton());
        }
        answer_list.clear();
        radio_buttons.clear();
        for (int i = 0; i < current_premise.list_of_answer.size(); i++) {
            RadioButton button = new RadioButton();
            button.setValue(current_premise.list_of_answer.get(i));
            button.setText(current_premise.list_of_answer.get(i).getAnswer());
            radio_buttons.add(button);
            radio_buttons.get(i).getButton().setBounds(300, 140 + i * 25, 100, 20);
            this.getContentPane().add(radio_buttons.get(i).getButton());
            radio_buttons.get(i).getButton().addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    Answer ans = (Answer) button.getValue();
                    System.out.println(ans.getId() + ". " + ans.getAnswer());
                    selected_answer = ans;
                }
            });
            button_group.add(radio_buttons.get(i).getButton());
        }
        button_group.clearSelection();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        questionLabel = new javax.swing.JLabel();
        submitButton = new javax.swing.JButton();
        whyButton = new javax.swing.JButton();
        conclusionLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        environmentList = new javax.swing.JList<>();
        titleLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        howButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(800, 600));
        setResizable(false);

        questionLabel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        questionLabel.setText("Question: ");

        submitButton.setText("Submit");
        submitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitButtonActionPerformed(evt);
            }
        });

        whyButton.setText("why");
        whyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                whyButtonActionPerformed(evt);
            }
        });

        conclusionLabel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        conclusionLabel.setText("Conclusion: ");

        textArea.setColumns(20);
        textArea.setRows(5);
        jScrollPane1.setViewportView(textArea);

        environmentList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(environmentList);

        titleLabel.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        titleLabel.setText("Expert's Name");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Logs");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Environment");

        howButton.setText("How?");
        howButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                howButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(conclusionLabel)
                            .addComponent(jLabel2)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(submitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(whyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(howButton, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addGap(211, 211, 211))))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(questionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 757, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 760, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(titleLabel))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(titleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(questionLabel)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(howButton, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(whyButton, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(submitButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(conclusionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 131, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitButtonActionPerformed
        // TODO add your handling code here:
        
        if (current_premise == null) {
            System.out.println("current_premise: null");
            return;
        }
        
        manager.setAnswer(current_premise, selected_answer.getId());
        
        setEnvironmentList();
        
        if (manager.isObtainConclusion()) {
            conclusionLabel.setText(
                    "Conclusion: " + 
                    manager.last_triggered_rule.getConclusion()
            );
            questionLabel.setText("Question: -");
            whyButton.setVisible(false);
            howButton.setVisible(true);
        } else if (manager.isUnknownConclusion()) {
            conclusionLabel.setText("Conclusion: UNKNOWN");
            questionLabel.setText("Question: -");
            whyButton.setVisible(false);
            howButton.setVisible(true);
        }
        
        current_premise = manager.getNextPremise();
        
        if (current_premise == null) {
            return;
        }
        
        questionLabel.setText("Question: " + current_premise.getQuestion());
        
    }//GEN-LAST:event_submitButtonActionPerformed

    private void whyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_whyButtonActionPerformed
        // TODO add your handling code here:
        if (manager.marked_rule == null) {
            JOptionPane.showMessageDialog (
                this, "this is the First Question!", "Why ask this question ?",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }
        String msg = "Rule " + manager.marked_rule.getConclusion() + "\n";
        for (int i = 0; i < manager.marked_rule.premises.size(); i++) {
            msg += (i + 1) + ". " + manager.marked_rule.premises.get(i).getQuestion() + "\n";
        }
        JOptionPane.showMessageDialog(
                this, msg, "Why ask this question ?",
                JOptionPane.INFORMATION_MESSAGE
        );
    }//GEN-LAST:event_whyButtonActionPerformed

    private void howButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_howButtonActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog (
            this, manager.how(), "How can Expertise get this conclusion ?",
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
            java.util.logging.Logger.getLogger(FCConsult.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FCConsult.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FCConsult.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FCConsult.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FCConsult().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel conclusionLabel;
    private javax.swing.JList<String> environmentList;
    private javax.swing.JButton howButton;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel questionLabel;
    private javax.swing.JButton submitButton;
    private javax.swing.JTextArea textArea;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JButton whyButton;
    // End of variables declaration//GEN-END:variables
}
