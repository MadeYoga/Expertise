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
public class AddPremise extends javax.swing.JPanel
{
    protected MainForm _MainForm = null;
    protected int _ExpertID = 0;
    protected int _PremiseIDCounter = 0;
    protected Premise _Premise = new Premise();
    protected ArrayList <Answer> _Answer = new ArrayList <Answer>();
    
    /**
     * Creates new form AddPremise
     */
    public AddPremise()
    {
	initComponents();
    }

    public AddPremise(MainForm frame, int expert_id)
    {
	initComponents();
	this._MainForm = frame;
	this._ExpertID = expert_id;
	this.AnswerList.setText("");
	this.LoadAnswer();
        this.IncrementPremiseCounter();
    }
    
    public void IncrementPremiseCounter()
    {
        this._PremiseIDCounter += 1;
        this.PremiseIDLabel.setText("Premise ID " + this._PremiseIDCounter);
    }
    
    public void LoadAnswer()
    {
	AnswerDatabase db = new AnswerDatabase();
	String[] answer = db.LoadAnswer(this._ExpertID);

	DefaultComboBoxModel model = (DefaultComboBoxModel) this.AnswerComboBox.getModel();
        
        model.removeAllElements();

        for (String item : answer) 
	{
            model.addElement(item);
        }
	
	this.AnswerComboBox.setModel(model);
    }
    
    public void AddAnswer()
    {
	if(this.PremiseText.getText().trim().equals(""))
	{
	    JOptionPane.showMessageDialog(this, "Premise name can't empty.");
	    return;
	}
	
	if(this.QuestionText.getText().trim().equals(""))
	{
	    JOptionPane.showMessageDialog(this, "Question can't empty.");
	    return;
	}
	
	for (Answer item : this._Answer)
	{
	    if(item.toString().equals(this.AnswerComboBox.getSelectedItem().toString()))
	    {
		JOptionPane.showMessageDialog(this, item.toString() + " already inserted.");
		return;
	    }
	}
	//copy to answer object
	String answer_combobox = this.AnswerComboBox.getSelectedItem().toString();
	Answer temp = new Answer();
	temp.setId(Integer.parseInt(answer_combobox.substring(0, answer_combobox.indexOf(".")).trim()));
	temp.setAnswer(answer_combobox.substring(answer_combobox.indexOf(".") + 1, answer_combobox.length()).trim());
	this._Answer.add(temp);
	
	//insert answer into answer list
	String result = this.AnswerList.getText();
	result += this.AnswerComboBox.getSelectedItem().toString() + "\n";
	this.AnswerList.setText(result);
    }
    
    public void NextPremise()
    {
	if (this.AnswerList.getText().split("\n").length <= 0)
	{
	    JOptionPane.showMessageDialog(this, "Premise answer list need at least 1 answer.");
	    return;
	}
	
	PremiseDatabase db = new PremiseDatabase();
	if (db.HasSamePremiseName(_ExpertID, this.PremiseText.getText().trim()) == true)
	{
	    JOptionPane.showMessageDialog(this, "Premise with name (" + this.PremiseText.getText().trim() +") " +
						"has already recorded.");
	    return;
	}
	
//	if (db.HasSamePremiseQuestion(_ExpertID, this.QuestionText.getText().trim()) == true)
//	{
//	    JOptionPane.showMessageDialog(this, "Premise with question (" + this.PremiseText.getText().trim() +") " +
//						"has already recorded.");
//	    return;
//	}
	
	//save premise to database
	this._Premise.SetName(this.PremiseText.getText());
	this._Premise.setQuestion(this.QuestionText.getText());
        this._Premise.SetCertaintyFactor((double)this.CertaintyFactorSpinner.getValue());
	int premise_id = db.InserPremise(this._ExpertID, this._Premise);
	
	this.PremiseText.setText("");
	this.QuestionText.setText("");
	this.CertaintyFactorSpinner.setValue(1);
        
	//save premise answer list
	//generate answer id from _answer
	int[] answer_id = new int[this._Answer.size()];
	for (int i = 0; i < this._Answer.size(); i++)
	{
	    answer_id[i] = this._Answer.get(i).getId();
	}
	
	//save 
	db.InserPremisAnswerList(premise_id, answer_id);
	
        //increment
        this.IncrementPremiseCounter();
        
	//clear 
	this._Premise.Clear();
	this._Premise = new Premise();
	this._Answer.clear();
	this._Answer = new ArrayList<Answer>();
	this.AnswerList.setText("");
    }
    
    public void Finish()
    {
	int result = JOptionPane.showConfirmDialog
			(this, 
			"Go to next step ?", 
			"Warning",JOptionPane.YES_NO_OPTION);

	if (result == JOptionPane.YES_OPTION)
	{
	    //buat form baru dengan conainer yang berbeda
	    MainForm form = new MainForm();
	    AddRule temp = new AddRule(form, this._ExpertID);
	    form.SetContainer(temp);
	    form.setVisible(true);
	    this._MainForm.Close();
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

        jLabel1 = new javax.swing.JLabel();
        PremiseIDLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        PremiseText = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        QuestionText = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        AnswerList = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        AnswerComboBox = new javax.swing.JComboBox<>();
        NextButton = new javax.swing.JButton();
        AddAnswer = new javax.swing.JButton();
        FinishButton = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        CertaintyFactorSpinner = new javax.swing.JSpinner();

        setMaximumSize(new java.awt.Dimension(1920, 1080));
        setMinimumSize(new java.awt.Dimension(1024, 600));
        setPreferredSize(new java.awt.Dimension(1024, 600));

        jLabel1.setFont(new java.awt.Font("Monospaced", 0, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Add Premise");
        jLabel1.setMaximumSize(new java.awt.Dimension(300, 50));
        jLabel1.setMinimumSize(new java.awt.Dimension(300, 50));
        jLabel1.setPreferredSize(new java.awt.Dimension(300, 50));

        PremiseIDLabel.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        PremiseIDLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PremiseIDLabel.setText("Premise ID 1");
        PremiseIDLabel.setToolTipText("");
        PremiseIDLabel.setMaximumSize(new java.awt.Dimension(300, 25));
        PremiseIDLabel.setMinimumSize(new java.awt.Dimension(300, 25));
        PremiseIDLabel.setPreferredSize(new java.awt.Dimension(300, 25));

        jLabel2.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        jLabel2.setText("Premise name : ");
        jLabel2.setMaximumSize(new java.awt.Dimension(200, 25));
        jLabel2.setMinimumSize(new java.awt.Dimension(200, 25));
        jLabel2.setPreferredSize(new java.awt.Dimension(200, 25));

        PremiseText.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        PremiseText.setToolTipText("Premise name.");
        PremiseText.setPreferredSize(new java.awt.Dimension(400, 31));

        jLabel3.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        jLabel3.setText("Question :");
        jLabel3.setMaximumSize(new java.awt.Dimension(200, 25));
        jLabel3.setMinimumSize(new java.awt.Dimension(200, 25));
        jLabel3.setPreferredSize(new java.awt.Dimension(200, 25));

        QuestionText.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        QuestionText.setToolTipText("Question for premise.");
        QuestionText.setPreferredSize(new java.awt.Dimension(400, 31));

        jLabel4.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        jLabel4.setText("Answer List");
        jLabel4.setMaximumSize(new java.awt.Dimension(200, 25));
        jLabel4.setMinimumSize(new java.awt.Dimension(200, 25));
        jLabel4.setPreferredSize(new java.awt.Dimension(200, 25));

        jScrollPane1.setMinimumSize(new java.awt.Dimension(200, 100));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(400, 200));

        AnswerList.setEditable(false);
        AnswerList.setColumns(20);
        AnswerList.setRows(5);
        jScrollPane1.setViewportView(AnswerList);

        jLabel5.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        jLabel5.setText("Answer");
        jLabel5.setMaximumSize(new java.awt.Dimension(200, 25));
        jLabel5.setMinimumSize(new java.awt.Dimension(200, 25));
        jLabel5.setPreferredSize(new java.awt.Dimension(200, 25));

        AnswerComboBox.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        AnswerComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        AnswerComboBox.setToolTipText("Answer");
        AnswerComboBox.setMinimumSize(new java.awt.Dimension(200, 35));
        AnswerComboBox.setPreferredSize(new java.awt.Dimension(200, 35));

        NextButton.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        NextButton.setText("Next");
        NextButton.setMaximumSize(new java.awt.Dimension(150, 40));
        NextButton.setMinimumSize(new java.awt.Dimension(150, 40));
        NextButton.setPreferredSize(new java.awt.Dimension(150, 40));
        NextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NextButtonActionPerformed(evt);
            }
        });

        AddAnswer.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        AddAnswer.setText("Add");
        AddAnswer.setMaximumSize(new java.awt.Dimension(150, 40));
        AddAnswer.setMinimumSize(new java.awt.Dimension(150, 40));
        AddAnswer.setPreferredSize(new java.awt.Dimension(150, 40));
        AddAnswer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddAnswerActionPerformed(evt);
            }
        });

        FinishButton.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        FinishButton.setText("Finish");
        FinishButton.setMaximumSize(new java.awt.Dimension(150, 40));
        FinishButton.setMinimumSize(new java.awt.Dimension(150, 40));
        FinishButton.setPreferredSize(new java.awt.Dimension(150, 40));
        FinishButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FinishButtonActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        jLabel6.setText("Certainty Factor");
        jLabel6.setMaximumSize(new java.awt.Dimension(200, 25));
        jLabel6.setMinimumSize(new java.awt.Dimension(200, 25));
        jLabel6.setPreferredSize(new java.awt.Dimension(200, 25));

        CertaintyFactorSpinner.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        CertaintyFactorSpinner.setModel(new javax.swing.SpinnerNumberModel(1.0d, -1.1d, 1.1d, 0.10000000149011612d));
        CertaintyFactorSpinner.setMinimumSize(new java.awt.Dimension(200, 20));
        CertaintyFactorSpinner.setPreferredSize(new java.awt.Dimension(200, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(380, 380, 380)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(344, 344, 344))
            .addGroup(layout.createSequentialGroup()
                .addGap(362, 362, 362)
                .addComponent(PremiseIDLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(362, 362, 362))
            .addGroup(layout.createSequentialGroup()
                .addGap(312, 312, 312)
                .addComponent(PremiseText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(312, 312, 312))
            .addGroup(layout.createSequentialGroup()
                .addGap(312, 312, 312)
                .addComponent(QuestionText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(312, 312, 312))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(312, 312, 312)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(312, 312, 312)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(250, 250, 250)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(AnswerComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CertaintyFactorSpinner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(AddAnswer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(NextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(FinishButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(104, 104, 104))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(PremiseIDLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(PremiseText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(QuestionText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(AnswerComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(CertaintyFactorSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(AddAnswer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(NextButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(FinishButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void NextButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_NextButtonActionPerformed
    {//GEN-HEADEREND:event_NextButtonActionPerformed
	this.NextPremise();
    }//GEN-LAST:event_NextButtonActionPerformed

    private void FinishButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_FinishButtonActionPerformed
    {//GEN-HEADEREND:event_FinishButtonActionPerformed
        this.Finish();
    }//GEN-LAST:event_FinishButtonActionPerformed

    private void AddAnswerActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_AddAnswerActionPerformed
    {//GEN-HEADEREND:event_AddAnswerActionPerformed
        this.AddAnswer();
    }//GEN-LAST:event_AddAnswerActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JButton AddAnswer;
    protected javax.swing.JComboBox<String> AnswerComboBox;
    protected javax.swing.JTextArea AnswerList;
    protected javax.swing.JSpinner CertaintyFactorSpinner;
    protected javax.swing.JButton FinishButton;
    protected javax.swing.JButton NextButton;
    protected javax.swing.JLabel PremiseIDLabel;
    protected javax.swing.JTextField PremiseText;
    protected javax.swing.JTextField QuestionText;
    protected javax.swing.JLabel jLabel1;
    protected javax.swing.JLabel jLabel2;
    protected javax.swing.JLabel jLabel3;
    protected javax.swing.JLabel jLabel4;
    protected javax.swing.JLabel jLabel5;
    protected javax.swing.JLabel jLabel6;
    protected javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
