package ch.n1b.ucum;

import ch.n1b.ucum.lib.Decimal;
import ch.n1b.ucum.lib.UcumService;
import ch.n1b.ucum.thomas.UcumEssenceService;
import tech.units.indriya.unit.Units;

import javax.measure.Unit;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static javax.measure.MetricPrefix.MILLI;

public class Converters {

  static DecimalBenchmark.Converter<Number> indrya() {

    return new DecimalBenchmark.Converter<Number>() {
      @Override
      public Number convert(Number value, String sourceUnit, String destUnit) throws Exception {

        // that's a bit cheating, we skip parsing the units :)
        var src = Units.MOLE.divide(Units.CUBIC_METRE);
        var dst = MILLI(Units.MOLE).divide(Units.LITRE);

        var converter = src.getConverterTo((Unit) dst);

        return converter.convert(value);
      }

      @Override
      public Number parse(String raw) throws Exception {
        var v = new BigDecimal(raw);
        v = v.setScale(64, RoundingMode.UNNECESSARY);
        return v;
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
