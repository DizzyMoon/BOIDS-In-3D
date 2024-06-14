// 3D Vector
// Bjørn Christensen, 2/2-2024

public class V3 {
    double x, y, z;

    V3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    V3(){}

    V3 add(V3 v) {
        return new V3(x + v.x, y + v.y, z + v.z);
    }

    V3 sub(V3 v) {
        return new V3(x - v.x, y - v.y, z - v.z);
    }

    V3 mul(double d) {
        return new V3(d * x, d * y, d * z);
    }

    double distanceTo(V3 other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double dz = this.z - other.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }



    double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    V3 unit() {
        double l = length();
        return new V3(x / l, y / l, z / l);
    }

    double dot(V3 v) {
        return x * v.x + y * v.y + z * v.z;
    }

    V3 cross(V3 v) {
        return new V3(y * v.z - z * v.y,
                z * v.x - x * v.z,
                x * v.y - y * v.x);
    }

    public String toString() {
        return "("+x+","+y+","+z+")";
    }

    public static void main(String[] args) {
        System.out.println("Test V3");
        V3 a = new V3(1, 1, 2);
        V3 b = new V3(2, -1, 1);
        System.out.println("a=" + a);
        System.out.println("a.add(a)=" + a.add(a));
        System.out.println("a.sub(a)=" + a.sub(a));
        System.out.println("a.mul(3)=" + a.mul(3));
        System.out.println("a.mul(-1)=" + a.mul(-1));
        System.out.println("a.dot(a)=" + a.dot(a));
        System.out.println("a.cross(b)=" + a.cross(b));
    } // main()}
}