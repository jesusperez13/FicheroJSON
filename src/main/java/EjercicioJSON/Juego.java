package EjercicioJSON;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Juego {
    private static final String fichero_personajes = "src/main/resources/personajes.json";
    private static final String fichero_ranking = "src/main/resources/ranking.json";
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<Personaje> listaPersonajes = cargarPersonajes(gson);
        if (listaPersonajes == null || listaPersonajes.isEmpty()) {
            System.out.println("No se pudieron cargar los personajes");
            return;
        }
        List<Puntuacion> ranking = cargarRanking(gson);
        System.out.println("Adivina el personaje ");
        System.out.print("Introduce tu nombre de jugador: ");
        String nombreJugador = scanner.nextLine().trim();
        if (nombreJugador.isEmpty()) {
            nombreJugador = "Anónimo";
        }
        boolean jugarDeNuevo = true;
        Random random = new Random();
        while (jugarDeNuevo) {
            Personaje personajeActual = listaPersonajes.get(random.nextInt(listaPersonajes.size()));
            List<String> pistas = personajeActual.getPistas();
            boolean acorralado = false;
            int puntosPartida = 0;
            System.out.println("Nueva partida iniciada");
            for (int i = 0; i < pistas.size(); i++) {
                System.out.println("Pista " + (i + 1) + ": " + pistas.get(i));
                System.out.print("¿Quién soy?: ");
                String respuesta = scanner.nextLine().trim();
                if (respuesta.equalsIgnoreCase(personajeActual.getNombre())) {
                    System.out.println("Correcto. Has acertado el personaje");
                    puntosPartida = (pistas.size() - i) * 10;
                    acorralado = true;
                    break;
                } else {
                    System.out.println("Incorrecto");
                }
            }
            if (!acorralado) {
                System.out.println("Te has quedado sin pistas. El personaje era: " + personajeActual.getNombre());
                puntosPartida = 0;
            }
            System.out.println("Puntos obtenidos en esta ronda: " + puntosPartida);
            ranking.add(new Puntuacion(nombreJugador, puntosPartida));
            Collections.sort(ranking);
            guardarRanking(gson, ranking);
            mostrarRanking(ranking);
            System.out.print("¿Deseas volver a jugar? (Si/No): ");
            String respuestaContinuar = scanner.nextLine().trim();
            if (respuestaContinuar.equalsIgnoreCase("No")) {
                jugarDeNuevo = false;
                System.out.println("Gracias por jugar. Saliendo de la aplicación");
            }
        }
        scanner.close();
    }
    private static List<Personaje> cargarPersonajes(Gson gson) {
        try (Reader reader = new InputStreamReader(new FileInputStream(fichero_personajes))) {
            Personaje[] array = gson.fromJson(reader, Personaje[].class);
            return array != null ? Arrays.asList(array) : null;
        } catch (IOException e) {
            System.err.println("Error al leer el fichero de personajes: " + e.getMessage());
            return null;
        }
    }
    private static List<Puntuacion> cargarRanking(Gson gson) {
        File archivo = new File(fichero_ranking);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        try (Reader reader = new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8)) {
            Puntuacion[] array = gson.fromJson(reader, Puntuacion[].class);
            return array != null ? new ArrayList<>(Arrays.asList(array)) : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
    private static void guardarRanking(Gson gson, List<Puntuacion> ranking) {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(fichero_ranking))) {
            gson.toJson(ranking, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar el ranking: " + e.getMessage());
        }
    }
    private static void mostrarRanking(List<Puntuacion> ranking) {
        System.out.println("Ranking");
        int limite = Math.min(ranking.size(), 5);
        for (int i = 0; i < limite; i++) {
            Puntuacion p = ranking.get(i);
            System.out.println((i + 1) + ". " + p.getJugador() + " - " + p.getPuntos() + " puntos");
        }
        System.out.println("------------------------------");
    }
}