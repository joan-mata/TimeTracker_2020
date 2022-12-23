package timetracker;

import java.time.LocalDateTime;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/*Clase Tarea, extendida de Actividad
Su función es dividir una clase proyecto en partes más pequeñas,
y con ello más sencillas de trabajar.
Tendrá una lista de los intervalos que se han realizado durante la tarea*/
public class Tarea extends Actividad {
  private ArrayList<Intervalo> tarListaIntervalos;
  private Reloj tarReloj;
  private boolean active = false;
  
  private static final Logger logger = LoggerFactory.getLogger(Tarea.class);

  public Tarea(String name, Proyecto p) {
    super(name, p, "Tarea");
    this.tarListaIntervalos = new ArrayList<Intervalo>();
    this.getProyectoSuperior().anadirTarea(this);
    assert tarInvariant() : "Invariante";
  }

  protected boolean tarInvariant() {
    return actInvariant() && this.getProyectoSuperior() != null;
  }

  public boolean getActive(){
    return this.active;
  }

  //Conseguimos la instancia única del reloj
  public Reloj tarGetInstance() {
    assert tarInvariant() : "Invariante";
    return tarReloj.getInstance();
  }

  /*De los 5 casos posibles, sólo cojemos los 4 que nos interesan para la suma del tiempo total,
  aquí vemos cada caso por separado con su método para hacer la suma*/
  @Override
  public int getTtTotalTime(LocalDateTime ini, LocalDateTime fin) {
    assert tarInvariant() : "Invariante";
    for (int i = 0; i < tarListaIntervalos.size(); i++) {

      // Caso 1: Tiempo Inicial fuera, tiempo final dentro
      if (tarListaIntervalos.get(i).getFechaInicial().isBefore(ini)
          && tarListaIntervalos.get(i).getFechaFinal().isBefore(fin)) {
                      
        // inicial = ini, final = getfinal
        sumaTtTiempoTotal(tarListaIntervalos.get(i).getFechaFinal().minusSeconds(ini.getSecond()).getSecond());

      // Caso 2: Tiempo inicial dentro, tiempo final dentro
      } else if (tarListaIntervalos.get(i).getFechaInicial().isAfter(ini) 
                && tarListaIntervalos.get(i).getFechaFinal().isBefore(fin)) {

        // inicial = getinicial, final = getfinal
        sumaTtTiempoTotal(tarListaIntervalos.get(i).getFechaFinal().minusSeconds(tarListaIntervalos.get(i).getFechaInicial().getSecond()).getSecond());

      // Caso 3: Tiempo inicial dentro, tiempo final fuera
      } else if (tarListaIntervalos.get(i).getFechaInicial().isAfter(ini) 
          && tarListaIntervalos.get(i).getFechaFinal().isAfter(fin)) {
                       
        // inicial = getinicial, final = fin
        sumaTtTiempoTotal(fin.minusSeconds(tarListaIntervalos.get(i).getFechaInicial().getSecond()).getSecond());
      
      //Caso 4: Tiempo inicial fuera, tiempo final fuera
      } else if (tarListaIntervalos.get(i).getFechaInicial().isBefore(ini)
          && tarListaIntervalos.get(i).getFechaFinal().isAfter(fin)) {

        // inicial = ini,  final = fin
        sumaTtTiempoTotal(fin.minusSeconds(ini.getSecond()).getSecond());
      }
    }
    assert tarInvariant() : "Invariante";
    return getTtTiempoTotal();
  }

  //Añade un nuevo intervalo a la lista de intervalos de la tarea.
  public void anadirIntervalo(Intervalo i) {
    assert tarInvariant() : "Invariante";
    this.tarListaIntervalos.add(i);
    assert tarInvariant() : "Invariante";
  }

  public Intervalo getLastInterval(){
    return this.tarListaIntervalos.get(tarListaIntervalos.size() - 1);
  }

  //Conseguimos el tiempo total de tarea al sumar los tiempos de sus intervalos.
  @Override
  public int setTiempoTotal() {
    assert tarInvariant() : "Invariante";

    logger.debug("Tiempo de intervalo sumado.");
    logger.trace("Estoy en el método setTiempoTotal de la clase Tarea.");

    int totalTime = 0;
    int k;
    for (int i = 0; i < tarListaIntervalos.size(); i++) {
      k = tarListaIntervalos.get(i).intGetTiempoTotal();
      if (k > 0) { //Prevenir que no sume con tiempoTotal sin inicializar (=-100).
        totalTime += k;
      }
    }
    logger.debug("(t.t) {}", totalTime);
    assert (totalTime >= getTiempoTotal()) :
        "El tiempo total futuro es inferior al tiempo total anterior.";
    assert tarInvariant() : "Invariante";
    return totalTime;
  }

  //Inicializa el intervalo que toca, nuevo en la lista y lo muestra.
  public void start() {
    assert tarInvariant() : "Invariante";

    logger.trace("Estoy en el método start de la clase Tarea.");

    LocalDateTime hora = LocalDateTime.now();
    Intervalo i = new Intervalo(this, hora);
    tarGetInstance().addObserver(i);
    active = true;
    this.tarListaIntervalos.get(this.tarListaIntervalos.size() - 1).updateActive(true);

    assert tarInvariant() : "Invariante";
  }

  //Finaliza la tarea.
  public void stop() {
    assert tarInvariant() : "Invariante";

    logger.trace("Estoy en el método stop de la clase Tarea.");

    Intervalo i = this.tarListaIntervalos.get(this.tarListaIntervalos.size() - 1);
    tarGetInstance().deleteObserver(i);
    active = false;
    this.tarListaIntervalos.get(this.tarListaIntervalos.size() - 1).updateActive(false);
    logger.debug("Salí stop()");
    assert tarInvariant() : "Invariante";
  }

  /*Crea un objeto JSON con los datos del proyecto 
  y un array JSON con los datos de los intervalos hijo.*/
  @Override
  public JSONObject toJson(int depth) {
    assert tarInvariant() : "Invariante";

    logger.info("Generando JSON...");
    logger.trace("Estoy en el método getJson de la clase Tarea.");
    JSONObject jo = new JSONObject();
    try {
      jo.put("name", getNombre());
      jo.put("id", getId());
      jo.put("class", "Tarea");
      jo.put("active", getActive());
      jo.put("initialDate", getFechaInicial());
      jo.put("finalDate", getFechaFinal());

      if (getTiempoTotal() == -100) {
        jo.put("duration", 0);
      } else {
        jo.put("duration", getTiempoTotal());
      }
      JSONArray ja = new JSONArray();
      if(depth>0) {
        depth--;
        for (int i = 0; i < tarListaIntervalos.size(); i++) {
          ja.put(tarListaIntervalos.get(i).toJson(depth));
        }
      }
      jo.put("intervals", ja);
    } catch (JSONException e) {
      logger.error("{}", e);
    }
    assert tarInvariant() : "Invariante";
    return jo;
  }

  @Override
  public Actividad findActivityById(int id) {
    return null;
  }

}