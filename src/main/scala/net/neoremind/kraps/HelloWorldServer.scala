package net.neoremind.kraps
import net.neoremind.kraps.rpc._
import net.neoremind.kraps.rpc.netty.NettyRpcEnvFactory
import net.neoremind.kraps.rpc.{RpcEnv, RpcEnvServerConfig}

object HelloWorldServer {
  def main(args: Array[String]): Unit = {
    // 配置服务参数
    val conf = new RpcConf()
    val serverConfig: RpcEnvServerConfig = new RpcEnvServerConfig(conf, "hello-server", "localhost", 52345)
    val rpcEnv: RpcEnv = NettyRpcEnvFactory.create(serverConfig)
    // 在server Env中配置EndPoint
    val helloEndpoint: HelloEndpoint = new HelloEndpoint(rpcEnv)
    rpcEnv.setupEndpoint("hello-service", helloEndpoint)
    rpcEnv.awaitTermination()
  }
}