package net.neoremind.kraps

import net.neoremind.kraps.rpc.{RpcCallContext, RpcEndpoint, RpcEnv}

import net.neoremind.kraps.rpc._
/**
  * 用于装入serve以及client端口
  * @param rpcEnv
  */
class HelloEndpoint(val rpcEnv: RpcEnv) extends scala.AnyRef with RpcEndpoint {

//  def this() = {
//    this()(null)
//  }

  override def onStart(): Unit = {
    println(s"${rpcEnv.address} ::: start  hello  ")
  }

  override def receiveAndReply(context: RpcCallContext): PartialFunction[Any, Unit] = {
    case SayHi(msg) =>{
      println(s"recieve $msg")
      context.reply(s"hi , $msg")
    }
    case SayBye(msg:String) => {
      println(s"recieve sayBay messge: $msg")
      context.reply(s"bye  $msg")
    }
  }

  override def onStop(): Unit = {
    println("stop hello service")
  }

}


/**
  * 根据不同class 模板类型恢复数据
  * @param msg
  */
case class SayHi(msg: String)
case class SayBye(msg: String)