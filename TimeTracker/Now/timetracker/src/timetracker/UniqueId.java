package timetracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UniqueId {
  private int id;
  private static UniqueId uniqueInstance;
  private static final Logger logger = LoggerFactory.getLogger(UniqueId.class);

  private UniqueId() { this.id = -1; }

  public static synchronized UniqueId uniqueGetInstance() {
    if (uniqueInstance == null) {
      uniqueInstance = new UniqueId();
      logger.info("UniqueId instanciado");
    }
    logger.trace("Estoy en el método getInstance de la clase UniqueId");
    return uniqueInstance;
  }

  public int getUniqueId(){
    id++;
    return id;
  }
}