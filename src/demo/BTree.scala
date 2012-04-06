package demo

object BTree {
  val CountPerNode = 512
  val HalfCountPerNode = CountPerNode/2
}

class BTree {
  
  var root: BTreeNode = new BTreeNodeLeaf(this)
  val firstLeaf: BTreeNodeLeaf = root.asInstanceOf[BTreeNodeLeaf]
  
  def search(key: Int) = root.search(key)
  def insert(key: Int, ptr:Long) {
    val (node, index) = search(key)    
    node.insertBefore(key, ptr, index)    
  }
  
}