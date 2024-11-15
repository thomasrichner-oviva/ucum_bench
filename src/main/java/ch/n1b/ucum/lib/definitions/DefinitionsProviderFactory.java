package ch.n1b.ucum.lib.definitions;

public class DefinitionsProviderFactory {

  private static DefinitionsProvider provider = new XmlDefinitionsParser();

  public static DefinitionsProvider getProvider() {
    return provider;
  }

  public static void setProvider(DefinitionsProvider provider) {
    DefinitionsProviderFactory.provider = provider;
  }
  
}
