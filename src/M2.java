public class M2 {
    double a,b;
    double c,d;

    M2(double a, double b, double c, double d){
        this.a=a;
        this.b=b;
        this.c=c;
        this.d=d;
    }

    M2 add(M2 m){    // matrix addition
        return new M2(this.a+m.a, this.b+m.b,
                this.c+m.c, this.d+m.d);
    }

    M2 sub(M2 m){    // matrix subtraction
        return new M2(this.a-m.a, this.b-m.b,
                this.c-m.c, this.d-m.d);
    }

    M2 mul(double k){    // scalar multiplication
        return new M2(a*k, b*k,
                c*k, d*k);
    }

    V2 mul(V2 v){        // matrix * vector multiplication
        return new V2(a*v.x+b*v.y,
                c*v.x+d*v.y);
    }

    M2 mul(M2 m){        // matrix multiplication
        return new M2( a*m.a+b*m.c, a*m.b+b*m.d,
                c*m.a+d*m.c, c*m.b+d*m.d);
    }


    double det(){       // Determinant
        return a*d-b*c;
    }

    M2 adj(){           // Adjungatet Matrix
        return new M2( d,-b,
                -c, a);
    }

    M2 inv(){           // Inverse
        if (det()==0) throw new ArithmeticException("Matrix "+this+" has no inverse.");
        return adj().mul(1.0/det());
    }

    public String toString() {
        return "("+a+","+b+","+c+","+d+")";
    }

    public static void main(String[] args) {
        M2 E=new M2(1,0,0,1);     // 2x2 unit-matrix
        System.out.println("Test M2 - 2x2 matrix");
        System.out.println("E="+E);
        M2 m=new M2(1,2,3,4);
        System.out.println("m="+m);
        System.out.println("m+E="+m.add(E));
        System.out.println("m-E="+m.sub(E));
        System.out.println("m*3="+m.mul(3));

        V2 v=new V2(2,3);
        System.out.println("E*"+v+"="+E.mul(v));

        System.out.println("E*"+m+"="+E.mul(m));
    } // main()
}
