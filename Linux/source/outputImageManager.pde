// outputImageManager renders everything you can do and see when adjusting output mosaic settings
class OutputImageManager{
  
  private Mosaic mosaic;               // mosaic
  private color background;
  private PImage input;                // input image
  private PImage infoGraphics;         // info image about using arrow keys, E and D
  private Tile[] tiles;                // tiles
  private float evenRowShift;          // shift of even rows inside mosaic  (units is percentage (of tile size))
  private float oddRowShift;           // shift of odd rows inside mosaic (units is percentage (of tile size))
  private int tileSize;                // size of Tiles in mosaic
  private int pdfScale;                // scale size of output pdf 1 = 1*size of input image, 2 = 2* size of original image ..
  private boolean[][] toShow;          // any tile at any position in mosaic can be disabled = blank spot
  private boolean[][] backupHorizontalFlip;  // any tile at any position can be flipped horizontally
  private boolean[][] backupVerticalFlip;    // any tile at any position can be flipped vertically
  private boolean toRerender;          // is there anything new to render
  
  public OutputImageManager(PImage img, Tile[] tiles, int tileSize, int pdfScale, float evenRowShift, float oddRowShift, color background)
  {
    this.mosaic = null;
    this.background = background;
    this.tiles = tiles;
    this.evenRowShift = evenRowShift;
    this.oddRowShift = oddRowShift;
    this.tileSize = tileSize;
    this.pdfScale = pdfScale;
    this.infoGraphics = loadImage("keys.png");
    toRerender = true;
  }
  
  public int getTileSize()
  {
    return tileSize;
  }
  
  public Mosaic getMosaic()
  {
    return mosaic;
  }
  
  public PImage getInput()
  {
    return input;
  }
  
  public float getEvenRowShift()
  {
    return this.evenRowShift;
  }
  
  public float getOddRowShift()
  {
    return this.oddRowShift;
  }
  
  public void setInput(PImage in)
  {
    input = in;
    if(mosaic != null)
    {
      mosaic.setImage(in);
      // new image means new tiling parameters in Mosaic class - set number of tiles along x and y..
      mosaic.setTiling();
    }
  }
  
  public void setToRerender()
  {
    toRerender = true;
  }
  
  
  public void setTiles(Tile[] newTiles)
  {
    tiles = newTiles;
    if(mosaic != null)
    {
      mosaic.setTiles(newTiles);
      // new tiles means new tiling parameters in Mosaic class - set number of tiles along x and y..
      mosaic.setTiling();
    }
  }
  
  public void setEvenRowShift(float evenRowShift)
  {
    this.evenRowShift = evenRowShift;
    mosaic.setEvenRowShift(evenRowShift);
  }
  
  public void setOddRowShift(float oddRowShift)
  {
    this.oddRowShift = oddRowShift;
    mosaic.setOddRowShift(oddRowShift);
  }
  
  public void setTileSize(int tileSize)
  {
    this.tileSize = tileSize;
    if(mosaic != null)
    {
      mosaic.setTileSize(tileSize);
      mosaic.setMiniatures();
    }
  }
  
  public void recoverFlipMatrices()
  {
    if(mosaic != null && backupVerticalFlip!=null && backupHorizontalFlip !=null)
    {
      mosaic.setVFlipMatrix(backupVerticalFlip);
      mosaic.setHFlipMatrix(backupHorizontalFlip);
    }
  }
  
  public void saveFlipMatrices()
  {
    if(mosaic != null)
    {
      backupVerticalFlip = mosaic.getVFlipMatrix();
      backupHorizontalFlip = mosaic.getHFlipMatrix();
    }
  }
  
  public void setPdfScale(int pdfScale)
  {
    this.pdfScale = pdfScale;
    mosaic.setPdfScale(pdfScale);
  }
  
  // responsible for rendering everything in PDF output section
  public void renderOutputImageManager()
  {
    if(input != null)
    {
      if(toRerender)
      {
         background(background);
         mosaic.newCanvas();
         mosaic.setPreviewCanvas();
         mosaic.drawImage();
         renderPreview();
         renderHover();
         image(infoGraphics,10,270);
         toRerender = false;
      }
    }
    else
    {
       background(background);
       image(infoGraphics,40,270);
    }
  }
  
  // create mosaic based on new input image
  private void createMosaic(PImage img, PImage lastSeenImage, Tile[] lastSeenTiles)
  {
    mosaic = new Mosaic(img, lastSeenImage, tiles, lastSeenTiles, tileSize, pdfScale, evenRowShift, oddRowShift);
  }
  
  public void renderPreview()
  {
    
  }
  
  public void renderHover()
  {
    
  }
}