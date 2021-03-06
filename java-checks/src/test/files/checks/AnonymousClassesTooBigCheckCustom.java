class A {
  private void f() {
    Object a = new Comparable();

    new Comparable<T>() { // 1
      private void f() { // 2
        System.out.println(); // 3
        System.out.println(); // 4
      } // 5
    }; // 6

    new Comparable<T>() { // Noncompliant {{Reduce this anonymous class number of lines from 7 to at most 6, or make it a named class.}}
      private void f() { // 2
        System.out.println(); // 3
        System.out.println(); // 4
        System.out.println(); // 5
      } // 6
    }; // 7

    new Comparable<T>() { // Noncompliant {{Reduce this anonymous class number of lines from 17 to at most 6, or make it a named class.}}

      @Override
      private void f(int a, int b) {
        if (a == b) {
          return 0;
        }

        return a < b;
      }

      private void g() {
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        return;
      }

    };
  }
}

public enum SizeUnit {

  BYTES {
    @Override
    public long toBytes(long size) {
      return size;
    }

    @Override
    public long toKB(long size) {
      return size / (C1 / C0);
    }

    @Override
    public long toMB(long size) {
      return size / (C2 / C0);
    }

    @Override
    public long toGB(long size) {
      return size / (C3 / C0);
    }
  };

  public abstract long toBytes(long size);
  public abstract long toKB(long size);
  public abstract long toMB(long size);
  public abstract long toGB(long size);

  private static final long C0 = 1L;
  private static final long C1 = C0 * 1024L;
  private static final long C2 = C1 * 1024L;
  private static final long C3 = C2 * 1024L;

}

@interface plop {


}
