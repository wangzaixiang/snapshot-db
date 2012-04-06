package demo

class BTreeNodeLeaf(val btree:BTree) extends BTreeNode {

  class Child {
    var key: Int = _ // prototype support only int key
    var ptr: Long = _
    
    override def toString  = "" + key
  }

  val children = new Array[Child](BTree.CountPerNode)
  var count = 0
  var parent: BTreeNodeInternal = _
  var next: BTreeNodeLeaf = _

  def search(key: Int): (BTreeNodeLeaf, Int) = {    
    var l = 0
    var r = count-1
    while(l <= r) {
      val m = (l + r) / 2
      val it = children(m).key
      if(key == it) return (this,m);
      else if(key < it) {
        r = m - 1
      }
      else {
        l = m + 1
      }
    }
    return (this, l)
    
//    for (i <- Range(0, count)) {
//      val node = children(i)
//      if (node.key >= key)
//        return (this, i)
//    }
//    return (this, count)

  }

  def insertBefore(key: Int, ptr: Long, index: Int) {

    if (index < count) {
      assert(children(index).key != key, "dupicated key")
    }

    if (count < BTree.CountPerNode) {
      System.arraycopy(children, index, children, index + 1, count - index)
      val child = new Child()
      child.key = key
      child.ptr = ptr
      children(index) = child
      count += 1
      return
    }

    if (parent == null) {	// the root node
      val parent = new BTreeNodeInternal(this)
      btree.root = parent;
      this.parent = parent;
    }

    var oldNext = this.next
    val right = parent.split(this).asInstanceOf[BTreeNodeLeaf]
    this.next = right
    right.next = oldNext
    
    if (index < BTree.HalfCountPerNode) {
      insertBefore(key, ptr, index)
      return
    } else {
      right.insertBefore(key, ptr, index - BTree.HalfCountPerNode)
      return
    }

  }
  
  def getMaxKey = children(count-1).key

}