package nl.ritogames.trui.uicomponent.chat;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

/**
 * <p>A queue that will only grow to {@link #maxSize}</p>
 *
 * @param <E> <p>The value the queue should hold</p>
 */
public class LimitedQueue<E> extends LinkedList<E> {

  private final int maxSize;

  public LimitedQueue(int maxSize) {
    this.maxSize = maxSize;
  }

  @Override
  public boolean add(E o) {
    boolean ret = super.add(o);
    trim();
    return ret;
  }

  @Override
  public boolean addAll(int index, Collection c) {
    boolean ret = super.addAll(index, c);
    trim();
    return ret;
  }

  @Override
  public void addFirst(E o) {
    super.addFirst(o);
    trim();
  }

  @Override
  public void addLast(E o) {
    super.addLast(o);
    trim();
  }

  /**
   * Trims the queue to the {@link #maxSize}
   */
  private void trim() {
    while (size() > maxSize) {
      super.remove();
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    LimitedQueue<?> that = (LimitedQueue<?>) o;
    return maxSize == that.maxSize;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), maxSize);
  }
}
