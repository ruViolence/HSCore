package me.hsgamer.hscore.expression;

import com.udojava.evalex.AbstractLazyFunction;
import com.udojava.evalex.Expression.LazyNumber;
import java.util.List;

/**
 * The String Comparator
 */
public abstract class StringComparator extends AbstractLazyFunction {

  /**
   * Create new comparator
   *
   * @param name the prefix of the comparator
   */
  public StringComparator(String name) {
    super(name, 2, true);
  }

  /**
   * Compare the two strings
   *
   * @param s1 the 1st string
   * @param s2 the 2nd string
   * @return the result
   */
  public abstract boolean compare(String s1, String s2);

  @Override
  public LazyNumber lazyEval(List<LazyNumber> lazyParams) {
    return BooleanLazyNumber
        .convert(compare(lazyParams.get(0).getString(), lazyParams.get(1).getString()));
  }
}
