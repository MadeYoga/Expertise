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
import experts.Input.Database.*;
import experts.Input.Entities.*;
import experts.Input.Form.MainForm;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Windows
 */
public class AddRule extends javax.swing.JPanel
{
    protected MainForm _MainForm = null;
    protected int _ExpertID = 1;
    protected int _RuleIDCounter = 1;
    protected ArrayList<Premise> _Premise = new ArrayList<Premise>();
    protected ArrayList<Answer> _Answer = new ArrayList<Answer>();
    
    /**
     * Creates new form AddRule
     */
    public AddRule()
    {
	initComponents();
    }

    public AddRule(MainForm frame, int expert_id)
    {
	initComponents();
	this._MainForm = frame;
	this._ExpertID = expert_id;
	this.LoadAnswer();
	this.LoadPremise();
    }
     
    public void LoadAnswer()
    {
	AnswerDatabase db = new AnswerDatabase();
	String[] answer = db.LoadAnswer(this._ExpertID);

	DefaultComboBoxModel model = (DefaultComboBoxModel) this.AnswerList.getModel();
        
        model.removeAllElements();

        for (String item : answer) 
	{
            model.addElement(item);
        }
	
	this.AnswerList.setModel(model);
	this.ConclusionValueList.setModel(model);
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
    
    public boolean ErrorHandling()
    {
	//ERROR HANDLING
	//CONCLUSION CAN'T EMPTY
	if(this.ConclusionText.getText().trim().equals(""))
	{
	    JOptionPane.showMessageDialog(this, "Conclusion can't empty.");
	    return false;
	}
	
	return true;
    }
    
    public boolean HasSamePremise(Premise premise)
    {
	for (int i = 0; i < this._Premise.size(); i++)
	{
	    if(this._Premise.get(i).GetName().equals(premise.GetName()))
	    {
		return true;
	    }
	}
	return false;
    }
    
    public void AddPremise()
    {
	//create temporary premise variable
	Premise premise = new Premise();
	premise.setId(Integer.parseInt(this.PremiseList.getSelectedItem().toString().substring(0, this.PremiseList.getSelectedItem().toString().indexOf(".")).trim()));
	premise.SetName(this.PremiseList.getSelectedItem().toString().substring(this.PremiseList.getSelectedItem().toString().indexOf(".") + 1, this.PremiseList.getSelectedItem().toString().length()).trim());
	
	//create temporary answer variable
	Answer answer = new Answer();
	answer.setId(Integer.parseInt(this.AnswerList.getSelectedItem().toString().substring(0, this.AnswerList.getSelectedItem().toString().indexOf(".")).trim()));
	answer.setAnswer(this.AnswerList.getSelectedItem().toString().substring(this.AnswerList.getSelectedItem().toString().indexOf(".") + 1, this.AnswerList.getSelectedItem().toString().length()).trim());
	
	if(this.HasSamePremise(premise) == true)
	{
	    JOptionPane.showMessageDialog(this, premise.GetName() + " already recorded.");
	    return;
	}
	
	//add to container variable
	this._Premise.add(premise);
	this._Answer.add(answer);
	
	//add to entered premise 
	String entered_premise = this.EnteredPremiseList.getText();
	entered_premise += premise.GetName() + " = " + answer.getAnswer();
	entered_premise += "\n";
	this.EnteredPremiseList.setText(entered_premise);
    }
    
    public void ResetRule()
    {
	this.EnteredPremiseList.setText("");
	this.ConclusionText.setText("");
	
	this._Answer.clear();
	this._Answer = new ArrayList<Answer>();
	
	this._Premise.clear();
	this._Premise = new ArrayList<Premise>();
    }
    
    /**
     * Set next rule
     */
    public void NextRule()
    {
	if (this.ErrorHandling() == false)
	{
	    return;
	}
	
	//save rule (answer, premise, conclusion) to database
	RuleDatabase db = new RuleDatabase();
	int conclusion_answer_id = Integer.parseInt(this.ConclusionValueList.getSelectedItem().toString().substring(0, this.ConclusionValueList.getSelectedItem().toString().indexOf(".")));
	int rule_id = db.InsertRule(this._ExpertID, this.ConclusionText.getText(), conclusion_answer_id);
	
	//save rulepremise (rule id, premise id, answer id) 
	//create premise id
	int[] premis_id = new int[this._Premise.size()];
	for(int i = 0; i < this._Premise.size(); i++)
	{
	    premis_id[i] = this._Premise.get(i).getId();
	}
	
	//create answer id
	int[] answer_id = new int[this._Answer.size()];
	for(int i = 0; i < this._Answer.size(); i++)
	{
	    answer_id[i] = this._Answer.get(i).getId();
	}
	
	//insert into database
	db.InserRulePremise(rule_id, premis_id, answer_id);
	
	//increment rule counter
	this._RuleIDCounter += 1;
	this.RuleIDLabel.setText("Rule ID " + this._RuleIDCounter);
	
	//clear
	this.ResetRule();
    }
    
    public void FinishAddRule()
    {
	int result = JOptionPane.showConfirmDialog
			(this, 
			"Go to next step ?", 
			"Warning",JOptionPane.YES_NO_OPTION);

	if (result == JOptionPane.YES_OPTION)
	{
	    //buat form baru dengan conainer yang berbeda
//	    MainForm form = new MainForm();
//	    AddPremiseRule temp = new AddPremiseRule(form, this._ExpertID);
//	    form.SetContainer(temp);
//	    form.setVisible(true);
//	    this._MainForm.Close();
            Menu main_menu = new Menu();
            main_menu.setVisible(true);
            this._MainForm.dispose();
	}
	
	return;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        RuleIDLabel = new javax.swing.JLabel();
        PremiseLabel = new javax.swing.JLabel();
        PremiseList = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        AnswerLabel = new javax.swing.JLabel();
        AnswerList = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        EnteredPremiseList = new javax.swing.JTextArea();
        AddButton = new javax.swing.JButton();
        ResetButton = new javax.swing.JButton();
        NextButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        FinishButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        ConclusionText = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        ConclusionValueList = new javax.swing.JComboBox<>();

        setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        setPreferredSize(new java.awt.Dimension(500, 400));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        RuleIDLabel.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        RuleIDLabel.setText("Rule ID 1");
        add(RuleIDLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, -1));

        PremiseLabel.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        PremiseLabel.setText("Premise");
        add(PremiseLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, -1, -1));

        PremiseList.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        PremiseList.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        PremiseList.setToolTipText("Answer for premise.");
        PremiseList.setPreferredSize(new java.awt.Dimension(200, 31));
        add(PremiseList, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 200, -1));

        jLabel1.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("=");
        jLabel1.setPreferredSize(new java.awt.Dimension(20, 25));
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 100, -1, 30));

        AnswerLabel.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        AnswerLabel.setText("Answer");
        add(AnswerLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 70, -1, -1));

        AnswerList.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        AnswerList.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        AnswerList.setToolTipText("Answer for premise.");
        AnswerList.setPreferredSize(new java.awt.Dimension(200, 31));
        add(AnswerList, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 100, 200, -1));

        EnteredPremiseList.setEditable(false);
        EnteredPremiseList.setColumns(20);
        EnteredPremiseList.setRows(5);
        jScrollPane1.setViewportView(EnteredPremiseList);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 300, 110));

        AddButton.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        AddButton.setText("Add");
        AddButton.setToolTipText("Add Premise to rule.");
        AddButton.setPreferredSize(new java.awt.Dimension(150, 33));
        AddButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                AddButtonActionPerformed(evt);
            }
        });
        add(AddButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 180, -1, -1));

        ResetButton.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        ResetButton.setText("Reset");
        ResetButton.setToolTipText("Reset all premise in this rule.");
        ResetButton.setPreferredSize(new java.awt.Dimension(150, 33));
        ResetButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                ResetButtonActionPerformed(evt);
            }
        });
        add(ResetButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 220, -1, -1));

        NextButton.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        NextButton.setText("Next");
        NextButton.setToolTipText("Insert new rule.");
        NextButton.setPreferredSize(new java.awt.Dimension(150, 33));
        NextButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                NextButtonActionPerformed(evt);
            }
        });
        add(NextButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 260, -1, -1));

        jLabel2.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        jLabel2.setText("Entered Premise :");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 150, -1, -1));

        FinishButton.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        FinishButton.setText("Finish");
        FinishButton.setToolTipText("Finish insert rule.");
        FinishButton.setPreferredSize(new java.awt.Dimension(150, 33));
        FinishButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                FinishButtonActionPerformed(evt);
            }
        });
        add(FinishButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 340, -1, 33));

        jLabel3.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        jLabel3.setText("Conclusion :");
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, -1, -1));

        ConclusionText.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        ConclusionText.setToolTipText("Premise name.");
        ConclusionText.setPreferredSize(new java.awt.Dimension(150, 31));
        add(ConclusionText, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 300, -1, -1));

        jLabel4.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        jLabel4.setText("Value      :");
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, -1, -1));

        ConclusionValueList.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        ConclusionValueList.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        ConclusionValueList.setToolTipText("Answer for premise.");
        ConclusionValueList.setPreferredSize(new java.awt.Dimension(200, 31));
        add(ConclusionValueList, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 340, 150, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void AddButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_AddButtonActionPerformed
    {//GEN-HEADEREND:event_AddButtonActionPerformed
        this.AddPremise();
    }//GEN-LAST:event_AddButtonActionPerformed

    private void ResetButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_ResetButtonActionPerformed
    {//GEN-HEADEREND:event_ResetButtonActionPerformed
        this.ResetRule();
    }//GEN-LAST:event_ResetButtonActionPerformed

    private void NextButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_NextButtonActionPerformed
    {//GEN-HEADEREND:event_NextButtonActionPerformed
        this.NextRule();
    }//GEN-LAST:event_NextButtonActionPerformed

    private void FinishButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_FinishButtonActionPerformed
    {//GEN-HEADEREND:event_FinishButtonActionPerformed
        this.FinishAddRule();
    }//GEN-LAST:event_FinishButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JButton AddButton;
    protected javax.swing.JLabel AnswerLabel;
    protected javax.swing.JComboBox<String> AnswerList;
    protected javax.swing.JTextField ConclusionText;
    protected javax.swing.JComboBox<String> ConclusionValueList;
    protected javax.swing.JTextArea EnteredPremiseList;
    protected javax.swing.JButton FinishButton;
    protected javax.swing.JButton NextButton;
    protected javax.swing.JLabel PremiseLabel;
    protected javax.swing.JComboBox<String> PremiseList;
    protected javax.swing.JButton ResetButton;
    protected javax.swing.JLabel RuleIDLabel;
    protected javax.swing.JLabel jLabel1;
    protected javax.swing.JLabel jLabel2;
    protected javax.swing.JLabel jLabel3;
    protected javax.swing.JLabel jLabel4;
    protected javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
