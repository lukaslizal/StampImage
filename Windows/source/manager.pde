import controlP5.*;
import java.util.*;

class Manager
{
  private int xCenter;  // center of application window on x axis
  private int yCenter;  // center of application window on y axis
  private color background;
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

  public Manager(ControlP5 cp5, color background)
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