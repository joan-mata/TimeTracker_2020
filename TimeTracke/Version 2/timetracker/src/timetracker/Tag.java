package TimeTracker;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/*Clase Tag
 Permite buscar actividades a partir de un tag.
 Tiene la relación entre tags y actividades.*/
public class Tag {
  private ArrayList<String> tagList;
  private ArrayList<String> actList;

  private static final Logger logger = LoggerFactory.getLogger(Tag.class);

  /*Constructor crea dos arrays, uno de tags y otro de actividades,
  que iremos actualizando a la par.*/
  public Tag() {
    this.tagList = new ArrayList<String>();
    this.actList = new ArrayList<String>();
    assert tagInvariant() : "Invariante";
  }

  protected boolean tagInvariant() {
    return this.tagList.size() == this.actList.size();
  }

  //Introduce un tag y su correspondiente actividad.
  public void anadirTag(String act, String tag) {
    assert tagInvariant() : "Invariante";
    tagList.add(tag);
    actList.add(act);
    logger.debug("Añadiendo tag");
    assert tagInvariant() : "Invariante";
  }

  /*searchTag recorrera la lista de tags buscando el
  tag que haya recibido y por cada match, añadira la
  actividad correspondiente de la lista de actividades
  a una nueva lista que devolvera al finalizar.*/
  public ArrayList<String> searchTag(String tag) {
    logger.trace("Estoy en el método searchTag de la clase Tag");
    ArrayList<String> list = new ArrayList<String>();
    logger.debug("(tag) {}", tag);
    for (int i = 0; i < tagList.size(); i++) {
      logger.info("Buscando tag...");
      if (tagList.get(i).matches("(?i).*" + tag + ".*")) {
        list.add(actList.get(i));
        logger.info("Tag encontrado");
      }
    }
    if (list.isEmpty()) {
      logger.info("Lista vacia");
    }
    return list;
  }
}
