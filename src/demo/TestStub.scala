package demo

object TestStub {

  def main(args: Array[String]) {

    val table: Table = null
    Range(0, 10000).foreach { it=>
      val userip = new UserIP
      val record:Record = null	// TODO util to convert POJO to record
      table.insert( record )
    }
    
    val ip = 100;
    table.foreach { rec=>
      rec("ip") == ip
    }
  }
}