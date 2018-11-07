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

import java.sql.Connection;
import java.sql.Statement;
import java.io.File;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ricky Setiawan
 */
public class SQLiteDatabase
{
    protected String _Path = "./Sample.db";
    protected String _Username = "root";
    protected String _Password = "";
    protected Connection _Connection = null;
    protected String url = "jdbc:mysql://localhost:3306/expert";
    
    /**
     * Constructor.
     */
    public SQLiteDatabase()
    {
	
    }
    
    /**
     * Constructor.
     * @param databse_name Database name or database location. 
     */
    public SQLiteDatabase(String databse_name)
    {
	this._Path = databse_name;
    }
    
    /**
     * Open connection to database.
     */
    protected void Connect() throws Exception
    {
	this.Connect(this._Path);
    }
    
    /**
     * Open connection to database.
     * @param database_name Database name or database location.
     * @throws Exception Database can't be empty or null.
     */
    protected void Connect(String database_name) throws Exception
    {
//	if(database_name.equals("") ||database_name == null)
//	{
//	    throw new Exception("Database name can't be empty.");
//	}
//	
//	File temp = new File(this._Path);
//	if(temp.exists() == false)
//	{
//	    //System.out.println("Database not found.");
//	    throw new Exception("Database not found.");
//	}
	
	try
	{
	    // db parameters
            //String url = "jdbc:sqlite:" + database_name;
	    
            // create a connection to the database
            this._Connection = DriverManager.getConnection(url, _Username, _Password);
            
            System.out.println("Connection to SQLite has been established. \n");
	} 
	catch (Exception e)
	{
	    System.out.println(e.getMessage());
	    //throw new Exception("Can't connect to database.");
	}
    }
    
    /**
     * Close database connection.
     */
    protected void Close()
    {
	try
	{
	    if(this._Connection != null)
	    {
		this._Connection.close();
	    }
	    //System.out.println("Connection to SQLite has been closed.");
	} 
	catch (SQLException e)
	{
	    System.out.println(e.getMessage());
	}
    }

    public void Test() throws Exception
    {
	this.Connect("hhh");
	this.Close();;
    }
    
    public void Load()
    {
	try
	{
	    this.Connect();
	    
	    String sql = "SELECT * FROM EXPERT";
	    
	    Statement statement = this._Connection.createStatement();
	    ResultSet resultset = statement.executeQuery(sql);
	    
	    while(resultset.next())
	    {
		System.out.println(resultset.getString(1));
		System.out.println(resultset.getString(2));
		System.out.println(resultset.getString(3));
		System.out.println();
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
    
    /**
     * INsert query template.
     * @param sql insert SQL query
     */
    public void InsertQuery(String sql)
    {
	try
	{
	    this.Connect();
	    
	    try(Statement stmt = this._Connection.createStatement())
	    {
		stmt.execute(sql);
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
    
    /**
     * Only for SELECT query that return only
     * 1 integer column.
     * @param query SQL query.
     * @return 
     */
    protected int SelectINT(String query)
    {
	try
	{
	    this.Connect();
	    try(Statement stmt = this._Connection.createStatement())
	    {
		try(ResultSet rs = stmt.executeQuery(query))
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
	return -1;
    }
    
    protected String SelectString(String query, int column_index)
    {
	try
	{
	    this.Connect();
	    try(Statement stmt = this._Connection.createStatement())
	    {
		try(ResultSet rs = stmt.executeQuery(query))
	        {
		    while (rs.next())
		    {
			return rs.getString(column_index);
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
	return null;
    }
    
}
