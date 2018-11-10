/*
 * The MIT License
 *
 * Copyright 2018 Windows.
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
package experts.Input.Database;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Windows
 */
public class RuleDatabase extends SQLiteDatabase
{
    public RuleDatabase()
    {
	super();
    }    
    
    public int InsertRule(int expert_id, String conclusion, int answer_id, int hierarchy)
    {
	try
	{
	    this.Connect();
	    String sql = "INSERT INTO RULE (EXPERT_ID, CONCLUSION, CONCLUSION_VALUE, HIERARCHY) " + 
			 "VALUES (" + expert_id + ", '" + conclusion + "', " + answer_id + ", " + hierarchy + ")";
	    
	    try (Statement stmt = this._Connection.createStatement())
	    {
		//insert rule 
		stmt.execute(sql);
	    
		sql = "SELECT MAX(ID) " +
		      "FROM RULE " +
		      "WHERE EXPERT_ID = " + expert_id;
		
		//get inserted rule id in database
		try (ResultSet rs = stmt.executeQuery(sql))
		{
		    while(rs.next())
		    {
			return rs.getInt(1);
		    }
		}
	    }
	} 
	catch (Exception ex)
	{
	    ex.printStackTrace();
	}
	finally
	{
	    this.Close();
	}
	return -1;
    }
    
    public void InserRulePremise(int rule_id, int[] premise_id, int[] answer_id)
    {
	try
	{
	    this.Connect();
	    
	    String sql = "";
	    
	    try (Statement stmt = this._Connection.createStatement())
	    {
		for (int i = 0; i < premise_id.length; i++)
		{
		    sql = "INSERT INTO RULES_PREMISE (RULE_ID, PREMISE_ID, PREMISE_VAL) " +
			  "VALUES (" + rule_id + ", " + premise_id[i] + ", " + answer_id[i] + ")";
		    stmt.execute(sql);
		}		
	    }
	} 
	catch (Exception ex)
	{
	    Logger.getLogger(RuleDatabase.class.getName()).log(Level.SEVERE, null, ex);
	}
	finally
	{
	    this.Close();
	}
    }
    
    public int CountRule(int expert_id)
    {
        String sql = " SELECT COUNT(ID) FROM RULE WHERE EXPERT_ID = " + expert_id;
        return this.SelectINT(sql);
    }
    
    public String[] LoadRule(int expert_id)
    {
        String[] rule = new String[this.CountRule(expert_id)];
                
        try 
        {
            this.Connect();
            
            String sql = "SELECT ID, CONCLUSION FROM RULE WHERE EXPERT_ID = " + expert_id;
            
            try (Statement stmt = this._Connection.createStatement())
	    {
		try (ResultSet rs = stmt.executeQuery(sql))
		{
		    int counter = 0;
		    while(rs.next())
		    {
			rule[counter] = rs.getInt(1) + ". " + rs.getString(2);
			counter++;
		    }
		}
	    }
            return rule;
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(RuleDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            this.Close();
        }
        return rule;
    }
    
    public String CreateStatement(int expert_id)
    {
        return "";
    }
    
    public void InsertPremiseRule(int premise_id, int[] rule_id)
    {
        try 
        {
            this.Connect();
            
            for (int i = 0; i < rule_id.length; i++)            
            {
                String sql = "INSERT INTO PREMISE_RULES (PREMISE_ID, RULE_ID) " +
                        "VALUES (" + premise_id + ", "  + rule_id[i] + ")";
                
                try (Statement stmt = this._Connection.createStatement())
                {
                    stmt.execute(sql);
                }
            }
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(RuleDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
	finally
        {
            this.Close();
        }
    }
}