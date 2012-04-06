package demo
import scala.util.Random

object TestBTree {

  def main(args: Array[String]) {
	  val btree = new BTree
	  
	  val array = Random.shuffle( List.range(1000, 0, -1) ) 
	  array.foreach { i=>
	    btree.insert(i,i)
	  }
	  
	  var leaf = btree.firstLeaf
	  while(leaf != null){
	    println("LeafNode: deepth=" + leaf.deepth)
	    for(i<- Range(0,leaf.count))
	      println(leaf.children(i).key)
	    leaf = leaf.next
	  }
	  
  }

}