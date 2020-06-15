package net.neoremind.kraps

import net.neoremind.kraps.rpc.netty.NettyRpcEnvFactory
import net.neoremind.kraps.rpc.{RpcAddress, RpcEnv, RpcEnvClientConfig}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

object HelloWorldClient {



  def main(args: Array[String]): Unit = {
    val rpcConf = new RpcConf()

    val rpcEnv: RpcEnv = NettyRpcEnvFactory.create(new RpcEnvClientConfig(rpcConf,"hello-client"))
    // 配置引用的EndPoint，指定远程服务端口和名称,返回一个引用，与服务端进行沟通通信
    var address: RpcAddress = RpcAddress("localhost", 52345)
    val endpointRef = rpcEnv.setupEndpointRef(address, "hello-service")
    println("123123")
    val future = endpointRef.ask(SayHi("hhhh"))

    Await.result(future, Duration("30s"))

    import scala.concurrent.ExecutionContext.Implicits.global
    future.onComplete{
      case Success(value) => println(s"client recieve $value")
      case Failure(e) => println(s"Got error: $e")
    }
  }
}
