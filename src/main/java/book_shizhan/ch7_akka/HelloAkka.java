package book_shizhan.ch7_akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.typesafe.config.ConfigFactory;

public class HelloAkka {
    public static void test(){
        ActorSystem system = ActorSystem.create("HelloSystem", ConfigFactory.load("samplehello.conf"));

        ActorRef helloer = system.actorOf(Props.create(Helloer.class),"helloer");
        System.out.println("Helloer-Actor path :" + helloer.path());
    }

}

enum Msg {
    GREET, DONE
}

class Greeter extends UntypedActor {

    @Override
    public void onReceive(Object msg) {
        //TODO    收到'GREET'则回复'DONE'
        if (msg == Msg.GREET) {
            System.out.println("Hello World");
            getSender().tell(Msg.DONE, getSelf());
        } else {
            unhandled(msg);
        }
    }
}

class Helloer extends UntypedActor {
    ActorRef greeter;

    //TODO    启动前：创建greeter、向其发送'GREET'
    @Override
    public void preStart() {
        greeter = getContext().actorOf(Props.create(Greeter.class), "greeter");
        System.out.println("Greeter-Actor path :" + greeter.path());
        greeter.tell(Msg.GREET, getSelf());
    }

    @Override
    public void onReceive(Object msg) {
        //TODO  收到'DONE'则向greeter说'GREET'，随后自己就停止
        if (msg == Msg.DONE) {
            greeter.tell(Msg.GREET,getSelf());
            getContext().stop(getSelf());
        } else{
            unhandled(msg);
        }
    }
}