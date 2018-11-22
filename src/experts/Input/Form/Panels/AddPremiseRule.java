/*
 * The MIT License
 *
 * Copyright 2018 Windows.
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
package experts.Input.Form.Panels;

import experts.Frame.Menu;
import experts.Input.Database.PremiseDatabase;
import experts.Input.Database.RuleDatabase;
import experts.Input.Form.MainForm;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Windows
 */
public class AddPremiseRule extends javax.swing.JPanel
{
    protected MainForm _MainForm = null;
    protected int _ExpertID = -1;
    
    protected ArrayList <Integer> RuleID = new ArrayList<Integer>();
    
    /**
     * Creates new form AddPremiseRule
     */
    public AddPremiseRule()
    {
	initComponents();
    }

    public AddPremiseRule(MainForm frame, int expert_id)
    {
	initComponents();
	this._MainForm = frame;
	this._ExpertID = expert_id;
        this.LoadPremise();
        this.LoadRule();
        //this.RuleDetail.setText(this.RuleList.getSelectedItem().toString());
        this.SelectedRuleListItemChanged();
    }

    public void LoadPremise()
    {
	PremiseDatabase db = new PremiseDatabase();
	String[] premise = db.LoadPremise(this._ExpertID);

	DefaultComboBoxModel model = (DefaultComboBoxModel) this.PremiseList.getModel();
        
        model.removeAllElements();

        for (String item : premise) 
	{
            model.addElement(item);
        }
	
	this.PremiseList.setModel(model);
    }
    
    public void LoadRule()
    {
	RuleDatabase db = new RuleDatabase();
	String[] rule = db.LoadRule(this._ExpertID);

	DefaultComboBoxModel model = (DefaultComboBoxModel) this.RuleList.getModel();
        
        model.removeAllElements();

        for (String item : rule) 
	{
            model.addElement(item);
        }
	
	this.RuleList.setModel(model);
    }
    
    public void AddPremiseRule()
    {
        //set log 
        String log = this.Log.getText();
        log += this.PremiseList.getSelectedItem().toString() + " = " + this.RuleList.getSelectedItem().toString();
        log += "\n";
        this.Log.setText(log);
        
        String rule = this.RuleList.getSelectedItem().toString();
        rule = rule.substring(0, rule.indexOf("."));
        
        //add to array
        this.RuleID.add(Integer.parseInt(rule));
    }
    
    public void ResetPremiseRule()
    {
        this.Log.setText("");
        this.RuleDetail.setText("");
        this.RuleID.clear();
        this.RuleID = new ArrayList<Integer>();
    }
    
    public void NextPremiseRule()
    {
        //get premise id
        String temp = this.PremiseList.getSelectedItem().toString();
        temp = temp.substring(0, temp.indexOf("."));
        int premis_id = Integer.parseInt(temp);
        
        int[] array_rule_id = new int[this.RuleID.size()];
        for (int i = 0; i < this.RuleID.size(); i++)
        {
            array_rule_id[i] = this.RuleID.get(i);
        }
        
        //save to database
        RuleDatabase db = new RuleDatabase();
        db.InsertPremiseRule(premis_id, array_rule_id);
        
        this.ResetPremiseRule();
    }
    
    public void FinishAddPremiseRule()
    {
	int result = JOptionPane.showConfirmDialog
			(this, 
			"Go main menu ?", 
			"Warning",JOptionPane.YES_NO_OPTION);

	if (result == JOptionPane.YES_OPTION)
	{
            //To main menu
            Menu main_menu = new Menu();
            main_menu.setVisible(true);
            this._MainForm.dispose();
	}
	
	return;
    }
    
    public void SelectedRuleListItemChanged()
    {
        if (this.RuleList.getSelectedItem() == null)
        {
            return;
        }
        
        int rule_id = Integer.parseInt(this.RuleList.getSelectedItem().toString().substring(0, this.RuleList.getSelectedItem().toString().indexOf(".")));
        
        PremiseDatabase db = new PremiseDatabase();
        this.RuleDetail.setText(db.GetPremiseFromRule(rule_id));

        //temporary rule detail, will changed another day
        //this.RuleDetail.setText(this.RuleList.getSelectedItem().toString());        
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        PremiseList = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        RuleList = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        RuleDetail = new javax.swing.JTextArea();
        AddButton = new javax.swing.JButton();
        ResetButton = new javax.swing.JButton();
        NextButton = new javax.swing.JButton();
        FinishButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        Log = new javax.swing.JTextArea();

        setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        setMaximumSize(new java.awt.Dimension(1920, 1080));
        setMinimumSize(new java.awt.Dimension(1024, 600));
        setPreferredSize(new java.awt.Dimension(1024, 600));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Monospaced", 0, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Add Premise Rule");
        jLabel1.setMaximumSize(new java.awt.Dimension(360, 50));
        jLabel1.setMinimumSize(new java.awt.Dimension(360, 50));
        jLabel1.setPreferredSize(new java.awt.Dimension(360, 50));
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(332, 50, -1, -1));

        jLabel2.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("Premise");
        jLabel2.setMaximumSize(new java.awt.Dimension(200, 35));
        jLabel2.setMinimumSize(new java.awt.Dimension(200, 35));
        jLabel2.setPreferredSize(new java.awt.Dimension(200, 35));
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(282, 140, -1, -1));

        PremiseList.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        PremiseList.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        PremiseList.setMaximumSize(new java.awt.Dimension(250, 35));
        PremiseList.setMinimumSize(new java.awt.Dimension(250, 35));
        PremiseList.setPreferredSize(new java.awt.Dimension(250, 35));
        add(PremiseList, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 140, -1, -1));

        jLabel3.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        jLabel3.setText("Rule");
        jLabel3.setMaximumSize(new java.awt.Dimension(200, 35));
        jLabel3.setMinimumSize(new java.awt.Dimension(200, 35));
        jLabel3.setPreferredSize(new java.awt.Dimension(200, 35));
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(282, 180, -1, -1));

        RuleList.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        RuleList.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        RuleList.setMaximumSize(new java.awt.Dimension(250, 35));
        RuleList.setMinimumSize(new java.awt.Dimension(250, 35));
        RuleList.setPreferredSize(new java.awt.Dimension(250, 35));
        RuleList.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                RuleListItemStateChanged(evt);
            }
        });
        add(RuleList, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 180, -1, -1));

        jScrollPane1.setPreferredSize(new java.awt.Dimension(450, 131));

        RuleDetail.setEditable(false);
        RuleDetail.setColumns(20);
        RuleDetail.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        RuleDetail.setRows(5);
        jScrollPane1.setViewportView(RuleDetail);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(282, 230, -1, -1));

        AddButton.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        AddButton.setText("Add");
        AddButton.setMaximumSize(new java.awt.Dimension(100, 35));
        AddButton.setMinimumSize(new java.awt.Dimension(100, 35));
        AddButton.setPreferredSize(new java.awt.Dimension(100, 35));
        AddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddButtonActionPerformed(evt);
            }
        });
        add(AddButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 180, -1, -1));

        ResetButton.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        ResetButton.setText("Reset");
        ResetButton.setMaximumSize(new java.awt.Dimension(100, 35));
        ResetButton.setMinimumSize(new java.awt.Dimension(100, 35));
        ResetButton.setPreferredSize(new java.awt.Dimension(100, 35));
        ResetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ResetButtonActionPerformed(evt);
            }
        });
        add(ResetButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 230, -1, -1));

        NextButton.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        NextButton.setText("Next");
        NextButton.setMaximumSize(new java.awt.Dimension(100, 35));
        NextButton.setMinimumSize(new java.awt.Dimension(100, 35));
        NextButton.setPreferredSize(new java.awt.Dimension(100, 35));
        NextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NextButtonActionPerformed(evt);
            }
        });
        add(NextButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(632, 370, -1, -1));

        FinishButton.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        FinishButton.setText("Finish");
        FinishButton.setMaximumSize(new java.awt.Dimension(100, 35));
        FinishButton.setMinimumSize(new java.awt.Dimension(100, 35));
        FinishButton.setPreferredSize(new java.awt.Dimension(100, 35));
        FinishButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FinishButtonActionPerformed(evt);
            }
        });
        add(FinishButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(632, 420, -1, -1));

        jScrollPane2.setPreferredSize(new java.awt.Dimension(200, 100));

        Log.setColumns(20);
        Log.setRows(5);
        jScrollPane2.setViewportView(Log);

        add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 370, 330, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void AddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddButtonActionPerformed
        this.AddPremiseRule();
    }//GEN-LAST:event_AddButtonActionPerformed

    private void ResetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ResetButtonActionPerformed
        this.ResetPremiseRule();
    }//GEN-LAST:event_ResetButtonActionPerformed

    private void NextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NextButtonActionPerformed
        this.NextPremiseRule();
    }//GEN-LAST:event_NextButtonActionPerformed

    private void FinishButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FinishButtonActionPerformed
        this.FinishAddPremiseRule();
    }//GEN-LAST:event_FinishButtonActionPerformed

    private void RuleListItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_RuleListItemStateChanged
        this.SelectedRuleListItemChanged();
    }//GEN-LAST:event_RuleListItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JButton AddButton;
    protected javax.swing.JButton FinishButton;
    protected javax.swing.JTextArea Log;
    protected javax.swing.JButton NextButton;
    protected javax.swing.JComboBox<String> PremiseList;
    protected javax.swing.JButton ResetButton;
    protected javax.swing.JTextArea RuleDetail;
    protected javax.swing.JComboBox<String> RuleList;
    protected javax.swing.JLabel jLabel1;
    protected javax.swing.JLabel jLabel2;
    protected javax.swing.JLabel jLabel3;
    protected javax.swing.JScrollPane jScrollPane1;
    protected javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
