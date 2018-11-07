/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experts.Input.Entities;

import java.util.ArrayList;

/**
 *
 * @author owner
 */
public class Premise 
{    
    int	    id		= -1;
    String  question	= "";
    String  answer	= "";
    String  Name	= "";
    
    // NILAI KETENTUAN PREMISE DARI RULE
    int rules_premise_val = -1; // ACTUAL VALUE
    int rules_premise_id  = -1;
    
    public ArrayList <Answer> list_of_answer = new ArrayList <Answer>();
    public ArrayList <Rule> rules = null;
    
    public Premise()
    {
        
    }
    
    public Premise(int _id, String _question){
        id          = _id;
        question    = _question;
    }
    
    public void showPremiseOnConsole(){
        System.out.println(id + ". " + question + " = " + rules_premise_val);
        for (int i = 0; i < list_of_answer.size(); i++){
            Answer ans = list_of_answer.get(i);
            System.out.print(ans.getId() + ". " + ans.getAnswer() + "\t");
        }
        System.out.println();
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
    
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getRulesPremiseValue() {
        return rules_premise_val;
    }

    public int getRules_premise_id() {
        return rules_premise_id;
    }

    public void setRules_premise_id(int rules_premise_id) {
        this.rules_premise_id = rules_premise_id;
    }
    
    public void setRulesPremiseValue(int value){
        this.rules_premise_val = value;
    }
    
    public void SetName(String _name)
    {
	this.Name = _name;
    }
    
    public String GetName()
    {
	return this.Name;
    }
    
    public void Clear()
    {
	this.id = -1;
	this.question = "";
	this.answer = "";
	this.Name = "";
	
	if(this.list_of_answer != null)
	{
	    this.list_of_answer.clear();
	    this.list_of_answer = null;
	    this.list_of_answer = new ArrayList<Answer>();	
	}
	
	if(this.rules != null)
	{
	    this.rules.clear();
	    this.rules = null;
	    this.rules = new ArrayList<Rule>();
	}
	
    }
    
    @Override
    public String toString()
    {
	return this.Name + " = " + this.answer;
    }
}
