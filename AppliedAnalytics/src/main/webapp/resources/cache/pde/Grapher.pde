float[] values = new float[20];
float plotX1, plotX2, plotY1, plotY2;
int leftMargin = 20;
int topMargin = 100;
int plotHeight = 250;
float timer = 0.0;
PFont helvetica;
int x1, y1, x2, y2;
int rx, ry;
int w, h;
int hypoCount = 320;
int amount = 9;
float[] xhval = new float[amount];
float[] yhval = new float[amount];
int count = 301;


void setup() {
  size(640, 480);

  smooth();
  helvetica = createFont("Helvetica-Bold", 14);
  textFont(helvetica);


  generateValues();

  // set plot size
  plotX1 = leftMargin;
  plotX2 = width - leftMargin;
  plotY1 = topMargin;
  plotY2 = height - topMargin;

  //x1 = 500;
  //x2 = 500;
  y1 = 378; 
  y2 = 100;
  x1 = 300;
  x2 = 300;


  w = 620;
  h = 380;

  rx = 20;
  ry = 100;
}

void draw() {
  background(192, 192, 192);
  //fill(211, 211, 211);
  fill(238,238,224);
  noStroke();
  rectMode(CORNERS);
  rect(plotX1, plotY1, plotX2, plotY2);
  
  drawTickMarks();

  noFill();
  stroke(255, 128, 0);
  strokeWeight(3);
  beginShape(); 
  float x, y;
  float[] xval = new float[values.length];
  float[] yval = new float[values.length];
  for (int i = 0; i < values.length; i++) {    
    x = map(i, 0, values.length-1, plotX1, plotX2);    
    y = map(values[i], 0, 200, height - topMargin, height - topMargin - plotHeight);
    xval[i] = x;
    yval[i] = y;    
    vertex(x, y);
  }  
  endShape();


  noFill();
  stroke(255, 0, 0);
  strokeWeight(3);
  beginShape();
  // draw hypothetical lines
  int xv = 301;
  int xamt = 9;
  int dist = (int) 301/8 +2;
  float[] xh = new float[xamt];
  float[] yh = new float[xamt];

  // generate y values 100-378
  yh = generateYValues(xamt, 205);
  for(int i = 0; i < xamt; i++)
  {
    yhval[i] = yh[i];
  }
  // generate x values
  for (int i = 0; i < xamt; i++)
  {
    xh[i] = xv;
    xhval[i] = xh[i];
    xv = xv+dist;
  }
  for (int i = 0; i < xamt; i++)
  {
    stroke(255, 0, 127);
    vertex(xh[i], yh[i]);
  }  
  endShape();


  // draw outline
  stroke(104, 104, 104);
  strokeWeight(4);
  //draw line
  line(x1, y1, x2, y2);
  rect(rx, ry, w, h);
  drawLineLabels();

  // draw points on mouse over
  for (int i = 0; i < values.length; i++) {
    x = map(i, 0, values.length-1, plotX1, plotX2);
    y = map(values[i], 0, 200, height - topMargin, height - topMargin - plotHeight);

    // check mouse pos
    // float delta = dist(mouseX, mouseY, x, y);
    float delta = abs(mouseX - x);
    if ((delta < 15) && (y > plotY1) && (y < plotY2)) {
      stroke(255);
      fill(0);
      ellipse(x, y, 8, 8);

      int labelVal = round(values[i]);
      Label label = new Label("" + labelVal, x, y);
    }
  }
  // draw points on mouse over hypothetical line
  highlightHypo(amount, xhval, yhval);
}

void keyPressed() {
  generateValues();
}

void generateValues() {
  for (int i = 0; i < values.length; i++) {
    //values[i] = (int) random(200);
    values[i] = noise(timer) * 200;
    timer += 0.7;
  }

  // get min/max range
  plotX1 = leftMargin;
  plotX2 = width - plotX1;
}


void drawLineLabels()
{
  textFont(helvetica, 10);
  fill(64, 64, 64);
  text("UNCHANGED", 60, 85);
  text("HYPOTHETICAL", 220, 85);
  stroke(255, 128, 0);
  strokeWeight(2);
  line(20, 80, 55, 80);
  stroke(255, 0, 127);
  strokeWeight(2);
  line(180, 80, 215, 80);
}


float[] generateYValues(int amt, int start)
{
  float[] ret = new float[amt];
  // generate y values between 100 and 378
  int increment = 14;
  ret[0] = start;
  for(int i = 1; i < amt; i++)
  {
    // if even number
    if(i%2 == 0)
    {
      ret[i] = start+increment;
      increment = increment + 7;
    }
    else
    {
      ret[i] = start-increment;
      increment = increment-10;
    }
  }
  return ret;
}

void highlightHypo(int amt, float[] xh, float[] yh)
{
  float x,y;
  // draw points on mouse over
  for (int i = 0; i < amt; i++) {
    x = xh[i];
    y = yh[i];

    // check mouse pos
    // float delta = dist(mouseX, mouseY, x, y);
    float delta = abs(mouseX - x);
    if ((delta < 15) && (y > plotY1) && (y < plotY2)) {
      stroke(255);
      fill(0);
      ellipse(x, y, 8, 8);

      int labelVal = round(yh[i]);
      Label label = new Label("" + labelVal, x, y);
    }
  }
}

void drawTickMarks()
{
  int yaxis = y1+20;
  int xaxis = x1-270;
  int y_1 = y1-5;
  int y_2 = y1+5;

  textFont(helvetica, 10);
  fill(0);
  // text
  text("TODAY", x1-15, y1+20); 
  text("SUNDAY", x1-270, yaxis);
  text("SUNDAY", xaxis+90, yaxis);
  text("SUNDAY", xaxis+180, yaxis);
  text("SUNDAY", xaxis+330, yaxis);
  text("SUNDAY", xaxis+420, yaxis);
  text("SUNDAY", xaxis+510, yaxis);
    
  // lines
  stroke(104, 104, 104);
  strokeWeight(3);
  line(xaxis+17, y_1, xaxis+17, y_2);
  line(xaxis+107, y_1, xaxis+107, y_2);
  line(xaxis+197, y_1, xaxis+197, y_2);
  line(xaxis+347, y_1, xaxis+347, y_2);
  line(xaxis+437, y_1, xaxis+437, y_2);
  line(xaxis+527, y_1, xaxis+527, y_2);
  
  // dates 
  text("9/29", x1-270+10, yaxis+15);
  text("10/6", xaxis+90+10, yaxis+15);
  text("10/13", xaxis+180+7, yaxis+15);
  text("10/20", xaxis+330+5, yaxis+15);
  text("10/27", xaxis+420+5, yaxis+15);
  text("11/3", xaxis+510+8, yaxis+15);
  
}

