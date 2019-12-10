package rsa;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class RSA {
    final static BigInteger b216=BigInteger.valueOf((long) Math.pow(2,16));
    public static boolean MR(BigInteger n){
        Scanner scan=new Scanner(System.in);
        BigInteger two = BigInteger.valueOf(2);
        BigInteger d=n.subtract(BigInteger.ONE);
        BigInteger s=BigInteger.ZERO;
        while(d.mod(two)==BigInteger.ZERO){
            d=d.divide(two);
            s=s.add(BigInteger.ONE);
        }
        System.out.println("Введите количество тестов для него для проверки "+n
                +" на простоту");
        BigInteger r=scan.nextBigInteger().add(two);
        BigInteger a=two;
        boolean isprime=true;
        while(a.compareTo(r)==-1){
            BigInteger x=a;
            x=x.modPow(d, n);
            if(x.equals(BigInteger.ONE)||x.equals(n.subtract(BigInteger.ONE))){
                a=a.add(BigInteger.ONE);
                continue;
            }
            BigInteger i= BigInteger.ONE;
            while(i.compareTo(s)==-1){
                x=x.modPow(two,n);
                if(x.equals(BigInteger.ONE)){
                    isprime=false;
                    break;
                }
                if(x.equals(n.subtract(BigInteger.ONE))){
                    break;
                }
                i=i.add(BigInteger.ONE);
            }
            if(!x.equals(n.subtract(BigInteger.ONE))){
                isprime=false;
            }
            if(!isprime){
                break;
            }
            a=a.add(BigInteger.ONE);
        }
        return isprime;
    }
    public static BigInteger REVklid(BigInteger a, BigInteger b){
        if(a.gcd(b).compareTo(BigInteger.ONE)!=0){
            return BigInteger.ONE.negate();
        }
        final BigInteger a1=a;
        //Stage 1
        BigInteger c;
        ArrayList<BigInteger> divs=new ArrayList();
        do{
            divs.add(a.divide(b)); // a div b
            c=b;
            b=a.mod(b); //b(i+1)=a(i) mod b(i)
            a=c; //a(i+1)=b(i)
        }
        while(b.compareTo(BigInteger.ZERO)==1);
        //Stage 2
        BigInteger x=BigInteger.ZERO;
        BigInteger y=BigInteger.ONE;
        for(int i=divs.size()-1;i>0;i--){
            c=x;
            x=y; //x(i-1)=y(i)
            y=c.subtract(y.multiply(divs.get(i-1))); //y(i-1)=x(i)-y(i)*(a(i-1) div b(i-1))
        }
        if(y.compareTo(BigInteger.ZERO)==-1){
            y=y.add(a1);
        }
        return y;
    }
    public static ArrayList<BigInteger> cipher1(String s, BigInteger e, BigInteger n){
        BigInteger k;
        ArrayList <BigInteger> ciph = new ArrayList<>();
        int i=0;
        while(i<s.length()){
            k=BigInteger.ZERO;
            do{
                k=k.add(BigInteger.valueOf((long) s.charAt(i)));
                k=k.multiply(b216);
                i++;
            }while(i<s.length()&&(i%3!=0||i==0));
            k=k.modPow(e,n);
            ciph.add(k); 
        }
        return ciph;
    }
    public static String cipher2(ArrayList<BigInteger> ciph, BigInteger d, BigInteger n){
        String s="";
        for(BigInteger k: ciph){
            String s1 = new String();
            k=k.modPow(d, n);
            k=k.divide(b216);
            while(k.compareTo(BigInteger.ZERO)>0){
                char c=(char) k.mod(b216).longValue();
                System.out.println(c+""+k);
                s1+=c;
                k=k.divide(b216);
            }
            s1=new StringBuffer(s1).reverse().toString();
            s+=s1;
        }
        return s;
    }
    public static void main(String[] args) {
        //n=p*q
        BigInteger p,q;
        Random rnd = new Random(System.currentTimeMillis());
        Scanner scan = new Scanner(System.in);
        System.out.println("Введите случайные простые p и q:");
        System.out.println("Введите p");
        p=scan.nextBigInteger();
        if(!MR(p)){
            System.out.println("p не простое! Выход из приложения...");
            System.exit(0);
        }
        System.out.println("Введите q");
        q=scan.nextBigInteger();
        if(!MR(q)){
            System.out.println("q не простое! Выход из приложения...");
            System.exit(0);
        }
        BigInteger n=q.multiply(p);
        BigInteger phin=q.subtract(BigInteger.ONE).multiply(p.subtract(BigInteger.ONE));
        BigInteger e = BigInteger.valueOf(rnd.nextLong()).abs();
        while(e.gcd(phin).compareTo(BigInteger.ONE)!=0){
            e=BigInteger.valueOf(rnd.nextLong()).abs();
        }
        BigInteger d=REVklid(phin,e);
        //h=c^e(mod n)
        System.out.println("Введите слово или фразу");
        scan=new Scanner(System.in);
        String s = scan.nextLine();
        ArrayList<BigInteger> ciph = cipher1(s,e,n);
        s=cipher2(ciph,d,n);
        System.out.println(s);
    }    
}
