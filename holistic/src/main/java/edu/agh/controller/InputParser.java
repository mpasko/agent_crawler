/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.agh.controller;

import edu.agh.commandgrammar.CommandTreeLexer;
import edu.agh.commandgrammar.CommandTreeParser;
import edu.agh.commandgrammar.CommandTreeParser.output_return;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
/**
 *
 * @author marcin
 */
public class InputParser {
	private final CommandTreeLexer lexer;
	
	public InputParser(){
		lexer = new CommandTreeLexer();
	}

    public CommonTree genericParse(String out) {
        CommandTreeParser parser = prepareParser(out);
        CommonTree tree = null;
        try{
//            System.out.println("Parsing:");
//            System.out.println(Util.attachLines(out));
            output_return out_ret = parser.output();
            tree = (CommonTree)out_ret.getTree();
//            System.out.println("tree:");
//            System.out.println(tree.toStringTree());
        }catch(RecognitionException ex){
            Logger.getLogger(InputParser.class.getName()).log(Level.SEVERE, null, ex);
            //throw new RuntimeException(ex);
        }
        return tree;
    }

    public CommandTreeParser prepareParser(String out) {
        String processed = out.toLowerCase(Locale.ROOT).replace("\n", " ");
//        System.out.println(processed);
        lexer.setCharStream(new ANTLRStringStream(processed));
        lexer.reset();
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        //		System.out.println("lex:");
        //		tokens.LT(1);
        CommandTreeParser parser = new CommandTreeParser(tokens);
        return parser;
    }
}
