package timetracker;

import java.time.LocalDateTime;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/*Clase proyecto extendidad de Actividad
Es utilizada para generalizar las partes de un trabajo o entrega.
Tiene acceso a una lista de sus proyectos inferiores, en el caso que tuviese. 
Lo mismo con las tareas que pudieran depender de él.*/
public class Proyecto extends Actividad {
  private ArrayList<Tarea> proListaTareas;
  private ArrayList<Proyecto> proListaProyectos;

  private static final Logger logger = LoggerFactory.getLogger(Proyecto.class);

  public Proyecto(String name, Proyecto p) {
    super(name, p, "Proyecto");
    this.proListaTareas = new ArrayList<Tarea>();
    this.proListaProyectos = new ArrayList<Proyecto>();
    if (this.getProyectoSuperior() != null) {
      this.getProyectoSuperior().anadirProyecto(this);
    }

    assert actInvariant() : "Invariante";
  }

  public void anadirTarea(Tarea t) {
    assert actInvariant() : "Invariante";
    proListaTareas.add(t);
    assert actInvariant() : "Invariante";
  }

  public void anadirProyecto(Proyecto p) {
    assert actInvariant() : "Invariante";
    proListaProyectos.add(p);
    assert actInvariant() : "Invariante";

  }

  /*Calcula el tiempo total del proyecto sumando los tiempos 
  totales de sus proyectos hijo y de sus tareas hijo.*/
  @Override
  public int setTiempoTotal() {
    assert actInvariant() : "Invariante";

    logger.trace("Estoy en el método setTiempoTotal de la clase Proyecto.");

    int totalTime = 0;
    int k;
    for (int i = 0; i < proListaTareas.size(); i++) {
      k = proListaTareas.get(i).setTiempoTotal();
      if (k > 0) { //Prevenir que no sume con tiempoTotal sin inicializar (=-100).
        totalTime += k;
      }
    }
    for (int i = 0; i < proListaProyectos.size(); i++) {
      k = proListaProyectos.get(i).setTiempoTotal();
      if (k > 0) { //Prevenir que no sume con tiempoTotal sin inicializar (=-100).
        totalTime += k;
      }
    }

    logger.debug("Tiempo de proyecto calculado. (t.t) {}", totalTime);

    assert actInvariant() : "Invariante";
    return totalTime;
  }


  /*Recorre todas las sub tareas y sub proyectos del proyecto en questión ejecutando 
  el getTtTotalTime en ellos */
  @Override
  public int getTtTotalTime(LocalDateTime ini, LocalDateTime fin) {
    for (int i = 0; i < proListaTareas.size(); i++) {
      sumaTtTiempoTotal(proListaTareas.get(i).getTtTotalTime(ini, fin));
    }
    for (int i = 0; i > proListaProyectos.size(); i++) {
      sumaTtTiempoTotal(proListaProyectos.get(i).getTtTotalTime(ini, fin));
    }
    return getTtTiempoTotal();
  }

  /*Crea un objeto JSON con los datos del proyecto y un array
  JSON con los datos de los proyectos, tareas e intervalos hijos*/
  @Override
  public JSONObject toJson(int depth) {
    assert actInvariant() : "Invariante";

    logger.info("Generando JSON...");
    logger.trace("Estoy en el método getJson de la clase Proyecto.");

    JSONObject jo = new JSONObject();
    try {
      jo.put("name", getNombre());
      jo.put("id", getId());
      jo.put("class", "Proyecto");
      jo.put("initialDate", getFechaInicial());
      jo.put("finalDate", getFechaFinal());
      if (getTiempoTotal() == -100) {
        jo.put("duration", 0);
      } else {
        jo.put("duration", getTiempoTotal());
      }
      if (depth > 0) {
        depth--;
        JSONArray ja = new JSONArray();
        for (int i = 0; i < proListaProyectos.size(); i++) {
          ja.put(proListaProyectos.get(i).toJson(depth));
        }
        for (int i = 0; i < proListaTareas.size(); i++) {
          ja.put(proListaTareas.get(i).toJson(depth));
        }
        jo.put("activities", ja);
      }
    } catch (JSONException e) {
      logger.error("{}", e);
    }
    assert actInvariant() : "Invariante";
    return jo;
  }

  public Actividad findActivityById(int id) {
    Actividad ActivityWithId = null;
    if (this.getId() == id) {
      ActivityWithId = this;
    }
    else {
      for (int i = 0; i < proListaProyectos.size(); i++) {
        if (proListaProyectos.get(i).getId() == id) {
          ActivityWithId = proListaProyectos.get(i);
          return ActivityWithId;
        } else {
          ActivityWithId = proListaProyectos.get(i).findActivityById(id);
          if (ActivityWithId != null){
            return ActivityWithId;
          }
        }
      }
      if (ActivityWithId == null) {
        for (int i = 0; i < proListaTareas.size(); i++) {
          if (proListaTareas.get(i).getId() == id) {
            ActivityWithId = proListaTareas.get(i);
            return ActivityWithId;
          } else {
            ActivityWithId = proListaTareas.get(i).findActivityById(id);
            if (ActivityWithId != null){
              return ActivityWithId;
            }
          }
        }
      }
    }

    return ActivityWithId;
  }
}