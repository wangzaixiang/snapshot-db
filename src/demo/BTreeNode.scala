package demo

trait BTreeNode {

  val btree: BTree
  var parent: BTreeNodeInternal
  var count: Int	// count of element in this node
  
  // BTreeNode(index).key >= key, update it or insert at it
  def search(key: Int): (BTreeNodeLeaf, Int)
  
  def getMaxKey: Int
  
  def deepth = {
    var i = 1
    var parent = this.parent
    while(parent != null) {
      i += 1
      parent = parent.parent
    }
    i
  }
  
}