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
package experts.Input.Form;

import experts.Input.Database.SQLiteDatabase;

/**
 *
 * @author Windows
 */
public class program
{
    public static void main(String[] args) throws Exception
    {
	
	//SQLiteDatabase db = new SQLiteDatabase();
	//db.Test();
        
        MainForm a = new MainForm();
        System.out.println(a.getWidth());
        System.out.print(a.getHeight());
        
        
//	Rule rule = new Rule();
//	Premise premise = new Premise();
//	
//	int rule_number = 1;
//	
//	String[] statement = new String[4];
//	statement[0] = "lulus mtk = ya";
//	statement[1] = "lulus inggris = ya";
//	statement[2] = "listrik < 2000 VA = ya";
//	statement[3] = "tidak punya mobil pribadi = ya";
//	
//	String[] konklusi = new String[2];
//	konklusi[0] = "Akademik = ya";
//	konklusi[1] = "Finansial = ya";
//	
//	for(int i = 0; i<konklusi.length ; i++)
//	{
//	    rule = new Rule();
//	    rule.setId(i + 1);
//	    
//	    //set premise
//	    for (int j = 0; j < 2; j++)
//	    {
//		premise = new Premise();
//		String item="";
//		if(i == 0)
//		{
//		    item=statement[j];
//		}
//		else if(i == 1)
//		{
//		    item=statement[j+2];
//		}
//		premise.name = item.substring(0, item.indexOf("=")).trim();
//		premise.setAnswer(item.substring(item.indexOf("=") + 1, item.length()).trim());
//		
//		//System.out.println(premise.toString());
//		
//		rule.premises.add(premise);
//	    }
//	    rule.setConclusion(konklusi[i]);
//	    System.out.println(rule.toString());
//	}
	
    }
}
