/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experts.Entities;

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
    
}
