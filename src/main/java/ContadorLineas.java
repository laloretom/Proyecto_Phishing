import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class ContadorLineas extends SimpleFileVisitor<Path>
{
    private Map< String, Integer> mapa;


    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
    {
        mapa = new HashMap< String, Integer>();
        FileReader fl = null;
        BufferedReader in = null;

        String name = file.toAbsolutePath().toString();

        if( name.toLowerCase().endsWith(".txt")) {

            fl = new FileReader(name);

            in = new BufferedReader(fl);

            int contadorLineas = 0;
            int contadorCaracteres = 0;
            String linea = null;

            StreamTokenizer tokenizer = new StreamTokenizer(in);


            // Valida las palabras y las agrega al mapa
            while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) // Lee todas las palabras
            {
                if (tokenizer.ttype == StreamTokenizer.TT_WORD){ //Compara si es una palabra
                    String palabra = tokenizer.sval.toLowerCase(); // obtiene una palabra
                    // si el mapa contiene la palabra
                    if (mapa.containsKey(palabra)) // esta la palabra en el mapa?
                    {
                        int cuenta = mapa.get(palabra); // obtiene la cuenta actual
                        mapa.put(palabra, cuenta + 1); // incrementa la cuenta
                    } // fin de if
                    else {
                        mapa.put(palabra, 1); // agrega una nueva palabra con una cuenta de 1 al mapa
                    }

                }
            }

            in.close();
            contadorLineas = tokenizer.lineno();
            int contadorPalabras = mapa.size();
            System.out.printf("%-50s %6d %6d%n", name, contadorLineas, contadorPalabras);

                    Set< String> claves = mapa.keySet(); // obtiene las claves
                    // ordena las claves
                    TreeSet< String> Ordenadas = new TreeSet< String>(claves);
                    for (String clave : Ordenadas) {
                        System.out.printf(clave + " ");
                    }
                    System.out.printf("\n");

        }
        return super.visitFile(file, attrs);
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        System.out.printf("No se puede procesar:%30s%n", file.toString()) ;
        return super.visitFileFailed(file, exc);
    }

    public static void main(String[] args) throws IOException
    {

        // /Users/rnavarro/datos
        if (args.length < 1)
        {
            System.exit(2);
        }

        // iniciar en este directorio
        Path startingDir = Paths.get(args[0]);
        // clase para procesar los archivos
        ContadorLineas contadorLineas = new ContadorLineas();

        // iniciar el recorrido de los archivos
        Files.walkFileTree(startingDir, contadorLineas);

    }


}
