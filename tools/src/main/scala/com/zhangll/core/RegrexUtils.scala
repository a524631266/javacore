package com.zhangll.core

import java.util.regex.Pattern

/**
  * 一个由10位数字组成的字符串（如：0123456789），如何找到一个正则表达式，表示在 1324736000 到 1546272000之间的数
  */
object RegrexUtils {
  def compute(start: String, end: String): Map[String, String] = {
    val len = start.length
    if(start == end) {
      Map("prex" -> s"${start}","left" -> s"","middle" -> s"", "right"-> s"")
    } else {
      if(len == 1) {
        Map("prex" -> "","left" -> s"${start}","middle" -> s"[${start}${end}]", "right"-> s"${end}")
      } else {
        // 算法从左中右三种算法实现
        var prex = ""
        var left = ""
        var middle = "["
        var right = ""
        var equallen =0
        // 提取前面重复的前缀
        while(start(equallen) == end(equallen)) {
          prex += start(equallen)
          equallen += 1
        }
        val restlen = len - equallen
        // 有没有两个数据一摸一样的？
        if(equallen != len ) {
          val startpoint = (start(equallen)+"").toInt
          val endpoint =(end(equallen)+"").toInt
          // 这两个点一定不相同
          // 这里当间隔大于等于2个的时候
          val leftstart = start.substring(equallen)
          val rightend = end.substring(equallen)
          var leftend = ""
          var rightstart = ""
          // 只有一个的时候
          if(restlen == 1) {
            middle  += startpoint + "-" + endpoint + "]"
          } else{
            if(endpoint - startpoint > 1) {
              middle+=s"${startpoint+1}-${endpoint-1}]\\d{${restlen - 1}}"
            } else {
              middle = ""
            }
            // 2. 计算左值
            // 123456  19999
            leftend = startpoint + "9" * (restlen - 1)
            rightstart = endpoint + "0" * (restlen - 1)
            val leftmap = compute(leftstart,leftend)
            val rightmap = compute(rightstart, rightend)
            left += formatResult(leftmap)
            right += formatResult(rightmap)
          }
        } else {
          prex = start
        }
        //        val regx = prex + "("+ left + "|" + middle + "|" + right + ")"
        Map("prex" -> prex,"left" -> left,"middle" -> middle, "right" -> right)
      }
    }
  }
  def formatResult(map: Map[String,String])= {
    val leftstring = {
      if(s"${map.getOrElse("left","")}"!="") {
        s"(${map.getOrElse("left","")})|"
      }  else {
        ""
      }
    }
    val middlestring = {
      if(s"${map.getOrElse("middle","")}"!="") {
        s"(${map.getOrElse("middle","")})|"
      }  else {
        ""
      }
    }
    val rightstring = {
      if(s"${map.getOrElse("right","")}"!="") {
        s"(${map.getOrElse("right","")})"
      }  else {
        ""
      }
    }
    var tuple = s"${leftstring}${middlestring}${rightstring}"
    if(tuple.endsWith("|")){
      tuple = tuple.substring(0,tuple.length-1)
    }
    if(tuple == "") {
      s"${map.getOrElse("prex","")}"
    } else {
      s"${map.getOrElse("prex","")}(${tuple})"
    }
  }
  def timeRangeRegrex(starttime: String, endtime: String):String = {
    val startlen = starttime.length
    val endlen = endtime.length
    var maxlen = startlen
    if(maxlen < endlen) {
      maxlen = endlen
    }
    val newstarttime: String = String.format(s"%0${maxlen}d",java.lang.Long.valueOf(starttime))
    val newendtime: String = String.format(s"%0${maxlen}d",java.lang.Long.valueOf(endtime.toLong))
    formatResult(compute(newstarttime,newendtime))
  }
  def main(args: Array[String]): Unit = {
      val starttime = "1324736000"
      val endtime = "1546272000"

//    val starttime = "1"
//    val endtime = "52"
//    val data = compute(starttime,endtime)
      val regrex = RegrexUtils.timeRangeRegrex(starttime,endtime)
      println(regrex)
      val pattern = Pattern.compile(regrex)

      for(a <- starttime.toLong until  (endtime.toLong+1)){
        val str = "%010d".format(a)
        val matcher = pattern.matcher(str)
        val find = matcher.find()
        if(!find){
          println(str)
        }
        assert(find);
      }

  }


}
