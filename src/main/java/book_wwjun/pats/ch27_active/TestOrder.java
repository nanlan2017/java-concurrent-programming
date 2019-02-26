package book_wwjun.pats.ch27_active;

import top.MyUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;



public class TestOrder {
}

class ActiveMessageQueue {
    public void offer(MethodMessage msg){

    }
}

//class ActiveFuture<T> implements Future<T> {
//    @Override
//    public boolean cancel(boolean mayInterruptIfRunning){
//        return false;
//    }
//}

interface MethodMessage{

}
class FindDetailInfo_MethodMsg implements MethodMessage{
    private Map params;
    private OrderService service;

    public FindDetailInfo_MethodMsg(Map params, OrderService service){
        this.params = params;
        this.service = service;
    }
}

class Order_MethodMsg implements MethodMessage{
    private Map params;
    private OrderService service;

    public Order_MethodMsg(Map params, OrderService service){
        this.params = params;
        this.service = service;
    }
}

interface OrderService{
    // 有返回值： 根据订单编号查询订单信息
    Future<String> findOrderDetail(long orderId);
    // 无返回值： 提交用户的订单信息
    void order(String account,long orderId);
}

class OrderServiceImpl implements OrderService{
    @Override
    public Future<String> findOrderDetail(long orderId){

        Callable<String> task = ()->{
            MyUtils.sleepForSeconds(3);
            System.out.println("Acquiring detailed info of the orderID : " + orderId);
            return "XXX-00-Detailed info-";
        };

        return Executors.newFixedThreadPool(1).submit(task);
    }

    @Override
    public void order(String account,long orderId) {
        MyUtils.sleepForSeconds(10);
        System.out.println("Process the order for account : "+account + " , order-id : " + orderId);
    }
}


class OrderServiceProxy implements OrderService{
    private final OrderService orderService;
    private final ActiveMessageQueue msgQueue;

    public OrderServiceProxy(OrderService service,ActiveMessageQueue q){
        this.orderService = service;
        this.msgQueue = q;
    }

    @Override
    public Future<String> findOrderDetail(long orderId){

        /*
        final ActiveFuture<String> activeFuture = new ActiveFuture<>();

        Map<String,Object> params = new HashMap<>();
        params.put("orderId",orderId);
        params.put("activeFuture",activeFuture);

        MethodMessage msg = new FindDetailInfo_MethodMsg(params,orderService);
        msgQueue.offer(msg);
        return activeFuture;
        */
        return null;
    }

    @Override
    public void order(String account,long orderId) {

        Map<String,Object> params = new HashMap<>();
        params.put("orderId",orderId);
        params.put("account",account);

        MethodMessage msg = new Order_MethodMsg(params,orderService);
        msgQueue.offer(msg);

    }
}
