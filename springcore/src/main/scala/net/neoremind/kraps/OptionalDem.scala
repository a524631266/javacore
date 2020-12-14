package net.neoremind.kraps

import java.util.Optional
;

/**
  * Optional是java8 新增的一个项目
  * 利用optional机制可以有效放置空指针，或者在下游使用会有一个警示作用！！表示这个返回值可能
  * 为null，因此
  *  ctrl + shift +G java --> scala代码的转化
  * 哪里有用到optional
  */
object OptionalDem {
  def main(args: Array[String]): Unit = {
    val data = "bada"
    val integer = Optional.of(1)
    System.out.println(integer.isPresent)
    val data1 = Optional.ofNullable(null)
    if (data1.isPresent) {
      val s = data1.get
      println(s)
    }
    else System.out.println("empty list")
    val apply = Option.apply(10)
    val value = apply.get
    println(value)
  }
}
