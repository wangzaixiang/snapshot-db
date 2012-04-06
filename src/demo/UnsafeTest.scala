package demo
import scala.util.Random

object UnsafeTest {

  def main(args: Array[String]) {

    val direct = unsafe.UnsafeUtil.getUnsafe()
    val address = direct.allocateMemory(256 * 1024 * 1024)

    try {
      val btree = new BTree
      val loop = 10 * 1024 * 1024
      var i = 0
      var start = System.currentTimeMillis()
      while (i < loop) {
        val userip = new UserIP
        userip.userid = i

        val base = address + 20 * i
        directWrite(direct, userip, base)
        btree.insert(i, base)
        i += 1
        if( (i & 0xFFFFF)==0 )
          println("process " + i + " records")
      }
      var end = System.currentTimeMillis()
      println("write 10M record used " + (end - start))
      
      start = System.currentTimeMillis();
      i = 0
      val userip = new UserIP
      while(i < loop) {
//        val key = Random.nextInt(loop)
        val key = i
        val (leaf, index) = btree.search(i)
        val ptr = leaf.children(index).ptr
        directRead(direct, userip, ptr)
        assert(userip.userid == key)
        i += 1
      }
      end = System.currentTimeMillis()
      println("search " + loop + " record used " + (end - start))

    } finally {
      direct.freeMemory(address)
    }
  }

  def directWrite(direct: sun.misc.Unsafe, userip: UserIP, base: Long) {
    direct.putInt(base, userip.userid)
    direct.putInt(base + 4, userip.ip)
    direct.putShort(base + 8, userip.port)
    direct.putInt(base + 10, userip.serverip)
    direct.putShort(base + 14, userip.serverport)
    direct.putInt(base + 16, userip.timestamp)
  }

  def directRead(direct: sun.misc.Unsafe, userip: UserIP, base: Long) {
    userip.userid = direct.getInt(base)
    userip.ip = direct.getInt(base + 4)
    userip.port = direct.getShort(base + 8)
    userip.serverip = direct.getInt(base + 10)
    userip.serverport = direct.getShort(base + 14)
    userip.timestamp = direct.getInt(base + 16)
  }
}