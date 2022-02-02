import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Locale;

public class ContadorLineas extends SimpleFileVisitor<Path> {
  // prueba de pull

    //    se manda llamar para cada archivo en un directorio
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

        FileReader fl = null;
        BufferedReader in = null;

        String name = file.toAbsolutePath().toString();

        if( name.toLowerCase().endsWith(".txt")) {

            fl = new FileReader(name);

            in = new BufferedReader(fl);

            int contadorLineas = 0;
            int contadorCaracteres = 0;
            String linea = null;
            while ( (linea = in.readLine() )!= null) {
                contadorLineas++;
                contadorCaracteres = contadorCaracteres + linea.length();
            }
            in.close();
            System.out.printf("%-50s %6d %6d%n", name, contadorLineas, contadorCaracteres);
        }
        return super.visitFile(file, attrs);
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        System.out.printf("No se puede procesar:%30s%n", file.toString()) ;
        return super.visitFileFailed(file, exc);
    }

    public static void main(String[] args)
            throws IOException {

        // /Users/rnavarro/datos
        if (args.length < 1) {
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
