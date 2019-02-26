package book_shizhan.ch6;

import java.util.function.Function;
import java.util.stream.IntStream;

public class MoreLambda {

    public static void test(){
        int num = 2;

        Function<Integer,Integer> mult2 = (i) -> i*num;
        // num++;   (variable一旦被lambda闭包引用之后，该object就被视为final) // 否则函数对象的语义会被不断变化？

        System.out.println(mult2.apply(9));
    }


    public static boolean isPrime(int num){
        return false;
    }

    public static void testParallel(){
        // 1~10000 中所有质数的个数
        Long cnt = IntStream.range(1,10000).parallel().filter(MoreLambda::isPrime).count();

        // 卧槽，map化以后不能改变类型啊？！
        // IntStream.range(1,10000).map(MoreLambda::isPrime).count();
        IntStream.range(1,10000).mapToObj(MoreLambda::isPrime).count();   //TODO mapToObj即可
    }
}
