// 2D coordinate system class
// Bjørn Christensen, 3/3-2024

import java.awt.*;

public class S2 {
    static final V2 Origo=new V2(0,0);
    V2 O;
    M2 S;
    M2 F;
    M2 T;

    S2(int sx, int sy, int ox, int oy){
        O=new V2(ox,oy);        // Position in pixels of coordinate system on screen
        F=new M2(1, 0,          // Matrix to flip y-axis
                0,-1);
        S=new M2(sx,  0,        // Scaling matrix
                0, sy);         // sx: number of pixels for 1 unit in x-axis of virtual world coordinate system
        T=F.mul(S);             // Combined transformation matrix
    }

    V2 transform(V2 v){         // Transform from 2D virtual world coordinates to pixels
        return T.mul(v).add(O);
    }

    public void moveTo(V2 p){
        O=transform(p);
    }

    public void rotate(double phi){
        M2 R=new M2(Math.cos(phi), -Math.sin(phi),
                Math.sin(phi), Math.cos(phi));
        T=T.mul(R);
    }

    public void drawAxis(Graphics g){
        drawLine(g, new V2(0,0), new V2(1,0));              // X-axis
        drawLine(g, new V2(0.9,0.1), new V2(1,0));
        drawLine(g, new V2(0.9,-0.1), new V2(1,0));
        drawString(g, new V2(1.1,0), "x");

        drawLine(g, new V2(0,0), new V2(0,1));              // Y-axis
        drawLine(g, new V2(-0.1,0.9), new V2(0,1));
        drawLine(g, new V2(0.1,0.9), new V2(0,1));
        drawString(g, new V2(0,1.1), "y");
    }

    public void drawLine(Graphics g, V2 v1, V2 v2){
        V2 p1=transform(v1);              // point in pixels
        V2 p2=transform(v2);              // point in pixels
        g.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
    }
    public void drawLine(Graphics g, V2 v1, V2 v2, Color c){
        Color oldColor = g.getColor();
        g.setColor(c);
        drawLine(g, v1, v2);
        g.setColor(oldColor);
    }
    public void drawLine(Graphics g, V2 v1, V2 v2, Color c, float weight){
        Stroke line1 = new BasicStroke(1.0f);
        Stroke line2 = new BasicStroke(weight);
        Graphics2D g2=(Graphics2D)g;
        g2.setStroke(line2);
        drawLine(g, v1, v2, c);
        g2.setStroke(line1);
    }

    public void drawPoint(Graphics g, V2 v){
        V2 p=transform(v);                // p in pixels
        g.fillOval((int)p.x-1, (int)p.y-1, 2, 2);
    }
    public void drawPoint(Graphics g, V2 v, int size){
        V2 p=transform(v);                // p in pixels
        g.fillOval((int)p.x-size/2, (int)p.y-size/2, size, size);
    }
    public void drawPoint(Graphics g, V2 v, Color c){
        Color oldColor = g.getColor();
        g.setColor(c);
        drawPoint(g, v);
        g.setColor(oldColor);
    }
    public void drawPoint(Graphics g, V2 v, Color c, int size){
        Color oldColor = g.getColor();
        g.setColor(c);
        drawPoint(g, v, size);
        g.setColor(oldColor);
    }

    void drawString(Graphics g, V2 p, String text){
        V2 pp=transform(p);   // point in pixels
        g.drawString(text, (int)pp.x, (int)pp.y);
    }

    public static void main(String[] args) {
        System.out.println("S2");
        S2 S=new S2(50,50, 100,300);
    }
}