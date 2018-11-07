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
public class AnswerDatabase extends SQLiteDatabase
{
    public AnswerDatabase()
    {
	super();
    }
    
    public int CountAnswer(int expert_id)
    {
	try
	{
	    this.Connect();
	    String sql = "SELECT COUNT(ID) FROM ANSWER WHERE EXPERTID = " + expert_id;
	    try (Statement stmt = this._Connection.createStatement())
	    {
		try (ResultSet rs = stmt.executeQuery(sql))
		{
		    while (rs.next())
		    {
			return rs.getInt(1);
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
	
	return 0;
    }
    
    public void InsertAnswer(int expert_id, String[] answer)
    {
	try
	{
	    this.Connect();
	    String sql = "";
	    try (Statement stmt = this._Connection.createStatement())
	    {
		for(int i = 0; i < answer.length; i++)
		{
		    sql = "INSERT INTO ANSWER (EXPERTID, ANSWER) "
			    + "VALUES (" + expert_id + ", '" + answer[i] + "') ";
		
		    stmt.execute(sql);
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
    }

    public String[] LoadAnswer(int expert_id)
    {
	String[] answer = new String[this.CountAnswer(expert_id)];
	
	try
	{
	    this.Connect();
	    String sql = "SELECT ID, ANSWER FROM ANSWER WHERE EXPERTID = " + expert_id;
	    try (Statement stmt = this._Connection.createStatement())
	    {
		try (ResultSet rs = stmt.executeQuery(sql))
		{
		    int counter = 0;
		    while(rs.next())
		    {
			answer[counter] = rs.getInt(1) + ". " + rs.getString(2);
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
	
	return answer;
    }
    
}
