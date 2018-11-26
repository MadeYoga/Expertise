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

import experts.Input.Entities.Premise;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Windows
 */
public class PremiseDatabase extends SQLiteDatabase
{
    public PremiseDatabase()
    {
	super();
    }
    
    public int CountPremise(int expert_id)
    {
	try
	{
	    this.Connect();
	    String sql = "SELECT COUNT(ID) " + 
			 "FROM PREMISE " + 
			 "WHERE EXPERTID = " + expert_id;
	    
	    return this.SelectINT(sql);
	} 
	catch (Exception ex)
	{
	    Logger.getLogger(PremiseDatabase.class.getName()).log(Level.SEVERE, null, ex);
	}
	finally
	{
	    this.Close();
	}
	return -1;
    }
    
    public String[] LoadPremise(int expert_id)
    {
	String[] premise = new String[this.CountPremise(expert_id)];
	
	try
	{
	    this.Connect();
		String sql = "SELECT ID, NAME " + 
			     "FROM PREMISE " + 
			     "WHERE EXPERTID = " + expert_id;
		
	    try (Statement stmt = this._Connection.createStatement())
	    {
		try (ResultSet rs = stmt.executeQuery(sql))
		{
		    int counter = 0;
		    while(rs.next())
		    {
			premise[counter] = rs.getInt(1) + ". " + rs.getString(2);
			counter++;
		    }
		}
	    }
	} 
	catch (Exception ex)
	{
	    Logger.getLogger(SQLiteDatabase.class.getName()).log(Level.SEVERE, null, ex);
	}
	finally
	{
	    this.Close();
	}
	
	return premise;
    }
    
    public int InserPremise(int expert_id, Premise premise)
    {
	try
	{
	    this.Connect();
	    String sql = "INSERT INTO PREMISE (EXPERTID, NAME, QUESTION, CF) "
		       + "VALUES (" + expert_id + ", '" + premise.GetName() + "', '" + premise.getQuestion() + "', " + premise.GetCertaintyFactor() + ")";
	    try(Statement stmt = this._Connection.createStatement())
	    {
		stmt.execute(sql);
		
		//get inserted premise id
		sql = "SELECT MAX(ID) FROM PREMISE";
		int id = this.SelectINT(sql);
		return id;
	    }
	} 
	catch (Exception e)
	{
	    Logger.getLogger(SQLiteDatabase.class.getName()).log(Level.SEVERE, null, e);
	}
	finally
	{
	    this.Close();
	}
	return -1;
    }
    
    public boolean HasSamePremiseName(int expert_id, String name)
    {
	String sql = "SELECT COUNT(ID) " + 
		     "FROM PREMISE " +
		     "WHERE EXPERTID = " + expert_id + " " +
			    "AND LOWER(NAME) = LOWER('" + name + "')";
	    
	int result = this.SelectINT(sql);
	
	if (result == 1)
	{
	    return true;
	}
	return false;
    }
    
    public boolean HasSamePremiseQuestion(int expert_id, String question)
    {
	String sql = "SELECT COUNT(ID) " + 
		     "FROM PREMISE " +
		     "WHERE EXPERTID = " + expert_id + " " + 
			    "AND LOWER(QUESTION) = LOWER('" + question + "')";
	    
	int result = this.SelectINT(sql);
	
	if (result == 1)
	{
	    return true;
	}
	return false;
    }
    
    public void InserPremisAnswerList(int premise_id, int[] answer_id)
    {
	for (int id : answer_id)
	{
	    String sql = "INSERT INTO PREMISE_ANSWER_LIST (PREMISE_ID, ANSWER_ID) " +
		     "VALUES (" + premise_id + ", " + id + ")";
	    this.InsertQuery(sql);
	}
    }
    
    public String GetPremiseFromRule(int rule_id)
    {
        String sql = "SELECT PREMISE.NAME, ANSWER.ANSWER " +
                     "FROM PREMISE " +
                     "INNER JOIN RULES_PREMISE ON RULES_PREMISE.PREMISE_ID = PREMISE.id " + 
                     "INNER JOIN ANSWER ON RULES_PREMISE.PREMISE_VAL = ANSWER.ID " +
                     "WHERE PREMISE.ID IN (SELECT DISTINCT(RULES_PREMISE.premise_id) FROM RULES_PREMISE WHERE RULES_PREMISE.RULE_ID = " + rule_id + ") " +
                     "AND RULES_PREMISE.RULE_ID = " + rule_id;
        
        String temp = "";
        
        try 
        {
            this.Connect();
            
            try (Statement stmt = this._Connection.createStatement())
	    {
		try (ResultSet rs = stmt.executeQuery(sql))
		{
		    while(rs.next())
		    {
                        temp += rs.getString(1) + " = " + rs.getString(2);
                        temp += " AND ";
		    }
                    return temp.substring(0, temp.length() - 4);
		}
	    }            
        }
        catch (Exception e) 
        {
            System.out.println(e.getMessage());
        }
        finally
        {
            this.Close();
        }
        
        return null;
    }
}
