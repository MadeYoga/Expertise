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
import experts.Engine.cf.MYCIN;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author owner
 */
public class FCConsult extends javax.swing.JFrame {
    
    public  Menu                   parent;
    public  FCManager              manager;
    public  Premise                current_premise;
    public  AnswerStore            answerStore;
    private DefaultListModel       environment_list;
    private DefaultListModel       answer_list;
    private ArrayList<RadioButton> radio_buttons = new ArrayList<RadioButton>();
    private ButtonGroup            button_group  = new ButtonGroup();
    private Answer                 selected_answer;
    
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
        
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                parent.setVisible(true);
            }
        });
        
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
        
        setEnvironmentList();
        
        howButton.setVisible(false);
        
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                parent.setVisible(true);
            }
        });
        
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
            //param x,y,w,h
            radio_buttons.get(i).getButton().setBounds(305, 150 + i * 25, 200, 20);
            
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
        howButton = new javax.swing.JButton();
        conclusionLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        titleLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        environmentList = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(800, 600));
        setResizable(false);

        questionLabel.setBackground(new java.awt.Color(255, 255, 255));
        questionLabel.setFont(new java.awt.Font("Palatino Linotype", 1, 24)); // NOI18N
        questionLabel.setText("Question: ");

        submitButton.setFont(new java.awt.Font("Palatino Linotype", 0, 13)); // NOI18N
        submitButton.setText("Submit");
        submitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitButtonActionPerformed(evt);
            }
        });

        whyButton.setFont(new java.awt.Font("Palatino Linotype", 0, 13)); // NOI18N
        whyButton.setText("why");
        whyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                whyButtonActionPerformed(evt);
            }
        });

        howButton.setFont(new java.awt.Font("Palatino Linotype", 0, 13)); // NOI18N
        howButton.setText("How?");
        howButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                howButtonActionPerformed(evt);
            }
        });

        conclusionLabel.setFont(new java.awt.Font("Palatino Linotype", 1, 24)); // NOI18N
        conclusionLabel.setText("Conclusion: ");

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));

        titleLabel.setFont(new java.awt.Font("Typoster Outline", 0, 36)); // NOI18N
        titleLabel.setForeground(new java.awt.Color(255, 255, 255));
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("Expert's Name");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(102, 102, 102));

        textArea.setColumns(20);
        textArea.setFont(new java.awt.Font("Palatino Linotype", 0, 13)); // NOI18N
        textArea.setRows(5);
        jScrollPane1.setViewportView(textArea);

        environmentList.setFont(new java.awt.Font("Palatino Linotype", 0, 13)); // NOI18N
        environmentList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(environmentList);

        jLabel2.setFont(new java.awt.Font("Palatino Linotype", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Logs");

        jLabel3.setFont(new java.awt.Font("Palatino Linotype", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Environment");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(11, 11, 11)
                            .addComponent(jLabel2))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                            .addGap(0, 38, Short.MAX_VALUE)
                            .addComponent(jLabel3)
                            .addGap(199, 199, 199)))
                    .addContainerGap()))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 259, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jLabel3))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane2)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(questionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 757, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(conclusionLabel)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(submitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(whyButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(howButton, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(76, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(questionLabel)
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(howButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(whyButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(submitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(conclusionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitButtonActionPerformed
        // TODO add your handling code here:
        
        if (selected_answer == null) {
            JOptionPane.showMessageDialog (
                this, "Choose 1 answer", "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        if (current_premise == null) {
            System.out.println("current_premise: null");
            return;
        }
        
        manager.setAnswer(current_premise, selected_answer.getId());
        
        setEnvironmentList();
        
        if (manager.isObtainConclusion()) {
            try {
                conclusionLabel.setText(
                    "Conclusion: " +
                    new MYCIN().getUncertaintyTerm(manager.last_triggered_rule)
                    + " " + 
                    manager.last_triggered_rule.getConclusion() + " = " + 
                    answerStore.get_answer_by_id(
                            manager.last_triggered_rule.getConclusionValue())
                );
                questionLabel.setText("Question: -");
                whyButton.setVisible(false);
                howButton.setVisible(true);
            } catch (Exception ex) {
                Logger.getLogger(FCConsult.class.getName()).log(Level.SEVERE, null, ex);
            }
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
        
        setAnswerList();
        
        questionLabel.setText("Question: " + current_premise.getQuestion());
        
    }//GEN-LAST:event_submitButtonActionPerformed

    private void whyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_whyButtonActionPerformed
        // TODO add your handling code here:
        if (manager.marked_rule == null) {
            String msg = "Rule " + manager.first_rule.getConclusion() + " = " + 
                    answerStore.get_answer_by_id(
                            manager.first_rule.getConclusionValue()
                    ) + "\n";
            for (int i = 0; i < manager.first_rule.premises.size(); i++) {
                Premise p = manager.first_rule.premises.get(i);
                msg += (i + 1) + ". " + p.getQuestion() + " = " + 
                        answerStore.get_answer_by_id(p.getRulesPremiseValue()) + 
                        "\n";
            }
            JOptionPane.showMessageDialog (
                this, msg, "Why ask this question ?",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }
        String msg = "Rule " + manager.marked_rule.getConclusion() + "\n";
        for (int i = 0; i < manager.marked_rule.premises.size(); i++) {
            msg += (i + 1) + ". " + manager.marked_rule.premises.get(i).getQuestion() + "\n";
        }
        JOptionPane.showMessageDialog(
                this, 
                "the question is being asked because:\n" + msg, 
                "Why ask this question ?",
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
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
