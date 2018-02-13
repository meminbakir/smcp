package mchecking.translator.qt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mchecking.ModelChecker;
import mchecking.translator.qt.PQueryParser.PQueryContext;

/**
 * All translator should inherit from this class. It holds both user defined pattern query, and translated query,
 * 
 * @author Mehmet Emin BAKIR
 * 
 */
public class MCQuery extends PQueryBaseVisitor<String> {

	Logger log = LoggerFactory.getLogger(MCQuery.class);
	ModelChecker targetMC = null;

	private int lineNumber = 0;
	private int queryID = 0;
	private PQuery pQuery = null;
	private String translatedQuery = "";
	boolean isVerifiable = true;// can the pattern be verified by target MC
	boolean isComment = false;
	// If extra comment needs to be presented, e.g., PLASMA ignores P>=n and P=? operations.
	String note = "";

	MCQuery(ModelChecker mcType) {
		targetMC = mcType;
	}

	/**
	 * @return the mcType
	 */
	public ModelChecker getTargetMC() {
		return targetMC;
	}

	/**
	 * Get the user defined pattern query
	 * 
	 * @return the pattern query
	 */
	public PQuery getPQuery() {
		return pQuery;
	}

	/**
	 * @param pQuery
	 *            the pattern query to set
	 */
	public void setPQuery(PQuery pQuery) {
		this.pQuery = pQuery;
		setLineNumber(pQuery.getLineNumber());
		setQueryID(pQuery.getQueryID());
	}

	/**
	 * Which line of query
	 * 
	 * @return the queryID
	 */
	public int getQueryID() {
		return queryID;
	}

	/**
	 * @param queryID
	 *            the queryID to set
	 */
	public void setQueryID(int queryID) {
		this.queryID = queryID;
	}

	/**
	 * @return the translatedQuery
	 */
	public String getTranslatedQuery() {
		return translatedQuery;
	}

	/**
	 * @param translatedQuery
	 *            the translatedQuery to set
	 */
	public void setTranslatedQuery(String translatedQuery) {
		this.translatedQuery = translatedQuery;
	}

	/**
	 * @return the isVerifiable
	 */
	public boolean isVerifiable() {
		return isVerifiable;
	}

	/**
	 * @return the isComment
	 */
	public boolean isComment() {
		return isComment;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mchecking.translator.qt.PQueryBaseVisitor#visitPQuery(mchecking.translator .qt.PQueryParser.PQueryContext)
	 */
	@Override
	public String visitPQuery(PQueryContext ctx) {
		String result = "";
		int childCount = ctx.getChildCount();
		// query
		if (childCount > 0) {
			result = visit(ctx.getChild(0));

		} else// comment
		{
			isVerifiable = false;
			isComment = true;
			result = "Comment line has skipped.";
		}
		setTranslatedQuery(result);
		return result;
	}

	/**
	 * @return the lineNumber
	 */
	public int getLineNumber() {
		return lineNumber;
	}

	/**
	 * @param lineNumber
	 *            the lineNumber to set
	 */
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	@Override
	public String toString() {
		String result = "Query ID:" + getQueryID() + "\nSelected Model Chekcer: " + targetMC + "\nPattern Query:"
				+ getPQuery() + "\nTranslated Query:" + getTranslatedQuery() + "\n";
		return result;
	}
}
