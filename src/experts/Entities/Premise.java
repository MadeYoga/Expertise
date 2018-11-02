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
public class Premise {
    
    int     id              = -1;
    String  question        = "";
    String  answer          = "";
    
    // NILAI KETENTUAN PREMISE DARI RULE
    int rules_premise_val = -1; // ACTUAL VALUE
    int rules_premise_id  = -1;
    
    public ArrayList <Answer> list_of_answer = new ArrayList <Answer>();
    public ArrayList <Rule> rules = null;
    
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
    
    public Premise(){
        
    }
    
}
