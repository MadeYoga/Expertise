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

package experts.Engine;

import java.sql.Connection;
import java.sql.Statement;
import experts.Entities.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author owner
 */
public class BCDatabase {
    
    // DATABASE CONNECTOR
    private String url      = "jdbc:mysql://localhost:3306/expert"; 
    private String username = "root";
    private String password = "";
    
    private ArrayList <Rule> rules = new ArrayList <Rule>();
    
    public BCDatabase(){
        
    }

    public ArrayList <Rule> getRules() {
        return rules;
    }
    
    public ArrayList <Rule> loadRules(int expert_id){
        
        ArrayList<Rule> result = new ArrayList<Rule>();
        String query = "SELECT * FROM RULE" + 
                       "\nWHERE RULE.EXPERT_ID = " + expert_id + 
                       "\nAND RULE.hierarchy = 1";
        try{
            Connection conn = (Connection) DriverManager.getConnection(url, username, password);
            Statement stmt  = (Statement) conn.createStatement();
            ResultSet rs    = stmt.executeQuery(query);
            while(rs.next()) {
                Rule rule = new Rule();
                rule.setId(rs.getInt(1));
                rule.setConclusion(rs.getString("conclusion"));
                rule.setConclusionValue(rs.getInt("conclusion_value"));
                rule.setHierarchy(rs.getInt("hierarchy"));
                result.add(rule);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch(SQLException e){
            e.printStackTrace();
            return null;
        }
        
        return result;
    }
    
    public ArrayList <Rule> loadAllRules(int expert_id){
        
        ArrayList<Rule> result = new ArrayList<Rule>();
        String query = "SELECT * FROM RULE" + 
                       "\nWHERE RULE.EXPERT_ID = " + expert_id;
        try{
            Connection conn = (Connection) DriverManager.getConnection(url, username, password);
            Statement stmt  = (Statement) conn.createStatement();
            ResultSet rs    = stmt.executeQuery(query);
            while(rs.next()) {
                Rule rule = new Rule();
                rule.setId(rs.getInt(1));
                rule.setConclusion(rs.getString("conclusion"));
                rule.setConclusionValue(rs.getInt("conclusion_value"));
                rule.setHierarchy(rs.getInt("hierarchy"));
                result.add(rule);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch(SQLException e){
            e.printStackTrace();
            return null;
        }
        
        return result;
    }
    
    public ArrayList <Premise> loadRulesPremise(Rule rule) {
        ArrayList<Premise> result = null; 
        String query = "SELECT *, RP.premise_val FROM PREMISE P\n" + 
                       "JOIN RULES_PREMISE RP ON RP.premise_id = P.id\n" +
                       "WHERE RP.rule_id = " + rule.getId();
        try{
            Connection conn = (Connection) DriverManager.getConnection(url, username, password);
            Statement stmt  = (Statement) conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            result = new ArrayList <Premise> ();
            while(rs.next()) {
                Premise loaded_premise = new Premise(rs.getInt(1), rs.getString(2));
                loaded_premise.rules = loadPremiseRules(loaded_premise);
                loaded_premise.list_of_answer = loadPremiseAnswers(loaded_premise);
                loaded_premise.setRulesPremiseValue(rs.getInt("RP.premise_val"));
                
                // UPDATE 18 NOVEMBER, PREMISE's PARENT
                loaded_premise.parent = rule;
                result.add(loaded_premise);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch(SQLException e){
            return null;
        }
        return result;
    }
    
    public ArrayList <Rule> loadPremiseRules(Premise premise){
        ArrayList <Rule> result = null;
        String query = "SELECT * FROM RULE R\n" + 
                       "JOIN PREMISE_RULES PR ON PR.rule_id = R.id\n" + 
                       "WHERE PR.premise_id = " + premise.getId();
        try{
            Connection conn = (Connection) DriverManager.getConnection(url, username, password);
            Statement stmt  = (Statement) conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            result = new ArrayList <Rule> ();
            while(rs.next()) {
                Rule loaded_rule = new Rule();
                loaded_rule.setId(rs.getInt(1));
                loaded_rule.setConclusion(rs.getString("conclusion"));
                loaded_rule.setConclusionValue(rs.getInt("conclusion_value"));
                loaded_rule.setHierarchy(rs.getInt("hierarchy"));
                loaded_rule.premises = loadRulesPremise(loaded_rule);
                
                // UPDATE 18 NOVEMBER, RULE's PARENT
                loaded_rule.parent = premise;
                result.add(loaded_rule);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch(SQLException e){
            return null;
        }
        return result;
    }
    
    public ArrayList <Answer> loadPremiseAnswers(Premise premise){
        
        ArrayList<Answer> result = new ArrayList<Answer>();
        String query =  "SELECT * FROM ANSWER A " + 
                        "JOIN PREMISE_ANSWER_LIST PAL ON A.id = PAL.answer_id " + 
                        "JOIN PREMISE P ON PAL.premise_id = P.id " + 
                        "WHERE P.id = " + premise.getId();
        try{
            Connection conn = (Connection) DriverManager.getConnection(url, username, password);
            Statement stmt  = (Statement) conn.createStatement();
            ResultSet rs = stmt.executeQuery(query); // RETURNS PREMISE PREMISES
            while(rs.next()) {
                Answer answer = new Answer(rs.getInt(1), rs.getString(2));
                result.add(answer);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch(SQLException e){
            return null;
        }
        
        return result;
    }
    
    public void loadExperts(int expert_id){
        rules = loadRules(expert_id);
        for (int i = 0; i < rules.size(); i++){
            rules.get(i).showRuleOnConsole();
            rules.get(i).premises = loadRulesPremise(rules.get(i));
        }
    }
    
}
