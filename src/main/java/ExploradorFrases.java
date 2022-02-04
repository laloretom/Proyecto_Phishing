import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.util.*;

public class ExploradorFrases extends SimpleFileVisitor<Path> {

    class Frases {
        String frase;
        int puntos;

        public Frases(String frase, String puntos) {
            this.frase = frase;
            this.puntos = Integer.parseInt(puntos);
        }
    }

    static LinkedList<Frases> frases = new LinkedList<Frases>();

    public void CargaFrases() {
        try {
            //Abrimos csv
            Scanner scanner = new Scanner(new File("phishing_phrase.csv"));
            //leemos archivo
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                String frase = parts[0];
                String puntos = parts[1];
                frases.add(new Frases(frase, puntos));
            }
        } catch (IOException g) {
            System.err.println(g.getMessage());
        }
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

        Map<String, Integer> mapa = new HashMap<String, Integer>();
        FileReader fl = null;
        BufferedReader in = null;

        String name = file.toAbsolutePath().toString();

        if (name.toLowerCase().endsWith(".txt")) {
            fl = new FileReader(name);
            in = new BufferedReader(fl);
            String texto = "";
            String linea;

            int puntos = 0;

            while ((linea = in.readLine()) != null) {
                texto = texto + " " + linea;
            }

            for (Frases x : frases) {
                if (texto.toLowerCase().contains(x.frase.toLowerCase())) {
                    puntos = x.puntos;
                }
            }

            in.close();

            System.out.printf("%-30s %6s%n", "Mensaje", "Puntos");
            System.out.printf("%-30s %6d%n", name, puntos);
        }
        return super.visitFile(file, attrs);
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        System.out.printf("No se puede procesar:%30s%n", file.toString());
        return super.visitFileFailed(file, exc);
    }


    public static void main(String[] args) throws IOException {

        if (args.length < 1) {
            System.exit(2);
        }

        // iniciar en este directorio
        Path startingDir = Paths.get(args[0]);

        // clase para procesar los archivos
        ExploradorFrases exploradorFrases = new ExploradorFrases();

        //carga la lista de frases
        exploradorFrases.CargaFrases();

        Files.walkFileTree(startingDir, exploradorFrases);
        // iniciar el recorrido de los archivos

    }
}
