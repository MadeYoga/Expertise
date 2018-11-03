/*
 * The MIT License
 *
 * Copyright 2018 Expertise Team.
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
