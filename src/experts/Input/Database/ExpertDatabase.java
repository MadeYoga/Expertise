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

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Windows
 */
public class ExpertDatabase extends SQLiteDatabase
{
    public ExpertDatabase()
    {
	super();
    }
    
    /**
     * insert new expert without checking if 
     * there's another expert with same name or not.
     * @param expert_name Expert name.
     * @return 
     */
    public int InsertExpert(String expert_name)
    {
	return this.InsertExpert(expert_name, "");
    }
    
    /**
     * insert new expert without checking if 
     * there's another expert with same name or not.
     * @param expert_name Expert name.
     * @param description Decription of the expert.
     * @return 
     */
    public int InsertExpert(String expert_name, String description)
    {
	if (expert_name == (null) || expert_name.trim().equals(""))
	{
	    return 0;
	}
	
	try
	{
	    this._Connection = DriverManager.getConnection(url, _Username, _Password);
            
	    String sql = "INSERT INTO EXPERTS (NAME, DESCRIPTION) "
		       + "VALUES ('" + expert_name + "', '" + description + "')";
	   
	    try(Statement stmt = this._Connection.createStatement())
	    {
		stmt.execute(sql);
		
		sql = "SELECT MAX(ID) FROM EXPERTS";
	    
		try(ResultSet rs = stmt.executeQuery(sql))
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
    
}
