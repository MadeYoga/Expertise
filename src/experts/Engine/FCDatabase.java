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
package experts.Engine;

import experts.Entities.Premise;
import experts.Entities.Rule;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author owner
 */
public class FCDatabase {
    
    private String url            = "jdbc:mysql://localhost:3306/expert"; 
    private String username       = "root";
    private String password       = "";
    private int    expert_id      = -1;
    public ArrayList <Rule> rules = new ArrayList<Rule>();
    public HashMap questions      = new HashMap();
    
    public FCDatabase(){
        
    }
    
    public FCDatabase(int _expert_id){
        
        expert_id = _expert_id;
        
        this.rules = new BCDatabase().loadAllRules(_expert_id);
        
        for (int i = 0; i < rules.size(); i++) {
            rules.get(i).statuses.add("A");
            rules.get(i).statuses.add("U");
            System.out.println(
                    rules.get(i).toString() + " " + 
                    rules.get(i).statuses.toString()
            );
            rules.get(i).premises = loadPremise(rules.get(i));
            for (int j = 0; j < rules.get(i).premises.size(); j++) {
                rules.get(i).premises.get(j).statuses.add("FR");
                // if leaf
                if (!premiseHaveRules(rules.get(i).premises.get(j))){
                    questions.put(
                        rules.get(i).premises.get(j).getId(), 
                        rules.get(i).premises.get(j)
                    );
                }
                System.out.println(
                        rules.get(i).premises.get(j).toString() + " " + 
                        rules.get(i).premises.get(j).statuses
                );
            }
        }
    }
    
    public boolean premiseHaveRules(Premise premise) {
        String query = "SELECT count(R.id) FROM RULE R\n" + 
                       "JOIN PREMISE_RULES PR ON PR.rule_id = R.id\n" + 
                       "WHERE PR.premise_id = " + premise.getId();
        try{
            Connection conn = (Connection) DriverManager.getConnection(
                    url, username, password);
            Statement stmt  = (Statement) conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                if (rs.getInt(1) > 0) 
                    return true;
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    
    public ArrayList <Premise> loadPremise(Rule rule){
        ArrayList<Premise> result = null; 
        String query = "SELECT *, RP.premise_val FROM PREMISE P\n" + 
                       "JOIN RULES_PREMISE RP ON RP.premise_id = P.id\n" +
                       "WHERE RP.rule_id = " + rule.getId();
        try{
            Connection conn = (Connection) DriverManager
                    .getConnection(url, username, password);
            Statement stmt  = (Statement) conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            result = new ArrayList <Premise> ();
            while(rs.next()) {
                Premise loaded_premise = new Premise();
                loaded_premise.setId(rs.getInt(1));
                loaded_premise.setQuestion(rs.getString(2));
                loaded_premise.list_of_answer = new BCDatabase()
                        .loadPremiseAnswers(loaded_premise);
                loaded_premise.setRulesPremiseValue(
                        rs.getInt("RP.premise_val"));
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
    
}
