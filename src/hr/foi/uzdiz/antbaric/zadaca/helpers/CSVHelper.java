/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.foi.uzdiz.antbaric.zadaca.helpers;

import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author javert
 */

public class CSVHelper {

    public static void writeLine(Writer w, List<String> values) throws Exception {
        boolean firstVal = true;
        for (String val : values) {
            if (!firstVal) {
                w.write(",");
            }
            w.write("\"");
            for (int i = 0; i < val.length(); i++) {
                char ch = val.charAt(i);
                if (ch == '\"') {
                    w.write("\""); // extra quote
                }
                w.write(ch);
            }
            w.write("\"");
            firstVal = false;
        }
        w.write("\n");
    }


    /**
    * returns a row of values as a list
    * returns null if you are past the end of the line
    */
    public static List<String> parseLine(Reader r) throws Exception {
        int ch = r.read();
        while (ch == '\r') {
            //ignore linefeed characters wherever they are, particularly just before end of file
            ch = r.read();
        }
        if (ch<0) {
            return null;
        }
        ArrayList<String> store = new ArrayList<>();
        StringBuffer curVal = new StringBuffer();
        boolean inquotes = false;
        boolean started = false;
        OUTER:
        while (ch>=0) {
            if (inquotes) {
                started=true;
                if (ch == '\"') {
                    inquotes = false;
                }
                else {
                    curVal.append((char)ch);
                }
            } else {
                switch (ch) {
                    case '\"':
                        inquotes = true;
                        if (started) {
                            // if this is the second quote in a value, add a quote
                            // this is for the double quote in the middle of a value
                            curVal.append('\"');
                        }   break;
                    case ';':
                        store.add(curVal.toString());
                        curVal = new StringBuffer();
                        started = false;
                        break;
                    //ignore LF characters
                    case '\r':
                        break;
                    case '\n':
                        //end of a line, break out
                        break OUTER;
                    default:
                        curVal.append((char)ch);
                        break;
                }
            }
            ch = r.read();
        }
        store.add(curVal.toString());
        return store;
    }
}
