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

import experts.Entities.*;
import experts.Database.FCDatabase;
import java.util.HashSet;
/**
 *
 * @author owner
 */
public class Manager {
    
    private FCDatabase database;
    private QueueTable queue_table;
    private WorkingMemory working_memory;
    
    private int rule_pointer = 0;
    
    private boolean conclusion_obtained = false;
    private boolean unknown_conclusion = false;
    
    public Manager(){
        database = new FCDatabase();
        // database.loadExperts();
        queue_table   = new QueueTable();
        working_memory = new WorkingMemory();
    }
    
    public Manager(int experts_id) {
        // INITIALIZE RULES / PREMISE CLAUSE
        database = new FCDatabase();
        database.loadExperts(experts_id);
        
        // INITIALIZE QUEUETABLE
        queue_table = new QueueTable();
        queue_table.current_rule = database.getRules().get(rule_pointer);
        queue_table.premises = database.getRules().get(rule_pointer++).premises;
        
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
    
    public void showKnowledgeBase(){
        
    }
    
    private Premise getNextPremise(Premise p){
        Premise premise = null;
        for (int i = 0; i < p.rules.size(); i++){
            Rule rule_target = p.rules.get(i);
            for (int j = 0; j < rule_target.premises.size(); j++){
                Premise premise_target = rule_target.premises.get(j);
                if (working_memory.memory.containsKey(premise_target.getId()) || 
                    working_memory.cache .containsKey(premise_target.getId())){
                    continue;
                }
                if (premise_target.rules.size() > 0){
                    Premise next_premise = getNextPremise(premise_target);
                    if (next_premise != null)
                        return next_premise;
                }
                else if (!working_memory.memory.containsKey(premise_target.getId())){
                    return premise_target;
                }
            }
        }
        return premise;
    }
    
    public Premise getNextPremise(){
        Premise premise = null;
        Rule current_rule = queue_table.current_rule;
        for (int i = 0; i < current_rule.premises.size(); i++){
            Premise target = current_rule.premises.get(i);
            if (working_memory.memory.containsKey(target.getId()) || 
                working_memory.cache .containsKey(target.getId())){
                continue;
            }
            if (target.rules.size() > 0){
                Premise next_premise = getNextPremise(target);
                if (next_premise != null)
                    return next_premise;
            }
            else if (!working_memory.memory.containsKey(target.getId())){
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
        Rule current_rule = queue_table.current_rule;
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
                queue_table.current_rule = database.getRules().get(rule_pointer++);
                System.out.println(
                        "CHECK RULE " + queue_table.current_rule.getId() + ": "
                        + queue_table.current_rule.getConclusion()
                );
            } while(!checkRuleStatus());
        } 
        
        if (allPremiseAnswered()){
            conclusion_obtained = true;
            System.out.println("Conclusion: " + queue_table.current_rule.getConclusion());
        }

    }
    
    public boolean allPremiseAnswered(){
        for (int i = 0; i < queue_table.current_rule.premises.size(); i++){
            Premise sample = queue_table.current_rule.premises.get(i);
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
    
    public QueueTable getQueueTable(){
        return queue_table;
    }
    
    public boolean getUnknownConclusion(){
        return unknown_conclusion;
    }
    
    public WorkingMemory getMemory(){
        return working_memory;
    }
    
    public FCDatabase getDatabase(){
        return database;
    }
    
}
