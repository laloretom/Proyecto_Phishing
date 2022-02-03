import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.util.*;

public class ContadorLineas extends SimpleFileVisitor<Path>
{
    private static String comandoLista;
    static Set<String> stops = new HashSet<String>();

    private static void stopWord() throws IOException {
        FileReader fl = new FileReader("stop-word-list.txt");
        BufferedReader in = new BufferedReader(fl);
        String linea = null;
        while ( (linea = in.readLine() )!= null) {
                   stops.add(linea);
        }
        in.close();
    }


    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
    {

        Map< String, Integer> mapa = new HashMap< String, Integer>();
        FileReader fl = null;
        BufferedReader in = null;

        String name = file.toAbsolutePath().toString();

        if( name.toLowerCase().endsWith(".txt")) {
            fl = new FileReader(name);
            in = new BufferedReader(fl);

            StreamTokenizer tokenizer = new StreamTokenizer(in);
            tokenizer.ordinaryChar('.');

            String linea = null;
            int contadorTotal = 0;

            // Valida las palabras y las agrega al mapa
            while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) // Lee todas las palabras
            {
                if (tokenizer.ttype == StreamTokenizer.TT_WORD)
                { //Compara si es una palabra

                    String palabra = tokenizer.sval.toLowerCase(); // obtiene una palabra
                    contadorTotal++;

                    if (!stops.contains(palabra)){ // si el mapa contiene la palabra
                        if (mapa.containsKey(palabra)) // esta la palabra en el mapa?
                        {
                            int cuenta = mapa.get(palabra); // obtiene la cuenta actual
                            mapa.put(palabra, cuenta + 1); // incrementa la cuenta
                        } // fin de if
                        else
                        {
                            mapa.put(palabra, 1); // agrega una nueva palabra con una cuenta de 1 al mapa
                        }
                    }
                }
            }

            in.close();

            int contadorLineas = tokenizer.lineno();
            int contadorMapa = mapa.size();
            double porcentaje = (contadorMapa * 100)/contadorTotal;
            DecimalFormat f = new DecimalFormat( "###.##");

            System.out.printf("%-30s %6s %8s %6s %10s%n", "Mensaje", "Lineas", "Palabras", "Utiles", "Porcentaje");
            System.out.printf("%-30s %6d %8d %6d %10s%n", name, contadorLineas, contadorTotal, contadorMapa,f.format(porcentaje)+"%");

            if(comandoLista != null)
            {
                if (comandoLista.equals("-v"))
                {
                    Set<String> claves = mapa.keySet(); // obtiene las claves
                    TreeSet<String> Ordenadas = new TreeSet<String>(claves); // ordena las claves
                    System.out.println(Ordenadas);
                }
            }
            System.out.println("|--------------------------------------------------------------------|");
        }
        return super.visitFile(file, attrs);
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException
    {
        System.out.printf("No se puede procesar:%30s%n", file.toString()) ;
        return super.visitFileFailed(file, exc);
    }


    public static void main(String[] args) throws IOException
    {

        if (args.length < 1)
        {
            System.exit(2);
        } else if (args.length > 1){
                comandoLista = args[1];
        }

        // iniciar en este directorio
        Path startingDir = Paths.get(args[0]);

        // clase para procesar los archivos
        ContadorLineas contadorLineas = new ContadorLineas();

        stopWord();


        Files.walkFileTree(startingDir, contadorLineas);
        // iniciar el recorrido de los archivos


    }


}
