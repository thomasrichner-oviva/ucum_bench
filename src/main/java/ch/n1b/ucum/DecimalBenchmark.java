package ch.n1b.ucum;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import ch.n1b.ucum.lib.UcumEssenceService;
import ch.n1b.ucum.lib.UcumException;
import ch.n1b.ucum.lib.UcumService;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.*;
import java.util.List;
import java.util.function.Function;

public class DecimalBenchmark {
  public static void main(String[] args) throws Exception {
    Main.main(args);
  }

  static final String SOURCE_UNIT = "mol.m-3";
  static final String TARGET_UNIT = "mmol/L";

  @Benchmark
  @Fork(value = 1, warmups = 1)
  @Warmup(iterations = 5, time = 500, timeUnit = MILLISECONDS)
  @Measurement(iterations = 10, time = 500, timeUnit = MILLISECONDS)
  @BenchmarkMode(Mode.Throughput)
  public void benchmarkDecimalConvert(ExecutionPlan plan, Blackhole blackhole) throws Exception {

    // start - hot loop
    var got = plan.converter.convert(plan.value, SOURCE_UNIT, TARGET_UNIT);
    blackhole.consume(got);
    // end
  }

  @State(Scope.Benchmark)
  public static class ExecutionPlan<T> {

    public List<String> testCases = testCases();

    @Param({"baseline", "thomas"})
    public String impl;

    public T value;
    public Converter<T> converter;

    @Setup(Level.Invocation)
    public void setUp() throws Exception {
      setupConverter();

      //      var testCase = 3;
      //      var raw = testCases.get(testCase);
      var raw = "2.997404248";
      value = converter.parse(raw);
    }

    private void setupConverter() {
      // TODO: add better implementations here :D
      converter =
          switch (impl) {
            //            case "nop" -> Converters.nop();
            case "baseline" -> (Converter<T>) Converters.baseline(loadUcumEssenceService());
            case "thomas" -> (Converter<T>) Converters.thomas(loadThomasUcumEssenceService());
            default -> throw new IllegalStateException("Unexpected value: " + impl);
          };
    }

    private static List<String> testCases() {
      return fixtures(
          "fixtures.lines",
          in -> {
            try (var ir = new InputStreamReader(in);
                var r = new BufferedReader(ir)) {
              return r.lines().toList();
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          });
    }

    private static ch.n1b.ucum.thomas.UcumEssenceService loadThomasUcumEssenceService() {
      var fileName = "ucum-essence.xml";
      return fixtures(
          fileName,
          in -> {
            try {
              return new ch.n1b.ucum.thomas.UcumEssenceService(in);
            } catch (ch.n1b.ucum.thomas.UcumException e) {
              throw new RuntimeException(e);
            }
          });
    }

    private static UcumService loadUcumEssenceService() {
      var fileName = "ucum-essence.xml";
      return fixtures(
          fileName,
          in -> {
            try {
              return new UcumEssenceService(in);
            } catch (UcumException e) {
              throw new RuntimeException(e);
            }
          });
    }

    private static <T> T fixtures(String fn, Function<InputStream, T> mapper) {
      var classLoader = ExecutionPlan.class.getClassLoader();
      try (var in = classLoader.getResourceAsStream(fn)) {
        return mapper.apply(in);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public interface Converter<T> {
    T convert(T value, String sourceUnit, String destUnit) throws Exception;

    T parse(String raw) throws Exception;
  }
}
