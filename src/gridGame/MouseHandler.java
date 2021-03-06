package gridGame;

import block.Unit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


public class MouseHandler extends MouseAdapter
{
    /* Private Variables
    pX, pY      - The selected units x and y grid coordinates
    cX, cY      - The current mouse x and y grid coordinates
    mX, mY      - The mouse x and y coordinates
    found       - True when the walk path has been found
    added       - True after walk path has been calculated up to current tile
    canPath     - True while left click is pressed
    selected    - Unit that is currently selected (Unit at [pX][pY])
    walkList    - List of tiles that the unit will walk on
    */
    private int pX, pY, cX, cY, mX, mY;
    private boolean found, added, canPath;
    private Unit selected;
    private ArrayList<int[]> walkList;
    
    //Initializes the selected grid to one that does not exist
    public MouseHandler()//Check again later
    {
        cX = Game.fieldWidth + Game.xOff;
        cY = Game.fieldWidth + Game.yOff;
    }

    //Updates the mouse coordinates, and interface
    @Override
    public void mousePressed(MouseEvent e) 
    {
        mX = e.getX();
        mY = e.getY();
        pX = (int) Math.floor((mX - Game.MAPOFFX) / (double) Game.TILESIZE) + Game.xOff;
        pY = (int) Math.floor((mY - Game.MAPOFFY) / (double) Game.TILESIZE) + Game.yOff;
        
        switch(e.getButton())
        {
            case MouseEvent.BUTTON1: //Left click
                        
                found = false;
                canPath = true;
                Game.gui.update(mX, mY, true, 1);
                //System.out.println("pressed left");
                break;
                
            case MouseEvent.BUTTON2: //Middle click
                Game.gui.update(mX, mY, true, 2);
                //System.out.println("pressed middle");
                break;
                
            case MouseEvent.BUTTON3: //Right click
                Game.gui.update(mX, mY, true, 3);
                //System.out.println("pressed right");
                break;
        }
        Game.gui.update(mX, mY);
    }
    
    //Updates the mouse coordinates, and interface.  Also does pathfinding
    @Override
    public void mouseDragged(MouseEvent e)
    {
        mX = e.getX();
        mY = e.getY();
        
        if(canPath)
        {
            if(found) //After all possible movement paths have been found, it can start finding walk path
            {
                //sets added to false if current tile changes, floor and cast to properly handle negatives
                added = (int) Math.floor((mX - Game.MAPOFFX) / (double) Game.TILESIZE) + Game.xOff == cX && (int) Math.floor((mY - Game.MAPOFFY) / (double) Game.TILESIZE) + Game.yOff == cY;
                
                if(!added) //If moved to new tile, find walk path to new tile
                {
                    cX = (int) Math.floor((mX - Game.MAPOFFX) / (double) Game.TILESIZE) + Game.xOff;
                    cY = (int) Math.floor((mY - Game.MAPOFFY) / (double) Game.TILESIZE) + Game.yOff;
                    added = Game.paths.addPath(cX, cY);
                }
                //System.out.println(cX + " & " + cY);
            }
            else
            {  
                selected = Game.map.getUnit(pX, pY);
                if(Game.gui.canSelect(selected))
                {
                    //Find Paths
                    Game.gui.update(selected);
                    Game.paths.findPath(selected.pathInfo());
                    //Game.paths.printPaths();
                    found = true;
                }
            }
        }
        Game.gui.update(mX, mY);
    }
    
    //Updates the mouse coordinates, and interface
    @Override
    public void mouseMoved(MouseEvent e)
    {
        mX = e.getX();
        mY = e.getY();
        cX = (int) Math.floor((mX - Game.MAPOFFX) / (double) Game.TILESIZE) + Game.xOff;
        cY = (int) Math.floor((mY - Game.MAPOFFY) / (double) Game.TILESIZE) + Game.yOff;

        Game.gui.update(mX, mY, false, 0); //need 1 so that buttons highlight
    }
    
    //Updates the interface, and also moves unit if applicable
    @Override
    public void mouseReleased(MouseEvent e) 
    {
        switch(e.getButton())
        {
            case MouseEvent.BUTTON1: //Left click
                //System.out.println("X: " + cX + ", Y: " + cY + "\t");
                selected = Game.map.getUnit(pX, pY);
                if(Game.gui.canSelect(selected))
                {
                    walkList = new ArrayList<>(Game.paths.getWalk());
                    Game.paths.clearPaths();
                    //if(Game.inGrid(cX, cY)) Shouldn't need this check, move function already performs check
                    //System.out.println("Moving unit");
                    selected.move(cX, cY, walkList);
                }
                canPath = false;
                Game.gui.update(selected);
                Game.gui.update(e.getX(), e.getY(), false, 1);
                //System.out.println("released left");
                break;
                
            case MouseEvent.BUTTON2: //Middle click
                Game.gui.update(e.getX(), e.getY(), false, 2);
                //System.out.println("released middle");
                break;
                
            case MouseEvent.BUTTON3: //Right click
                Game.gui.update(e.getX(), e.getY(), false, 3);
                //System.out.println("released right");
                break;
        }
    }
    
    //Hides mouse if you move out of the window
    @Override
    public void mouseExited(MouseEvent e)
    {
        Game.gui.update(false);
        Game.gui.update(0, 0);
    }
    
    //Brings mouse back
    @Override
    public void mouseEntered(MouseEvent e)
    {
        Game.gui.update(true);
    }
}

//Select unit when mouse is pressed
//show paths when mouse pressed for short duration instead of dragged
