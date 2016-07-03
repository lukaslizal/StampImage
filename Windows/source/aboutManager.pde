// inputImageManager renders everything you can do and see when working with input image
// selecting input image
// every output mosaic will be based on input image with respect for intensities inside input image
class AboutManager
{
  private color background;
  private PImage image;   // input image
  private PImage resized; // resized image to fit inside application window
  private PImage createdBy;
  private PImage info;
  private PImage[] img;
  private float t;
  private int oldT;
  private float elapsed;
  private int shift;
  
  public AboutManager(color background)
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
    t /= 200.0;
    shift = -150;
    //imageMode(CENTER);
    image(createdBy,width/2-createdBy.width/2+40,20);
    //image(info,width/2-info.width/2+40,450);
    for(int i = 0; i < img.length; i++)
    {
      image(img[i],width/2+shift,height/5+sin(t-i*0.4-1)*4);
      shift += img[i].width;
    }
    imageMode(CORNERS);
  }
}