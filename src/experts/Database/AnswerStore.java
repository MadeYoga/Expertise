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

import experts.Entities.Answer;
import experts.Entities.Expert;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author owner
 */
public class AnswerStore extends Storage {
    
    public AnswerStore(){
        super();
        String query = "SELECT * FROM ANSWER";
        try{
            Connection conn = (Connection) DriverManager.getConnection(
                url,
                username, 
                password
            );
            Statement stmt  = (Statement) conn.createStatement();
            ResultSet rs    = stmt.executeQuery(query);
            while(rs.next()) {
                Answer answer = new Answer();
                answer.setId(rs.getInt("id"));
                answer.setAnswer(rs.getString("answer"));
                super.getItems().add(answer);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public AnswerStore(int _expert_id){
        super();
        String query = "SELECT * FROM ANSWER WHERE EXPERTID = " + _expert_id;
        try{
            Connection conn = (Connection) DriverManager.getConnection(
                url,
                username, 
                password
            );
            Statement stmt  = (Statement) conn.createStatement();
            ResultSet rs    = stmt.executeQuery(query);
            while(rs.next()) {
                Answer answer = new Answer();
                answer.setId(rs.getInt("id"));
                answer.setAnswer(rs.getString("answer"));
                super.getItems().add(answer);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public Answer get_answer_by_id(int _id) {
        Answer ans = null;
        for (int i = 0; i < super.getItems().size(); i++) {
            ans = (Answer) super.getItems().get(i);
            if (ans.getId() == _id) {
                return ans;
            }
        }
        return ans;
    }

    @Override
    public String toString() {
        return "AnswerStore : " + super.getItems().toString();
    }
    
}
