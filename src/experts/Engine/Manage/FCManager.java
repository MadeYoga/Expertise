/*
 * The MIT License
 *
 * Copyright 2018 owner.
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
package experts.Engine.Manage;

import experts.Database.AnswerStore;
import experts.Engine.FCDatabase;
import experts.Engine.QueueTable;
import experts.Engine.WorkingMemory;
import experts.Entities.Premise;
import experts.Entities.Rule;
import java.util.Map;

/**
 *
 * @author owner
 */
public class FCManager {
        
    /* STATUSES
     * A  = Active    Rule
     * D  = Discarded Rule
     * U  = Unmarked  Rule
     * M  = Marked    Rule
     * TD = Triggered Rule
     * FD = Fired     Rule
     * FR = Free      Clause
     * FA = False     Clause
     * TU = True      Clause
     */
    
    public FCDatabase database;
    public WorkingMemory working_memory;
    public QueueTable queue_table;
    public AnswerStore answerStore;
    public Rule marked_rule;
    public Rule last_triggered_rule;
    private boolean obtain_conclusion;
    private boolean unknown_conclusion;
    
    public FCManager(){
        
        working_memory     = new WorkingMemory();
        database           = new FCDatabase();
        queue_table        = new QueueTable();
        answerStore        = new AnswerStore();
        obtain_conclusion  = false;
        unknown_conclusion = false;
        
    }
    
    public FCManager(int _expert_id){
        
        database           = new FCDatabase(_expert_id);
        working_memory     = new WorkingMemory();
        queue_table        = new QueueTable();
        answerStore        = new AnswerStore();
        obtain_conclusion  = false;
        unknown_conclusion = false;
        
    }

    public boolean isObtainConclusion() {
        return obtain_conclusion;
    }

    public boolean isUnknownConclusion() {
        return unknown_conclusion;
    }
    
    public Premise getNextPremise(){
        if (obtain_conclusion || unknown_conclusion) 
        {
            return null;
        }
        if (marked_rule == null) // first question
        {
            for (int i = 0; i < database.rules.size(); i++) 
            {
                for (int j = 0; j < database.rules.get(i).premises.size(); j++)
                {
                    Premise target = database.rules.get(i).premises.get(j);
                    if (database.questions.containsKey(target.getId()))
                    {
                        return target;
                    }
                }
            }
            return null;
        }
        for (int i = 0; i < marked_rule.premises.size(); i++) {
            Premise target = marked_rule.premises.get(i);
            if (target.statuses.contains("FR") && 
                database.questions.containsKey(target.getId())) {
                return target;
            }
        }
        return null;
    }
    
    public Premise getFirstQuestion() {
        return (Premise) database.questions
                .get(database.questions.keySet().toArray()[0]);
    }
    
    public void setAnswer(Premise answered_premise, int user_answer){
        working_memory.environment.put(
                answered_premise.getQuestion(), user_answer);
        updateDatabase(answered_premise, user_answer);                          // update status by user answer
        do {
            updateStatusByEnvironment();                                        // update status by environment value
        } while (updateTriggeredStatus());
        updateFiredStatus();
        if (!anyActiveRule() && !anyTriggeredRule())
        {
            unknown_conclusion = true;
            showRulesStatuses();
            return;
        }
        else if (obtain_conclusion || unknown_conclusion)
        {
            showRulesStatuses();
            return;
        }
        marked_rule = getMarkedRule();
        showRulesStatuses();
        return;
    }
    
    public void updateDatabase(int user_answer){
        for (int i = 0; i < database.rules.size(); i++) {
            Rule current_rule = database.rules.get(i);
            if (current_rule.statuses.contains("D") ||
                current_rule.statuses.contains("TD"))
                continue;
            for (int j = 0; j < current_rule.premises.size(); j++) {
                Premise current_premise = current_rule.premises.get(j);
                if (!working_memory.environment
                            .containsKey(current_premise.getQuestion()))        // not yet answered
                    continue;
                if ( !current_premise.statuses.contains("FR") )                 // already checked&updated before
                    continue;                                                   // answered + not updated == current answered premise
                if (current_premise.getRulesPremiseValue() != user_answer) {
                    database.rules.get(i).statuses.remove("U");
                    database.rules.get(i).statuses.remove("A");
                    database.rules.get(i).statuses.remove("M");
                    database.rules.get(i).statuses.add("U");
                    database.rules.get(i).statuses.add("D");
                    database.rules.get(i).premises.get(j).statuses.remove("FR");
                    database.rules.get(i).premises.get(j).statuses.add("FA");
                } else {
                    database.rules.get(i).premises.get(j).statuses.remove("FR");
                    database.rules.get(i).premises.get(j).statuses.add("TU");
                }
                working_memory.environment.put(
                        current_premise.getQuestion(), user_answer);
            }
        }
        return;
    }
    
    public void updateDatabase(Premise p, int user_answer)                      // enhanced performance
    {
        for (int i = 0; i < database.rules.size(); i++) 
        {
            Rule current_rule = database.rules.get(i);
            if (current_rule.statuses.contains("D") ||
                current_rule.statuses.contains("TD"))
                continue;
            for (int j = 0; j < current_rule.premises.size(); j++) 
            {
                Premise current_premise = current_rule.premises.get(j);
                if (current_premise.getId() == p.getId()) 
                {
                    if (current_premise.getRulesPremiseValue() != user_answer) 
                    {
                        database.rules.get(i).statuses.remove("U");
                        database.rules.get(i).statuses.remove("A");
                        database.rules.get(i).statuses.remove("M");
                        database.rules.get(i).statuses.add("U");
                        database.rules.get(i).statuses.add("D");
                        database.rules.get(i).premises.get(j).statuses.remove("FR");
                        database.rules.get(i).premises.get(j).statuses.add("FA");
                    } 
                    else 
                    {
                        database.rules.get(i).premises.get(j).statuses.remove("FR");
                        database.rules.get(i).premises.get(j).statuses.add("TU");
                    }
                    working_memory.environment.put(
                            current_premise.getQuestion(), user_answer);
                    break;
                }
            }
        }
        return;
    }
    
    private void updateStatusByEnvironment(){
        for (int i = 0; i < database.rules.size(); i++) {
            for (int j = 0; j < database.rules.get(i).premises.size(); j++) {
                Premise p = database.rules.get(i).premises.get(j);
                if (working_memory.environment.containsKey(p.getQuestion()) &&
                        p.statuses.contains("FR"))
                {
                    if ((int) working_memory.environment.get(p.getQuestion()) == 
                            p.getRulesPremiseValue()) {
                        database.rules.get(i).premises.get(j).statuses.remove("FR");
                        database.rules.get(i).premises.get(j).statuses.add("TU"); 
                    } else {
                        database.rules.get(i).statuses.remove("U");
                        database.rules.get(i).statuses.remove("D");
                        database.rules.get(i).statuses.remove("A");
                        database.rules.get(i).statuses.remove("M");
                        database.rules.get(i).statuses.add("U");
                        database.rules.get(i).statuses.add("D");
                        database.rules.get(i).premises.get(j).statuses.remove("FR");
                        database.rules.get(i).premises.get(j).statuses.add("FA");
                    }
                }
            }
        }
    }
    
    private boolean updateTriggeredStatus(){
        boolean something_is_changed = false;
        for (int i = 0; i < database.rules.size(); i++) {
            if (database.rules.get(i).statuses.contains("TD"))
                continue;
            if (ruleTriggered(database.rules.get(i))) {
                working_memory.environment.put(
                    database.rules.get(i).getConclusion(), 
                    database.rules.get(i).getConclusionValue());
                database.rules.get(i).statuses.remove("A");
                database.rules.get(i).statuses.remove("M");
                database.rules.get(i).statuses.add("TD");
                last_triggered_rule = database.rules.get(i);
                something_is_changed = true;
            }
        }
        return something_is_changed;
    }
    
    public Rule getMarkedRule() {
        if (marked_rule != null && markedRuleValid()) {
            return marked_rule;
        }
        for (int i = 0; i < database.rules.size(); i++) {
            if (database.rules.get(i).statuses.contains("A")) {
                boolean premise_have_rules = false;
                for (int j = 0; j < database.rules.get(i).premises.size(); j++){
                    Premise p = database.rules.get(i).premises.get(j);
                    if (database.premiseHaveRules(p)) {
                        premise_have_rules = true;
                        break;
                    }
                }
                if (premise_have_rules){
                    continue;
                }
                database.rules.get(i).statuses.remove("U");
                database.rules.get(i).statuses.add("M");
                return database.rules.get(i);
            }
        }
        return null;
    }
    
    private boolean markedRuleValid() {
        for (int i = 0; i < database.rules.size(); i++) {
            if (database.rules.get(i).getId() == marked_rule.getId()) {
                if (database.rules.get(i).statuses.contains("D") ||
                    database.rules.get(i).statuses.contains("TD"))
                    return false;
            }
        }
        return true;
    }
    
    private boolean ruleTriggered(Rule rule) {
        for (int i = 0; i < rule.premises.size(); i++) {
            Premise p = rule.premises.get(i);
            if (!p.statuses.contains("TU"))
                return false;
        }
        return true;
    }
    
    private boolean ruleFired(Rule rule) {
        return true;
    }
    
    private void updateFiredStatus() {
        boolean use_in_future = false;
        int ltr_idx = -1;
        for (int i = 0; i < database.rules.size(); i++) {
            if (database.rules.get(i).statuses.contains("D")) {
                continue;
            } else if (database.rules.get(i).statuses.contains("A")) {
                for (int j = 0; j < database.rules.get(i).premises.size(); j++) {
                    Premise p = database.rules.get(i).premises.get(j);
                    if (working_memory.environment.containsKey(p.getQuestion())) {
                        use_in_future = true;
                    }
                }
                if (use_in_future) break;
            } else {
                if (!use_in_future && 
                    database.rules.get(i).getId() == last_triggered_rule.getId()) 
                {
                    ltr_idx = i;
                    database.rules.get(i).statuses.remove("TD");
                    database.rules.get(i).statuses.add("FD");
                    obtain_conclusion = true;
                    return;
                }
            }
        }
        if (!anyActiveRule() && ltr_idx != -1) {
            database.rules.get(ltr_idx).statuses.remove("TD");
            database.rules.get(ltr_idx).statuses.add("FD");
            obtain_conclusion = true;
        }
    }
    
    private boolean anyActiveRule() {
        for (int i = 0; i < database.rules.size(); i++) {
            if (database.rules.get(i).statuses.contains("A")) {
                return true;
            }
        }
        return false;
    }
    
    private boolean anyTriggeredRule() {
        for (int i = 0; i < database.rules.size(); i++) {
            if (database.rules.get(i).statuses.contains("TR"))
                return true;
        }
        return false;
    }
    
    private boolean anyFiredRule() {
        for (int i = 0; i < database.rules.size(); i++) {
            if (database.rules.get(i).statuses.contains("FR"))
                return true;
        }
        return false;
    }
    
    public String how() {
        String how_ = "";
        for (int i = 0; i < database.rules.size(); i++) {
            if (database.rules.get(i).statuses.contains("TD")) {
                how_ += "Rule " + database.rules.get(i).getConclusion() + "\n";
                for (int j = 0; j < database.rules.get(i).premises.size();j++)
                {
                    Premise p = database.rules.get(i).premises.get(j);
                    how_ += p.getQuestion() + ", User Answer: ";
                    how_ += answerStore.get_answer_by_id(p.getRulesPremiseValue());
                    how_ += "\n";
                }
                how_ += "\n";
            }
        }
        how_ += "Rule " + last_triggered_rule.getConclusion() + "\n";
        for (int j = 0; j < last_triggered_rule.premises.size(); j++) {
            Premise p = last_triggered_rule.premises.get(j);
            how_ += p.getQuestion() + ", Conclusion Answer: ";
            how_ += answerStore.get_answer_by_id(p.getRulesPremiseValue());
            how_ += "\n";
        }
        return how_;
    }
    
    public void showRulesStatuses() {
        System.out.println("--------------");
        for (int i = 0; i < database.rules.size(); i++) {
            System.out.println(
                database.rules.get(i).toString() + " " + 
                answerStore.get_answer_by_id(
                        database.rules.get(i).getConclusionValue()) + " " + 
                database.rules.get(i).statuses.toString()
            );
        }
    }
    
}
