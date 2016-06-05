package djdbc.utils;

import fj.F0;
import org.junit.Assert;

public final class AssertExtras {
  static void assertTrue(F0<String> message, boolean condition) {
    if (!condition) {
      Assert.fail(message.f());
    }
  }
}
