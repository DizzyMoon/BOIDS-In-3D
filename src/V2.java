// 2D Vector
// Bjørn Christensen, 2/2-2024

public class V2 {
    double x,y;

    V2(double x, double y){
        this.x=x;
        this.y=y;
    }

    V2 add(V2 v){
        return new V2(x+v.x, y+v.y);
    }

    V2 sub(V2 v){
        return new V2(x-v.x, y-v.y);
    }

    V2 mul(double d){
        return new V2(d*x, d*y);
    }

    V2 div(double k){
        return new V2(x/k, y/k);
    }

    double dot(V2 v){
        return x*v.x+y*v.y;
    }

    double length(){
        return Math.sqrt(x*x+y*y);
    }

    V2 unit(){
        double l=length();
        return new V2(x/l, y/l);
    }
    @Override
    public String toString() {
        return "("+x+","+y+")";
    }

    public static void main(String[] args) {
        System.out.println("Test V2 - 2D vector");
        V2 v=new V2(-4,5);
        System.out.println("v="+v);
        System.out.println("v*3="+v.mul(3));

        System.out.println("v.length()*v.length()="+v.length()*v.length());
        System.out.println("v.dot(v)="+v.dot(v));
    }
}
