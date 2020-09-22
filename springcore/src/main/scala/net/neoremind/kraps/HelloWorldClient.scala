package net.neoremind.kraps

import net.neoremind.kraps.rpc.netty.NettyRpcEnvFactory
import net.neoremind.kraps.rpc.{RpcAddress, RpcEnv, RpcEnvClientConfig, RpcTimeout}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

object HelloWorldClient {



  def main(args: Array[String]): Unit = {
    val rpcConf = new RpcConf()
//    rpcConf.set()
    val rpcEnv: RpcEnv = NettyRpcEnvFactory.create(new RpcEnvClientConfig(rpcConf,"hello-client"))
    // 配置引用的EndPoint，指定远程服务端口和名称,返回一个引用，与服务端进行沟通通信
    var address: RpcAddress = RpcAddress("localhost", 52345)
    // 创建一个远程的EndPointRef，并通过EndPointVerifier来验证是否有指定的EndPoint
    val endpointRef = rpcEnv.setupEndpointRef(address, "hello-service")
    // 异步操作
    val future = endpointRef.ask[String](SayHi("hello im client"))

    // 同步操作 ，即会同步堵塞线程
//    println("end future")
//    val response: String = endpointRef.askWithRetry[String](SayHi("hello im client asking with retrey"),
//      RpcTimeout(rpcConf, "spark.executor.heartbeatInterval", "10s"))
    import scala.concurrent.ExecutionContext.Implicits.global
    future.onComplete{
      case Success(value) => {
        println(s"client recieve $value")
      }
      case Failure(e) => println(s"Got error: $e")
    }
    Await.result(future, Duration("30s"))

//  ================================第二次=====================================
    val future2 = endpointRef.ask[String](SayHi("hello im client222"))
    future.onComplete{
      case Success(value) => {
        println(s"client recieve $value")
      }
      case Failure(e) => println(s"Got error: $e")
    }
    Await.result(future, Duration("30s"))


//    println(response)
  }
}
