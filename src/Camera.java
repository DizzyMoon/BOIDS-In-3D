import java.awt.*;

public class Camera {
    V3 O=new V3(0,0,0); // Virtual world Basis
    V3 i=new V3(1,0,0);
    V3 j=new V3(0,1,0);
    V3 k=new V3(0,0,1);

    V3 E=new V3(0,0,0); // Camera Basis
    V3 D=new V3(1,0,0);
    V3 U=new V3(0,1,0);
    V3 R=new V3(0,0,1);

    double alpha = 0;
    double beta = 0;
    double gamma = 0.5 * Math.PI;


    double z=2;

    S2 s2;

    Camera(int sx, int sy, int ox, int oy){
        s2=new S2(sx,sy, ox,oy);
    }

    V2 project(V3 p){
        V3 EP=p.sub(E);
        double d=D.dot(EP);
        double u=U.dot(EP);
        double r=R.dot(EP);
        double rm=r*z/d;
        double um=u*z/d;
        return new V2(rm, um);
    }

    void moveTo(V3 p){
        E=new V3(p.x, p.y, p.z);
    }

    void focus(V3 p){
        D=p.sub(E).unit();
        R=D.cross(k).unit();
        U=R.cross(D);
    }

    void drawAxis(Graphics g){
        drawLine(g, O, i);
        drawLine(g, O, j);
        drawLine(g, O, k);
    }

    void rotateCamera(int dx, int dy) {
        double sensitivity = 0.003;
        alpha += dx * sensitivity;
        beta += dy * sensitivity;

        setNewRotation(alpha, beta, gamma);
    }

    void setNewRotation(double alpha, double beta, double gamma) {
        double m11 = Math.cos(alpha) * Math.cos(beta);
        double m12 = Math.cos(alpha) * Math.sin(beta) * Math.sin(gamma) - Math.sin(alpha) * Math.cos(gamma);
        double m13 = Math.cos(alpha) * Math.sin(beta) * Math.cos(gamma) + Math.sin(alpha) * Math.sin(gamma);
        double m21 = Math.sin(alpha) * Math.cos(beta);
        double m22 = Math.sin(alpha) * Math.sin(beta) * Math.sin(gamma) + Math.cos(alpha) * Math.cos(gamma);
        double m23 = Math.sin(alpha) * Math.sin(beta) * Math.cos(gamma) - Math.cos(alpha) * Math.sin(gamma);
        double m31 = -Math.sin(beta);
        double m32 = Math.cos(beta) * Math.sin(gamma);
        double m33 = Math.cos(beta) * Math.cos(gamma);

        M3 rotationMatrix = new M3(m11, m12, m13,
                m21, m22, m23,
                m31, m32, m33);

        D = rotationMatrix.mul(new V3(1, 0, 0));
        U = rotationMatrix.mul(new V3(0, 1, 0));
        R = rotationMatrix.mul(new V3(0, 0, 1));
    }

    void moveUp(double distance) {
        E = E.add(U.mul(distance));
    }

    void moveDown(double distance) {
        E = E.sub(U.mul(distance));
    }

    void moveRight(double distance) {
        E = E.add(R.mul(distance));
    }

    void moveLeft(double distance) {
        E = E.sub(R.mul(distance));
    }

    void moveForward(double distance) {
        E = E.add(D.mul(distance));
    }

    void moveBackward(double distance) {
        E = E.sub(D.mul(distance));
    }

    void drawPoint(Graphics g, V3 p){
        s2.drawPoint(g, project(p));
    }

    void drawLine(Graphics g, V3 p1, V3 p2){
        s2.drawLine(g, project(p1), project(p2));
    }
}
