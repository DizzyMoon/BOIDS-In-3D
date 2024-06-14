import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class Boid {
    ArrayList<V3> vertices = new ArrayList<>();
    V3 position = new V3(0, 0, 0);
    double radius = 1.0;
    double height = 1.0;
    int complexity = 5;
    V3 velocity = new V3(0, 0, 0);
    double speed = 1;
    double maxSpeed = 2;
    boolean ruleOfSeparationOn = true;
    boolean ruleOfAlignmentOn = true;
    boolean ruleOfCohesionOn = true;
    double turnSpeed = 0.4;
    double desiredSeperation = 5;
    double maxDisFromCenter = 100;
    double neighborDistance = 10;
    V3 D=new V3(1,0,0);
    V3 U=new V3(0,1,0);
    V3 R=new V3(0,0,1);

    public Boid(V3 position) {
        this.position = position;
        for (int i = 0; i < complexity; i++) {
            double angle = 2 * Math.PI * i / complexity;
            vertices.add(new V3(radius * Math.cos(angle), radius * Math.sin(angle), 0));
            position.add(vertices.get(i));
        }

        vertices.add(new V3(0, 0, height) );
        this.position = position.mul(1.0 / complexity);
    }

    public V3 ruleOfSeparation(Boid[] boids) {
        double desiredSeparation = this.desiredSeperation;
        V3 steer = new V3(0, 0, 0);
        int count = 0;

        for (Boid boid : boids) {
            if (boid != this) {
                double distance = Math.sqrt(Math.pow(boid.position.x - this.position.x, 2) + Math.pow(boid.position.y - this.position.y, 2) + Math.pow(boid.position.z - this.position.z, 2));
                if (distance > 0 && distance < desiredSeparation) {
                    V3 diff = new V3(this.position.x - boid.position.x, this.position.y - boid.position.y, this.position.z - boid.position.z);
                    steer = steer.add(diff);
                    count++;
                }
            }
        }

        if (count > 0) {
            steer = steer.mul(1.0 / count);
        }
        return steer;
    }

    public V3 ruleOfAlignment(Boid[] boids) {
        double neighborDist = this.neighborDistance;
        V3 sum = new V3(0, 0, 0);
        int count = 0;

        for (Boid boid : boids) {
            if (boid != this) {
                double distance = Math.sqrt(Math.pow(boid.position.x - this.position.x, 2) + Math.pow(boid.position.y - this.position.y, 2) + Math.pow(boid.position.z - this.position.z, 2));
                if ( distance > 0 && distance < neighborDist) {
                    sum = sum.add(boid.velocity);
                    count++;
                }
            }
        }

        if (count > 0) {
            sum = sum.mul(1.0 / count);
            if (sum.length() != 0){
                sum = sum.unit().mul(this.speed);
            }
            V3 steer = sum.sub(this.velocity);
            return steer;
        } else {
            return new V3(0, 0, 0);
        }
    }

    public V3 ruleOfCohesion(Boid[] boids) {
        double neighborDist = this.neighborDistance;
        V3 sum = new V3(0, 0, 0);
        int count = 0;

        for (Boid boid : boids) {
            if (boid != this) {
                double distance = Math.sqrt(Math.pow(boid.position.x - this.position.x, 2) + Math.pow(boid.position.y - this.position.y, 2) + Math.pow(boid.position.z - this.position.z, 2));
                if (distance > 0 && distance < neighborDist) {
                    sum = sum.add(new V3(boid.position.x, boid.position.y, boid.position.z));
                    count++;
                }
            }
        }

        if (count > 0) {
            sum = sum.mul(1.0 / count);
            return new V3(sum.x - this.position.x, sum.y - this.position.y, sum.z - this.position.z).unit().mul(this.speed);
        } else {
            return new V3(0, 0, 0);
        }
    }

    public V3 steerToCenter() {
        V3 PO = new V3(-this.position.x, -this.position.y, -this.position.z);
        return PO;
    }

    public boolean tooFarAway() {
        return this.position.distanceTo(new V3(0, 0, 0)) > maxDisFromCenter;
    }

    public void AI(Boid[] boids) {
        V3 steerSeparation = new V3(0, 0, 0);
        V3 steerAlignment = new V3(0, 0, 0);
        V3 steerCohesion = new V3(0, 0, 0);
        V3 steerToCenter = new V3(0, 0, 0);

        if (this.ruleOfSeparationOn) {
            steerSeparation = this.ruleOfSeparation(boids);
        }
        if (this.ruleOfAlignmentOn) {
            steerAlignment = this.ruleOfAlignment(boids);
        }
        if (this.ruleOfCohesionOn) {
            steerCohesion = this.ruleOfCohesion(boids);
        }

        if (tooFarAway()){
            steerToCenter = this.steerToCenter();
        }


        this.updatePosition(steerSeparation, steerAlignment,steerCohesion, steerToCenter);
        //this.updateOrientation();
    }

    public void updatePosition(V3 steerSeparation, V3 steerAlignment, V3 steerCohesion, V3 steerToCenter) {
        V3 acceleration = new V3(0, 0, 0);
        acceleration = acceleration.add(steerSeparation.mul(1000)); // Add separation force
        acceleration = acceleration.add(steerAlignment);
        acceleration = acceleration.add(steerCohesion);
        acceleration = acceleration.add(steerToCenter);

        if (acceleration.length() > this.turnSpeed) {
            acceleration = acceleration.unit().mul(this.turnSpeed);
        }

        this.velocity = this.velocity.add(acceleration);

        if (this.velocity.length() > this.maxSpeed) {
            this.velocity = this.velocity.unit().mul(this.maxSpeed);
        }

        this.position.x += this.velocity.x;
        this.position.y += this.velocity.y;
        this.position.z += this.velocity.z;
    }

    public void updateOrientation() {
        if (this.velocity.length() == 0) return;

        V3 newDirection = this.velocity.unit();

        double alpha = Math.atan2(newDirection.y, newDirection.x);
        double beta = Math.atan2(newDirection.z, Math.sqrt(newDirection.x * newDirection.x + newDirection.y * newDirection.y));
        double gamma = 0; // Assuming no roll

        M3 rotationMatrix = new M3(0, 0, 0, 0, 0, 0, 0 ,0 ,0).getRotationMatrix(alpha, beta, gamma);

        // Define the new orientation vectors using the rotation matrix
        this.D = rotationMatrix.mul(new V3(1, 0, 0));
        this.U = rotationMatrix.mul(new V3(0, 1, 0));
        this.R = rotationMatrix.mul(new V3(0, 0, 1));

        // Rotate the vertices according to the new orientation
        for (int i = 0; i < this.vertices.size(); i++) {
            this.vertices.set(i, rotationMatrix.mul(this.vertices.get(i)));
        }
    }
}
