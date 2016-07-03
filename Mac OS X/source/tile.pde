//  Class tile represent a Tile - used as the very basic construction unit for this app output images.
//  Tile is made of cells - something like pixels.
//  When creating output image there are several different tiles which are placed to output image by their
//  tileLevel - higher tileLevel means higher intensity in input image.
//  Tile is defined by its dimensions along x, y = xCells, yCells.
//  Pattern of tile is stored in pattern[][].
//  There are several more variables not as much importatnt (eg. hover helps highlight cells
//  when mouse is over them etc...)
class Tile
{
  private int xCells;          // number of cells or pixels along tiles x axis
  private int yCells;          // number of cells or pixels along tiles y axis
  private int xCanvasSize;     // size of interactive preview area of tiles
  private int yCanvasSize;     // size of interactive preview area of tiles
  private int tileLevel;       // each tile has continuous range of intensities. later when we have input image tiles will be placed to the image by their intensity range
  private boolean[][] pattern; // pattern of tile
  private int[] hover;         // which cell has cursor inide its area in interactive preview
  private boolean showHover;    //false if mouse cursor is outside of tile interactive preview area
  private int[] firstCellPosition;  // position, where first tile beggins (first tile is top leftmost)
  private int cellSize;             // size of cell in interactive preview area 
  private int brushSize;            // size of brush 1*1/2*2/3*3 cels
  private color startColor;         // intensity range of this tile
  private color endColor;
  private float startBrightness;
  private float endBrightness;
  
  // init tile
  public Tile(int xCells, int yCells, int brushSize,int tileLevel)
  {
    this.xCells = xCells;
    this.yCells = yCells;
    this.xCanvasSize = width/4*3;
    this.yCanvasSize = height;
    this.tileLevel = tileLevel;
    this.pattern = new boolean [xCells][yCells];
    this.hover = new int[2];
    this.showHover = false;
    this.firstCellPosition = new int[2];
    this.firstCellPosition[0] = 170;
    this.firstCellPosition[1] = 10;
    this.brushSize = brushSize;
    
    // cell size is determinated so tile best fits whole canvas along x axes or y axes
    // (depends on tile ratio compared to screen canvas ratio)
    if((float)xCells/((float)yCells) < (float) (xCanvasSize-2*75-20)/((float)yCanvasSize-20))
      this.cellSize = (int)(yCanvasSize-20)/(int)(yCells);
    else
      this.cellSize = (int)(xCanvasSize-2*75-20)/(int)(xCells);
    
  }
  
  // tile width
  public int getW()
  {
    return xCells;
  }
  
  // tile height
  public int getH()
  {
    return yCells;
  }
  
  public color getStartColor()
  {
    return startColor;
  }
  
  public color getEndColor()
  {
    return endColor;
  }
  
  public float getStartBrightness()
  {
    return startBrightness;
  }
  
  public float getEndBrightness()
  {
    return endBrightness;
  }
  
  // get position of actual hover cell
  public int[] getHover()
  {
    return hover;
  }
  
  // get patern of thi tile
  public boolean[][] getPattern()
  {
    return pattern;
  }
  
  // get size of rendered cell on screen
  public int getCellSize()
  {
    return cellSize;
  }
  
  // get level of this tile
  // low level tiles are for dark areas of image, high level tiles are for lighter areas of input image
  public int getTileLevel()
  {
    return tileLevel;
  }
  
  // get cell position in screen space
  public int[] getCellPosition(int i, int j)
  {
    int[] position = {firstCellPosition[0]+i*cellSize,firstCellPosition[1]+j*cellSize};
    return position;
  }
   
  // is mouse inside the tile area (so hover should be rendered)
  public boolean isHover()
  {
    return showHover;
  }
  
  // set patern of this tile to new pattern
  public void setPattern(boolean[][] p)
  {
    pattern = p;
  }
  
  // set one cell value
  public void setPatternAtCell(int i, int j, boolean val)
  {
    pattern[i][j] = val;
  }
  
  // set new hover cell coordinates
  public void setHover(int cellX, int cellY)
  {
    hover = new int[2];
    hover[0] = cellX;
    hover[1] = cellY;
  }
  
  public void setStartColor(color c)
  {
    startColor = c;
    startBrightness = brightness(c);
  }
  
  public void setEndColor(color c)
  {
    endColor = c;
    endBrightness = brightness(c);
  }
  
  // sets showHover to disable or enable highliting
  public void setShowHover(boolean h)
  {
    showHover = h;
  }
  
  // sets different brush size
  public void setBrushSize(int size)
  {
    brushSize = size;
  }
  
  // sets tile level
  public void setTileLevel(int level)
  {
    tileLevel = level;
  }
  
  // draw highlited cells according to brush size and actual mouse position
  public void drawCellHover()
  {
    int[] cell = mouseToCell();
    
    // if mouse is not inside tile area dont highlight anything
    if(showHover)
    {
      setHover(cell[0],cell[1]);
      fill(0,255,0,100);
      noStroke();
      
      if(brushSize == 1)
      {
        rect(firstCellPosition[0]+cell[0]*cellSize,firstCellPosition[1]+cell[1]*cellSize,cellSize-1,cellSize-1);
      }
      
      if(brushSize == 2)
      {
        for(int i = 0; i < 2; i++)
        {
          for(int j = 0; j < 2; j++)
          {
            if(cell[0]+i < xCells && cell[1]+j < yCells)
            {
              rect(firstCellPosition[0]+(cell[0]+i)*cellSize,firstCellPosition[1]+(cell[1]+j)*cellSize,cellSize-1,cellSize-1);
            }
          }
        }
      }
      
      if(brushSize == 3)
      {
        for(int i = -1; i < 2; i++)
        {
          for(int j = -1; j < 2; j++)
          {
            if(cell[0]+i < xCells && cell[1]+j < yCells &&
               cell[0]+i >= 0 && cell[1]+j >= 0 )
            {
              rect(firstCellPosition[0]+(cell[0]+i)*cellSize,firstCellPosition[1]+(cell[1]+j)*cellSize,cellSize-1,cellSize-1);
            }
          }
        }
      }
    }
  }
  
  // sets cells at current hover highlited area to their negation black to white or white to black.
  public void drawCell()
  {
    // if mouse is not inside tile area dont draw anything
    if(showHover)
    {
      noStroke();
      
      rect(firstCellPosition[0]+hover[0]*cellSize,firstCellPosition[1]+hover[1]*cellSize,cellSize-1,cellSize-1);
      
      if(brushSize == 1)
      {
        if(!pattern[hover[0]][hover[1]])
        {
         pattern[hover[0]][hover[1]] = true;
         fill(0);
        }
        else
        {
          pattern[hover[0]][hover[1]] = false;
          fill(255);
        }
        rect(firstCellPosition[0]+hover[0]*cellSize,firstCellPosition[1]+hover[1]*cellSize,cellSize-1,cellSize-1);
      }
      if(brushSize == 2)
      {
        for(int i = 0; i < 2; i++)
        {
          for(int j = 0; j < 2; j++)
          {
            if(hover[0]+i < xCells && hover[1]+j < yCells)
            {
              if(!pattern[hover[0]+i][hover[1]+j])
              {
                pattern[hover[0]+i][hover[1]+j] = true;
                fill(0);
              }
              else
              {
                pattern[hover[0]+i][hover[1]+j] = false;
                fill(255);
              }
              rect(firstCellPosition[0]+(hover[0]+i)*cellSize,firstCellPosition[1]+(hover[1]+j)*cellSize,cellSize-1,cellSize-1);
            }
          }
        }
      }
      if(brushSize == 3)
      {
        for(int i = -1; i < 2; i++)
        {
          for(int j = -1; j < 2; j++)
          {
            if(hover[0]+i < xCells && hover[1]+j < yCells &&
               hover[0]+i >= 0 && hover[1]+j >= 0 )
            {
              if(!pattern[hover[0]+i][hover[1]+j])
              {
                pattern[hover[0]+i][hover[1]+j] = true;
                fill(0);
              }
              else
              {
                pattern[hover[0]+i][hover[1]+j] = false;
                fill(255);
              }
              rect(firstCellPosition[0]+(hover[0]+i)*cellSize,firstCellPosition[1]+(hover[1]+j)*cellSize,cellSize-1,cellSize-1);
            }
          }
        }
      }
    }
  }
  
  // sets cells at current hover highlited area to white or black
  public void drawCellWithBrush(boolean isWhite)
  {
    // if mouse is not inside tile area dont draw anything
    if(showHover)
    {
      noStroke();
      
      rect(firstCellPosition[0]+hover[0]*cellSize,firstCellPosition[1]+hover[1]*cellSize,cellSize-1,cellSize-1);
      
      if(brushSize == 1)
      {
        if(isWhite)
        {
         pattern[hover[0]][hover[1]] = isWhite;
         fill(0);
        }
        else
        {
          pattern[hover[0]][hover[1]] = isWhite;
          fill(255);
        }
        rect(firstCellPosition[0]+hover[0]*cellSize,firstCellPosition[1]+hover[1]*cellSize,cellSize-1,cellSize-1);
      }
      if(brushSize == 2)
      {
        for(int i = 0; i < 2; i++)
        {
          for(int j = 0; j < 2; j++)
          {
            if(hover[0]+i < xCells && hover[1]+j < yCells)
            {
              if(isWhite)
              {
                pattern[hover[0]+i][hover[1]+j] = isWhite;
                fill(0);
              }
              else
              {
                pattern[hover[0]+i][hover[1]+j] = isWhite;
                fill(255);
              }
              rect(firstCellPosition[0]+(hover[0]+i)*cellSize,firstCellPosition[1]+(hover[1]+j)*cellSize,cellSize-1,cellSize-1);
            }
          }
        }
      }
      if(brushSize == 3)
      {
        for(int i = -1; i < 2; i++)
        {
          for(int j = -1; j < 2; j++)
          {
            if(hover[0]+i < xCells && hover[1]+j < yCells &&
               hover[0]+i >= 0 && hover[1]+j >= 0 )
            {
              if(isWhite)
              {
                pattern[hover[0]+i][hover[1]+j] = isWhite;
                fill(0);
              }
              else
              {
                pattern[hover[0]+i][hover[1]+j] = isWhite;
                fill(255);
              }
              rect(firstCellPosition[0]+(hover[0]+i)*cellSize,firstCellPosition[1]+(hover[1]+j)*cellSize,cellSize-1,cellSize-1);
            }
          }
        }
      }
    }
  }
    
  
  // render cells from tile according to whats in pattern[][] rn
  public void drawGrid()
  {
    noStroke();
    for(int i = 0; i < xCells; i++)
    {
      for(int j = 0; j < yCells; j++)
      {
        if(pattern[i][j])
          fill(0);
        else
          fill(255);
        rect(firstCellPosition[0]+i*cellSize,firstCellPosition[1]+j*cellSize,cellSize-1,cellSize-1);
      }
    }
  }
  
  // mouse coordinates to particular cell indexes
  public int[] mouseToCell()
  {
    int x = mouseX;
    int y = mouseY;
    if(x > firstCellPosition[0] && x < firstCellPosition[0]+xCells*cellSize &&
       y > firstCellPosition[1] && y < firstCellPosition[1]+yCells*cellSize)
       {
         showHover = true;
         x = x-firstCellPosition[0];
         y = y-firstCellPosition[1];
         int[] cell = new int[2];
         cell[0] = x/cellSize;
         cell[1] = y/cellSize;
         return cell;
       }
    showHover = false;
    return null;
  }
}