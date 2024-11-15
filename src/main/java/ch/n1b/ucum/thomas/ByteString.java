package ch.n1b.ucum.thomas;

import java.nio.charset.StandardCharsets;

public class ByteString {
  byte[] bytes;
  int len;
  int cap;

  public static ByteString wrapping(byte[] bytes) {
    return new ByteString(bytes, bytes.length);
  }

  public static ByteString ofZeros(int length) {
    return new ByteString(null, length);
  }

  public static ByteString ofString(String s) {
    var bytes = s.getBytes(StandardCharsets.ISO_8859_1);
    for (int i = 0; i < bytes.length; i++) {
      bytes[i] = (byte) (bytes[i] - '0');
    }
    return new ByteString(bytes, bytes.length);
  }

  private ByteString(byte[] bytes, int len) {
    this.len = len;
    this.cap = bytes == null ? 0 : minSize(bytes.length);
    if (cap > 0) {
      this.bytes = new byte[cap];
      System.arraycopy(bytes, 0, this.bytes, 0, len);
    }
  }

  static int minSize(int len) {
    return Math.max(clp2(len), 8);
  }

  /** closest upper power of 2 */
  static int clp2(int x) {
    // https://ptgmedia.pearsoncmg.com/images/0201914654/samplechapter/warrench03.pdf
    x = x - 1;
    x = x | (x >>> 1);
    x = x | (x >>> 2);
    x = x | (x >>> 4);
    x = x | (x >>> 8);
    x = x | (x >>> 16);
    return x + 1;
  }

  //  public void trimLeftZeros() {
  //    if (bytes == null || len == 0) {
  //      // if we only have '0' bytes then this will trim them all
  //      this.len = 0;
  //      return;
  //    }
  //
  //    var i = 0;
  //    for (; i < len; i++) {
  //      if (bytes[i] != 0) {
  //        break;
  //      }
  //    }
  //    var newBytes = new byte[len - i];
  //    System.arraycopy(bytes, i, newBytes, 0, len - i);
  //    bytes = newBytes;
  //  }

  public byte at(int i) {
    if (i < 0 || i >= len) {
      throw new IndexOutOfBoundsException("0 <= %d < %d".formatted(i, len));
    }
    if (bytes == null) {
      return 0;
    }
    return bytes[i];
  }

  public void set(int i, byte b) {
    if (bytes == null) {
      bytes = new byte[len];
    }
    bytes[i] = b;
  }

  @Override
  public String toString() {
    if (bytes == null) {
      return "0".repeat(len);
    }
    byte[] chars = new byte[len];
    for (int i = 0; i < len; i++) {
      chars[i] = (byte) (bytes[i] + '0');
    }
    return new String(chars, 0, len, StandardCharsets.ISO_8859_1);
  }

  public boolean hasAllZeros() {
    if (len == 0 || bytes == null) {
      return true;
    }
    for (byte b : bytes) {
      if (b != 0) {
        return false;
      }
    }
    return true;
  }

  public ByteString substring(int beginIndex, int endIndex) {
    var newLength = endIndex - beginIndex;
    if (bytes == null) {
      return ofZeros(newLength);
    }
    var newBytes = new byte[newLength];
    System.arraycopy(bytes, beginIndex, newBytes, 0, newLength);
    return new ByteString(newBytes, newLength);
  }

  public ByteString substring(int beginIndex) {
    return substring(beginIndex, len);
  }

  public ByteString concat(ByteString other) {
    var newLength = this.len + other.len;

    var oldBytes = bytes;
    growTo(newLength);
    if (oldBytes != null) {
      System.arraycopy(oldBytes, 0, bytes, 0, len);
    }

    if (other.bytes != null) {
      System.arraycopy(other.bytes, 0, bytes, len, other.len);
    }

    this.len = newLength;
    return this;
  }

  public int compareTo(ByteString other) {
    if (this.bytes == null && other.bytes == null) {
      return this.len - other.len;
    }

    int min = Math.min(this.len, other.len);
    if (this.bytes == null) {
      for (int i = 0; i < min; i++) {
        if (0 != other.bytes[i]) {
          return -other.bytes[i];
        }
      }
      return this.len - other.len;
    }

    if (other.bytes == null) {
      for (int i = 0; i < min; i++) {
        if (this.bytes[i] != 0) {
          return this.bytes[i];
        }
      }
      return this.len - other.len;
    }

    for (int i = 0; i < min; i++) {
      if (this.bytes[i] != other.bytes[i]) {
        return this.bytes[i] - other.bytes[i];
      }
    }
    return this.len - other.len;
  }

  public int length() {
    return len;
  }

  public boolean isEmpty() {
    return len == 0;
  }

  /* WARN: this will not copy over the old bytes */
  private void growTo(int n) {
    if (cap >= n) {
      return;
    }

    cap = clp2(n);
    bytes = new byte[cap];
  }
}
