/**
 * 
 */
package mchecking.translator;

import java.util.Arrays;
import java.util.List;

/**
 * @author Mehmet
 *
 */
public class TranslatorUtil {
	static String keywords= "A,bool,clock,const,ctmc,C,double,dtmc,E,endinit,"
			+ "endinvariant,endmodule,endrewards,endsystem,false,"
			+ "formula,filter,func,F,global,G,init,invariant,I,int,"
			+ "label,max,mdp,min,module,X,nondeterministic,Pmax,Pmin,"
			+ "P,probabilistic,prob,pta,rate,rewards,Rmax,Rmin,R,S,stochastic,"
			+ "system,true,U,W,"
			+ "all,V,d";
	public static final List<String> reservedKeywords = Arrays
			.asList(keywords.split(","));
	public static final String prefix = "wasKeyword_";
	// YMER has bug and ignores underscore char, so if a variable starts with like _5 it sees only 5 and rises error.
	public static final String underScore = "underScore";

	/***
	 * Check if the param is a reserved keyword, then rename it with prefix.
	 * 
	 * @param name
	 *            param
	 * @return
	 */
	public static String checkKeyword(String name) {

		if (reservedKeywords.contains(name))
			name = prefix + name;
		if (name.startsWith("_")) {
			name = underScore + name;
		}
		return name;
	}

}
