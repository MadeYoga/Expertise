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
package experts.Database;

import experts.Entities.Expert;
import experts.Entities.Rule;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author owner
 */
public class ExpertStore extends Storage {
    
    public ExpertStore(){
        String query = "SELECT * FROM EXPERTS";
        try{
            Connection conn = (Connection) DriverManager.getConnection(
                    url,
                    username, 
                    password
            );
            Statement stmt  = (Statement) conn.createStatement();
            ResultSet rs    = stmt.executeQuery(query);
            while(rs.next()) {
                Expert expert = new Expert();
                expert.setId(rs.getInt("id"));
                expert.setName(rs.getString("name"));
                expert.setDescription(rs.getString("description"));
                super.getItems().add(expert);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < super.getItems().size(); i++){
            str += super.getItems().get(i).toString() + "\n";
        }
        return str;
    }
    
}
