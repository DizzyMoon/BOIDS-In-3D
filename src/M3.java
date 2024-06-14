// 3x3 Matrix
// Bjørn Christensen, 2/2-2024

public class M3 {
    double a11, a12, a13;       // first index is row no,
    double a21, a22, a23;       // second index is column no.
    double a31, a32, a33;

    M3(double a11, double a12, double a13, double a21, double a22, double a23, double a31, double a32, double a33){
        this.a11=a11;
        this.a12=a12;
        this.a13=a13;
        this.a21=a21;
        this.a22=a22;
        this.a23=a23;
        this.a31=a31;
        this.a32=a32;
        this.a33=a33;
    }

    M3 getRotationMatrix(double alpha, double beta, double gamma) {
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

        return rotationMatrix;
    }

    M3(V3 r1, V3 r2, V3 r3) {     // matrix from 3 row vectors
        a11=r1.x; a12=r1.y; a13=r1.z;
        a21=r2.x; a22=r2.y; a23=r2.z;
        a31=r3.x; a32=r3.y; a33=r3.z;
    }

    M3 add(M3 m){       // matrix addition
        return new M3(  a11+m.a11, a12+m.a12, a13+m.a13,
                        a21+m.a21, a22+m.a22, a23+m.a23,
                        a31+m.a31, a32+m.a32, a33+m.a33);
    }

    M3 sub(M3 m){       // matrix subtraction
        return new M3(  a11-m.a11, a12-m.a12, a13-m.a13,
                        a21-m.a21, a22-m.a22, a23-m.a23,
                        a31-m.a31, a32-m.a32, a33-m.a33);
    }

    M3 mul(double d){    // scalar multiplication
        return new M3(  d*a11, d*a12, d*a13,
                        d*a21, d*a22, d*a23,
                        d*a31, d*a32, d*a33);
    }

    V3 mul(V3 v){        // matrix * vector multiplication
        return new V3(  a11*v.x+a12*v.y+a13*v.z,
                        a21*v.x+a22*v.y+a23*v.z,
                        a31*v.x+a32*v.y+a33*v.z);
    }

    M3 mul(M3 m){        // matrix multiplication
        return new M3(  a11*m.a11+a12*m.a21+a13*m.a31, a11*m.a12+a12*m.a22+a13*m.a32, a11*m.a13+a12*m.a23+a13*m.a33,
                        a21*m.a11+a22*m.a21+a23*m.a31, a21*m.a12+a22*m.a22+a23*m.a32, a21*m.a13+a22*m.a23+a23*m.a33,
                        a31*m.a11+a32*m.a21+a33*m.a31, a31*m.a12+a32*m.a22+a33*m.a32, a31*m.a13+a32*m.a23+a33*m.a33);
    }

    public String toString() {
        return "[("+a11+","+a12+","+a13+"),("+a21+","+a22+","+a23+"),("+a31+","+a32+","+a33+")]";
    }

    public static void main(String[] args) {
        System.out.println("Ny Test M3 - 3x3 matrix");

        M3 A=new M3(1,-1,1, -2,0,3, 0,1,-2);
        M3 B=new M3(0,0,1, 0,2,3, 1,2,3);
        M3 C=new M3(1,2,2, 0,1,1, 1,0,1);
        V3 v=new V3(1,0,1);

        System.out.println("A="+A);
        System.out.println("B="+B);
        System.out.println("C="+C);
//        System.out.println("A.transpose()="+A.transpose());
        System.out.println("A.add(B)="+A.add(B));
        System.out.println("A.sub(B)="+A.sub(B));
        System.out.println("A.mul(2)="+A.mul(2));
        System.out.println("A.mul(B)="+A.mul(B));
        System.out.println("C.mul(v)="+C.mul(v));

        System.out.println("Change of basis");
        V3 X=new V3(1,1,1);
        V3 E=new V3(0,0,0);
        double phi=Math.PI/4;
        V3 D=new V3(Math.cos(phi),Math.sin(phi),0);
        V3 U=new V3(-Math.sin(phi),Math.cos(phi),0);
        V3 R=D.cross(U);
        M3 M=new M3(D,U,R);
        V3 Y=M.mul(X.sub(E));
        System.out.println("X="+X);
        System.out.println("E="+E);
        System.out.println("D="+D);
        System.out.println("U="+U);
        System.out.println("R="+R);
        System.out.println("M="+M);
        System.out.println("Y="+Y);

        M3 m1=new M3(1,2,3,4,5,6,7,8,9);
        M3 m2=new M3(9,8,7,6,5,4,3,2,1);
        M3 m3;
        final int N=10000000;
        double start=System.currentTimeMillis();
        for (int i=0; i<N; i++){
            m3=m1.mul(m2);
        }
        double stop=System.currentTimeMillis();
        double dt=(stop-start)/1000;
        System.out.println("dt="+dt+" s");
        System.out.println("N/dt="+(int)(N/dt));



    } // main()
}
