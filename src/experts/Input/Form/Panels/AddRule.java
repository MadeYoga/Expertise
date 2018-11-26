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
	
        int hirarki = 2; //default to non-final rule
        int result = JOptionPane.showConfirmDialog
			(this, 
			"Final rule?", 
			"Warning",JOptionPane.YES_NO_OPTION);

	if (result == JOptionPane.YES_OPTION)
	{
            hirarki = 1; //set to final rule
	}
        
	//save rule (answer, premise, conclusion, hierarchy) to database
	RuleDatabase db = new RuleDatabase();
	int conclusion_answer_id = Integer.parseInt(this.ConclusionValueList.getSelectedItem().toString().substring(0, this.ConclusionValueList.getSelectedItem().toString().indexOf(".")));
	int rule_id = db.InsertRule(this._ExpertID, this.ConclusionText.getText(), conclusion_answer_id, hirarki, (double)this.CertaintyFactorSpinner.getValue());
	
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
	    MainForm form = new MainForm();
	    AddPremiseRule temp = new AddPremiseRule(form, this._ExpertID);
	    form.SetContainer(temp);
	    form.setVisible(true);
	    this._MainForm.Close();
//            Menu main_menu = new Menu();
//            main_menu.setVisible(true);
//            this._MainForm.dispose();
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
    private void initComponents() {

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
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        CertaintyFactorSpinner = new javax.swing.JSpinner();

        setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        setMaximumSize(new java.awt.Dimension(1920, 1080));
        setMinimumSize(new java.awt.Dimension(1024, 600));
        setPreferredSize(new java.awt.Dimension(1024, 600));

        RuleIDLabel.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        RuleIDLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        RuleIDLabel.setText("Rule ID 1");
        RuleIDLabel.setMaximumSize(new java.awt.Dimension(300, 50));
        RuleIDLabel.setMinimumSize(new java.awt.Dimension(300, 50));
        RuleIDLabel.setPreferredSize(new java.awt.Dimension(300, 50));

        PremiseLabel.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        PremiseLabel.setText("Premise");
        PremiseLabel.setPreferredSize(new java.awt.Dimension(150, 25));

        PremiseList.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        PremiseList.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        PremiseList.setToolTipText("Answer for premise.");
        PremiseList.setPreferredSize(new java.awt.Dimension(200, 31));

        jLabel1.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("=");
        jLabel1.setPreferredSize(new java.awt.Dimension(20, 25));

        AnswerLabel.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        AnswerLabel.setText("Answer");

        AnswerList.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        AnswerList.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        AnswerList.setToolTipText("Answer for premise.");
        AnswerList.setPreferredSize(new java.awt.Dimension(200, 31));

        jScrollPane1.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        jScrollPane1.setMaximumSize(new java.awt.Dimension(320, 150));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(320, 150));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(320, 150));

        EnteredPremiseList.setEditable(false);
        EnteredPremiseList.setColumns(20);
        EnteredPremiseList.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        EnteredPremiseList.setRows(5);
        jScrollPane1.setViewportView(EnteredPremiseList);

        AddButton.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        AddButton.setText("Add");
        AddButton.setToolTipText("Add Premise to rule.");
        AddButton.setPreferredSize(new java.awt.Dimension(150, 33));
        AddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddButtonActionPerformed(evt);
            }
        });

        ResetButton.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        ResetButton.setText("Reset");
        ResetButton.setToolTipText("Reset all premise in this rule.");
        ResetButton.setPreferredSize(new java.awt.Dimension(150, 33));
        ResetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ResetButtonActionPerformed(evt);
            }
        });

        NextButton.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        NextButton.setText("Next");
        NextButton.setToolTipText("Insert new rule.");
        NextButton.setPreferredSize(new java.awt.Dimension(150, 33));
        NextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NextButtonActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        jLabel2.setText("Entered Premise :");
        jLabel2.setMaximumSize(new java.awt.Dimension(200, 25));
        jLabel2.setMinimumSize(new java.awt.Dimension(200, 25));
        jLabel2.setPreferredSize(new java.awt.Dimension(200, 25));

        FinishButton.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        FinishButton.setText("Finish");
        FinishButton.setToolTipText("Finish insert rule.");
        FinishButton.setPreferredSize(new java.awt.Dimension(150, 33));
        FinishButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FinishButtonActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("Conclusion :");
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabel3.setPreferredSize(new java.awt.Dimension(150, 25));

        ConclusionText.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        ConclusionText.setToolTipText("Premise name.");
        ConclusionText.setPreferredSize(new java.awt.Dimension(200, 31));

        jLabel4.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel4.setText("Value      :");
        jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        jLabel4.setPreferredSize(new java.awt.Dimension(150, 25));

        ConclusionValueList.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        ConclusionValueList.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        ConclusionValueList.setToolTipText("Answer for premise.");
        ConclusionValueList.setPreferredSize(new java.awt.Dimension(200, 31));

        jLabel5.setFont(new java.awt.Font("Monospaced", 0, 36)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Add Rule");
        jLabel5.setMaximumSize(new java.awt.Dimension(300, 50));
        jLabel5.setMinimumSize(new java.awt.Dimension(300, 50));
        jLabel5.setPreferredSize(new java.awt.Dimension(300, 50));

        jLabel6.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        jLabel6.setText("Certainty  :");
        jLabel6.setMaximumSize(new java.awt.Dimension(200, 25));
        jLabel6.setMinimumSize(new java.awt.Dimension(200, 25));
        jLabel6.setPreferredSize(new java.awt.Dimension(150, 25));

        CertaintyFactorSpinner.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        CertaintyFactorSpinner.setModel(new javax.swing.SpinnerNumberModel(1.0d, -1.1d, 1.1d, 0.10000000149011612d));
        CertaintyFactorSpinner.setMinimumSize(new java.awt.Dimension(200, 20));
        CertaintyFactorSpinner.setPreferredSize(new java.awt.Dimension(200, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(252, 252, 252)
                .addComponent(PremiseLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(120, 120, 120)
                .addComponent(AnswerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(252, 252, 252))
            .addGroup(layout.createSequentialGroup()
                .addGap(252, 252, 252)
                .addComponent(PremiseList, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(50, 50, 50)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(AnswerList, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(252, 252, 252))
            .addGroup(layout.createSequentialGroup()
                .addGap(252, 252, 252)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGap(252, 252, 252)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(53, 53, 53)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(AddButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ResetButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(244, 244, 244))
            .addGroup(layout.createSequentialGroup()
                .addGap(252, 252, 252)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(ConclusionText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(25, 25, 25)
                .addComponent(NextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(234, 234, 234))
            .addGroup(layout.createSequentialGroup()
                .addGap(252, 252, 252)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(ConclusionValueList, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(25, 25, 25)
                .addComponent(FinishButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(234, 234, 234))
            .addGroup(layout.createSequentialGroup()
                .addGap(250, 250, 250)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(CertaintyFactorSpinner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(409, 409, 409))
            .addGroup(layout.createSequentialGroup()
                .addGap(362, 362, 362)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(RuleIDLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(362, 362, 362))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(RuleIDLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PremiseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AnswerLabel))
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PremiseList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AnswerList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(AddButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)
                        .addComponent(ResetButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(ConclusionText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(NextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ConclusionValueList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FinishButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CertaintyFactorSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
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
    protected javax.swing.JSpinner CertaintyFactorSpinner;
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
    protected javax.swing.JLabel jLabel5;
    protected javax.swing.JLabel jLabel6;
    protected javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
