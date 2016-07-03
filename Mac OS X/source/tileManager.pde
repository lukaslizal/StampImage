// tileManager manages and renders everything you can do and see when working with tiles
class TileManager
{
  private Tile[] tiles;
  private ControlP5 cp5;
  private PImage[] previews;         // images of tile previews for right side menu selection
  private PImage[] previewGradients; // images of previews of color intensity inside input image which will be subtituted with appropriate tile in the final output image
  private boolean[] previewHover;    // true when preview tile has cursor over area
  private boolean somethingToRender; // is there anything (eg highlited cells, highlited preview tiles..) new to render so we can rerender whole screen
  private color background;
  private int activeTile;     // which tile is currently active = is being modified
  private int hoverTile;      // which of tile preview (on the right side) has cursor over it
  private int xCells;         // number of pixels of tile in x direction
  private int yCells;         // number of pixels of tile in y direction
  private int brushSize;      // size of brush
  private boolean brushMode;  // do you change pixels like with brush(black color+ white color) or do you change pixels with inverting pixels
  private boolean isBlack;    // color when working in brush mode

  public TileManager(int xCells, int yCells, int brushSize, boolean brushMode, boolean isBlack, ControlP5 cp5, color background)
  {
    somethingToRender = false;
    this.cp5 = cp5;
    this.background = background;
    previews = new PImage[8];
    previewGradients = new PImage[8];
    previewHover = new boolean[8];
    tiles = new Tile[8];
    this.xCells = xCells;
    this.yCells = yCells;
    this.brushSize = brushSize;
    this.brushMode = brushMode;
    this.isBlack = isBlack;
    newTile();
    newTile();
  }
  
  public TileManager(int xCells, int yCells, ControlP5 cp5, color background )
  {
    somethingToRender = false;
    previews = new PImage[8];
    previewGradients = new PImage[8];
    previewHover = new boolean[8];
    tiles = new Tile[8];
    this.cp5 = cp5;
    this.background = background;
    this.xCells = xCells;
    this.yCells = yCells;
    this.brushSize = (int)cp5.getController("brushSize").getValue();
    this.brushMode = !((Toggle)cp5.getController("brushMode")).getState();
    this.isBlack = !((Toggle)cp5.getController("blackWhite")).getState();
  }
  
  //public TileManager(int xCells, int yCells, int brushSize, ControlP5 cp5)
  //{
  //  somethingToRender = false;
  //  previews = new PImage[8];
  //  previewGradients = new PImage[8];
  //  previewHover = new boolean[8];
  //  tiles = new Tile[8];
  //  this.cp5 = cp5;
  //  this.xCells = xCells;
  //  this.yCells = yCells;
  //  this.brushSize = brushSize;
  //  newTile();
  //  newTile();
  //}

  public Tile getActiveTile()
  {
    return tiles[activeTile];
  }
  
  public Tile[] getTiles()
  {
    return tiles;
  }
  
  public int getXCells()
  {
    return xCells;
  }
  
  public int getYCells()
  {
    return yCells;
  }
  
  public int getBrushSize()
  {
    return brushSize;
  }

  public int getHoverIndex()
  {
    return hoverTile;
  }
  
  public boolean getBrushMode()
  {
    return brushMode;
  }
  
  public boolean getIsBlack()
  {
    return isBlack;
  }
  
  public void setActiveTile(int i)
  {
    activeTile = i;
  }
  
  public void setXCells(int xCells)
  {
    this.xCells = xCells;
  }
  
  public void setYCells(int yCells)
  {
    this.yCells = yCells;
  }
  
  public void setBrushSize(int brushSize)
  {
    this.brushSize = brushSize;
  }
  
  public void setBrushMode(boolean brushMode)
  {
    this.brushMode = brushMode;
  }
  
  public void setIsBlack(boolean isBlack)
  {
    this.isBlack = isBlack;
  }
  
  // responsible for refreshing/drawing/rendering everything we see and do in Tile section
  public void renderTileManager()
  {
    background(background);
    tiles[activeTile].drawGrid();
    tiles[activeTile].drawCellHover();
    createPreview(tiles[activeTile]);
    drawPreviewHover();
    drawPreviews();
  }
  
  // removes tile and its preview, recomputes gradients/intensities for all other tiles
  public void removeTile(int i)
  {
    int c = 0;
    for(Tile t: tiles)
    {
      if(t != null)
        c += 1;
    }
    c -=1;
    
    
    if(!(c < 2))
    {
      if(i == c)
      {
        tiles[i] = null;
        previews[i] = null;
        previewGradients[i] = null;
        previewHover[i] = false;
        activeTile = i-1;
        createPreviewGradients();
        return;
      }
      else
      {
        int last = 0;
        for(int j = i; j < c; j++)
        {
          tiles[j] = tiles[j+1];
          if(tiles[j] != null)
           tiles[j].setTileLevel(j);
          previews[j] = previews[j+1];
          previewGradients[j] = previewGradients[j+1];
          previewHover[j] = previewHover[j+1];
          last = j;
        }
        last += 1;
        tiles[last] = null;
        previews[last] = null;
        previewGradients[last] = null;
        previewHover[last] = false;
        createPreviewGradients();
        
        return;
      }
    }
    
  }

  // highlights cursor area in interactive tile canvas
  public void drawPreviewHover()
  {
    int previewNumber = mouseToPreviewTile();
    if (previewNumber >= 0)
    {
      for (int i = 0; i < previewHover.length; i++)
      {
        previewHover[i] = false;
      }
      previewHover[previewNumber] = true;
      hoverTile = previewNumber;
    } else
    {
      for (int i = 0; i < previewHover.length; i++)
      {
        previewHover[i] = false;
      }
      hoverTile = -1;
    }
  }

  // if cursor is over any tile preview this function returns which tile is it
  public int mouseToPreviewTile()
  {
    for (int i = 0; i < previews.length; i += 2)
    {
      if (mouseX > (width/4*3 + 30) && mouseX < (width/4*3 + 30 + previews[0].width) &&
        mouseY > (20 + (i/2)*130) && mouseY < (20 + (i/2)*130 + previews[0].height) &&
        previews[i] != null)
      {
        return i;
      }
      if (mouseX > (width/4*3 + 160) && mouseX < (width/4*3 + 160 + previews[0].width) &&
        mouseY > (20 + (i/2)*130) && mouseY < (20 + (i/2)*130 + previews[0].height) &&
        previews[i+1] != null)
      {
        return i+1;
      }
    }
    return -1;
  }
  
  // creates preview for newly created tile
  public void createPreview(Tile t)
  {
    
    boolean[][] pattern = t.getPattern();
    PImage preview = createImage(pattern.length, pattern[0].length, RGB);

    for (int i = 0; i < pattern.length; i++)
    {
      for (int j = 0; j < pattern[0].length; j++)
      {
        int loc = i + j * pattern.length;
        if (pattern[i][j])
          preview.pixels[loc] = color(0);
        else
          preview.pixels[loc] = color(255);
      }
    }
    preview.resize(100, 0);
    previews[t.getTileLevel()] = resizeToFit(preview, 100, 100);
  }
  
  // draws all previews (on the right side of Tile section)
  public void drawPreviews()
  {
    for (int i = 0; i < previews.length; i += 2)
    {
      if (previews != null && previewHover != null && previews[i] != null && previewGradients[i] != null)
      {
        if (previewHover[i])
        {
          fill(cp5.getController("quit").getColor().getForeground());
          rect(width/4*3 + 30 - 10, 20 + (i/2)*130 - 10, previews[i].width+20, previews[i].height+20);
        } else if (activeTile == i)
        {
          fill(cp5.getController("quit").getColor().getForeground());
          rect(width/4*3 + 30 - 10, 20 + (i/2)*130 - 10, previews[i].width+20, previews[i].height+20);
        }
        image(previews[i], width/4*3 + 30, 20 + (i/2)*130);
        image(previewGradients[i], width/4*3 + 30, 10 + (i/2)*130);
      }
      if (previews != null && previewHover != null && previews[i+1] != null && previewGradients[i+1] != null)
      {
        if (previewHover[i+1])
        {
          fill(cp5.getController("quit").getColor().getForeground());
          rect(width/4*3 + 160 - 10, 20 + (i/2)*130 - 10, previews[i].width+20, previews[i].height+20);
        } else if (previewHover != null && activeTile == i+1)
        {
          fill(cp5.getController("quit").getColor().getForeground());
          rect(width/4*3 + 160 - 10, 20 + (i/2)*130 - 10, previews[i].width+20, previews[i].height+20);
        }
        image(previews[i+1], width/4*3 + 160, 20 + (i/2)*130);
        image(previewGradients[i+1], width/4*3 + 160, 10 + (i/2)*130);
      }
    }
  }

  // creates previews of color intensities to be subtituted in input image for matching tile (each tile gets one unique color intensity preview)
  public void createPreviewGradients()
  {
    int i = 0;
    while (i < previews.length && previews[i] != null)
    {
      i++;
    }
    if (i==0)
      i++;
    int step = 255/i+1;
    int intensity = 255;

    for (int j = 0; j < i; j++)
    {
      color startIntensity = color(intensity-j*step);
      color endIntensity;
      if(intensity-(j+1)*step > 0)
        endIntensity = color(intensity-(j+1)*step);
      else
        endIntensity = color(0);
      previewGradients[j] = createImage(2, 1, RGB);
      previewGradients[j].pixels[0] = startIntensity;
      previewGradients[j].pixels[1] = endIntensity;
      previewGradients[j].resize(100, 10);
      tiles[j].setStartColor(startIntensity);
      tiles[j].setEndColor(endIntensity);
    }
  }
  
  // creates new tile
  public void newTile()
  {
    int i = 0;
    while (i < tiles.length && tiles[i] != null)
    {
      i++;
    }

    if (i < tiles.length)
    {
      if(tiles[0] != null)
      {
        tiles[i] = new Tile(tiles[0].getW(), tiles[0].getH(), brushSize, i);
      }
      else
      {
        tiles[i] = new Tile(xCells, yCells, brushSize, i);
      }
      createPreview(tiles[i]);
      createPreviewGradients();
    }
  }

  // used for creating image preview miniatures
  public PImage resizeToFit(PImage original, int xMax, int yMax)
  {
    PImage resized = original.copy();
    if (original.width > xMax && original.height > yMax)
    {
      if ((float)(original.width)/(float)(original.height) > (float)(xMax)/(float)yMax)
        resized.resize(xMax, 0);
      else
        resized.resize(0, yMax);
    } else if (original.width > xMax)
      resized.resize(xMax, 0);
    else if (original.height > yMax)
      resized.resize(0, yMax);
    return resized;
  }
  
  // moves selected tile to the lighter color intensities
  public void moveTileBack()
  {
    if(!(activeTile-1 < 0) && activeTile > 0)
    {
      Tile tmpTile = tiles[activeTile];
      PImage tmpPreview = previews[activeTile];
      PImage tmpPreviewGradients = previewGradients[activeTile];
      boolean tmpPreviewHover = previewHover[activeTile];
      
      tiles[activeTile-1].setStartColor(tmpTile.getStartColor());
      tiles[activeTile-1].setEndColor(tmpTile.getEndColor());
      tmpTile.setStartColor(tiles[activeTile-1].getStartColor());
      tmpTile.setEndColor(tiles[activeTile-1].getEndColor());
      
      tiles[activeTile-1].setTileLevel(tiles[activeTile-1].getTileLevel()+1);
      tiles[activeTile] = tiles[activeTile-1];
      previews[activeTile] = previews[activeTile-1];
      previewGradients[activeTile] = previewGradients[activeTile-1];
      previewHover[activeTile] = previewHover[activeTile-1];
      
      
      tmpTile.setTileLevel(tmpTile.getTileLevel()-1);
      tiles[activeTile-1] = tmpTile;
      tiles[activeTile-1].setStartColor(tiles[activeTile-1].getStartColor());
      previews[activeTile-1] = tmpPreview;
      previewGradients[activeTile-1] = tmpPreviewGradients;
      previewHover[activeTile-1] = tmpPreviewHover;
      
      activeTile -= 1;
      createPreviewGradients();
    }
  }
  
  // moves tile to the darker intensities
  public void moveTileForward()
  {
    if(!(activeTile+1 > tiles.length-1) && tiles[activeTile+1] != null)
    {
      Tile tmpTile = tiles[activeTile];
      PImage tmpPreview = previews[activeTile];
      PImage tmpPreviewGradients = previewGradients[activeTile];
      boolean tmpPreviewHover = previewHover[activeTile];
      
      tiles[activeTile+1].setStartColor(tmpTile.getStartColor());
      tiles[activeTile+1].setEndColor(tmpTile.getEndColor());
      tmpTile.setStartColor(tiles[activeTile+1].getStartColor());
      tmpTile.setEndColor(tiles[activeTile+1].getEndColor());
      
      tiles[activeTile+1].setTileLevel(tiles[activeTile+1].getTileLevel()-1);
      tiles[activeTile] = tiles[activeTile+1];
      previews[activeTile] = previews[activeTile+1];
      previewGradients[activeTile] = previewGradients[activeTile+1];
      previewHover[activeTile] = previewHover[activeTile+1];
      
      
      tmpTile.setTileLevel(tmpTile.getTileLevel()+1);
      tiles[activeTile+1] = tmpTile;
      tiles[activeTile+1].setStartColor(tiles[activeTile+1].getStartColor());
      previews[activeTile+1] = tmpPreview;
      previewGradients[activeTile+1] = tmpPreviewGradients;
      previewHover[activeTile+1] = tmpPreviewHover;
      
      activeTile += 1;
      createPreviewGradients();
    }
  }
}