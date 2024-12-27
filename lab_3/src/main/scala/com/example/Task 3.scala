package lab_3.src.main.scala.com.example

import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object ActorIntegral:
    case class Message(f:Double => Double, l:Double, r:Double, steps:Int, replyTo:ActorRef[Double])

    def apply():Behavior[Message] = Behaviors.receive{ (context, message)  =>
        val h = (message.r-message.l)/message.steps
        val integral = (1 to message.steps).map(i => message.f(message.l+i*h) * h).sum
        
        context.log.info(s"Result: $integral")

        message.replyTo ! integral
        Behaviors.same
    }

    // Объект, отвечающий за агрегацию частичных результатов
object ActorSum:
    def apply(maxSteps: Int, replyTo: ActorRef[Double]):Behavior[Double] = Behaviors.setup{ context =>
        //
        def summation(sum:Double, count: Int):Behavior[Double] = Behaviors.receiveMessage{ message =>
            if (count > 1) {
                summation(sum + message, count-1)
            } else {
                replyTo ! sum + message
                Behaviors.stopped
            }
        }
        summation(0.0, maxSteps)
    }

object integralSystem:
    case class Integrals(f: Double => Double, l: Double, r: Double, steps: Int, tasks: Int,replyTo: ActorRef[Double])
    
    def apply():Behavior[Integrals]= Behaviors.setup{ context =>
        val integrateActors = Seq(context.spawn(ActorIntegral(), "actorIntegral0"),
                                    context.spawn(ActorIntegral(), "actorIntegral1"),
                                    context.spawn(ActorIntegral(), "actorIntegral2"),
                                    context.spawn(ActorIntegral(), "actorIntegral3"))
        val numActors = integrateActors.length
        
        Behaviors.receiveMessage {
            case Integrals (f, l, r, steps, tasks, replyTo) =>
                val sumActor = context.spawn(ActorSum(tasks, replyTo), "SumActor")
                val stepSize = (r - l)/tasks
                
                (0 until tasks).foreach { i =>
                    val left = l + i * stepSize
                    val right = left + stepSize
                    integrateActors(i % integrateActors.length) ! ActorIntegral.Message(f, left, right, steps/tasks, sumActor)
                }
            Behaviors.same
        }
    }

object ResultLogger:
    def apply(): Behavior[Double] = Behaviors.receive { (context, result) =>
        context.log.info(s"received Double:: $result")
        Behaviors.same
    }

def f(x: Double): Double = math.sin(x) * math.exp(-x)

@main def main():Unit = {
    val system = ActorSystem(integralSystem(), "integralSystem")
    val resLog = ActorSystem(ResultLogger(), "resultLogger")

    system ! integralSystem.Integrals(f, 1.0, 4.0, 500, 10, resLog)
}