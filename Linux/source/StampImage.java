import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import controlP5.*; 
import java.util.*; 
import java.io.*; 
import processing.pdf.*; 
import controlP5.*; 
import java.util.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class StampImage extends PApplet {






ControlP5 cp5; // for menu buttons and sliders
Manager m; // manager of application logic - everything starts and is managed in Manager
int background;



public void setup() {
  //setIcon();
  frameRate(24);
  
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

public void draw() {
  m.renderManager();
}

public void mousePressed()
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

public void mouseDragged()
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

public void keyPressed()
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

public void tiles()
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

public void inputImage()
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

public void outputImage()
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

public void about()
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
public void quit()
{
  exit();
}

// tilesMenu button functions section
// each button calls its function (specified in each buttons attributes) everytime user hits button or slides slider 

public void xTileSize(int xCells)
{
  if (m != null)
    m.getTileManager().setXCells(xCells);
}

public void yTileSize(int yCells)
{
  if (m != null)
    m.getTileManager().setYCells(yCells);
}

public void newTileSet()
{
  m.setTileManager(new TileManager((int)cp5.getController("xTileSize").getValue(), (int)cp5.getController("yTileSize").getValue(), (int) cp5.getController("brushSize").getValue(), !((Toggle) cp5.getController("brushMode")).getBooleanValue(), !((Toggle) cp5.getController("blackWhite")).getBooleanValue(), cp5, background));
}

public void loadTiles()
{
  selectInput("Select a Tile Set file:", "readTileSet"); // select image window -> readTileSet()
}

public void saveTiles()
{
  selectFolder("Select folder to save Tile Set:", "writeTiles");
}

public void addTile()
{
  m.getTileManager().newTile();
}

public void removeTile()
{
  m.getTileManager().removeTile(m.getTileManager().getActiveTile().getTileLevel());
}

public void brushSize(int brushSize)
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

public void brushMode(boolean brushMode)
{
  if(m != null)
    m.getTileManager().setBrushMode(!brushMode);
}

public void blackWhite(boolean isWhite)
{
  if(m != null)
    m.getTileManager().setIsBlack(!isWhite);
}

public void moveTileForward()
{
  m.getTileManager().moveTileForward();
}

public void moveTileBack()
{
  m.getTileManager().moveTileBack();
}

// inputImageMenu button functions section
// each button calls its function (specified in each buttons attributes) everytime user hits button or slides slider 

public void loadInputImage()
{
  selectInput("Select a file to process:", "imageLoader"); // select image window -> imageLoader()
}

public void saveInputImage()
{
  selectFolder("Select folder to save Image:", "saveImage");
}

public void saveImage(File file)
{
  if(file != null && m.getInputImageManager() != null)
    m.getInputImageManager().getImage().save(file.toString()+"/"+"file");
}

// outputImageMenu button functions section
// each button calls its function (specified in each buttons attributes) everytime user hits button or slides slider 

public void sizeOfTiles(int size)
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
public void evenRowShift(int shift)
{
  if(m != null && m.getOutputImageManager().getMosaic()!= null)
  {
    m.getOutputImageManager().setEvenRowShift((float)(shift*0.01f));
    m.getOutputImageManager().setToRerender();
  }
}

// row shift in percentage
public void oddRowShift(int shift)
{
  if(m != null && m.getOutputImageManager().getMosaic()!= null)
  {
    m.getOutputImageManager().setOddRowShift((float)(shift*0.01f));
    m.getOutputImageManager().setToRerender();
  }
}

public void saveOutput()
{
  selectFolder("Select folder to save output configuration:", "writeOutputSettings");
}

public void loadOutput()
{
  selectInput("Select a file to load:", "readOutputSettings");
}

public void scaleOutputSize(int scale)
{
  if(m != null && m.getOutputImageManager().getMosaic() != null)
    m.getOutputImageManager().setPdfScale(scale);
}

public void transparent(boolean isTransparent)
{
  if(m != null &&m.getOutputImageManager() != null && m.getOutputImageManager().getMosaic() != null )
  {
   m.getOutputImageManager().getMosaic().setTransparentBackgroud(isTransparent);
  }
}

public void savePDF()
{
  selectFolder("Select folder to save PDF:", "exportPDF");
}

public void loadProject()
{
  selectFolder("Select project folder:", "loadProjectFolder");
}

public void saveProject()
{
  selectFolder("Select folder to save Project:", "saveProjectToFolder");
}

public void loadProjectFolder(File file)
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

public void saveProjectToFolder(File file)
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

public void exportPDF(File file)
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
public void imageLoader(File file)
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
        ratio = 1.0f;
        if(img.width < 400)
        {
          ratio = 400/((float)img.width);
        }
        img.resize((int)(ratio*img.width),(int)(ratio*img.width*2.5f));
      }
      if(img.height/((float)img.width)<0.25f)
      {
        ratio = 1.0f;
        if(img.height < 400)
        {
          ratio = 400/((float)img.height);
        }
        img.resize((int)(ratio*img.height*2.5f),(int)(ratio*img.height));
      }
      if(img.width < 400 || img.height < 400)
      {
        ratio = 1.0f;
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

public void writeTiles(File file) throws IOException
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
public void writeOutputSettings(File file) throws IOException
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

public void readTileSet(File file)
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

public void readOutputSettings(File file)
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
public boolean isParsable(String input){
    boolean parsable = true;
    try{
        Integer.parseInt(input);
    }catch(NumberFormatException e){
        parsable = false;
    }
    return parsable;
}

public void setIcon()
{
  PGraphics icon = createGraphics(16,16);
  icon.beginDraw();
  //icon.image(getToolkit.getImage("icon.ico"), 0, 0, 16, 16);
  icon.endDraw();
  frame.setIconImage(icon.image);
}

//auxilary function for readOutputConfigration to check if input configuration fits dimentions of current mosaic
public boolean isApplicable(int xCells, int yCells, int tileSize)
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
// inputImageManager renders everything you can do and see when working with input image
// selecting input image
// every output mosaic will be based on input image with respect for intensities inside input image
class AboutManager
{
  private int background;
  private PImage image;   // input image
  private PImage resized; // resized image to fit inside application window
  private PImage createdBy;
  private PImage info;
  private PImage[] img;
  private float t;
  private int oldT;
  private float elapsed;
  private int shift;
  
  public AboutManager(int background)
  {
    this.background = background;
    this.img = new PImage[7];
    for(int i = 0; i<img.length;i++)
    {
      img[i] = loadImage("podpis/podpis"+(i+1)+".png");
    }
    createdBy = loadImage("podpis/createdBy.png");
    //info = loadImage("podpis/info.png");
  }
  
  // responsible for rendering everything within about section
  public void renderAboutManager()
  {
    background(background);
    t = millis();
    t /= 200.0f;
    shift = -150;
    //imageMode(CENTER);
    image(createdBy,width/2-createdBy.width/2+40,20);
    //image(info,width/2-info.width/2+40,450);
    for(int i = 0; i < img.length; i++)
    {
      image(img[i],width/2+shift,height/5+sin(t-i*0.4f-1)*4);
      shift += img[i].width;
    }
    imageMode(CORNERS);
  }
}
// inputImageManager renders everything you can do and see when working with input image
// selecting input image
// every output mosaic will be based on input image with respect for intensities inside input image
class InputImageManager
{
  private int background;
  private PImage image;   // input image
  private PImage resized; // resized image to fit inside application window
  
  public InputImageManager(int background)
  {
    this.background = background;
  }
  
  public PImage getImage()
  {
    return image;
  }
  
  public void setImage(PImage i)
  {
    image = i;
    image.filter(GRAY);
  }
  
  public void setResizedImage(PImage r)
  {
    resized = r;
  }
  
  // responsible for rendering everything within input image section
  public void renderInputImageManager()
  {
    background(background);
    if(resized != null)
    {
      imageMode(CENTER);
      image(resized,(width-150)/2+140,height/2);
      imageMode(CORNERS);
    }
  }
  
  // for resizing input images for preview to fit application canvas
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
}



class Manager
{
  private int xCenter;  // center of application window on x axis
  private int yCenter;  // center of application window on y axis
  private int background;
  private int activeManager;        // currently active manager (creating tiles, selecting input image or mosaic generating)
  private TileManager ti;           // tile manager is where user defines all mosaic tiles
  private InputImageManager in;     // input image manager is where user selects input image used for generating mosaic
  private OutputImageManager ou;    // output image manager is where user creates final mosaic
  private AboutManager ab;    // output image manager is where user creates final mosaic
  ControlP5 cp5;
  List<Controller> basicMenu;        // lists of menu sections basic menu is on top left side, and than section specific menus (tiles menu, input menu, output menu..)
  List<Controller> tilesMenu;
  List<Controller> inputImageMenu;
  List<Controller> outputImageMenu;

  public Manager(ControlP5 cp5, int background)
  {
    this.cp5 = cp5;
    xCenter = width/2;
    yCenter = height/2;
    this.background = background;
    activeManager = 0;
    ti = new TileManager(20,20, 2, true, true, cp5, background);
    in = new InputImageManager(background);
    ou = new OutputImageManager(null, ti.getTiles(), 5, 1, 0.f, 0.f, background);
    ab = new AboutManager(background);
    basicMenu = new ArrayList<Controller>();
    tilesMenu = new ArrayList<Controller>();
    inputImageMenu = new ArrayList<Controller>();
    outputImageMenu = new ArrayList<Controller>();
    
     cp5.setColorBackground(color(70));
     cp5.setColorForeground(color(0,255,0,100));
     cp5.setColorActive(color(0,255,0));
     cp5.setColorCaptionLabel(color(255));
     cp5.setColorValueLabel(color(255));
    
    // general (basic) buttons section
    cp5.addButton("tiles")
    .setCaptionLabel("1. tiles")
    .setPosition(10, 10)
    .setColorBackground(color(255))
    .setColorForeground(color(190,255,190))
    .setColorActive(color(0,255,0))
    .setColorCaptionLabel(color(0))
    .setColorValueLabel(color(0))
    .setSize(120, 30);
    
    cp5.addButton("inputImage")
    .setCaptionLabel("2. input image")
    .setPosition(10, 10+40)
    .setSize(120, 30)
    .setColorBackground(color(255))
    .setColorForeground(color(190,255,190))
    .setColorActive(color(0,255,0))
    .setColorCaptionLabel(color(0))
    .setColorValueLabel(color(0))
    ;
    
    cp5.addButton("outputImage")
    .setCaptionLabel("3. output image")
    .setPosition(10, 10+80)
    .setSize(120, 30)
    .setColorBackground(color(255))
    .setColorForeground(color(190,255,190))
    .setColorActive(color(0,255,0))
    .setColorCaptionLabel(color(0))
    .setColorValueLabel(color(0))
    ;
    
    cp5.addButton("about")
    .setCaptionLabel("about")
    .setPosition(10, 10+120)
    .setSize(120, 30)
    .setColorBackground(color(255))
    .setColorForeground(color(190,255,190))
    .setColorActive(color(0,255,0))
    .setColorCaptionLabel(color(0))
    .setColorValueLabel(color(0))
    ;
    
    cp5.addButton("quit")
    .setCaptionLabel("quit")
    .setPosition(10, 10+160)
    .setSize(120, 30)
    .setColorBackground(color(255))
    .setColorForeground(color(190,255,190))
    .setColorActive(color(0,255,0))
    .setColorCaptionLabel(color(0))
    .setColorValueLabel(color(0))
    ;
     
    // tile manager buttons section
     cp5.addSlider("xTileSize")
     .setCaptionLabel("new x tile size")
     .setPosition(10, height - 10 - 470)
     .setSize(80,30)
     .setRange(5, 60)
     .setValue(ti.getXCells())
     .getValueLabel().align(ControlP5.CENTER, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0)
     ;
     
     cp5.addSlider("yTileSize")
     .setCaptionLabel("new y tile size")
     .setPosition(10, height - 10 - 420)
     .setSize(80,30)
     .setRange(5, 60)
     .setValue(ti.getYCells())
     .getValueLabel().align(ControlP5.CENTER, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0)
     ;
     
     cp5.addButton("newTileSet")
     .setCaptionLabel("new tile set")
     .setPosition(10, height - 10 - 370)
     .setSize(120, 30);
     
     cp5.addButton("loadTiles")
     .setCaptionLabel("load tile set...")
     .setPosition(10, height - 10 - 330)
     .setSize(120, 30);
     
     cp5.addButton("saveTiles")
     .setCaptionLabel("save tile set to folder...")
     .setPosition(10, height - 10 - 290)
     .setSize(120, 30);
     
     cp5.addButton("addTile")
     .setCaptionLabel("add tile")
     .setPosition(10, height - 10 - 250)
     .setSize(120, 30);
     
     cp5.addButton("removeTile")
     .setCaptionLabel("remove this tile")
     .setPosition(10, height - 10 - 210)
     .setSize(120, 30);
     
     cp5.addSlider("brushSize")
     .setCaptionLabel("brush size")
     .setPosition(10, height - 10 - 160)
     .setSize(80,30)
     .setRange(1, 3)
     .setValue(ti.getBrushSize())
     .setNumberOfTickMarks(3)
     .getValueLabel().align(ControlP5.CENTER, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0).hide()
     ;
     
     cp5.addToggle("brushMode")
     //.setMode(ControlP5.SWITCH)
     .setCaptionLabel("brush/inverter")
     .setPosition(10, height - 10 - 110)
     .setSize(30,30)
     .setValue(false);
     ;
     
     cp5.addToggle("blackWhite")
     //.setMode(ControlP5.SWITCH)
     .setCaptionLabel("black/white")
     .setPosition(10, height - 10 - 60)
     .setSize(30,30)
     .setValue(false);
     ;
     
     cp5.addButton("moveTileBack")
     .setCaptionLabel("move tile back")
     .setPosition(width-260, height - 100)
     .setSize(100, 30);
     
     cp5.addButton("moveTileForward")
     .setCaptionLabel("move tile forward")
     .setPosition(width-130, height - 100)
     .setSize(100, 30);
    
    // input image buttons section
    cp5.addButton("loadInputImage")
    .setCaptionLabel("load image...")
    .setPosition(10, height - 10 - 70)
    .setSize(120, 30);
    
    cp5.addButton("saveInputImage")
    .setCaptionLabel("save image to folder...")
    .setPosition(10, height - 10 - 30)
    .setSize(120, 30);
    
    // output image buttons section
    cp5.addSlider("sizeOfTiles")
    .setCaptionLabel("size of tiles")
    .setPosition(10, height - 350)
    .setSize(120, 30)
    .setRange(1, 10)
    .setValue(ou.getTileSize())
    .setNumberOfTickMarks(10)
    .setDecimalPrecision(1)
    //.getValueLabel().align(ControlP5.CENTER, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0).hide()
    ;
    
    cp5.addSlider("evenRowShift")
    .setCaptionLabel("even row shift")
    .setPosition(10, height - 305)
    .setSize(120, 30)
    .setRange(-100,0)
    .setValue(0)
    //.getValueLabel().align(ControlP5.CENTER, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0).hide()
    ;
    
    cp5.addSlider("oddRowShift")
    .setCaptionLabel("odd row shift")
    .setPosition(10, height - 265)
    .setSize(120, 30)
    .setRange(-100,0)
    .setValue(0)
    //.getValueLabel().align(ControlP5.CENTER, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0).hide()
    ;
    
    
    cp5.addButton("loadOutput")
    .setCaptionLabel("load output configuration")
    .setPosition(10, height - 215)
    .setSize(120, 30);
    
    cp5.addButton("saveOutput")
    .setCaptionLabel("save output configuration")
    .setPosition(10, height - 175)
    .setSize(120, 30);
    
    //cp5.addSlider("scaleOutputSize")
    //.setCaptionLabel("scale PDF size")
    //.setPosition(10, height - 135)
    //.setSize(120, 30)
    //.setRange(1, 10)
    //.setNumberOfTickMarks(10)
    //.setValue(1)
    //.setDecimalPrecision(1)
    ////.getValueLabel().align(ControlP5.CENTER, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0).hide()
    //;
    
    cp5.addToggle("transparent")
     //.setMode(ControlP5.SWITCH)
     .setCaptionLabel("transparent background")
     .setPosition(10, height - 90)
     .setSize(30,30)
     .setValue(false)
     ;
    
    cp5.addButton("savePDF")
    .setCaptionLabel("export PDF to folder...")
    .setPosition(10, height - 40)
    .setSize(120, 30);
    
    //cp5.addButton("loadProject")
    //.setCaptionLabel("load project folder...")
    //.setPosition(10, height - 10 - 70)
    //.setSize(120, 30);
    
    //cp5.addButton("saveProject")
    //.setCaptionLabel("save project to folder...")
    //.setPosition(10, height - 10 - 30)
    //.setSize(120, 30);
    
    
    // buttons need to be assigned to each group basic,tiles,inuput and output menus, so application can eg set visibility of these groups when switching between sections
    basicMenu.add((Controller)cp5.get("tiles"));
    basicMenu.add((Controller)cp5.get("inputImage"));
    basicMenu.add((Controller)cp5.get("outputImage"));
    basicMenu.add((Controller)cp5.get("about"));
    basicMenu.add((Controller)cp5.get("quit"));
    
    tilesMenu.add((Controller)cp5.get("xTileSize"));
    tilesMenu.add((Controller)cp5.get("yTileSize"));
    tilesMenu.add((Controller)cp5.get("newTileSet"));
    tilesMenu.add((Controller)cp5.get("loadTiles"));
    tilesMenu.add((Controller)cp5.get("saveTiles"));
    tilesMenu.add((Controller)cp5.get("addTile"));
    tilesMenu.add((Controller)cp5.get("removeTile"));
    tilesMenu.add((Controller)cp5.get("brushSize"));
    tilesMenu.add((Controller)cp5.get("brushMode"));
    tilesMenu.add((Controller)cp5.get("blackWhite"));
    tilesMenu.add((Controller)cp5.get("moveTileBack"));
    tilesMenu.add((Controller)cp5.get("moveTileForward"));
    
    inputImageMenu.add((Controller)cp5.get("loadInputImage"));
    inputImageMenu.add((Controller)cp5.get("saveInputImage"));
    
    outputImageMenu.add((Controller)cp5.get("sizeOfTiles"));
    //outputImageMenu.add((Controller)cp5.get("scaleOutputSize"));
    outputImageMenu.add((Controller)cp5.get("evenRowShift"));
    outputImageMenu.add((Controller)cp5.get("oddRowShift"));
    outputImageMenu.add((Controller)cp5.get("saveOutput"));
    outputImageMenu.add((Controller)cp5.get("loadOutput"));
    outputImageMenu.add((Controller)cp5.get("transparent"));
    outputImageMenu.add((Controller)cp5.get("savePDF"));
    //outputImageMenu.add((Controller)cp5.get("loadProject"));
    //outputImageMenu.add((Controller)cp5.get("saveProject"));
    
    
    // setup tile manager as default on startup
    showButtonsOfManager(0);
    hideButtonsOfManager(1);
    hideButtonsOfManager(2);
    activateManager(0);
  }

  public int getXCenter()
  {
    return xCenter;
  }

  public int getYCenter()
  {
    return yCenter;
  }
  
  public int getActiveManager()
  {
    return activeManager;
  }
  
  public TileManager getTileManager()
  {
    return ti;
  }
  
  public InputImageManager getInputImageManager()
  {
    return in;
  }
  
  public OutputImageManager getOutputImageManager()
  {
    return ou;
  }
  
  public void setTileManager(TileManager ti)
  {
    this.ti = ti;
  }

  // responsible for rendering everything in application
  public void renderManager()
  {
    switch(activeManager)
    {
      case 0: 
        ti.renderTileManager();
        break;
      case 1:
        in.renderInputImageManager();
        break;
      case 2:
        ou.renderOutputImageManager();
        break;
      case 3:
        ab.renderAboutManager();
        break;
    }
  }
  
  // set which manager is active (is on screen/is shown)
  // when switching between sections it is necesery to check/change/set some attributes (mainly because non linear workflow - user can go back and forward in his worksflow and do changes in one section ..so we have to make sure changes are applied to other sections) 
  public void activateManager(int manager)
  {
    switch(manager)
    {
    case 0:
      if(activeManager == 2)
      {
        ou.saveFlipMatrices();
        if(ou.getMosaic()!= null)
        {
          ou.getMosaic().setLastSeenTiles(ti.getTiles());
          ou.getMosaic().setLastSeenImage();
          ou.getMosaic().setLastImgRatio();
          ou.getMosaic().setLastTileRatio();
        }
      }
      cp5.getController("tiles").setColorBackground(color(190,255,190));
      cp5.getController("inputImage").setColorBackground(cp5.getController("quit").getColor().getBackground());
      cp5.getController("outputImage").setColorBackground(cp5.getController("quit").getColor().getBackground());
      cp5.getController("about").setColorBackground(cp5.getController("quit").getColor().getBackground());
      activeManager = 0;
      break;
    case 1:
      if(activeManager == 2)
      {
        ou.saveFlipMatrices();
        if(ou.getMosaic()!= null)
        {
          ou.getMosaic().setLastSeenTiles(ti.getTiles());
          ou.getMosaic().setLastSeenImage();
          ou.getMosaic().setLastImgRatio();
          ou.getMosaic().setLastTileRatio();
        }
      }
      cp5.getController("tiles").setColorBackground(cp5.getController("quit").getColor().getBackground());
      cp5.getController("inputImage").setColorBackground(color(190,255,190));
      cp5.getController("outputImage").setColorBackground(cp5.getController("quit").getColor().getBackground());
      cp5.getController("about").setColorBackground(cp5.getController("quit").getColor().getBackground());
      activeManager = 1;
      break;
    case 2: 
      ou.setInput(in.getImage());
      ou.setTiles(ti.getTiles());
      if(ou.getMosaic() != null)
      {
       ou.getMosaic().setMiniatures();
       ou.setToRerender();
       // ou.recoverFlipMatrices keeps flipped tiles in mosaic flipped even if user leaves to different section and returns back, however when user choose new input image or different tile size information about flips is not restored
       if(in.getImage() != null && activeManager != 2
         && in.getImage().width == ou.getMosaic().getLastSeenImage().width && in.getImage().height == ou.getMosaic().getLastSeenImage().height
         && ti.getTiles()[0].getW() == ou.getMosaic().getLastSeenTiles()[0].getW() && ti.getTiles()[0].getH() == ou.getMosaic().getLastSeenTiles()[0].getH())
       {
         ou.recoverFlipMatrices();
       }
       //println(in.getImage().width/((float)in.getImage().height), ou.getInput().width/((float)ou.getInput().height));
       //if(in.getImage().width/((float)in.getImage().height) == ou.getInput().width/((float)ou.getInput().height))
       //  ou.recoverFlipMatrices();
      }
      //background(background);
      //if(ou.getInput() != null)
      //{
      //  ou.getMosaic().setPreviewCanvas();
      //  ou.getMosaic().drawImage();
      //  //renderPreview();
      //  //renderHover();
      //}
      cp5.getController("tiles").setColorBackground(cp5.getController("quit").getColor().getBackground());
      cp5.getController("inputImage").setColorBackground(cp5.getController("quit").getColor().getBackground());
      cp5.getController("outputImage").setColorBackground(color(190,255,190));
      cp5.getController("about").setColorBackground(cp5.getController("quit").getColor().getBackground());
      activeManager = 2;
      break;
      case 3:
      if(activeManager == 2)
      {
        ou.saveFlipMatrices();
        if(ou.getMosaic()!= null)
        {
          ou.getMosaic().setLastSeenTiles(ti.getTiles());
          ou.getMosaic().setLastSeenImage();
          ou.getMosaic().setLastImgRatio();
          ou.getMosaic().setLastTileRatio();
        }
      }
      cp5.getController("tiles").setColorBackground(cp5.getController("quit").getColor().getBackground());
      cp5.getController("inputImage").setColorBackground(cp5.getController("quit").getColor().getBackground());
      cp5.getController("outputImage").setColorBackground(cp5.getController("quit").getColor().getBackground());
      cp5.getController("about").setColorBackground(color(190,255,190));
      activeManager = 3;
      break;
    }
  }
  
  // when switching between section different button sets have to be visible
  public void hideButtonsOfManager(int i)
  {
    switch(i)
    {
      case 0: 
        for(Controller b: tilesMenu)
        {
          if(b!= null)
          {
            b.setVisible(false);
          }
        }
        break;
      case 1:
        for(Controller b: inputImageMenu)
        {
          if(b!= null)
          {
            b.setVisible(false);
          }
        }
        break;
      case 2:
        for(Controller b: outputImageMenu)
        {
          if(b!= null)
          {
            b.setVisible(false);
          }
        }
        break;
    }
  }

  // when switching between section different button sets have to be visible
  public void showButtonsOfManager(int i)
  {
    switch(i)
    {
      case 0: 
        for(Controller b: tilesMenu)
        {
          b.setVisible(true);
        }
        break;
      case 1:
        for(Controller b: inputImageMenu)
        {
          b.setVisible(true);
        }
        break;
      case 2:
        for(Controller b: outputImageMenu)
        {
          b.setVisible(true);
        }
        break;
    }
    
  }
}
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
    this.lastImgRatio = 0.0f;
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
      avaragedImgs[0].resize((int)(avaragedImgs[0].width*0.1f),(int)(avaragedImgs[0].height*0.1f));
      int w = avaragedImgs[0].width;
      int h = avaragedImgs[0].height;
      avaragedImgs[0].resize(xCanvasSize,yCanvasSize);
      for(int i = 1; i < 5; i++)
      {
       avaragedImgs[i] = avaragedImgs[i-1].copy();
       avaragedImgs[i].resize((int)(w - w*(float)(i)*0.24f),(int)(h - h*(float)(i)*0.24f));
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
    for(int i = 0; i < img.width; i++)
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
    int evenRowShift = (int)(xTileSize*evenRowS*0.01f);
    int oddRowShift = (int)(yTileSize*oddRowS*0.01f);
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
          pdf.rect(x+i*cellSize, y+j*cellSize, cellSize*1.1f, cellSize*1.1f);
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
          pdf.rect(x+i*cellSize, y+j*cellSize, cellSize*1.1f, cellSize*1.1f);
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
          pdf.rect(x+i*cellSize, y+j*cellSize, cellSize*1.1f, cellSize*1.1f);
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
          pdf.rect(x+i*cellSize, y+j*cellSize, cellSize*1.1f, cellSize*1.1f);
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
// outputImageManager renders everything you can do and see when adjusting output mosaic settings
class OutputImageManager{
  
  private Mosaic mosaic;               // mosaic
  private int background;
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
  
  public OutputImageManager(PImage img, Tile[] tiles, int tileSize, int pdfScale, float evenRowShift, float oddRowShift, int background)
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
  private int startColor;         // intensity range of this tile
  private int endColor;
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
  
  public int getStartColor()
  {
    return startColor;
  }
  
  public int getEndColor()
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
  
  public void setStartColor(int c)
  {
    startColor = c;
    startBrightness = brightness(c);
  }
  
  public void setEndColor(int c)
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
// tileManager manages and renders everything you can do and see when working with tiles
class TileManager
{
  private Tile[] tiles;
  private ControlP5 cp5;
  private PImage[] previews;         // images of tile previews for right side menu selection
  private PImage[] previewGradients; // images of previews of color intensity inside input image which will be subtituted with appropriate tile in the final output image
  private boolean[] previewHover;    // true when preview tile has cursor over area
  private boolean somethingToRender; // is there anything (eg highlited cells, highlited preview tiles..) new to render so we can rerender whole screen
  private int background;
  private int activeTile;     // which tile is currently active = is being modified
  private int hoverTile;      // which of tile preview (on the right side) has cursor over it
  private int xCells;         // number of pixels of tile in x direction
  private int yCells;         // number of pixels of tile in y direction
  private int brushSize;      // size of brush
  private boolean brushMode;  // do you change pixels like with brush(black color+ white color) or do you change pixels with inverting pixels
  private boolean isBlack;    // color when working in brush mode

  public TileManager(int xCells, int yCells, int brushSize, boolean brushMode, boolean isBlack, ControlP5 cp5, int background)
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
  
  public TileManager(int xCells, int yCells, ControlP5 cp5, int background )
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
      int startIntensity = color(intensity-j*step);
      int endIntensity;
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

    if (i < tiles.length)
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
    if(!(activeTile-1 < 0) && activeTile > 0)
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
  public void settings() {  size(1160, 720); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "StampImage" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
