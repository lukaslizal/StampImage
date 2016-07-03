// represents output mosaic contains some extra attributes needed for real time previews (downscaled tile preview images etc)
class Mosaic
{
  private PImage grayImg;           // grayscale input image
  private PImage grayImgToFit;      // downscaled grayscale input image to fit application window
  private PImage lastSeenImage;      // last image you had in output image section (this is used when chceking wheter function recoverFlipMatrices (in OutputManager) can be called)
  private Tile[] lastSeenTiles;
  private float lastImgRatio;       // ratio of last image you had in output image section (this is used when chceking wheter function recoverFlipMatrices (in OutputManager) can be called)
  private int lastTileRatio;     // ratio of last tiles you had in output image section (this is used when chceking wheter function recoverFlipMatrices (in OutputManager) can be called)
  private PImage[] avaragedImgs;    // imgs used when sampling for output mosaic with different tile sizes
  private PImage[] tileMiniatures;  // tileMiniatures used for live preview mocaic rendering - no flip
  private PImage[] tileMiniaturesV;  // tileMiniatures used for live preview mocaic rendering - flipped vertically
  private PImage[] tileMiniaturesH;  // tileMiniatures used for live preview mocaic rendering - flipped horizontally
  private PImage[] tileMiniaturesVH;  // tileMiniatures used for live preview mocaic rendering flipped both vertically and horizontally
  private Tile[] tiles;             // all current tiles
  private PGraphics canvas;         // canvas of live preview
  private int xTiles;               // number of tiles in mosaic alon x direction
  private int yTiles;               // number of tiles in mosaic alon y direction
  private int xTileSize;            // x size of tiles in image
  private int yTileSize;            // y size in tiles in image
  private int xCanvasSize;          // size of live preview canvas
  private int yCanvasSize;
  private int[] firstCellPosition;  // position in window where live preview starts (top left tile)
  private int[] hoverIndex;         // index of active tile in tile preview
  //private boolean[][] notShow;       // any tile at any position in mosaic can be disabled = blank spot
  private boolean[][] horizontalFlip;  // any tile at any position can be flipped horizontally
  private boolean[][] verticalFlip;    // any tile at any position can be flipped vertically
  private int pdfScale;        // scale size of output pdf 1 = 1*size of input image, 2 = 2* size of original image ..
  private boolean transparentBackground;
  private int evenRowShift;    // shift all tiles in even rows of mosaic
  private int oddRowShift;     // shift all tiles in odd rows of mosaic 
  private int tileSize;        // size of tiles in output image (and preview image)
  //private int tileSum;         // number of active levels of tiles
  
  // init tile
  public Mosaic(PImage img, PImage lastSeenImage, Tile[] tiles, Tile[] lastSeenTiles, int tileSize, int pdfScale, float evenRowShift, float oddRowShift)
  {
    //this.tileSum = tilesCounter(tiles);
    this.tiles = tiles;
    this.pdfScale = pdfScale;
    this.transparentBackground = true;
    this.tileSize = tileSize;
    this.xTiles = 0;
    this.yTiles = 0;
    this.xCanvasSize = width/4*3+70;
    this.yCanvasSize = height-20;
    this.hoverIndex = new int[2];
    this.hoverIndex[0] = 0;
    this.hoverIndex[1] = 0;
    this.firstCellPosition = new int[2];
    this.firstCellPosition[0] = 210;
    this.firstCellPosition[1] = 10;
    this.xTileSize = tiles[0].getW();
    this.yTileSize = tiles[0].getH();
    this.evenRowShift = (int) (xTileSize * evenRowShift);
    this.oddRowShift = (int) (xTileSize * oddRowShift);
    this.tileMiniatures = new PImage[8];
    this.tileMiniaturesV = new PImage[8];
    this.tileMiniaturesH = new PImage[8];
    this.tileMiniaturesVH = new PImage[8];
    this.avaragedImgs = new PImage[5];
    this.lastSeenImage = lastSeenImage;
    this.lastSeenTiles = lastSeenTiles;
    this.lastImgRatio = 0.0;
    this.lastTileRatio = tiles[0].getW()*tiles[0].getH();
    setImage(img);
    setMiniatures();
    
  }
  
  public boolean[][] getFlipV()
  {
    return verticalFlip;
  }
  
  public boolean[][] getFlipH()
  {
    return horizontalFlip;
  }
  
  public int getEvenRowShift()
  {
    return evenRowShift;
  }
  
  public int getLastTileRatio()
  {
    return lastTileRatio;
  }
  
  public Tile[] getLastSeenTiles()
  {
    return lastSeenTiles;
  }
  
  public PImage getLastSeenImage()
  {
    return lastSeenImage;
  }
  
  public PImage getImage()
  {
    return grayImg;
  }
  
  public Tile[] getTiles()
  {
    return tiles;
  }
  
  public int getXCanvasSize()
  {
    return xCanvasSize;
  }
  
  public int getYCanvasSize()
  {
    return yCanvasSize;
  }
  
  public void setHoverIndex(int x, int y)
  {
    hoverIndex[0] = x;
    hoverIndex[1] = y;
  }
  
  public void setPdfScale(int scale)
  {
    pdfScale = scale;
  }
  
  public void setTransparentBackgroud(boolean isTransparent)
  {
    transparentBackground = isTransparent;
  }
  
  public void setFlipVAtCell(int j, int k, boolean value)
  {
    verticalFlip[j][k] = value;
  }
  
  public void setFlipHAtCell(int j, int k, boolean value)
  {
    horizontalFlip[j][k] = value;
  }
  
  public void setLastSeenImage()
  {
    lastSeenImage = grayImg;
  }
  
  public void setLastSeenTiles(Tile[] tiles)
  {
    lastSeenTiles = tiles;
  }
  
  public void setLastSeenTiles()
  {
    lastSeenTiles = tiles;
  }
  
  public void setLastImgRatio()
  {
    lastImgRatio = grayImg.width/((float)grayImg.height);
  }
  
  public float getLastImgRatio()
  {
    return lastImgRatio;
  }
  
  public void setLastTileRatio()
  {
    lastTileRatio = tiles[0].getW()*tiles[0].getH();
  }
  
  public void moveHoverIndex(int up,int down, int left, int right)
  {
    if(hoverIndex[1] > 0)
      hoverIndex[1] -= up;
    if(hoverIndex[1] < yTiles)
      hoverIndex[1] += down;
    if(hoverIndex[0] > 0)
      hoverIndex[0] -= left;
    if(hoverIndex[0] < xTiles+1)
      hoverIndex[0] += right;
    setPreviewCanvas();
  }
  
  public void setTileSize(int tileSize)
  {
    this.tileSize = tileSize;
    setTiling();
    setTiles(this.tiles);
    drawImage();
  }
  
  public void setEvenRowShift(float evenRowShift)
  {
    this.evenRowShift = (int) (xTileSize*evenRowShift);
    //createPreview();
    drawImage();
  }
  
  public void setOddRowShift(float oddRowShift)
  {
    this.oddRowShift = (int) (xTileSize*oddRowShift);
    //createPreview();
    drawImage();
  }
  
  public void setImage(PImage img)
  {
    grayImg = img;
    grayImg.filter(GRAY);
    grayImgToFit = resizeToFit(grayImg);
      createAvaragedImgs();
    newCanvas();
    setTiling();
  }
  public void setTiling()
  {
    xTiles = (xCanvasSize)/(tiles[0].getW()*tileSize);
    yTiles = (yCanvasSize)/(tiles[0].getH()*tileSize);
    //notShow = new boolean[xTiles+3][yTiles+1];
    horizontalFlip = new boolean[xTiles+3][yTiles+1];
    verticalFlip = new boolean[xTiles+3][yTiles+1];
    //tileSum = tilesCounter(tiles);
    //println(horizontalFlip[0].length);
  }
  
  public void setTiles(Tile[] tiles)
  {
    this.tiles = tiles;
    xTileSize = tiles[0].getW()*tileSize;
    yTileSize = tiles[0].getH()*tileSize;
  }
  
  public void setVFlipMatrix(boolean[][] m)
  {
    verticalFlip = m;
  }
  
  public void setHFlipMatrix(boolean[][] m)
  {
    horizontalFlip = m;
  }
  
  public boolean[][] getVFlipMatrix()
  {
    return verticalFlip;
  }
  
  public boolean[][] getHFlipMatrix()
  {
    return horizontalFlip;
  }
  
  // renders preview mosaic image
  public void drawImage()
  {
    imageMode(CORNERS);
    //image(grayImgToFit, firstCellPosition[0], firstCellPosition[1]);
    image(canvas, firstCellPosition[0], firstCellPosition[1]);
    //image(tileMiniaturesV[0],10,250);
    //if(avaragedImgs.length > 4)
    // image(avaragedImgs[3],200,200);
    //getTileIntensityAtIndex(15,15);
    //println(tiles[7].getEndBrightness());
    
  }
  
  public void newCanvas()
  {
    canvas = createGraphics(grayImgToFit.width, grayImgToFit.height);
    xCanvasSize = canvas.width;
    yCanvasSize = canvas.height;
  }
  
  public void createAvaragedImgs()
  {
    if(avaragedImgs != null && grayImgToFit != null)
    {
      avaragedImgs[0] = grayImgToFit.copy();
      avaragedImgs[0].resize((int)(avaragedImgs[0].width*0.1),(int)(avaragedImgs[0].height*0.1));
      int w = avaragedImgs[0].width;
      int h = avaragedImgs[0].height;
      avaragedImgs[0].resize(xCanvasSize,yCanvasSize);
      for(int i = 1; i < 5; i++)
      {
       avaragedImgs[i] = avaragedImgs[i-1].copy();
       avaragedImgs[i].resize((int)(w - w*(float)(i)*0.24),(int)(h - h*(float)(i)*0.24));
       avaragedImgs[i].resize(xCanvasSize,yCanvasSize);
      }
    }
  }
  
  public int getTileIntensityAtIndex(int i, int j)
  {
    int locX = i * xTileSize;
    int locY = j * yTileSize;
    int c = color(0,0,0);
    float intensity = brightness(c);
    if(tileSize == 1 || tileSize == 2)
    {
      c = avaragedImgs[0].get(locX,locY);
      intensity = brightness(c);
      
      for(Tile t: tiles)
      {
        if(t != null && t.getStartBrightness() > intensity && t.getEndBrightness() <= intensity ) //&& t.getEndBrightness() > intensity
        {
          return t.getTileLevel();
        }
      }
    }
    else if(tileSize == 3 || tileSize == 4)
    {
      c = avaragedImgs[1].get(locX,locY);
      intensity = brightness(c);
      
      for(Tile t: tiles)
      {
        if(t != null && t.getStartBrightness() > intensity && t.getEndBrightness() <= intensity ) //&& t.getEndBrightness() > intensity
        {
          return t.getTileLevel();
        }
      }
    }
    else if(tileSize == 5 || tileSize == 6)
    {
      c = avaragedImgs[2].get(locX,locY);
      intensity = brightness(c);
      
      for(Tile t: tiles)
      {
        if(t != null && t.getStartBrightness() > intensity && t.getEndBrightness() <= intensity ) //&& t.getEndBrightness() > intensity
        {
          return t.getTileLevel();
        }
      }
    }
    else if(tileSize == 7 || tileSize == 8)
    {
      c = avaragedImgs[3].get(locX,locY);
      intensity = brightness(c);
      
      for(Tile t: tiles)
      {
        if(t != null && t.getStartBrightness() > intensity && t.getEndBrightness() <= intensity ) //&& t.getEndBrightness() > intensity
        {
          return t.getTileLevel();
        }
      }
    }
    else if(tileSize == 9 || tileSize == 10)
    {
      c = avaragedImgs[4].get(locX,locY);
      intensity = brightness(c);
      
      for(Tile t: tiles)
      {
        if(t != null && t.getStartBrightness() > intensity && t.getEndBrightness() <= intensity ) //&& t.getEndBrightness() > intensity
        {
          return t.getTileLevel();
        }
      }
    }
    
    return 0;
  }
  
  // creates mosaic
  // fills interactive preview canvas with tiles with respect to intensities in input image 
  public void setPreviewCanvas()
  {
    if(grayImgToFit!=null)
    {
      canvas.beginDraw();
      for(int i = 0; i < xTiles+3; i++)
      {
        for(int j = 0; j < yTiles+1; j++)
        {
          //if(i<verticalFlip[0].length && j<verticalFlip.length)
          //{
          //if(!notShow[i][j])
          //{
            // even or odd row shift
            if(j%2 == 0)
            {
              if(verticalFlip[i][j] && ! horizontalFlip[i][j])
              {
                canvas.image(tileMiniaturesV[getTileIntensityAtIndex(i,j)],i * tileMiniaturesV[0].width + evenRowShift,j * tileMiniatures[0].height);
              }
              else if(horizontalFlip[i][j] && !verticalFlip[i][j])
              {
                canvas.image(tileMiniaturesH[getTileIntensityAtIndex(i,j)],i * tileMiniaturesH[0].width + evenRowShift,j * tileMiniatures[0].height);
              }
              else if(horizontalFlip[i][j] && verticalFlip[i][j])
              {
                canvas.image(tileMiniaturesVH[getTileIntensityAtIndex(i,j)],i * tileMiniaturesVH[0].width + evenRowShift,j * tileMiniatures[0].height);
              }
              else
              {
                canvas.image(tileMiniatures[getTileIntensityAtIndex(i,j)],i * tileMiniatures[0].width + evenRowShift,j * tileMiniatures[0].height);
              }
            }
            else
            {
              if(verticalFlip[i][j] && ! horizontalFlip[i][j])
              {
                canvas.image(tileMiniaturesV[getTileIntensityAtIndex(i,j)],i * tileMiniaturesV[0].width + oddRowShift,j * tileMiniatures[0].height);
              }
              else if(horizontalFlip[i][j] && !verticalFlip[i][j])
              {
                canvas.image(tileMiniaturesH[getTileIntensityAtIndex(i,j)],i * tileMiniaturesH[0].width + oddRowShift,j * tileMiniatures[0].height);
              }
              else if(horizontalFlip[i][j] && verticalFlip[i][j])
              {
                canvas.image(tileMiniaturesVH[getTileIntensityAtIndex(i,j)],i * tileMiniaturesVH[0].width + oddRowShift,j * tileMiniatures[0].height);
              }
              else
              {
                canvas.image(tileMiniatures[getTileIntensityAtIndex(i,j)],i * tileMiniatures[0].width + oddRowShift,j * tileMiniatures[0].height);
              }
            }
          //}
          //else
          //{
          //  canvas.fill(255,255,255);
          //  canvas.noStroke();
          //  if(hoverIndex[1]%2 == 0)
          //    canvas.rect(i * tileMiniatures[0].width + evenRowShift,j * tileMiniatures[0].height,tileMiniatures[0].width, tileMiniatures[0].height);
          //  else
          //    canvas.rect(i * tileMiniatures[0].width + oddRowShift,j * tileMiniatures[0].height,tileMiniatures[0].width, tileMiniatures[0].height);
          //}
          //}
        }
      }
      
      canvas.fill(0,255,0,100);
      canvas.noStroke();
      if(hoverIndex[1]%2 == 0)
      {
        canvas.rect(hoverIndex[0] * tileMiniatures[0].width + evenRowShift,hoverIndex[1] * tileMiniatures[0].height,tileMiniatures[0].width, tileMiniatures[0].height);
      }
      else
      {
        canvas.rect(hoverIndex[0] * tileMiniatures[0].width + oddRowShift,hoverIndex[1] * tileMiniatures[0].height,tileMiniatures[0].width, tileMiniatures[0].height);
      }
      canvas.endDraw();
    }
  }
  
  public void flipVerticalPreviewTile()
  {
    verticalFlip[hoverIndex[0]][hoverIndex[1]] = !verticalFlip[hoverIndex[0]][hoverIndex[1]];
    canvas.beginDraw();
    canvas.fill(0,255,0,100);
    canvas.noStroke();
    //if(!notShow[hoverIndex[0]][hoverIndex[1]])
    //{
      if(hoverIndex[1]%2 == 0)
      {
        if(!horizontalFlip[hoverIndex[0]][hoverIndex[1]])
        {
          if(verticalFlip[hoverIndex[0]][hoverIndex[1]])
          {
            canvas.image(tileMiniaturesV[getTileIntensityAtIndex(hoverIndex[0],hoverIndex[1])],hoverIndex[0] * tileMiniatures[0].width + evenRowShift,hoverIndex[1] * tileMiniatures[0].height);
          }
          else
          {
            canvas.image(tileMiniatures[getTileIntensityAtIndex(hoverIndex[0],hoverIndex[1])],hoverIndex[0] * tileMiniatures[0].width + evenRowShift,hoverIndex[1] * tileMiniatures[0].height);
          }
        }
        else
        {
          if(verticalFlip[hoverIndex[0]][hoverIndex[1]])
          {
            canvas.image(tileMiniaturesVH[getTileIntensityAtIndex(hoverIndex[0],hoverIndex[1])],hoverIndex[0] * tileMiniatures[0].width + evenRowShift,hoverIndex[1] * tileMiniatures[0].height);
          }
          else
          {
            canvas.image(tileMiniaturesH[getTileIntensityAtIndex(hoverIndex[0],hoverIndex[1])],hoverIndex[0] * tileMiniatures[0].width + evenRowShift,hoverIndex[1] * tileMiniatures[0].height);
          }
        }
        canvas.rect(hoverIndex[0] * tileMiniatures[0].width + evenRowShift,hoverIndex[1] * tileMiniatures[0].height,tileMiniatures[0].width, tileMiniatures[0].height);
      }
      else
      {
        if(!horizontalFlip[hoverIndex[0]][hoverIndex[1]])
        {
          if(verticalFlip[hoverIndex[0]][hoverIndex[1]])
          {
            canvas.image(tileMiniaturesV[getTileIntensityAtIndex(hoverIndex[0],hoverIndex[1])],hoverIndex[0] * tileMiniatures[0].width + oddRowShift,hoverIndex[1] * tileMiniatures[0].height);
          }
          else
          {
            canvas.image(tileMiniatures[getTileIntensityAtIndex(hoverIndex[0],hoverIndex[1])],hoverIndex[0] * tileMiniatures[0].width + oddRowShift,hoverIndex[1] * tileMiniatures[0].height);
          }
        }
        else
        {
          if(verticalFlip[hoverIndex[0]][hoverIndex[1]])
          {
            canvas.image(tileMiniaturesVH[getTileIntensityAtIndex(hoverIndex[0],hoverIndex[1])],hoverIndex[0] * tileMiniatures[0].width + oddRowShift,hoverIndex[1] * tileMiniatures[0].height);
          }
          else
          {
            canvas.image(tileMiniaturesH[getTileIntensityAtIndex(hoverIndex[0],hoverIndex[1])],hoverIndex[0] * tileMiniatures[0].width + oddRowShift,hoverIndex[1] * tileMiniatures[0].height);
          }
        }
        canvas.rect(hoverIndex[0] * tileMiniatures[0].width + oddRowShift,hoverIndex[1] * tileMiniatures[0].height,tileMiniatures[0].width, tileMiniatures[0].height);
      }
    //}
    canvas.endDraw();
    drawImage();
  }
  
  public void flipHorizontalPreviewTile()
  {
    horizontalFlip[hoverIndex[0]][hoverIndex[1]] = !horizontalFlip[hoverIndex[0]][hoverIndex[1]];
    canvas.beginDraw();
    canvas.fill(0,255,0,100);
    canvas.noStroke();
    //if(!notShow[hoverIndex[0]][hoverIndex[1]])
    //{
      if(hoverIndex[1]%2 == 0)
      {
       if(!verticalFlip[hoverIndex[0]][hoverIndex[1]])
       {
         if(horizontalFlip[hoverIndex[0]][hoverIndex[1]])
         {
           canvas.image(tileMiniaturesH[getTileIntensityAtIndex(hoverIndex[0],hoverIndex[1])],hoverIndex[0] * tileMiniatures[0].width + evenRowShift,hoverIndex[1] * tileMiniatures[0].height);
         }
         else
         {
           canvas.image(tileMiniatures[getTileIntensityAtIndex(hoverIndex[0],hoverIndex[1])],hoverIndex[0] * tileMiniatures[0].width + evenRowShift,hoverIndex[1] * tileMiniatures[0].height);
         }
       }
       else
       {
         if(horizontalFlip[hoverIndex[0]][hoverIndex[1]])
         {
           canvas.image(tileMiniaturesVH[getTileIntensityAtIndex(hoverIndex[0],hoverIndex[1])],hoverIndex[0] * tileMiniatures[0].width + evenRowShift,hoverIndex[1] * tileMiniatures[0].height);
         }
         else
         {
           canvas.image(tileMiniaturesV[getTileIntensityAtIndex(hoverIndex[0],hoverIndex[1])],hoverIndex[0] * tileMiniatures[0].width + evenRowShift,hoverIndex[1] * tileMiniatures[0].height);
         }
       }
       canvas.rect(hoverIndex[0] * tileMiniatures[0].width + evenRowShift,hoverIndex[1] * tileMiniatures[0].height,tileMiniatures[0].width, tileMiniatures[0].height);
      }
      else
      {
       if(!verticalFlip[hoverIndex[0]][hoverIndex[1]])
       {
         if(horizontalFlip[hoverIndex[0]][hoverIndex[1]])
         {
           canvas.image(tileMiniaturesH[getTileIntensityAtIndex(hoverIndex[0],hoverIndex[1])],hoverIndex[0] * tileMiniatures[0].width + oddRowShift,hoverIndex[1] * tileMiniatures[0].height);
         }
         else
         {
           canvas.image(tileMiniatures[getTileIntensityAtIndex(hoverIndex[0],hoverIndex[1])],hoverIndex[0] * tileMiniatures[0].width + oddRowShift,hoverIndex[1] * tileMiniatures[0].height);
         }
       }
       else
       {
         if(horizontalFlip[hoverIndex[0]][hoverIndex[1]])
         {
           canvas.image(tileMiniaturesVH[getTileIntensityAtIndex(hoverIndex[0],hoverIndex[1])],hoverIndex[0] * tileMiniatures[0].width + oddRowShift,hoverIndex[1] * tileMiniatures[0].height);
         }
         else
         {
           canvas.image(tileMiniaturesV[getTileIntensityAtIndex(hoverIndex[0],hoverIndex[1])],hoverIndex[0] * tileMiniatures[0].width + oddRowShift,hoverIndex[1] * tileMiniatures[0].height);
         }
       }
       canvas.rect(hoverIndex[0] * tileMiniatures[0].width + oddRowShift,hoverIndex[1] * tileMiniatures[0].height,tileMiniatures[0].width, tileMiniatures[0].height);
      }
    //}
    canvas.endDraw();
    drawImage();
  }
  
  //public void changeTileVisibility()
  //{
  //  notShow[hoverIndex[0]][hoverIndex[1]] = ! notShow[hoverIndex[0]][hoverIndex[1]];
  //  if(notShow[hoverIndex[0]][hoverIndex[1]])
  //  {
  //    canvas.beginDraw();
  //    canvas.fill(255,255,255);
  //    canvas.noStroke();
  //    if(hoverIndex[1]%2 == 0)
  //    {
  //      canvas.rect(hoverIndex[0] * tileMiniatures[0].width + oddRowShift,hoverIndex[1] * tileMiniatures[0].height,tileMiniatures[0].width, tileMiniatures[0].height);
  //    }
  //    else
  //    {
  //      canvas.rect(hoverIndex[0] * tileMiniatures[0].width + evenRowShift,hoverIndex[1] * tileMiniatures[0].height,tileMiniatures[0].width, tileMiniatures[0].height);
  //    }
  //    canvas.endDraw();
  //  }
  //  else
  //  {
  //    setPreviewCanvas();
  //  }
  //  drawImage();
  //  //setPreviewCanvas();
  //}
  
  
  // creates preview miniatures in size according to tileSize
  public void setMiniatures()
  {
    for(int i = 0; i < tileMiniatures.length;i++)
    {
      if(tiles != null && tiles[i]!=null)
      {
        boolean[][] pattern = tiles[i].getPattern();
        tileMiniatures[i] = createImage(tiles[i].getW()*tileSize,tiles[i].getH()*tileSize,RGB);
        tileMiniatures[i].loadPixels();
        for(int j = 0; j < tiles[i].getH()*tileSize; j++)
        {
          for(int k = 0; k < tiles[i].getW()*tileSize; k++)
          {
            int loc = k + j * tileMiniatures[0].width;
            if(pattern[k/tileSize][j/tileSize])
            {
              tileMiniatures[i].pixels[loc] = color(0,0,0);
            }
            else
            {
              tileMiniatures[i].pixels[loc] = color(255,255,255);
            }
          }
        }
        tileMiniatures[i].updatePixels();
        tileMiniaturesV[i] = flipV(tileMiniatures[i]);
        tileMiniaturesH[i] = flipH(tileMiniatures[i]);
        tileMiniaturesVH[i] = flipVH(tileMiniatures[i]);
      }
    }
  }
  
  // flips tileMiniatures vertically
  public PImage flipV(PImage img)
  {
    PImage flipped = createImage(img.width,img.height,RGB);
    flipped.loadPixels();
    for(int i = 0; i < img.width; i++)
    {
      for(int j = 0; j < img.height; j++)
      {
        int loc = i + j * flipped.width;
        int locFlipped = i + (img.height-j-1) * flipped.width;
        if(img.pixels[loc] == color(0,0,0))
        {
          flipped.pixels[locFlipped] = color(0,0,0);
        }
        else
        {
          flipped.pixels[locFlipped] = color(255,255,255);
        }
      }
    }
    flipped.updatePixels();
    return flipped;
  }
  
  // flips tileMiniatures horizontally
  public PImage flipH(PImage img)
  {
    PImage flipped = createImage(img.width,img.height,RGB);
    flipped.loadPixels();
    for(int i = 0; i < img.width; i++)
    {
      for(int j = 0; j < img.height; j++)
      {
        int loc = i + j * flipped.width;
        int locFlipped = (img.width-i-1) + j * flipped.width;
        if(img.pixels[loc] == color(0,0,0))
        {
          flipped.pixels[locFlipped] = color(0,0,0);
        }
        else
        {
          flipped.pixels[locFlipped] = color(255,255,255);
        }
      }
    }
    flipped.updatePixels();
    return flipped;
  }
  
  // flips tileMiniatures verticaly and horizontaly
  public PImage flipVH(PImage img)
  {
    PImage flipped = createImage(img.width,img.height,RGB);
    flipped.loadPixels();
    for(int i = 0; i < img.width; i++)
    {
      for(int j = 0; j < img.height; j++)
      {
        int loc = i + j * flipped.width;
        int locFlipped = (img.width-i-1) + (img.height-j-1) * flipped.width;
        if(img.pixels[loc] == color(0,0,0))
        {
          flipped.pixels[locFlipped] = color(0,0,0);
        }
        else
        {
          flipped.pixels[locFlipped] = color(255,255,255);
        }
      }
    }
    flipped.updatePixels();
    return flipped;
  }
  
  // exports pds mosaic 
  public void exportToPdf(String file, int evenRowS, int oddRowS)
  {
    int xCanvasSize = this.xCanvasSize*pdfScale;
    int yCanvasSize = this.yCanvasSize*pdfScale;
    int xTiles = this.xTiles*pdfScale;
    int yTiles = this.yTiles*pdfScale;
    int xTileSize = this.xTileSize*pdfScale;
    int yTileSize = this.yTileSize*pdfScale;
    int cellSize = xTileSize/tiles[0].getH();
    int evenRowShift = (int)(xTileSize*evenRowS*0.01);
    int oddRowShift = (int)(yTileSize*oddRowS*0.01);
    PGraphics pdf = createGraphics(xCanvasSize, yCanvasSize, PDF, file);
    
    pdf.beginDraw();
    if(!transparentBackground)
      pdf.background(255,255,255);
    pdf.fill(0,0,0);
    pdf.noStroke();
    for(int i = 0; i < xTiles+3; i++)
    {
      for(int j = 0; j < yTiles+1; j++)
      {
        // even or odd row shift
        if(j%2 == 0)
        {
          if(verticalFlip[i][j] && ! horizontalFlip[i][j])
          {
            drawTileV(tiles[getTileIntensityAtIndex(i,j)],i*xTileSize+evenRowShift,j*yTileSize, cellSize, pdf);
          }
          else if(horizontalFlip[i][j] && !verticalFlip[i][j])
          {
            drawTileH(tiles[getTileIntensityAtIndex(i,j)],i*xTileSize+evenRowShift,j*yTileSize, cellSize, pdf);
          }
          else if(horizontalFlip[i][j] && verticalFlip[i][j])
          {
            drawTileVH(tiles[getTileIntensityAtIndex(i,j)],i*xTileSize+evenRowShift,j*yTileSize, cellSize, pdf);
          }
          else
          {
            drawTile(tiles[getTileIntensityAtIndex(i,j)],i*xTileSize+evenRowShift,j*yTileSize, cellSize, pdf);
          }
        }
        else
        {
          if(verticalFlip[i][j] && ! horizontalFlip[i][j])
          {
            drawTileV(tiles[getTileIntensityAtIndex(i,j)],i*xTileSize+oddRowShift,j*yTileSize, cellSize, pdf);
          }
          else if(horizontalFlip[i][j] && !verticalFlip[i][j])
          {
            drawTileH(tiles[getTileIntensityAtIndex(i,j)],i*xTileSize+oddRowShift,j*yTileSize, cellSize, pdf);
          }
          else if(horizontalFlip[i][j] && verticalFlip[i][j])
          {
            drawTileVH(tiles[getTileIntensityAtIndex(i,j)],i*xTileSize+oddRowShift,j*yTileSize, cellSize, pdf);
          }
          else
          {
            drawTile(tiles[getTileIntensityAtIndex(i,j)],i*xTileSize+oddRowShift,j*yTileSize, cellSize, pdf);
          }
        }
      }
    }
    
    pdf.dispose();
    pdf.endDraw();
  }
  
  // auxilary method for pdf export
  public void drawTile(Tile t, int x, int y, int cellSize, PGraphics pdf)
  {
    for(int i = 0; i < t.getW(); i++)
    {
      for(int j = 0; j < t.getH(); j++)
      {
        if(t.getPattern()[i][j])
          pdf.rect(x+i*cellSize, y+j*cellSize, cellSize*1.1, cellSize*1.1);
      }
    }
  }
  
  // auxilary method for pdf export
  public void drawTileV(Tile t, int x, int y, int cellSize, PGraphics pdf)
  {
    for(int i = 0; i < t.getW(); i++)
    {
      for(int j = 0; j < t.getH(); j++)
      {
        if(t.getPattern()[i][t.getH()-1-j])
          pdf.rect(x+i*cellSize, y+j*cellSize, cellSize*1.1, cellSize*1.1);
      }
    }
  }
  
  // auxilary method for pdf export
  public void drawTileH(Tile t, int x, int y, int cellSize, PGraphics pdf)
  {
    for(int i = 0; i < t.getW(); i++)
    {
      for(int j = 0; j < t.getH(); j++)
      {
        if(t.getPattern()[t.getW()-1-i][j])
          pdf.rect(x+i*cellSize, y+j*cellSize, cellSize*1.1, cellSize*1.1);
      }
    }
  }
  
  // auxilary method for pdf export
  public void drawTileVH(Tile t, int x, int y, int cellSize, PGraphics pdf)
  {
    for(int i = 0; i < t.getW(); i++)
    {
      for(int j = 0; j < t.getH(); j++)
      {
        if(t.getPattern()[t.getW()-1-i][t.getH()-1-j])
          pdf.rect(x+i*cellSize, y+j*cellSize, cellSize*1.1, cellSize*1.1);
      }
    }
  }
  
  
  // used for resizing grayscale image to fit application window
  public PImage resizeToFit(PImage original)
  {
    PImage resized = original.copy();
    if (original.width > xCanvasSize && original.height > yCanvasSize)
    {
      if ((float)(original.width)/(float)(original.height) > (float)(xCanvasSize)/(float)yCanvasSize)
        resized.resize(xCanvasSize, 0);
      else
        resized.resize(0, yCanvasSize);
    } else if (original.width > xCanvasSize)
      resized.resize(xCanvasSize, 0);
    else if (original.height > yCanvasSize)
      resized.resize(0, yCanvasSize);
    return resized;
  }
  
  //// how many different types of tiles do we have
  //public int tilesCounter(Tile[] tiles)
  //{
  //  int counter = 0;
  //  for(Tile t: tiles)
  //  {
  //    if(t!=null)
  //      counter += 1;
  //  }
  //  return counter;
  //}
}