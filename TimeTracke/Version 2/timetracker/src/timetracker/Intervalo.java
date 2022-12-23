package TimeTracker;

import java.lang.Object;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Observable;
import java.util.Observer;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

  
/*Clase Intervalo, tratada como observer
Trata de dividir una tarea en los lapsos de tiempos utilizados para finalizarla.*/
public class Intervalo implements Observer {
  private LocalDateTime intLdtFechaInicial;
  private LocalDateTime intLdtFechaFinal;
  private String intFechaInicial;
  private String intFechaFinal;
  private int intTiempoTotal;
  private Tarea intTareaSuperior;
  private String intClase;
  
  private static final Logger logger = LoggerFactory.getLogger(Intervalo.class);

  public Intervalo(Tarea t, LocalDateTime start) {
    this.intClase = "intervalo";
    this.intTareaSuperior = t;
    this.intLdtFechaInicial = start;
    this.intFechaInicial = start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    this.intTiempoTotal = -100; //un valor muy pequeño para trabajar con él.
    this.intTareaSuperior.setFechaInicial(start);
    this.intTareaSuperior.anadirIntervalo(this);

    logger.debug(start + " " + LocalDateTime.now());
    assert intInvariant() : "Invariante";
  }

  protected boolean intInvariant() {
    return intLdtFechaInicial != null
        && (intTiempoTotal == -100 || (intTiempoTotal > 0 && intTiempoTotal % 2 == 0));
  }

  public int intGetTiempoTotal() {
    assert intInvariant() : "Invariante";
    return this.intTiempoTotal;
  }
  
  public LocalDateTime getFechaInicial() {
    return this.intLdtFechaFinal;
  }

  public LocalDateTime getFechaFinal() {
    return this.intLdtFechaFinal;
  }

  /*Actualiza la fecha final del intervalo, calcula el tiempo total de este 
  y actuliza la fecha final de su tarea superior. Todos con el formato yyyy-MM-dd HH:mm:ss.*/
  public void intSetFechaFinal(LocalDateTime finish) {
    assert intInvariant() : "Invariante";
    assert (finish.isAfter(intLdtFechaInicial)) :
        "El tiempo final es inferior al tiempo inicial.";
    intLdtFechaFinal = finish;
    intFechaFinal = intLdtFechaFinal.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    this.intSetTiempoTotal();
    this.intTareaSuperior.setFechaFinal(finish);

    assert intInvariant() : "Invariante";
  }

  /*Actualiza el tiempo total del intervalo y comprueba 
  que no sea menor que el tiempo total anterior.*/ 
  public void intSetTiempoTotal() {
    assert intInvariant() : "Invariante";
    LocalDateTime total = intLdtFechaFinal.minusSeconds(intLdtFechaInicial.getSecond());

    logger.debug("(f.i) {}", intLdtFechaInicial);
    logger.debug("(f.f) {}", intLdtFechaFinal);
    logger.debug("(t.t) {}", total.getSecond());

    assert (total.getSecond() >= intTiempoTotal) :
        "El tiempo total futuro es inferior al tiempo total anterior.";
    this.intTiempoTotal = total.getSecond();
    assert intInvariant() : "Invariante";
  }

  //Muestra las variables del intervalo.
  public void intMostrar() {
    assert intInvariant() : "Invariante";

    logger.info("Interval: (f.i) {} (f.f) {} (t.t) {}",
                  intFechaInicial, intFechaFinal, intTiempoTotal);

    intTareaSuperior.actMostrar();

    assert intInvariant() : "Invariante";
  }

  //Crea un objeto JSON con los datos del intervalo.
  public JSONObject toJson(int depth) {
    assert intInvariant() : "Invariante";
    logger.info("Generando JSON...");
    logger.trace("Estoy en el método getJson de la clase Intervalo");

    JSONObject jo = new JSONObject();
    if(depth>0) {
      try {
        jo.put("class", intClase);
        jo.put("initialDate", intFechaInicial);
        jo.put("finalDate", intFechaFinal);
        jo.put("duration", intTiempoTotal);
      } catch (JSONException e) {
        logger.error("{}", e);
      }
      assert intInvariant() : "Invariante";
    }
    return jo;
  }

  /*Sobreescribe la clase update de Observer para guardar 
  la hora que recibe del notifyObserver de la clase Reloj.*/
  @Override
  public void update(Observable o, Object arg) {
    assert intInvariant() : "Invariante";

    this.intSetFechaFinal((LocalDateTime) arg);
    this.intMostrar();

    assert intInvariant() : "Invariante";
  }
}