import controlP5.*;
import java.util.*;
import java.io.*;
import processing.pdf.*;

ControlP5 cp5; // for menu buttons and sliders
Manager m; // manager of application logic - everything starts and is managed in Manager
color background;



void setup() {
  //setIcon();
  frameRate(24);
  size(1160, 720);
  this.background = color(50);
  surface.setTitle("StampImage by Lukas Lizal");
  background(background);
  cp5 = new ControlP5(this);  //button controller
  //cp5.setColor(color(25,25,25));
  //cp5.setColorActive();
  //cp5.setColorCaptionLabel();
  //cp5.setColorValueLabel();
  
  m = new Manager(cp5, background);
}

void draw() {
  m.renderManager();
}

void mousePressed()
{
  //when mouse is pressed and cursor is inside interactive preview area of tile
  //draw pixels according to brushmode
  if (m.getTileManager().getActiveTile()!=null)
  {
    if (m.getTileManager().getActiveTile().isHover())
    {
      if(!m.getTileManager().getBrushMode())
        m.getTileManager().getActiveTile().drawCell();
      else
        m.getTileManager().getActiveTile().drawCellWithBrush(m.getTileManager().getIsBlack());
    }
  }
  if (m.getTileManager().getHoverIndex()!=-1)
  {
    m.getTileManager().setActiveTile(m.getTileManager().getHoverIndex());
  }
}

void mouseDragged()
{
  if (m.getTileManager().getActiveTile()!=null)
  {
    if (m.getTileManager().getActiveTile().isHover())
    {
      if(m.getTileManager().getBrushMode())
        m.getTileManager().getActiveTile().drawCellWithBrush(m.getTileManager().getIsBlack());
    }
  }
}

void keyPressed()
{
  if(m.activeManager == 2 && m.getOutputImageManager().getMosaic()!= null)
  {
    if (key == CODED) {
      if (keyCode == UP) {
        m.getOutputImageManager().getMosaic().moveHoverIndex(1,0,0,0);
      } else if (keyCode == DOWN) {
        m.getOutputImageManager().getMosaic().moveHoverIndex(0,1,0,0);
      } else if (keyCode == LEFT) {
        m.getOutputImageManager().getMosaic().moveHoverIndex(0,0,1,0);
      } else if (keyCode == RIGHT) {
        m.getOutputImageManager().getMosaic().moveHoverIndex(0,0,0,1);
      }
      m.getOutputImageManager().getMosaic().setPreviewCanvas();
      m.getOutputImageManager().setToRerender();
    }
    else if (key == 'e' || key == 'E')
    {
      m.getOutputImageManager().getMosaic().flipVerticalPreviewTile();
    }
    else if (key == 'd' || key == 'D')
    {
      m.getOutputImageManager().getMosaic().flipHorizontalPreviewTile();
    }
    //else if (key == 'w' || key == 'W')
    //{
    //  m.getOutputImageManager().getMosaic().changeTileVisibility();
    //}
    else
    {
    }
  }
}

// basicMenu button functions section
// each button calls its function (specified in each buttons attributes) everytime user hits button or slides slider 

void tiles()
{
  if(m.getActiveManager() != 0)
  {
    m.hideButtonsOfManager(1);
    m.hideButtonsOfManager(2);
    m.showButtonsOfManager(0);
    m.activateManager(0);
    m.getTileManager().getActiveTile().setShowHover(true);
  }
}

void inputImage()
{
  if(m.getActiveManager() != 1)
  {
    m.hideButtonsOfManager(0);
    m.hideButtonsOfManager(2);
    m.showButtonsOfManager(1);
    m.activateManager(1);
    m.getTileManager().getActiveTile().setShowHover(false);
  }
}

void outputImage()
{
  if(m.getActiveManager() != 2)
  {
    m.hideButtonsOfManager(0);
    m.hideButtonsOfManager(1);
    m.showButtonsOfManager(2);
    m.activateManager(2);
    m.getTileManager().getActiveTile().setShowHover(false);
  }
}

void about()
{
  if(m.getActiveManager() != 3)
  {
    m.hideButtonsOfManager(0);
    m.hideButtonsOfManager(1);
    m.hideButtonsOfManager(2);
    m.activateManager(3);
    m.getTileManager().getActiveTile().setShowHover(false);
  }
}

// exits application
void quit()
{
  exit();
}

// tilesMenu button functions section
// each button calls its function (specified in each buttons attributes) everytime user hits button or slides slider 

void xTileSize(int xCells)
{
  if (m != null)
    m.getTileManager().setXCells(xCells);
}

void yTileSize(int yCells)
{
  if (m != null)
    m.getTileManager().setYCells(yCells);
}

void newTileSet()
{
  m.setTileManager(new TileManager((int)cp5.getController("xTileSize").getValue(), (int)cp5.getController("yTileSize").getValue(), (int) cp5.getController("brushSize").getValue(), !((Toggle) cp5.getController("brushMode")).getBooleanValue(), !((Toggle) cp5.getController("blackWhite")).getBooleanValue(), cp5, background));
}

void loadTiles()
{
  selectInput("Select a Tile Set file:", "readTileSet"); // select image window -> readTileSet()
}

void saveTiles()
{
  selectFolder("Select folder to save Tile Set:", "writeTiles");
}

void addTile()
{
  m.getTileManager().newTile();
}

void removeTile()
{
  m.getTileManager().removeTile(m.getTileManager().getActiveTile().getTileLevel());
}

void brushSize(int brushSize)
{
  if (m != null)
  {
    for (Tile t : m.getTileManager().getTiles())
    {
      if (t!=null)
        t.setBrushSize(brushSize);
      m.getTileManager().setBrushSize(brushSize);
    }
  }
}

void brushMode(boolean brushMode)
{
  if(m != null)
    m.getTileManager().setBrushMode(!brushMode);
}

void blackWhite(boolean isWhite)
{
  if(m != null)
    m.getTileManager().setIsBlack(!isWhite);
}

void moveTileForward()
{
  m.getTileManager().moveTileForward();
}

void moveTileBack()
{
  m.getTileManager().moveTileBack();
}

// inputImageMenu button functions section
// each button calls its function (specified in each buttons attributes) everytime user hits button or slides slider 

void loadInputImage()
{
  selectInput("Select a file to process:", "imageLoader"); // select image window -> imageLoader()
}

void saveInputImage()
{
  selectFolder("Select folder to save Image:", "saveImage");
}

void saveImage(File file)
{
  if(file != null && m.getInputImageManager() != null)
    m.getInputImageManager().getImage().save(file.toString()+"/"+"file");
}

// outputImageMenu button functions section
// each button calls its function (specified in each buttons attributes) everytime user hits button or slides slider 

void sizeOfTiles(int size)
{
  if(m != null )
  {
    m.getOutputImageManager().setTileSize(size);
    //refresh row shift for new size of tiles
    if(m.getOutputImageManager().getMosaic() != null)
    {
      m.getOutputImageManager().setEvenRowShift(m.getOutputImageManager().getEvenRowShift());
      m.getOutputImageManager().setOddRowShift(m.getOutputImageManager().getOddRowShift());
      m.getOutputImageManager().getMosaic().setHoverIndex(0,0);
      //println(m.getOutputImageManager().getMosaic().getFlipH()[0].length);
    }
    m.getOutputImageManager().setToRerender();
  }
}

// row shift in percentage
void evenRowShift(int shift)
{
  if(m != null && m.getOutputImageManager().getMosaic()!= null)
  {
    m.getOutputImageManager().setEvenRowShift((float)(shift*0.01));
    m.getOutputImageManager().setToRerender();
  }
}

// row shift in percentage
void oddRowShift(int shift)
{
  if(m != null && m.getOutputImageManager().getMosaic()!= null)
  {
    m.getOutputImageManager().setOddRowShift((float)(shift*0.01));
    m.getOutputImageManager().setToRerender();
  }
}

void saveOutput()
{
  selectFolder("Select folder to save output configuration:", "writeOutputSettings");
}

void loadOutput()
{
  selectInput("Select a file to load:", "readOutputSettings");
}

void scaleOutputSize(int scale)
{
  if(m != null && m.getOutputImageManager().getMosaic() != null)
    m.getOutputImageManager().setPdfScale(scale);
}

void transparent(boolean isTransparent)
{
  if(m != null &&m.getOutputImageManager() != null && m.getOutputImageManager().getMosaic() != null )
  {
   m.getOutputImageManager().getMosaic().setTransparentBackgroud(isTransparent);
  }
}

void savePDF()
{
  selectFolder("Select folder to save PDF:", "exportPDF");
}

void loadProject()
{
  selectFolder("Select project folder:", "loadProjectFolder");
}

void saveProject()
{
  selectFolder("Select folder to save Project:", "saveProjectToFolder");
}

void loadProjectFolder(File file)
{
  m = new Manager(cp5, background);
  m.activateManager(2);
  if(file != null)
  {
    readTileSet(new File(file+"/TileSet1.config"));
    imageLoader(new File(file+"/file.tif"));
    readOutputSettings(new File(file+"/OutputConfiguration1.config"));
  }
}

void saveProjectToFolder(File file)
{
  if(file != null && m.getInputImageManager().getImage() != null && m.getOutputImageManager().getMosaic() != null)
  {
    createOutput(file+"/ProjectFile/x");
    try
    {
    writeTiles(new File(file+"/ProjectFile/"));
    saveImage(new File(file+"/ProjectFile/"));
    writeOutputSettings(new File(file+"/ProjectFile/"));
    }
    catch(IOException e)
    {
     println("chyba ukladani");
    }
  }
}

void exportPDF(File file)
{
  if(m != null && m.getOutputImageManager().getMosaic() != null)
  {
    // list the files in the data folder
    String[] filenames = file.list();
    int fileNumber = 1;
    
    for(String f: filenames)
    {
      if(f.endsWith(".pdf") && f.startsWith("Mosaic"))
      {
        f = f.substring(f.length()-5,f.length()-4);
        if(isParsable(f))
        {
          fileNumber = max(Integer.parseInt(f),fileNumber);
          fileNumber++;
        }
        
      }
    }
    m.getOutputImageManager().getMosaic().exportToPdf(file+"/"+"Mosaic"+fileNumber+".pdf", (int)cp5.getController("evenRowShift").getValue(), (int)cp5.getController("oddRowShift").getValue());
  }
}


// input image manager loader
void imageLoader(File file)
{
  float ratio;
  if (file == null)
  {
    return;
  } else
  {
    try
    {
      PImage img = loadImage(file.getAbsolutePath());
      //if(img.width < 300)
      //{
      //  img.resize(300,img.height);
      //  if(img.height/((float)img.width)>3)
      //   img.resize(300, 900);
      //  if(img.width/((float)img.height)<0.3)
      //   img.resize(900, 300);
      //}
      //if(img.height < 300)
      //{
      //  img.resize(img.width, 300);
      //  if(img.width/((float)img.width)>3)
      //   img.resize(300, 900);
      //  if(img.width/((float)img.height)<0.3)
      //   img.resize(900, 300);
      //}
      if(img.height/((float)img.width)>3)
      {
        ratio = 1.0;
        if(img.width < 400)
        {
          ratio = 400/((float)img.width);
        }
        img.resize((int)(ratio*img.width),(int)(ratio*img.width*2.5));
      }
      if(img.height/((float)img.width)<0.25)
      {
        ratio = 1.0;
        if(img.height < 400)
        {
          ratio = 400/((float)img.height);
        }
        img.resize((int)(ratio*img.height*2.5),(int)(ratio*img.height));
      }
      if(img.width < 400 || img.height < 400)
      {
        ratio = 1.0;
        if(img.width < 400)
        {
          ratio = 400/((float)img.width);
        }
        if(img.height < 400 && img.height < img.width)
        {
          ratio = 400/((float)img.height);
        }
        img.resize((int)(ratio*img.width),(int)(ratio*img.height));
      }
      m.getInputImageManager().setImage(img);
      m.getInputImageManager().setResizedImage(m.getInputImageManager().resizeToFit(img, width-150, height-20));
      if(m.getOutputImageManager().getMosaic()!= null)
      {
        m.getOutputImageManager().createMosaic(m.getInputImageManager().getImage(),m.getOutputImageManager().getMosaic().getLastSeenImage(), m.getOutputImageManager().getMosaic().getTiles());
      }
      else
      {
        m.getOutputImageManager().createMosaic(m.getInputImageManager().getImage(),m.getInputImageManager().getImage(), m.getTileManager().getTiles());
      }
      m.getOutputImageManager().getMosaic().setMiniatures();
    }
    catch(NullPointerException e)
    {
      println(e);
    }
  }
}

void writeTiles(File file) throws IOException
{
  if (file != null)
  {
    Tile[] tiles = m.getTileManager().getTiles();
    String[] lines = new String[m.getTileManager().tiles.length+3];
    lines[0] = "tiles";
    lines[1] = new Integer(m.getTileManager().xCells).toString();
    lines[2] = new Integer(m.getTileManager().yCells).toString();
    int i = 3;
    
    for (Tile t : tiles)
    {
      if (t != null)
      {
        lines[i] = "";
        boolean[][] pattern = t.getPattern();
        for (int j = 0; j < pattern[0].length; j++)
        {
          for (int k = 0; k < pattern.length; k++)
          {
            if (pattern[k][j])
            {
              lines[i] += "t";
            } else
            {
              lines[i] += "f";
            }
            lines[i] += "-";
          }
          lines[i] = lines[i].substring(0,lines[i].length()-1);
          lines[i] += ";";
        }
      }
      i++;
    }
    
    // list the files in the data folder
    String[] filenames = file.list();
    int fileNumber = 1;
    
    for(String f: filenames)
    {
      if(f.endsWith(".config") && f.startsWith("TileSet"))
      {
        f = f.substring(f.length()-8,f.length()-7);
        if(isParsable(f))
        {
          fileNumber = max(Integer.parseInt(f),fileNumber);
          fileNumber++;
        }
        
      }
    }
    
    saveStrings(file+"/"+"TileSet"+fileNumber+".config", lines);
  }
}

// save settings from output image section
void writeOutputSettings(File file) throws IOException
{
  if (file != null && m.getOutputImageManager().getMosaic() != null)
  {
    boolean[][] vFlip = m.getOutputImageManager().getMosaic().getFlipV();
    boolean[][] hFlip = m.getOutputImageManager().getMosaic().getFlipH();
    String[] lines = new String[8];
    lines[0] = "output";
    lines[1] = new Integer((int)cp5.getController("sizeOfTiles").getValue()).toString(); //size of cells
    lines[2] = new Integer((int)cp5.getController("evenRowShift").getValue()).toString(); //even row shift
    lines[3] = new Integer((int)cp5.getController("oddRowShift").getValue()).toString(); //odd row shift
    lines[4] = new Integer(vFlip[0].length-1).toString(); //number of x cells
    lines[5] = new Integer(vFlip.length).toString(); //number of y cells
    int i = 6;
    
    boolean[][] array = vFlip;
    while(i < lines.length)
    {
      lines[i] = "";
      for (int j = 0; j < array[0].length; j++)
      {
        for (int k = 0; k < array.length; k++)
        {
          if (array[k][j])
          {
            lines[i] += "t";
          } else
          {
            lines[i] += "f";
          }
          lines[i] += "-";
        }
        lines[i] = lines[i].substring(0,lines[i].length()-1);
        lines[i] += ";";
      }
      array = hFlip;
      i++;
    }
    
    // list the files in the data folder
    String[] filenames = file.list();
    int fileNumber = 1;
    
    for(String f: filenames)
    {
      if(f.endsWith(".config") && f.startsWith("OutputConfiguration"))
      {
        f = f.substring(f.length()-8,f.length()-7);
        if(isParsable(f))
        {
          fileNumber = max(Integer.parseInt(f),fileNumber);
          fileNumber++;
        }
        
      }
    }
    
    saveStrings(file+"/"+"OutputConfiguration"+fileNumber+".config", lines);
  }
}

void readTileSet(File file)
{
  if(file != null)
  {
    String filename = file.getName();
    String extension = filename.substring(filename.lastIndexOf("."), filename.length());
    String ext = ".config";
    if (!extension.equals(ext)) {
      println("Choose .config file!");
    } else {
      String[] lines = loadStrings(file);
      if(!lines[0].equals("tiles"))
      {
        return;
      }
      int xCells = Integer.parseInt(lines[1]);
      int yCells = Integer.parseInt(lines[2]);
      lines = Arrays.copyOfRange(lines,3,lines.length);
      int i = 0;
      int j = 0;
      int k = 0;
      m.setTileManager(new TileManager(xCells, yCells, cp5, background));
      for(String tileString: lines)
      {
        if(!tileString.equals("null"))
        {
          m.getTileManager().newTile();
          String[] tileLineStrings = tileString.split(";");
          for(String tileLine: tileLineStrings)
          {
            String[] cells = tileLine.split("-");
            for(String cell: cells)
            {
              if(cell.equals("t"))
              {
                m.getTileManager().getTiles()[i].setPatternAtCell(j, k, true);
              }
              j++;
            }
            k++;
            j = 0;
          }
          i++;
          k = 0;
        }
      }
      for(Tile t: m.getTileManager().getTiles())
      {
        if(t != null)
        {
          m.getTileManager().createPreview(t);
          for(int p = 0; p<t.pattern[0].length;p++)
          {
            for(int o = 0; o<t.pattern[0].length;o++)
            {
              if(t.pattern[o][p]);
            }
          }
          
        }
        
      }
      m.getTileManager().createPreviewGradients();
    }
  }
}

void readOutputSettings(File file)
{
  if(file != null && m.getOutputImageManager().getMosaic() != null)
  {
    String filename = file.getName();
    String extension = filename.substring(filename.lastIndexOf("."), filename.length());
    String ext = ".config";
    if (!extension.equals(ext)) {
      println("Choose .config file!");
    } else {
      String[] lines = loadStrings(file);
      if(!lines[0].equals("output"))
      {
        return;
      }
      int sizeOfTile = Integer.parseInt(lines[1]);
      int evenRowShift = Integer.parseInt(lines[2]);
      int oddRowShift = Integer.parseInt(lines[3]);
      int yCells = Integer.parseInt(lines[4]);
      int xCells = Integer.parseInt(lines[5]);
      
      //if this file doesnt match dimensions off current mosaic stop loading
      if(!isApplicable(xCells,yCells, sizeOfTile))
      {
        return;
      }
      lines = Arrays.copyOfRange(lines,6,lines.length);
      int i = 0;
      int j = 0;
      int k = 0;
      
      m.getOutputImageManager().setTileSize(sizeOfTile);
      cp5.getController("sizeOfTiles").setValue(sizeOfTile);
      m.getOutputImageManager().setEvenRowShift(evenRowShift);
      cp5.getController("evenRowShift").setValue((float)evenRowShift);
      m.getOutputImageManager().setOddRowShift(oddRowShift);
      cp5.getController("oddRowShift").setValue(oddRowShift);
      
      boolean isArrayV = true;
      for(String flipArray: lines)
      {
        if(!flipArray.equals("null"))
        {
          String[] flipLines = flipArray.split(";");
          for(String tileLine: flipLines)
          {
            String[] cells = tileLine.split("-");
            for(String cell: cells)
            {
              if(cell.equals("t"))
              {
                if(isArrayV)
                  m.getOutputImageManager().getMosaic().setFlipVAtCell(j, k, true);
                else
                  m.getOutputImageManager().getMosaic().setFlipHAtCell(j, k, true);
              }
              j++;
            }
            k++;
            j = 0;
          }
          i++;
          k = 0;
        }
        isArrayV = false;
      }
      
    }
  }
}

// checks if string is parsable as int
// auxiliary function for czstom tile settings loader
boolean isParsable(String input){
    boolean parsable = true;
    try{
        Integer.parseInt(input);
    }catch(NumberFormatException e){
        parsable = false;
    }
    return parsable;
}

void setIcon()
{
  PGraphics icon = createGraphics(16,16);
  icon.beginDraw();
  //icon.image(getToolkit.getImage("icon.ico"), 0, 0, 16, 16);
  icon.endDraw();
  frame.setIconImage(icon.image);
}

//auxilary function for readOutputConfigration to check if input configuration fits dimentions of current mosaic
boolean isApplicable(int xCells, int yCells, int tileSize)
{
  //int actualXCells = m.getOutputImageManager().getMosaic().getFlipH().length;
  //int actualYCells = m.getOutputImageManager().getMosaic().getFlipH()[0].length;
  
  int actualTileSize = (int)cp5.getController("sizeOfTiles").getValue();
  boolean isThereMatch = false;
  
  int actualXCells;
  int actualYCells;
  
  for(int i = 1; i < cp5.getController("sizeOfTiles").getMax()+1; i++)
  {
    actualXCells = (m.getOutputImageManager().getMosaic().getXCanvasSize())/(m.getOutputImageManager().getMosaic().getTiles()[0].getW()*i)+3;
    actualYCells = (m.getOutputImageManager().getMosaic().getYCanvasSize())/(m.getOutputImageManager().getMosaic().getTiles()[0].getH()*i);
    if(xCells == actualXCells && yCells == actualYCells)
      isThereMatch = true;
    //println(xCells, yCells, i);
    //println(actualXCells,actualYCells, actualTileSize);
    //println("");
  }
  return isThereMatch;
}