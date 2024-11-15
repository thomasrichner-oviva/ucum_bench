package ch.n1b.ucum.lib.definitions;

import java.io.InputStream;
import ch.n1b.ucum.lib.UcumException;
import ch.n1b.ucum.lib.UcumModel;

public interface DefinitionsProvider {
  public UcumModel parse(String filename) throws UcumException;
  public UcumModel parse(InputStream stream) throws UcumException;
  
}
