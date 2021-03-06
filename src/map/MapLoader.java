package map;

import algorithm.Path;
import graphics.Sprite;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MapLoader 
{
    private FileInputStream inputStream;
    
    //Takes in path to a map, returns a Map object
    public Map load(String path, Sprite sprite)
    {
        try 
        {
            inputStream = new FileInputStream(new File(getClass().getResource(path).toURI())); //Some crap to read file
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            
            byte[] data = Files.readAllBytes(Paths.get(getClass().getResource(path).toURI()));
            
            return new Map(data, sprite, br);
        }
        catch (FileNotFoundException ex) 
        {
            System.out.println(ex.getMessage());
        }
        catch (IOException | URISyntaxException ex) 
        {
            System.out.println(ex.getMessage());
        }
        return null;
    }
}
