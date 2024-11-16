package ch.n1b.ucum;

import ch.n1b.ucum.lib.Decimal;
import ch.n1b.ucum.lib.UcumService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.measure.Unit;
import systems.uom.ucum.format.UCUMFormat;

public class Converters {

  static DecimalBenchmark.Converter<BigDecimal> indrya() {

    return new DecimalBenchmark.Converter<BigDecimal>() {
      @Override
      public BigDecimal convert(BigDecimal value, String sourceUnit, String destUnit)
          throws Exception {

        var src = UCUMFormat.getInstance(UCUMFormat.Variant.CASE_SENSITIVE).parse(sourceUnit);
        var dst = UCUMFormat.getInstance(UCUMFormat.Variant.CASE_SENSITIVE).parse(destUnit);

        var converter = src.getConverterTo((Unit) dst);

        return (BigDecimal) converter.convert(value);
      }

      @Override
      public BigDecimal parse(String raw) throws Exception {
        return new BigDecimal(raw).setScale(64, RoundingMode.UNNECESSARY);
      }
    };
  }

  static DecimalBenchmark.Converter<Decimal> baseline(UcumService ucumService) {
    return new DecimalBenchmark.Converter<Decimal>() {
      @Override
      public Decimal convert(Decimal value, String sourceUnit, String destUnit) throws Exception {
        return ucumService.convert(value, sourceUnit, destUnit);
      }

      @Override
      public Decimal parse(String raw) throws Exception {
        return new Decimal(raw, 64);
      }
    };
  }

  static DecimalBenchmark.Converter<ch.n1b.ucum.thomas.Decimal> thomas(
      ch.n1b.ucum.thomas.UcumEssenceService ucumService) {
    return new DecimalBenchmark.Converter<>() {
      @Override
      public ch.n1b.ucum.thomas.Decimal convert(
          ch.n1b.ucum.thomas.Decimal value, String sourceUnit, String destUnit) throws Exception {
        return ucumService.convert(value, sourceUnit, destUnit);
      }

      @Override
      public ch.n1b.ucum.thomas.Decimal parse(String raw) throws Exception {
        return new ch.n1b.ucum.thomas.Decimal(raw, 64);
      }
    };
  }

  public static <T> DecimalBenchmark.Converter<T> nop() {
    return new DecimalBenchmark.Converter<T>() {
      @Override
      public T convert(T value, String sourceUnit, String destUnit) throws Exception {
        return value;
      }

      @Override
      public T parse(String raw) throws Exception {
        return null;
      }
    };
  }
}
