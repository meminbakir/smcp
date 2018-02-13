/**
 * 
 */
package mce;

/**
 * @author Mehmet Emin BAKIR
 *
 */
public class QuickTests {

	/**
	 * 
	 */
	public static void main(String[] args) {

		final double baseMin = 0.0;
		final double baseMax = 360.0;
		final double limitMin = 90.0;
		final double limitMax = 270.0;

		double valueIn = 0;
		System.out.println(scale(valueIn, baseMin, baseMax, limitMin, limitMax));
		valueIn = 360;
		System.out.println(scale(valueIn, baseMin, baseMax, limitMin, limitMax));
		valueIn = 90;
		System.out.println(scale(valueIn, baseMin, baseMax, limitMin, limitMax));
	}

	/**
	 * <pre>
	 *       (b-a)(x - min)
	 *f(x) = --------------  + a
	 *         max - min
	 * </pre>
	 * 
	 * @param valueIn
	 * @param baseMin
	 * @param baseMax
	 * @param limitMin
	 * @param limitMax
	 * @return
	 */
	public static double scale(final double valueIn, final double baseMin, final double baseMax, final double limitMin,
			final double limitMax) {
		return ((limitMax - limitMin) * (valueIn - baseMin) / (baseMax - baseMin)) + limitMin;
	}

}
