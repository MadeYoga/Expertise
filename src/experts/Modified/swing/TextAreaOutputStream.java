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
package experts.Modified.swing;

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;

/**
 *
 * @author owner
 */
public class TextAreaOutputStream extends OutputStream{
    
    private javax.swing.JTextArea jTextArea1;
    
    /**
    * Creates a new instance of TextAreaOutputStream which writes
    * to the specified instance of javax.swing.JTextArea control.
    *
    * @param textArea   A reference to the javax.swing.JTextArea
    *                  control to which the output must be redirected to.
    */
    public TextAreaOutputStream( JTextArea textArea ) {
        this.jTextArea1 = textArea;
    }
    
    @Override
    public void write( int b ) throws IOException {
        jTextArea1.append( String.valueOf( ( char )b ) );
        jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
    }
    
    public void write(char[] cbuf, int off, int len) throws IOException {
        jTextArea1.append(new String(cbuf, off, len));
        jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
    }
    
}
