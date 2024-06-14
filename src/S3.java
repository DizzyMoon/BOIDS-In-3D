import java.awt.*;

public class S3 {
    S2 s2;

    S3(int sx, int sy, int ox, int oy){
        s2=new S2(sx,sy, ox,oy);
    }

    V2 project(V3 p){
        return new V2(p.y, p.z);
    }

    void drawAxis(Graphics g){
        s2.drawAxis(g);
    }

    void drawPoint(Graphics g, V3 p){
        s2.drawPoint(g, project(p));
    }

    void drawLine(Graphics g, V3 p1, V3 p2){
        s2.drawLine(g, project(p1), project(p2));
    }
}
