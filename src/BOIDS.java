import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static java.lang.Math.*;

public class BOIDS extends JFrame{
    public static void main(String[] args) {
        BOIDS frame = new BOIDS();
        frame.setTitle("BOIDS in 3D space");
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    BOIDS(){add(new PaintPanel());}

    class PaintPanel extends JPanel{
        Timer myTimer = new Timer(50, new TimerListener());
        int count = 0;
        Camera camera = new Camera(100, 100, 200, 400);
        M3 I = new M3(1, 0, 0,
                0, 1, 0, 0, 0, 1);
        M3 Sz = new M3(0, -1, 0, 1, 0, 0, 0, 0, 0);
        double phi = PI/100;
        M3 Rz = I.add(Sz.mul(sin(phi))).add(Sz.mul(Sz).mul(1-cos(phi)));
        //V3[] cone;
        //Boid boid = new Boid();
        Boid[] boids = new Boid[500];
        V3 C = new V3(0, 0, 0);


        int prevX, prevY;


        PaintPanel() {
            setFocusable(true);
            addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_W:
                            camera.moveForward(1);
                            break;
                        case KeyEvent.VK_S:
                            camera.moveBackward(1);
                            break;
                        case KeyEvent.VK_A:
                            camera.moveLeft(1);
                            break;
                        case KeyEvent.VK_D:
                            camera.moveRight(1);
                            break;
                        case KeyEvent.VK_SPACE:
                            camera.moveUp(1);
                            break;
                        case KeyEvent.VK_SHIFT:
                            camera.moveDown(1);
                            break;
                        case KeyEvent.VK_M:
                            toggleRuleOfSeparation();
                        case KeyEvent.VK_COMMA:
                            toggleRuleOfAlignment();
                            break;
                        case KeyEvent.VK_PERIOD:
                            toggleRuleOfCohesion();
                            break;
                    }
                    repaint();
                }
            });


            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    prevX = e.getX();
                    prevY = e.getY();
                }
            });

            addMouseMotionListener(new MouseAdapter() {
                public void mouseDragged(MouseEvent e) {
                    int dx = e.getX() - prevX;
                    //int dx = 0;
                    int dy = e.getY() - prevY;
                    prevX = e.getX();
                    prevY = e.getY();
                    //camera.rotateCamera(dx, dy);
                    camera.rotateCamera(-dx, dy);
                    repaint();
                }
            });


            addMouseListener(new MouseAdapter() {
                public void mouseReleased(MouseEvent e) {
                    prevX = e.getX();
                    prevY = e.getY();
                }
            });



            myTimer.start();

            /*
            int n = 5;
            double radius = 2.0;
            double height = 4.0;
            cone = new V3[n + 1];


            for (int i = 0; i < n; i++) {
                double angle = 2 * PI * i / n;
                cone[i] = new V3(radius * cos(angle), radius * sin(angle), 0);
                C = C.add(cone[i]);
            }

            cone[n] = new V3 (0, 0, height);
            C = C.mul(1.0 / n);
            */

            for (int i = 0; i < boids.length; i++) {
                double distance = new Boid(new V3(0,0,0)).maxDisFromCenter * 1.5;
                double x = (int) ((Math.random() * (distance)));
                double y = (int) ((Math.random() * (distance)));
                double z = (int) ((Math.random() * (distance)));
                boids[i] = new Boid(new V3(x, y, z));
            }

            camera.moveTo(new  V3(100, 5, 5));
            camera.focus(C);
            camera.z = 6;
        }


        V3 transformVertex(V3 vertex, Boid boid) {
            V3 transformedVertex = new V3(
                    vertex.x * boid.D.x + vertex.y * boid.R.x + vertex.z * boid.U.x,
                    vertex.x * boid.D.y + vertex.y * boid.R.y + vertex.z * boid.U.y,
                    vertex.x * boid.D.z + vertex.y * boid.R.z + vertex.z * boid.U.z
            );

            transformedVertex = transformedVertex.add(boid.position);
            return transformedVertex;
        }

        void drawBoid(Boid boid, Graphics g) {

            V3 position = boid.position;

            for (int i = 0; i < boid.vertices.size() - 1; i++) {
                V3 v1 = boid.vertices.get(i);
                V3 v2 = boid.vertices.get((i + 1) % (boid.vertices.size() - 1));

                V3 transformedV1 = transformVertex(v1, boid);
                V3 transformedV2 = transformVertex(v2, boid);

                camera.drawLine(g, transformedV1, transformedV2);
            }

            for (int i = 0; i < boid.vertices.size() - 1; i++) {
                V3 v1 = boid.vertices.get(i);
                V3 v2 = boid.vertices.get(boid.vertices.size() - 1);

                V3 transformedV1 = transformVertex(v1, boid);
                V3 transformedV2 = transformVertex(v2, boid);

                camera.drawLine(g, transformedV1, transformedV2);
            }
        }


        public void toggleRuleOfSeparation() {
            for (Boid boid : this.boids) {
                boid.ruleOfSeparationOn = !boid.ruleOfSeparationOn;
            }
        }

        public void toggleRuleOfAlignment() {
            for (Boid boid : this.boids) {
                boid.ruleOfAlignmentOn = !boid.ruleOfAlignmentOn;
            }
        }

        public void toggleRuleOfCohesion() {
            for (Boid boid : this.boids) {
                boid.ruleOfCohesionOn = !boid.ruleOfCohesionOn;
            }
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            count++;
            g.drawString("count=" + count, 10, 10);

            Shape originalClip = g.getClip(); // Save original clip
            ((Graphics2D) g).setClip(null); // Disable clipping

            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);

            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);



            camera.drawAxis(g);

            for (Boid boid : boids) {
                drawBoid(boid, g);
                boid.AI(boids);
            }


            ((Graphics2D) g).setClip(originalClip);



            //ROTATION
            /*
            for (int i = 0; i < boid.vertices.size(); i++) {
                boid.vertices.set(i, Rz.mul(boid.vertices.get(i).sub(C)).add(C));
            }

             */
        }



        class TimerListener implements ActionListener {
            public void actionPerformed(ActionEvent evt){
                repaint();
            }
        }
    }
}
