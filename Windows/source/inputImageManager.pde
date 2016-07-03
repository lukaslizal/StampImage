// inputImageManager renders everything you can do and see when working with input image
// selecting input image
// every output mosaic will be based on input image with respect for intensities inside input image
class InputImageManager
{
  private color background;
  private PImage image;   // input image
  private PImage resized; // resized image to fit inside application window
  
  public InputImageManager(color background)
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