package ch.n1b.ucum;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import ch.n1b.ucum.lib.Decimal;
import ch.n1b.ucum.lib.UcumEssenceService;
import ch.n1b.ucum.lib.UcumException;
import ch.n1b.ucum.lib.UcumService;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class DecimalBenchmark {
  public static void main(String[] args) throws Exception {
    org.openjdk.jmh.Main.main(args);
  }

  static final String SOURCE_UNIT = "mol.m-3";
  static final String TARGET_UNIT = "mmol/L";

  @Benchmark
  @Fork(value = 1, warmups = 1)
  @Warmup(iterations = 10, time = 500, timeUnit = MILLISECONDS)
  @Measurement(iterations = 10, time = 200, timeUnit = MILLISECONDS)
  @BenchmarkMode(Mode.Throughput)
  public void benchmarkDecimalConvert(ExecutionPlan plan, Blackhole blackhole)
      throws UcumException {

    var got = plan.ucumService.convert(plan.value, SOURCE_UNIT, TARGET_UNIT);
    blackhole.consume(got);
  }

  @State(Scope.Benchmark)
  public static class ExecutionPlan {

    static final int PRECISION = 64;

    public UcumService ucumService;

    @Param({"nop", "ucum"})
    public String alg;

    public Decimal value;

    @Setup(Level.Invocation)
    public void setUp() throws Exception {
      ucumService = getUcumEssenceService();

      value = new Decimal("3.1415926535", PRECISION);
    }

    private UcumService getUcumEssenceService() throws FileNotFoundException, UcumException {
      String fileName = "ucum-essence.xml";
      ClassLoader classLoader = getClass().getClassLoader();
      File file = new File(classLoader.getResource(fileName).getFile());
      InputStream inputStream = new FileInputStream(file);
      UcumService ucumService = new UcumEssenceService(inputStream);
      return ucumService;
    }
  }
}
