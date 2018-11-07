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
public class Rule {
    
    private int id                  = -1;
    private String conclusion       = "";
    private int conclusion_value    = -1;
    private int hierarchy           = -1;
    
    public ArrayList <Premise> premises = new ArrayList <Premise>();
    
    public Rule(){
        
    }
    
    public Rule(int id)
    {
	this.id = id;
    }
    
    public void showRuleOnConsole(){
        System.out.println("Hierarchy: " + hierarchy + ", RULES " + getId() + ". " + conclusion);
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public int getConclusionValue() {
        return conclusion_value;
    }

    public void setConclusionValue(int conclusion_value) {
        this.conclusion_value = conclusion_value;
    }

    public int getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(int hierarchy) {
        this.hierarchy = hierarchy;
    }
    
    public void Clear()
    {
	this.id = 0;
	this.conclusion = "";
	if(this.premises.size() > 0)
	{
	    this.premises.clear();
	}
	this.premises = new ArrayList <Premise>();
    }
    
    public boolean ContainPremise(Premise copy)
    {
	for(int i = 0; i < this.premises.size(); i++)
	{
	    if(this.premises.get(i).toString().equals(copy.toString()))
	    {
		return true;
	    }
	}
	return false;
    }
    
    @Override
    public String toString()
    {
	String result = "";
	result += id + "\n";
	if(this.premises.size() > 0)
	{
	    for(int i = 0; i < premises.size(); i++)
	    {
		if( i < premises.size() - 1)
		{
		    result += premises.get(i).toString();
		    result += " AND ";
		    continue;
		}
		result += premises.get(i).toString() + "\n";
	    }
	    result += "THEN " + this.conclusion;
	}
	return result;
    }
}
