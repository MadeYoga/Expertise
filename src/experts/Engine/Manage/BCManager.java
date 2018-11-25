/*
 * The MIT License
 *
 * Copyright 2018 Expertise Team                    .
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

package experts.Engine;

import experts.Database.AnswerStore;
import experts.Entities.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
/**
 *
 * @author owner
 */
public class BCManager {
    
    private BCDatabase      database;
    private GoalTable       goal_table;
    private WorkingMemory   working_memory;
    
    private int current_expert_id = -1;
    private int rule_pointer = 0;
    
    private boolean conclusion_obtained = false;
    private boolean unknown_conclusion  = false;
    
    private Stack path_ = new Stack();
    
    public AnswerStore answerStore = new AnswerStore();
    
    public BCManager(){
        database       = new BCDatabase();
        goal_table    = new GoalTable();
        working_memory = new WorkingMemory();
    }
    
    public BCManager(int experts_id) {
        // INITIALIZE RULES / PREMISE CLAUSE
        database = new BCDatabase();
        database.loadExperts(experts_id);
        
        // INITIALIZE GOALTABLE
        goal_table = new GoalTable();
        goal_table.current_rule = database.getRules().get(rule_pointer);
        goal_table.premises = database.getRules().get(rule_pointer++).premises;
        
        // INITIALIZE WORKING MEMORY
        working_memory = new WorkingMemory();
    }
    
    public boolean loadExperts(int experts_id){
        try{
            database.loadExperts(experts_id);
        } catch(Exception e){ 
            return false;
        }
        return true;
    }
    
    public String how() {
        String how_ = "RULE " + goal_table.current_rule.getConclusion() + "\n";
        Queue q = new LinkedList();
        for (int i = 0; i < goal_table.current_rule.premises.size(); i++) {
            Premise current_premise = goal_table.current_rule.premises.get(i);
            if (working_memory.cache.containsKey(current_premise.getId()) ||
                working_memory.memory.containsKey(current_premise.getId())){
                q.offer(current_premise);
                
                if (current_premise.rules.size() > 0)
                    how_ += (i + 1) + ". " + current_premise.getQuestion() + ", Conclusion: ";
                else
                    how_ += (i + 1) + ". " + current_premise.getQuestion() + ", User Answer: ";
                int _id = -1;
                if (working_memory.memory.containsKey(current_premise.getId()))
                    _id = (int) working_memory.memory.get(current_premise.getId());
                else
                    _id = (int) working_memory.cache.get(current_premise.getId());
                how_ += answerStore.get_answer_by_id(_id) + "\n";
            }
            if (current_premise.rules.size() > 0){
                how_ += how(current_premise) + "\n";
            }
        }
        return how_;
    }
    
    public String how(Premise current_premise){
        String how_ = "";
        Queue q = new LinkedList();
//        for (int i = 0; i < current_premise.rules.size(); i++){
        Rule rule_sample = current_premise.rules.get(0);
        for (int j = 0; j < rule_sample.premises.size(); j++){
            Premise next_premise = rule_sample.premises.get(j);
            if (working_memory.memory.containsKey(next_premise.getId())){
                if (!q.contains(next_premise))
                    q.offer(next_premise);
                else continue;
            }
            if (next_premise.rules.size() > 0){
                how_ += how(current_premise);
            }
        }
//            break;
//        }
        while (!q.isEmpty()){
            how_ += q.peek().toString();
            how_ += ", User Answer: ";
            int id = (int)working_memory.memory.get(((Premise)q.peek()).getId());
            how_ += answerStore.get_answer_by_id(id) + "\n";
            q.poll();
        }
        // System.out.println(how_);
        return how_;
    }
    
    public String why() {
        return get_path_();
    }
    
    public String get_path_(){
        String path = "<html>" + goal_table.current_rule.toString() + "<br> &nbsp -> ";
        int count_space = 1;
        Stack s = new Stack();
        while (!path_.empty()) {
            s.push(path_.pop());
        }
        while (!s.empty()){
            Object value = s.pop();
            if (s.empty())
                path += "<strong> " + value.toString() + "</strong>";
            else {
                path += value.toString() + "<br>";
                for (int i = count_space; i >= 0; i--){
                    path += " &nbsp ";
                }
                path += "-> ";
            }
            
            path_.push(value);
        }
        path += "</html>";
        return path;
    }
    
    private Premise getNextPremise(Premise p){
        Premise premise = null;
        for (int i = 0; i < p.rules.size(); i++){
            Rule rule_target = p.rules.get(i);
            path_.push(rule_target);
            for (int j = 0; j < rule_target.premises.size(); j++){
                Premise premise_target = rule_target.premises.get(j);
                if (working_memory.memory.containsKey(premise_target.getId()) || 
                    working_memory.cache .containsKey(premise_target.getId())){
                    continue;
                }
                if (premise_target.rules.size() > 0){
                    Premise next_premise = getNextPremise(premise_target);
                    if (next_premise != null) {
                        path_.push(next_premise);
                        return next_premise;
                    }
                    // path_.pop();
                }
                else if (!working_memory.memory.containsKey(premise_target.getId())){
                    path_.push(premise_target);
                    return premise_target;
                }
            }
            path_.pop();
        }
        return premise;
    }
    
    public Premise getNextPremise(){
        Premise premise = null;
        Rule current_rule = goal_table.current_rule;
        for (int i = 0; i < current_rule.premises.size(); i++){
            path_.clear();
            Premise target = current_rule.premises.get(i);
            if (working_memory.memory.containsKey(target.getId()) || 
                working_memory.cache .containsKey(target.getId())){
                continue;
            }
            if (target.rules.size() > 0){
                Premise next_premise = getNextPremise(target);
                if (next_premise != null){
                    // path_.push(next_premise);
                    return next_premise;
                }
                // path_.pop();
            }
            else if (!working_memory.memory.containsKey(target.getId())){
                path_.push(target);
                return target;
            }
        }
        return premise;
    }
    
    public int checkRuleStatus(Premise target){
        for (int i = 0; i < target.rules.size(); i++){
            Rule rule_target = target.rules.get(i);
            int count_answered_premise = 0;
            
            for (int j = 0; j < rule_target.premises.size(); j++){
                Premise premise_target = rule_target.premises.get(j);
                if (premise_target.rules.size() > 0){
                    int rule_status = checkRuleStatus(premise_target);
                    
                    if (rule_status == -1)
                        break;
                    
                    if (rule_status != premise_target.getRulesPremiseValue()){
                        working_memory.memory.put(premise_target.getId(), rule_status);
                        working_memory.cache.put(premise_target.getId(), rule_status);
                        return -1;
                    }
                    working_memory.memory.put(premise_target.getId(), premise_target.getRulesPremiseValue());
                    working_memory.cache.put(premise_target.getId(), premise_target.getRulesPremiseValue());
                    return premise_target.getRulesPremiseValue();
                } 
                else { 
                    boolean answered = working_memory.memory.containsKey(premise_target.getId());
                    if (answered){
                        count_answered_premise += 1;
                        int user_answer = (int) working_memory.memory.get(premise_target.getId());
                        if (premise_target.getRulesPremiseValue() != user_answer){
                            break;
                        }
                    } 
                    else 
                        // ASUMSI, JIKA 1 TIDAK TERJAWAB, // MAKA SISAHNYA TIDAK TERJAWAB
                        break;
                    // SUDAH TERJAWAB SEMUA DAN BENAR! RETURN RULE CONCLUSION VALUE
                    if (count_answered_premise >= rule_target.premises.size()){
                        System.out.println(rule_target.getConclusion() + " = " + rule_target.getConclusionValue());
                        return rule_target.getConclusionValue();
                    }
                }
            }
            
        }
        return -1;
    }
    
    public boolean checkRuleStatus(){
        Rule current_rule = goal_table.current_rule;
        for (int i = 0; i < current_rule.premises.size(); i++){
            Premise target = current_rule.premises.get(i);
            
            if (working_memory.cache .containsKey(target.getId())){
                int current_conclusion = (int) working_memory.cache.get(target.getId());
                if (current_conclusion != target.getRulesPremiseValue()){
                    System.out.println("Rule " + current_rule.getId() + " Salah");
                    return false;
                }
                continue;
            }
            
            if (target.rules.size() > 0){
                int rule_status = checkRuleStatus(target); // conclusion value
                
                if (rule_status == -1) // belum terjawab semua
                    break;
                if (rule_status != target.getRulesPremiseValue()){
                    working_memory.cache.put(target.getId(), rule_status);
                    return false;
                }
                
                System.out.println(rule_status + " : " + target.getRulesPremiseValue());
                
                working_memory.cache.put(target.getId(), target.getRulesPremiseValue());
            } else {
                boolean answered = working_memory.memory.containsKey(target.getId());
                if (answered) {
                    int user_answer = (int) working_memory.memory.get(target.getId());
                    if (target.getRulesPremiseValue() != user_answer){
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    public void printPath_(){
        System.out.println(path_.toString());
    }
    
    public void setAnswer(Premise active_premise, int answer_id){
        
        if (active_premise == null)
            return;
        
        working_memory.memory.put(active_premise.getId(), answer_id);
        System.out.println(working_memory.memory.toString());
        System.out.println(working_memory.cache.toString());
        if (checkRuleStatus() == false){
            do {
                if (rule_pointer >= database.getRules().size()){
                    unknown_conclusion = true;
                    System.out.println("Conclusion: UNKNOWN");
                    return ;
                }
                goal_table.current_rule = database.getRules().get(rule_pointer++);
                System.out.println(
                        "CHECK RULE " + goal_table.current_rule.getId() + ": "
                        + goal_table.current_rule.getConclusion()
                );
            } while(!checkRuleStatus());
        } 
        
        if (allPremiseAnswered()){
            conclusion_obtained = true;
            System.out.println("Conclusion: " + goal_table.current_rule.getConclusion());
        }

    }
    
    public boolean allPremiseAnswered(){
        for (int i = 0; i < goal_table.current_rule.premises.size(); i++){
            Premise sample = goal_table.current_rule.premises.get(i);
            if (
                    !working_memory.memory.containsKey(sample.getId()) && 
                    !working_memory.cache .containsKey(sample.getId())
               )
            {
                return false;
            }
        }
        return true;
    }
    
    public boolean conclusionObtained(){
        return conclusion_obtained;
    }
    
    public GoalTable getGoalTable(){
        return goal_table;
    }
    
    public boolean getUnknownConclusion(){
        return unknown_conclusion;
    }
    
    public WorkingMemory getMemory(){
        return working_memory;
    }
    
    public BCDatabase getDatabase(){
        return database;
    }
    
}
