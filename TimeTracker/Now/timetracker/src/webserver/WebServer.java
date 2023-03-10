package webserver;
import timetracker.*;

//import core.Actividad;
//import core.Tarea;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

// Based on
// https://www.ssaurel.com/blog/create-a-simple-http-web-server-in-java
// http://www.jcgonzalez.com/java-socket-mini-server-http-example

public class WebServer {
  private static final int PORT = 8080; // port to listen to

  private Actividad currentActivity;
  private final Actividad root;

  public WebServer(Actividad root) {
    this.root = root;
    System.out.println(root);
    currentActivity = root;
    try {
      ServerSocket serverConnect = new ServerSocket(PORT);
      System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");
      // we listen until user halts server execution
      while (true) {
        // each client connection will be managed in a dedicated Thread
        new SocketThread(serverConnect.accept());
        // create dedicated thread to manage the client connection
      }
    } catch (IOException e) {
      System.err.println("Server Connection error : " + e.getMessage());
    }
  }

  private Actividad findActivityById(int id) {
    return root.findActivityById(id);
  }

  private class SocketThread extends Thread {
    // SocketThread sees WebServer attributes
    private final Socket insocked;
    // Client Connection via Socket Class

    SocketThread(Socket insocket) {
      this.insocked = insocket;
      this.start();
    }

    @Override
    public void run() {
      // we manage our particular client connection
      BufferedReader in;
      PrintWriter out;
      String resource;

      try {
        // we read characters from the client via input stream on the socket
        in = new BufferedReader(new InputStreamReader(insocked.getInputStream()));
        // we get character output stream to client
        out = new PrintWriter(insocked.getOutputStream());
        // get first line of the request from the client
        String input = in.readLine();
        // we parse the request with a string tokenizer

        System.out.println("sockedthread : " + input);

        StringTokenizer parse = new StringTokenizer(input);
        String method = parse.nextToken().toUpperCase();
        // we get the HTTP method of the client
        if (!method.equals("GET")) {
          System.out.println("501 Not Implemented : " + method + " method.");
        } else {
          // what comes after "localhost:8080"
          resource = parse.nextToken();
          System.out.println("input " + input);
          System.out.println("method " + method);
          System.out.println("resource " + resource);

          parse = new StringTokenizer(resource, "/[?]=&");
          int i = 0;
          String[] tokens = new String[20];
          // more than the actual number of parameters
          while (parse.hasMoreTokens()) {
            tokens[i] = parse.nextToken();
            System.out.println("token " + i + "=" + tokens[i]);
            i++;
          }

          // Make the answer as a JSON string, to be sent to the Javascript client
          String answer = makeHeaderAnswer() + makeBodyAnswer(tokens);
          System.out.println("answer\n" + answer);
          // Here we send the response to the client
          out.println(answer);
          out.flush(); // flush character output stream buffer
        }

        in.close();
        out.close();
        insocked.close(); // we close socket connection
      } catch (Exception e) {
        System.err.println("Exception : " + e);
      }
    }

    private String makeBodyAnswer(String[] tokens) {
      String body = "";
      switch (tokens[0]) {
        case "get_tree" : {
          int id = Integer.parseInt(tokens[1]);
          Actividad Actividad = findActivityById(id);
          assert (Actividad != null);
          body = Actividad.toJson(1).toString();
          break;
        }
        case "start": {
          int id = Integer.parseInt(tokens[1]);
          Actividad Actividad = findActivityById(id);
          assert (Actividad != null);
          Tarea Tarea = (Tarea) Actividad;
          Tarea.start(); //TODO: cambiar start para configurar hora de inicio proyectos anteriores
          body = "{}"; //TODO: ??Que muestro por pantalla? ??Modo JSON?
          break;
        }
        case "stop": {
          int id = Integer.parseInt(tokens[1]);
          Actividad Actividad = findActivityById(id);
          assert (Actividad != null);
          Tarea Tarea = (Tarea) Actividad;
          Tarea.stop();
          body = "{}"; //TODO: ??Que muestro por pantalla? ??Modo JSON?
          break;
        }
        // TODO: add new Tarea, project
        case "new_project": {
          int id = Integer.parseInt(tokens[1]);
          Actividad Actividad = findActivityById(id);
          assert (Actividad != null);
          String nombre = "Proyecto X"; //TODO: Como pregunto el nombre?
          Proyecto Proyecto = new Proyecto(nombre, (Proyecto) Actividad);
          body = "{}"; //TODO: ??Que muestro por pantalla? ??Modo JSON?
          break;
        }
        case "new_task": {
          int id = Integer.parseInt(tokens[1]);
          Actividad Actividad = findActivityById(id);
          assert (Actividad!=null);
          //TODO: Como pregunto el nombre?
          String nombre = "Tarea X";
          Tarea Tarea = new Tarea(nombre, (Proyecto) Actividad);
          body = "{}"; //TODO: ??Que muestro por pantalla? ??Modo JSON?
          break;
        }
        // TODO: edit Tarea, project properties
        case "edit_project": {
          int id = Integer.parseInt(tokens[1]);
          Actividad Actividad = findActivityById(id);
          assert (Actividad!=null);
          //TODO: Como cogo los datos ha editar?
          //funcion
          body = "{}"; //TODO: ??Que muestro por pantalla? ??Modo JSON?
          break;
        }

        case "edit_task": {
          int id = Integer.parseInt(tokens[1]);
          Actividad Actividad = findActivityById(id);
          assert (Actividad!=null);
          //funcion
          body = "{}"; //TODO: ??Que muestro por pantalla? ??Modo JSON?
          break;
        }
        default:
          assert false;
      }
      System.out.println(body);
      return body;
    }

    private String makeHeaderAnswer() {
      String answer = "";
      answer += "HTTP/1.0 200 OK\r\n";
      answer += "Content-type: application/json\r\n";
      answer += "\r\n";
      // blank line between headers and content, very important !
      return answer;
    }
  } // SocketThread

} // WebServer