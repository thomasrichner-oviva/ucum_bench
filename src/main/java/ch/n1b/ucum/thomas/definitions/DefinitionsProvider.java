package ch.n1b.ucum.thomas.definitions;

import java.io.InputStream;
import ch.n1b.ucum.thomas.UcumException;
import ch.n1b.ucum.thomas.UcumModel;

public interface DefinitionsProvider {
  public UcumModel parse(String filename) throws UcumException;
  public UcumModel parse(InputStream stream) throws UcumException;
  
}
