package demo

class BTreeNodeInternal(val btree:BTree) extends BTreeNode {

  class Child {
    var key: Int = _	// prototype support only int key
    var leaf: BTreeNode = _	// either BTreeNodeInternal or BTreeNodeLeaf
    
    def this(key: Int, leaf: BTreeNode) = {
      this()
      this.key = key
      this.leaf = leaf
    }
    
    override def toString = "(" + leaf.count + ")" + key
  }
  
  val children = new Array[BTreeNodeInternal#Child](BTree.CountPerNode)
  var count = 0
  var parent: BTreeNodeInternal = _
  
  def getMaxKey = children(count-1).key
  
  def this(childNode:BTreeNode) = {
    this(childNode.btree)
    val child = new Child(childNode.getMaxKey, childNode)
    children(count) = child
    count += 1
    childNode.parent = this
  }
  
  def search(key: Int): (BTreeNodeLeaf, Int) = {

    var l = 0
    var r = count-1
    while(l <= r) {
      val m = (l + r) / 2
      val child = children(m)
      val it = child.key
      if(key == it) return child.leaf.search(key);
      else if(key < it) {
        r = m - 1
      }
      else {
        l = m + 1
      }
    }
    if(l == count) l -= 1
    return children(l).leaf.search(key)
    
//    for(i<-Range(0,count)){
//      val node = children(i)
//      if(node.key >= key)
//        return node.leaf.search(key)
//    }
//    return children(count-1).leaf.search(key)
  }
  
  private def searchChild(key: Int): BTreeNodeInternal#Child = {
    var l = 0
    var r = count-1
    while(l <= r) {
      val m = (l + r) / 2
      val it = children(m)
      if(key == it.key) return it
      else if(key < it.key) {
        r = m - 1
      }
      else {
        l = m + 1
      }
    }
    return children(count-1)	// return the last node
//    throw new AssertionError("no key " + key + " found")
  }
    
  def split(childNode: BTreeNode) : BTreeNode  = {
        
    childNode match {
      case x:BTreeNodeLeaf =>
//        val Some(child) = children.find(it => it != null && it.leaf == x)
        val child = searchChild(x.getMaxKey)
        
        val right = new BTreeNodeLeaf(childNode.btree)
        right.parent = this
        System.arraycopy(x.children, BTree.HalfCountPerNode, right.children, 0 , BTree.HalfCountPerNode);
        right.count = BTree.HalfCountPerNode
        childNode.count = BTree.HalfCountPerNode
        for(i<-Range(BTree.HalfCountPerNode,BTree.CountPerNode)){
          x.children(i) = null
          right.children(i) = null
        }
        
        child.key = x.getMaxKey
        val newchild = new Child(right.getMaxKey, right)
        addChildAfter(newchild, child)
        right
        
      case x:BTreeNodeInternal =>
//        val Some(child) = children.find(it => it != null && it.leaf == x)
        val child = searchChild(x.getMaxKey)
        
        val right = new BTreeNodeInternal(childNode.btree)
        right.parent = this
        System.arraycopy(x.children, BTree.HalfCountPerNode, right.children, 0 , BTree.HalfCountPerNode);
        right.count = BTree.HalfCountPerNode
        childNode.count = BTree.HalfCountPerNode
        for(i<-Range(0, right.count)) {	// update parent
          right.children(i).leaf.parent = right
        }
        for(i<-Range(BTree.HalfCountPerNode,BTree.CountPerNode)){
          x.children(i) = null
          right.children(i) = null
        }
        
        child.key = x.getMaxKey
        val newchild = new Child(right.getMaxKey, right)
        addChildAfter(newchild, child)
        right
    }
  }
  
  private def addChildAfter(newChild: BTreeNodeInternal#Child, old: BTreeNodeInternal#Child) {
    val index = children.indexOf(old) + 1
    if(count < BTree.CountPerNode) {
      System.arraycopy(children, index, children, index+1, count-index)
      children(index) = newChild
      newChild.leaf.parent = this;	//
      count += 1
      return
    }
    
    // need to split
     if (parent == null) {
      val parent = new BTreeNodeInternal(this)
      btree.root = parent;
      this.parent = parent;
    }
     
    val right = parent.split(this).asInstanceOf[BTreeNodeInternal]
    if (index < BTree.HalfCountPerNode) {
      addChildAfter(newChild, old)
      return
    } else {
      right.addChildAfter(newChild, old)
      return
    }

    
  }
  
}