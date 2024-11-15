package ch.n1b.ucum;

import ch.n1b.ucum.thomas.UcumException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConvertersTest {

  static final String SOURCE_UNIT = "mol.m-3";
  static final String TARGET_UNIT = "mmol/L";

  @Test
  void thomas() throws Exception {

    var ref = Converters.baseline(loadUcum());

    var sut = Converters.thomas(loadThomasUcum());
    var raw = "2.997404248";

    var value = sut.parse(raw);
    var refValue = ref.parse(raw);

    var expected = ref.convert(refValue, SOURCE_UNIT, TARGET_UNIT);
    var got = sut.convert(value, SOURCE_UNIT, TARGET_UNIT);
    assertEquals(got.toString(), expected.toString());
    System.out.printf("value: %s, refValue: %s%n", value, refValue);

    for (int i = 0; i < 4000; i++) {
      sut.convert(value, SOURCE_UNIT, TARGET_UNIT);
    }
  }

  private ch.n1b.ucum.thomas.UcumEssenceService loadThomasUcum() {
    var fileName = "/ucum-essence.xml";
    try (var in = this.getClass().getResourceAsStream(fileName)) {
      return new ch.n1b.ucum.thomas.UcumEssenceService(in);
    } catch (IOException | UcumException e) {
      throw new RuntimeException(e);
    }
  }

  private ch.n1b.ucum.lib.UcumEssenceService loadUcum() {
    var fileName = "/ucum-essence.xml";
    try (var in = this.getClass().getResourceAsStream(fileName)) {
      return new ch.n1b.ucum.lib.UcumEssenceService(in);
    } catch (IOException | ch.n1b.ucum.lib.UcumException e) {
      throw new RuntimeException(e);
    }
  }
}
